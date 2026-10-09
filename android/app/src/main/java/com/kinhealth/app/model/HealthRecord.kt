package com.kinhealth.app.model

data class HealthRecord(
    val id: Long = 0,
    val memberId: String,
    val timestamp: Long,
    val type: RecordType,
    val valuePrimary: String,
    val valueSecondary: String? = null,
    val notes: String? = null,
    val isAlert: Boolean = false
)

