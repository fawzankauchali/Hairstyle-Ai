package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisDao {
    @Query("SELECT * FROM saved_analyses ORDER BY timestamp DESC")
    fun getAllAnalyses(): Flow<List<AnalysisEntity>>

    @Query("SELECT * FROM saved_analyses WHERE id = :id")
    suspend fun getAnalysisById(id: Long): AnalysisEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysis(entity: AnalysisEntity): Long

    @Delete
    suspend fun deleteAnalysis(entity: AnalysisEntity)

    @Query("DELETE FROM saved_analyses WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM saved_analyses")
    suspend fun clearAll()
}
