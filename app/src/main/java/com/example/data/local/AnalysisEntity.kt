package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.FacialAnalysisData

@Entity(tableName = "saved_analyses")
data class AnalysisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val faceShape: String,
    val hairlinePattern: String,
    val imagePath: String? = null,
    val isSample: Boolean = false,
    val analysisDataJson: String
)
