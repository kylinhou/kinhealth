package com.ankangcare.app.ui.components

import androidx.compose.foundation.background
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
import com.ankangcare.app.ui.theme.Rose500
import com.ankangcare.app.ui.theme.Teal600

@Composable
fun TemperatureStepper(
    initialValue: Double = 38.5,
    onValueChange: (Double) -> Unit
) {
    var temp by remember { mutableStateOf(initialValue) }
    val isFever = temp >= 38.5

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isFever) Color(0xFFFFF1F2) else Color(0xFFF0FDFA),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isFever) "🌡️ 发热预警 (≥38.5℃)" else "🌡️ 正常 / 低热体温",
                color = if (isFever) Rose500 else Teal600,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FilledTonalButton(
                    onClick = {
                        temp = (temp - 0.1).coerceAtLeast(35.0)
                        onValueChange(temp)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("- 0.1", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = String.format("%.1f ℃", temp),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isFever) Rose500 else Teal600
                )

                Spacer(modifier = Modifier.width(16.dp))

                FilledTonalButton(
                    onClick = {
                        temp = (temp + 0.1).coerceAtMost(42.0)
                        onValueChange(temp)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ 0.1", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
