package com.kinhealth.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinhealth.app.ui.theme.Teal600

@Composable
fun FamilySettingsScreen() {
    var showAddWizard by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(text = "⚙️ 家庭空间与系统设置", fontSize = 18.sp, fontWeight = FontWeight.Black)
        Text(text = "成员档案 · 6项槽位定制 · 设备协同", fontSize = 11.sp, color = Color(0xFF64748B))

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "👨‍👩‍👧‍👦 家庭在册成员 (3人)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "• 👶 安安 (8个月 · 8.6kg) [儿科监护模版]", fontSize = 11.sp, color = Color(0xFF334155))
                Text(text = "• 👴 张大爷 (68岁) [慢病痛风模版]", fontSize = 11.sp, color = Color(0xFF334155))
                Text(text = "• 👩 林女士 (32岁) [健康成人模版]", fontSize = 11.sp, color = Color(0xFF334155))

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = { showAddWizard = true },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("➕ 启动 3 步向导添加新家人", fontWeight = FontWeight.Bold, color = Teal600)
                }
            }
        }

        if (showAddWizard) {
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF0FDFA),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "✨ 3步极速添加向导", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Teal600)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "1. 选择身份预设 (新生儿 / 幼儿 / 慢病老人 / 成人)\n2. 录入体重与关键过敏红线\n3. 智能推荐并激活专病看板与首屏 6 槽位", fontSize = 11.sp, color = Color(0xFF334155))
                }
            }
        }
    }
}

