package com.kinhealth.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_records")
data class HealthRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: String,
    val timestamp: Long,
    val type: String, // TEMPERATURE, MEDICATION, STOOL, etc.
    val valuePrimary: String,
    val valueSecondary: String? = null,
    val notes: String? = null,
    val isAlert: Boolean = false
)

