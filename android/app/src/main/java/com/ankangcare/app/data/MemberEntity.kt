package com.ankangcare.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey val id: String,
    val name: String,
    val avatar: String,
    val roleText: String,
    val ageText: String,
    val weightKg: Double,
    val currentTag: String,
    val hasFeverAlarm: Boolean,
    val allergiesCsv: String = "" // 逗号分隔的过敏项，如 "鸡蛋清,青霉素"
)
