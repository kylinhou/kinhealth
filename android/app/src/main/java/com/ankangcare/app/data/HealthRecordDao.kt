package com.ankangcare.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {
    @Query("SELECT * FROM health_records WHERE memberId = :memberId ORDER BY timestamp DESC")
    fun getRecordsByMember(memberId: String): Flow<List<HealthRecordEntity>>

    @Query("SELECT * FROM health_records WHERE memberId = :memberId AND type = :type ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentRecordsByType(memberId: String, type: String, limit: Int = 10): List<HealthRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: HealthRecordEntity): Long
}
