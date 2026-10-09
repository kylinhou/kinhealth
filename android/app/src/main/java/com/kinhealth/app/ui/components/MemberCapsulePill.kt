package com.kinhealth.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinhealth.app.ui.theme.Teal600

@Composable
fun MemberCapsulePill(
    currentMemberName: String,
    currentMemberAvatar: String,
    currentMemberTag: String,
    onSelectMember: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = currentMemberAvatar, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${currentMemberName}的专属健康档案",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFF1F2), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = currentMemberTag,
                                    color = Color(0xFFF43F5E),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "一人一档模式 · 上下文同步首页",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF0FDFA)
                ) {
                    Text(
                        text = "切换 ▾",
                        color = Teal600,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            DropdownMenuItem(
                text = { Text("👶 安安 (8个月 · 发热病程监护)") },
                onClick = {
                    onSelectMember("baby")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("👴 张大爷 (68岁 · 痛风与慢病专科)") },
                onClick = {
                    onSelectMember("grandpa")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("👩 林女士 (32岁 · 年度健康档案)") },
                onClick = {
                    onSelectMember("mom")
                    expanded = false
                }
            )
        }
    }
}

