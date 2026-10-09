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
import com.kinhealth.app.repository.MemberContextRepository
import com.kinhealth.app.ui.components.MemberCapsulePill
import com.kinhealth.app.ui.theme.Rose500
import com.kinhealth.app.ui.theme.Teal600

@Composable
fun HealthDossierScreen() {
    val currentMemberId by MemberContextRepository.currentMemberId.collectAsState()
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
            currentMemberName = if (currentMemberId == "baby") "安安" else if (currentMemberId == "grandpa") "张大爷" else "林女士",
            currentMemberAvatar = if (currentMemberId == "baby") "👶" else if (currentMemberId == "grandpa") "👴" else "👩",
            currentMemberTag = if (currentMemberId == "baby") "儿科发热监护" else if (currentMemberId == "grandpa") "痛风慢病管理" else "日常体检",
            onSelectMember = { selectedId ->
                MemberContextRepository.setCurrentMember(selectedId)
            }
        )

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
                if (currentMemberId == "baby") {
                    when (activeSubTab) {
                        "trends" -> {
                            Text(text = "📈 安安 · 48小时发热双轴曲线 (专属)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "热峰从 39.1℃ 逐渐回落，泰诺林给药后 45min 退热良好；小便 6 次无脱水征兆。", fontSize = 11.sp, color = Color(0xFF334155))
                        }
                        "doctor" -> {
                            Text(text = "🏥 安安 (8个月) 儿科急症门诊交接长单", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "1. 热峰: 持续 31h，最高 39.1℃\n2. 已服药: 泰诺林 3.5ml (已过间隔)\n3. 精神: 良好可玩耍\n4. 禁忌: 鸡蛋清过敏、青霉素禁忌！", fontSize = 11.sp, color = Color(0xFF334155))
                        }
                        "meds" -> {
                            Text(text = "💊 安安专属药箱与过敏红线", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Rose500)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "• 泰诺林 (按8.6kg): 3.5ml/次\n• 美林: 3.0ml/次\n• 严格禁忌: 青霉素类药物", fontSize = 11.sp, color = Color(0xFF334155))
                        }
                    }
                } else if (currentMemberId == "grandpa") {
                    when (activeSubTab) {
                        "trends" -> {
                            Text(text = "📈 张大爷 · 7天尿酸降酸走势 (基线 420)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "今晨 485 μmol/L 超出目标线，需全天足量饮水并杜绝肉汤；血压 126/80 连续平稳。", fontSize = 11.sp, color = Color(0xFF334155))
                        }
                        "doctor" -> {
                            Text(text = "🏥 张大爷 (68岁) 痛风专科交接长单", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "1. 尿酸: 今晨 485 μmol/L，无急性红肿发作\n2. 处方药: 晚服非布司他 20mg，晨服降压药 1片\n3. 随访: 肾小球滤过率正常", fontSize = 11.sp, color = Color(0xFF334155))
                        }
                        "meds" -> {
                            Text(text = "💊 张大爷慢病药箱与禁忌", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "• 非布司他片: 20mg/晚\n• 降压缓释片: 1片/晨\n• 禁忌: 痛风发作期禁用阿司匹林类抗炎药", fontSize = 11.sp, color = Color(0xFF334155))
                        }
                    }
                } else {
                    Text(text = "🌸 林女士 (32岁) 年度健康体检归档与日常作息正常", fontSize = 12.sp, color = Color(0xFF334155))
                }
            }
        }
    }
}

