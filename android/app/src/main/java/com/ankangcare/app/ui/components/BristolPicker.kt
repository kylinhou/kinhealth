package com.ankangcare.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankangcare.app.model.BristolStoolScale
import com.ankangcare.app.ui.theme.Teal600

@Composable
fun BristolPicker(
    selectedType: Int,
    onSelectType: (BristolStoolScale) -> Unit
) {
    Column {
        Text(
            text = "布里斯托排便 7 级图谱点选 (2-Tap 极速记录)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BristolStoolScale.ALL.forEach { item ->
                val isSelected = item.type == selectedType
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFFF0FDFA) else Color.White,
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Teal600 else Color(0xFFE2E8F0)
                    ),
                    modifier = Modifier
                        .width(96.dp)
                        .clickable { onSelectType(item) }
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = item.icon, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "类型 ${item.type}",
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = if (isSelected) Teal600 else Color(0xFF0F172A)
                        )
                        Text(
                            text = item.name,
                            fontSize = 10.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}
