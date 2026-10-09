package com.kinhealth.app.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 全局成员上下文仓库 (Single Source of Truth)
 * 严格确保【今日看板】与【健康档案】在任何时刻消费且展示的是同一位家庭成员的数据。
 */
object MemberContextRepository {
    private val _currentMemberId = MutableStateFlow("baby") // 默认安安
    val currentMemberId: StateFlow<String> = _currentMemberId.asStateFlow()

    fun setCurrentMember(memberId: String) {
        _currentMemberId.value = memberId
    }
}

