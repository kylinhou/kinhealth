package com.ankangcare.app.model

data class Member(
    val id: String,
    val name: String,
    val avatar: String,
    val roleText: String,
    val ageText: String,
    val weightKg: Double,
    val currentTag: String,
    val hasFeverAlarm: Boolean = false,
    val allergies: List<String> = emptyList()
)
