package com.example.ui

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.local.AnalysisEntity
import com.example.data.local.AppDatabase
import com.example.data.model.FacialAnalysisData
import com.example.data.remote.GeminiApiService
import com.example.data.repository.StyleVisionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStream

enum class MainTab {
    ANALYZE,
    RESULTS,
    HISTORY,
    BARBER_GUIDE
}

enum class GeometryOverlayType {
    ALL,
    THIRDS,
    JAWLINE_AXIS,
    HAIRLINE_GUIDE
}

data class StyleVisionUiState(
    val selectedBitmap: Bitmap? = null,
    val selectedImageUri: String? = null,
    val selectedSampleResId: Int? = null,
    val isAnalyzing: Boolean = false,
    val analysisResult: FacialAnalysisData? = null,
    val genderPreference: String = "Masculine / Barber",
    val hairTexture: String = "Natural / Normal",
    val userNotes: String = "",
    val errorMessage: String? = null,
    val activeTab: MainTab = MainTab.ANALYZE,
    val selectedRecommendationIndex: Int = 0,
    val savedAnalyses: List<AnalysisEntity> = emptyList(),
    val isCurrentAnalysisSaved: Boolean = false,
    val showGeometryOverlay: Boolean = true,
    val geometryOverlayType: GeometryOverlayType = GeometryOverlayType.ALL,
    val copiedPromptMessage: String? = null
)

class StyleVisionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StyleVisionRepository
    private val _uiState = MutableStateFlow(StyleVisionUiState())
    val uiState: StateFlow<StyleVisionUiState> = _uiState.asStateFlow()

    init {
        val db = AppDatabase.getInstance(application)
        repository = StyleVisionRepository(GeminiApiService(), db.analysisDao())

        // Load saved analyses
        viewModelScope.launch {
            repository.allSavedAnalyses.collect { list ->
                _uiState.update { it.copy(savedAnalyses = list) }
            }
        }

        // Set default sample model 1 initially so user sees immediate live preview
        loadSampleModel(R.drawable.sample_model_1, "Sample 1: Chiseled Oval / Modern Fade")
    }

    fun setTab(tab: MainTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun setGenderPreference(gender: String) {
        _uiState.update { it.copy(genderPreference = gender) }
    }

    fun setHairTexture(texture: String) {
        _uiState.update { it.copy(hairTexture = texture) }
    }

    fun setUserNotes(notes: String) {
        _uiState.update { it.copy(userNotes = notes) }
    }

    fun toggleGeometryOverlay() {
        _uiState.update { it.copy(showGeometryOverlay = !it.showGeometryOverlay) }
    }

    fun setGeometryOverlayType(type: GeometryOverlayType) {
        _uiState.update { it.copy(geometryOverlayType = type) }
    }

    fun selectRecommendation(index: Int) {
        _uiState.update { it.copy(selectedRecommendationIndex = index) }
    }

    fun onImageSelected(uri: Uri, context: Context) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            if (bitmap != null) {
                _uiState.update {
                    it.copy(
                        selectedBitmap = bitmap,
                        selectedImageUri = uri.toString(),
                        selectedSampleResId = null,
                        analysisResult = null,
                        isCurrentAnalysisSaved = false,
                        errorMessage = null
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Could not load selected photo: ${e.localizedMessage}") }
        }
    }

    fun loadSampleModel(resId: Int, label: String) {
        try {
            val bitmap = BitmapFactory.decodeResource(getApplication<Application>().resources, resId)
            _uiState.update {
                it.copy(
                    selectedBitmap = bitmap,
                    selectedImageUri = null,
                    selectedSampleResId = resId,
                    analysisResult = null,
                    isCurrentAnalysisSaved = false,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Error loading sample image") }
        }
    }

    fun analyzeCurrentPhoto() {
        val bitmap = _uiState.value.selectedBitmap ?: return
        _uiState.update { it.copy(isAnalyzing = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val result = repository.analyzeImage(
                    bitmap = bitmap,
                    genderPreference = _uiState.value.genderPreference,
                    hairTexture = _uiState.value.hairTexture,
                    notes = _uiState.value.userNotes
                )
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        analysisResult = result,
                        activeTab = MainTab.RESULTS,
                        selectedRecommendationIndex = 0,
                        isCurrentAnalysisSaved = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        errorMessage = "Analysis error: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun saveCurrentAnalysis() {
        val result = _uiState.value.analysisResult ?: return
        val title = "Scan: ${result.faceShape} Face (${result.hairlinePattern})"
        viewModelScope.launch {
            try {
                repository.saveAnalysis(
                    title = title,
                    faceShape = result.faceShape,
                    hairlinePattern = result.hairlinePattern,
                    imagePath = _uiState.value.selectedImageUri,
                    isSample = _uiState.value.selectedSampleResId != null,
                    data = result
                )
                _uiState.update { it.copy(isCurrentAnalysisSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error saving analysis") }
            }
        }
    }

    fun loadSavedAnalysis(entity: AnalysisEntity) {
        val data = repository.parseAnalysisJson(entity.analysisDataJson)
        if (data != null) {
            _uiState.update {
                it.copy(
                    analysisResult = data,
                    activeTab = MainTab.RESULTS,
                    selectedRecommendationIndex = 0,
                    isCurrentAnalysisSaved = true
                )
            }
        }
    }

    fun deleteSavedAnalysis(id: Long) {
        viewModelScope.launch {
            repository.deleteAnalysis(id)
        }
    }

    fun setCopiedPromptNotice(styleName: String) {
        _uiState.update { it.copy(copiedPromptMessage = "Prompt copied for: $styleName") }
    }

    fun clearCopiedPromptNotice() {
        _uiState.update { it.copy(copiedPromptMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
