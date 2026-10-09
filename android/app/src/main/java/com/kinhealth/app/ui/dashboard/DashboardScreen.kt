package com.kinhealth.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.kinhealth.app.ui.theme.Rose500
import com.kinhealth.app.ui.theme.Teal600

@Composable
fun DashboardScreen(
    onNavigateToQuickInput: () -> Unit
) {
    val currentMemberId by MemberContextRepository.currentMemberId.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "安康记 KinHealth", fontSize = 18.sp, fontWeight = FontWeight.Black)
                Text(text = "今日协同 · 全家健康状态大盘", fontSize = 11.sp, color = Color(0xFF64748B))
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF0FDFA)
            ) {
                Text(
                    text = "● 3人看护中",
                    color = Teal600,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // 1. Horizontal Member Switcher Rail (大头像滑轨)
        Text(
            text = "家庭看护成员 (轻触切换全局上下文)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val members = listOf(
                Triple("baby", "👶 安安", "39.1℃ 高热"),
                Triple("grandpa", "👴 张大爷", "尿酸 485 偏高"),
                Triple("mom", "👩 林女士", "良好")
            )

            members.forEach { (id, name, status) ->
                val isSelected = currentMemberId == id
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) Color(0xFFF0FDFA) else Color.White,
                    shadowElevation = if (isSelected) 2.dp else 1.dp,
                    modifier = Modifier
                        .width(110.dp)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Teal600 else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { MemberContextRepository.setCurrentMember(id) }
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = status,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (id == "baby") Rose500 else if (id == "grandpa") Color(0xFFD97706) else Teal600
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Focused Context Card (基于选中成员动态呈现)
        if (currentMemberId == "baby") {
            // 安安的发热病程与用药罗盘卡片
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌡️ 安安 · 发热安全罗盘与热峰",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Rose500
                        )
                        Text(
                            text = "当前 38.8℃",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Rose500
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFFF1F2),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🛡️", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "泰诺林已服 3h · 距离下次安全给药还需 1 小时 (防重复服药)",
                                fontSize = 11.sp,
                                color = Color(0xFFE11D48),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onNavigateToQuickInput,
                        colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ 极速记录体温 / 喂奶 / 便便", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else if (currentMemberId == "grandpa") {
            // 张大爷的慢病卡片
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🧪 张大爷 · 痛风慢病管理看板",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD97706)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "今晨指尖血尿酸 485 μmol/L (基线 420)，非布司他今晚待服",
                        fontSize = 12.sp,
                        color = Color(0xFF334155)
                    )
                }
            }
        } else {
            // 林女士卡片
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🌸 林女士 · 健康与体态追踪",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Teal600
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "深度睡眠 2.1h 达标 · 饮水达成 92%",
                        fontSize = 12.sp,
                        color = Color(0xFF334155)
                    )
                }
            }
        }
    }
}

