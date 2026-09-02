package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.local.AnalysisDao
import com.example.data.local.AnalysisEntity
import com.example.data.model.FacialAnalysisData
import com.example.data.remote.GeminiApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow

class StyleVisionRepository(
    private val apiService: GeminiApiService,
    private val dao: AnalysisDao
) {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(FacialAnalysisData::class.java)

    val allSavedAnalyses: Flow<List<AnalysisEntity>> = dao.getAllAnalyses()

    suspend fun analyzeImage(
        bitmap: Bitmap,
        genderPreference: String,
        hairTexture: String,
        notes: String
    ): FacialAnalysisData {
        return apiService.analyzeFacialGeometry(bitmap, genderPreference, hairTexture, notes)
    }

    suspend fun saveAnalysis(
        title: String,
        faceShape: String,
        hairlinePattern: String,
        imagePath: String?,
        isSample: Boolean,
        data: FacialAnalysisData
    ): Long {
        val json = adapter.toJson(data)
        val entity = AnalysisEntity(
            title = title,
            faceShape = faceShape,
            hairlinePattern = hairlinePattern,
            imagePath = imagePath,
            isSample = isSample,
            analysisDataJson = json
        )
        return dao.insertAnalysis(entity)
    }

    fun parseAnalysisJson(json: String): FacialAnalysisData? {
        return try {
            adapter.fromJson(json)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteAnalysis(id: Long) {
        dao.deleteById(id)
    }
}
