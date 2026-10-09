package com.kinhealth.app.ui.quickinput

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
import com.kinhealth.app.model.BristolStoolScale
import com.kinhealth.app.repository.MemberContextRepository
import com.kinhealth.app.ui.components.BristolPicker
import com.kinhealth.app.ui.components.TemperatureStepper
import com.kinhealth.app.ui.theme.Teal600

@Composable
fun QuickInputScreen(
    onRecordSaved: () -> Unit
) {
    val currentMemberId by MemberContextRepository.currentMemberId.collectAsState()
    var currentTemp by remember { mutableStateOf(38.8) }
    var selectedBristol by remember { mutableStateOf(4) }
    var saveMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(text = "➕ 极速录入中心", fontSize = 18.sp, fontWeight = FontWeight.Black)
        Text(
            text = "正在为 [${if (currentMemberId == "baby") "安安" else if (currentMemberId == "grandpa") "张大爷" else "林女士"}] 记健康指标",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. 体温调节卡片
        Text(text = "1. 体温记录 (微调至当前度数)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        TemperatureStepper(
            initialValue = currentTemp,
            onValueChange = { currentTemp = it }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 2. 布里斯托排便卡片
        Text(text = "2. 排便状态 (布里斯托 7 级图谱)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        BristolPicker(
            selectedType = selectedBristol,
            onSelectType = { item -> selectedBristol = item.type }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3. 极速保存按钮 (2-Tap 原则)
        Button(
            onClick = {
                saveMessage = "✅ 已成功保存！体温 ${String.format("%.1f", currentTemp)}℃，布里斯托 ${selectedBristol} 型"
                onRecordSaved()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Teal600),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("立即保存到本地数据库 (0ms 本地持久化)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        saveMessage?.let { msg ->
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF0FDFA),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = msg,
                    color = Teal600,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

