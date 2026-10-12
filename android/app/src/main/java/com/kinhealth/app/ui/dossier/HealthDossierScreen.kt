package com.kinhealth.app.ui.dossier

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinhealth.app.data.MemberEntity
import com.kinhealth.app.ui.components.MemberCapsulePill
import com.kinhealth.app.ui.theme.Rose500
import com.kinhealth.app.ui.theme.Teal600

@Composable
fun HealthDossierScreen(
    members: List<MemberEntity>,
    currentMemberId: String,
    onSelectMember: (String) -> Unit,
    onAddMemberClick: () -> Unit
) {
    val activeMember = members.find { it.id == currentMemberId } ?: members.firstOrNull()
    var activeSubTab by remember { mutableStateOf("trends") } // "trends", "doctor", "meds"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // 1. 微型身份胶囊 (一人一档)
        MemberCapsulePill(
            currentMember = activeMember,
            allMembers = members,
            onSelectMember = onSelectMember,
            onAddMemberClick = onAddMemberClick
        )

        // 空状态处理
        if (members.isEmpty() || activeMember == null) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📁", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "暂无健康档案资产",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "请先添加一位家庭成员，系统将为每位家人生成专属的就诊长单、趋势周报与药箱过敏账本。",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onAddMemberClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ 立即添加家庭成员", fontWeight = FontWeight.Bold)
                    }
                }
            }
            return@Column
        }

        // 2. 三大资产视角 Sub-Tabs
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFE2E8F0),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(modifier = Modifier.padding(3.dp)) {
                listOf(
                    Triple("trends", "📈 趋势周报", Teal600),
                    Triple("doctor", "🏥 就诊病程单", Color(0xFF2563EB)),
                    Triple("meds", "💊 药箱过敏", Color(0xFF9333EA))
                ).forEach { (key, title, activeColor) ->
                    val isSelected = activeSubTab == key
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Color.White else Color.Transparent,
                        shadowElevation = if (isSelected) 1.dp else 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeSubTab = key }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) activeColor else Color(0xFF64748B)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. 动态展示单人专属档案内容
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                when (activeSubTab) {
                    "trends" -> {
                        Text(
                            text = "📈 ${activeMember.avatar} ${activeMember.name} · 健康指标趋势与周期小结",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• 监护场景: ${activeMember.currentTag}\n• 年龄/月龄: ${activeMember.ageText} | 体重基线: ${activeMember.weightKg} kg\n• 指标状态: 运行良好，持续通过极速记积累流水后将生成专属平滑双轴曲线。",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 18.sp
                        )
                    }
                    "doctor" -> {
                        Text(
                            text = "🏥 ${activeMember.avatar} ${activeMember.name} 专属门诊就诊交接长单",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1. 身份档案: ${activeMember.name} (${activeMember.roleText} · ${activeMember.ageText} · ${activeMember.weightKg}kg)\n2. 过敏红线提示: ${activeMember.allergiesCsv}\n3. 就诊交接说明: 贯彻一人一档，过滤其他家人无关病历，供门诊医生30秒快速审阅。",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 18.sp
                        )
                    }
                    "meds" -> {
                        Text(
                            text = "💊 ${activeMember.avatar} ${activeMember.name} 专属常备药箱与禁忌",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Rose500
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• 关键过敏源: ${activeMember.allergiesCsv}\n• 用药安全原则: 严格遵守安全时间锁，严禁同一有效成分重复蓄积给药。",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
