package com.kinhealth.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kinhealth.app.data.HealthRecordEntity
import com.kinhealth.app.data.MemberEntity
import com.kinhealth.app.ui.theme.Rose500
import com.kinhealth.app.ui.theme.Teal600
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BatchSymptomDialog(
    member: MemberEntity,
    initialDateOffsetDays: Int = 0, // 0=今天, 1=昨天, 2=前天
    onDismissRequest: () -> Unit,
    onConfirm: (HealthRecordEntity) -> Unit
) {
    // 日期选择：0=今天, 1=昨天, 2=前天, 3=精确自定义
    var selectedDayOffset by remember { mutableStateOf(initialDateOffsetDays) }
    // 时段选择：0=刚才, 1=上午09:00, 2=下午15:00, 3=夜间21:00
    var selectedPeriod by remember { mutableStateOf(0) }
    var customTimeText by remember { mutableStateOf("") }
    var showCustomTimeInput by remember { mutableStateOf(false) }

    // 体温
    var selectedQuickTemp by remember { mutableStateOf<String?>(null) }
    var customTempInput by remember { mutableStateOf("") }

    // 用药
    var selectedMed by remember { mutableStateOf<String?>(null) }
    var showOtherMedPanel by remember { mutableStateOf(false) }
    var customMedInput by remember { mutableStateOf("") }

    // 症状多选标签集合
    val selectedTags = remember { mutableStateListOf<String>() }

    // 自定义标签输入弹窗状态
    var customTagDialogCategory by remember { mutableStateOf<String?>(null) }
    var customTagInput by remember { mutableStateOf("") }

    // 预设症状标签库
    val coughTags = remember {
        mutableStateListOf("偶尔轻咳", "频繁阵咳", "夜间睡咳", "剧烈呛咳 ⚠️", "有痰/呼噜声", "干咳无痰")
    }
    val noseTags = remember {
        mutableStateListOf("流清鼻涕", "黏稠黄涕", "鼻塞张嘴呼吸", "打喷嚏", "声音嘶哑")
    }
    val mentalTags = remember {
        mutableStateListOf("精神活泼", "稍显疲倦", "烦躁哭闹", "嗜睡蔫弱 ⚠️", "食欲尚可", "明显拒奶拒食", "呕吐 1 次 ⚠️")
    }

    var notesText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // 1. 顶栏标题
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = "🤒 感冒病程 · 批量极速记录", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        }
                        Text(text = "为 [${member.avatar} ${member.name}] 批量记体温、给药与咳嗽体征", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                    IconButton(onClick = onDismissRequest, modifier = Modifier.size(28.dp)) {
                        Text(text = "✕", fontSize = 14.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 滚动表单主体
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // ================= 1. 时间定位器 =================
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🕒 记录时间 (支持补录)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                                Text(
                                    text = calculateTimeLabel(selectedDayOffset, selectedPeriod, customTimeText),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Teal600
                                )
                            }

                            // 一级：日期胶囊
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("今天", "昨天", "前天").forEachIndexed { idx, title ->
                                    val isSelected = selectedDayOffset == idx && !showCustomTimeInput
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) Teal600 else Color.White,
                                        border = BorderStroke(1.dp, if (isSelected) Teal600 else Color(0xFFCBD5E1)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                selectedDayOffset = idx
                                                showCustomTimeInput = false
                                            }
                                    ) {
                                        Text(
                                            text = title,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else Color(0xFF475569),
                                            modifier = Modifier.padding(vertical = 6.dp),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (showCustomTimeInput) Color(0xFFF0FDFA) else Color.White,
                                    border = BorderStroke(1.dp, if (showCustomTimeInput) Teal600 else Color(0xFFCBD5E1)),
                                    modifier = Modifier.clickable { showCustomTimeInput = !showCustomTimeInput }
                                ) {
                                    Text(
                                        text = if (showCustomTimeInput) "收起自定义" else "自定义",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Teal600,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            // 二级：常用时段
                            if (!showCustomTimeInput) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("刚才", "上午09:00", "下午15:00", "夜间21:00").forEachIndexed { idx, pTitle ->
                                        val isSelected = selectedPeriod == idx
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isSelected) Color(0xFFE6FFFA) else Color.White,
                                            border = BorderStroke(1.dp, if (isSelected) Teal600 else Color(0xFFE2E8F0)),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedPeriod = idx }
                                        ) {
                                            Text(
                                                text = pTitle,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Teal600 else Color(0xFF64748B),
                                                modifier = Modifier.padding(vertical = 5.dp),
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            } else {
                                OutlinedTextField(
                                    value = customTimeText,
                                    onValueChange = { customTimeText = it },
                                    label = { Text("手输具体时间 (如 10-11 16:30)") },
                                    placeholder = { Text("格式：MM-dd HH:mm") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // ================= 2. 体温与给药 =================
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "🌡️ 体温与给药记录 (选填)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))

                        // 体温卡片 + 手输
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("37.0℃", "37.8℃", "38.5℃", "39.2℃").forEach { tVal ->
                                val isSelected = selectedQuickTemp == tVal && customTempInput.isBlank()
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) Color(0xFFFFF1F2) else Color(0xFFF8FAFC),
                                    border = BorderStroke(1.dp, if (isSelected) Rose500 else Color(0xFFE2E8F0)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            if (selectedQuickTemp == tVal) {
                                                selectedQuickTemp = null
                                            } else {
                                                selectedQuickTemp = tVal
                                                customTempInput = ""
                                            }
                                        }
                                ) {
                                    Text(
                                        text = tVal,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Rose500 else Color(0xFF334155),
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }

                            // 手动精准输入
                            OutlinedTextField(
                                value = customTempInput,
                                onValueChange = {
                                    customTempInput = it
                                    if (it.isNotBlank()) selectedQuickTemp = null
                                },
                                placeholder = { Text("手输℃", fontSize = 10.sp) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                modifier = Modifier
                                    .width(76.dp)
                                    .height(48.dp)
                            )
                        }

                        // 退热给药快捷项
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("美林布洛芬 (4ml)", "泰诺林对乙酰 (3.5ml)").forEach { medName ->
                                val isSelected = selectedMed == medName
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) Color(0xFFFFF1F2) else Color(0xFFF8FAFC),
                                    border = BorderStroke(1.dp, if (isSelected) Rose500 else Color(0xFFE2E8F0)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            selectedMed = if (selectedMed == medName) null else medName
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(text = "💊 ", fontSize = 11.sp)
                                        Text(
                                            text = medName,
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Rose500 else Color(0xFF475569)
                                        )
                                    }
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (showOtherMedPanel) Color(0xFFF0FDFA) else Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, if (showOtherMedPanel) Teal600 else Color(0xFFE2E8F0)),
                                modifier = Modifier.clickable { showOtherMedPanel = !showOtherMedPanel }
                            ) {
                                Text(
                                    text = "+ 其他药物",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Teal600,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp)
                                )
                            }
                        }

                        // 其他药物选择面板
                        if (showOtherMedPanel) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(text = "常见咳嗽/感冒备药：", fontSize = 10.sp, color = Color(0xFF64748B))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        listOf("易坦静口服液 2.5ml", "小儿氨酚黄那敏 1包", "布地奈德雾化 1支", "头孢克肟颗粒").forEach { preMed ->
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color.White,
                                                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                                modifier = Modifier.clickable {
                                                    selectedMed = preMed
                                                    showOtherMedPanel = false
                                                }
                                            ) {
                                                Text(text = preMed, fontSize = 9.sp, color = Color(0xFF334155), modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp))
                                            }
                                        }
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        OutlinedTextField(
                                            value = customMedInput,
                                            onValueChange = { customMedInput = it },
                                            placeholder = { Text("手输药品名及剂量...", fontSize = 10.sp) },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Button(
                                            onClick = {
                                                if (customMedInput.isNotBlank()) {
                                                    selectedMed = customMedInput.trim()
                                                    showOtherMedPanel = false
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("确定", fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ================= 3. 呼吸道：咳嗽表现 (多选) =================
                    SymptomTagSection(
                        title = "🫁 咳嗽表现 (多选)",
                        tags = coughTags,
                        selectedTags = selectedTags,
                        onAddCustomClick = {
                            customTagDialogCategory = "咳嗽表现"
                            customTagInput = ""
                        }
                    )

                    // ================= 4. 呼吸道：鼻腔咽喉 (多选) =================
                    SymptomTagSection(
                        title = "👃 鼻腔与咽喉表现 (多选)",
                        tags = noseTags,
                        selectedTags = selectedTags,
                        onAddCustomClick = {
                            customTagDialogCategory = "鼻腔与咽喉"
                            customTagInput = ""
                        }
                    )

                    // ================= 5. 精神状态与食欲 (多选) =================
                    SymptomTagSection(
                        title = "🥺 精神状态与食欲反应 (多选)",
                        tags = mentalTags,
                        selectedTags = selectedTags,
                        onAddCustomClick = {
                            customTagDialogCategory = "精神与食欲"
                            customTagInput = ""
                        }
                    )

                    // 备注输入
                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        label = { Text("补充细节描述 (选填)") },
                        placeholder = { Text("如：出汗多、夜里惊醒、手脚冰凉等") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 底部一键落库按钮
                val totalItemsCount = selectedTags.size +
                        (if (selectedQuickTemp != null || customTempInput.isNotBlank()) 1 else 0) +
                        (if (selectedMed != null) 1 else 0)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "已选 ${totalItemsCount} 项",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Teal600
                    )

                    Button(
                        onClick = {
                            // 确定时间戳
                            val targetTimestamp = calculateTimestamp(selectedDayOffset, selectedPeriod, customTimeText)
                            val finalTemp = if (customTempInput.isNotBlank()) "${customTempInput.trim()}℃" else selectedQuickTemp

                            // 主值构造
                            val valuePrimary = when {
                                finalTemp != null -> finalTemp
                                selectedTags.isNotEmpty() -> selectedTags.take(2).joinToString(" / ")
                                selectedMed != null -> selectedMed!!
                                else -> "日常症状记录"
                            }

                            val allNotes = buildString {
                                if (selectedTags.isNotEmpty()) {
                                    append(selectedTags.joinToString(","))
                                }
                                if (notesText.isNotBlank()) {
                                    if (isNotEmpty()) append(" | ")
                                    append(notesText.trim())
                                }
                            }

                            val isAlert = (finalTemp != null && (finalTemp.startsWith("38.5") || finalTemp.startsWith("39"))) ||
                                    allNotes.contains("⚠️")

                            val newRecord = HealthRecordEntity(
                                memberId = member.id,
                                timestamp = targetTimestamp,
                                type = "SYMPTOM_BATCH",
                                valuePrimary = valuePrimary,
                                valueSecondary = selectedMed,
                                notes = allNotes.ifBlank { null },
                                isAlert = isAlert
                            )

                            onConfirm(newRecord)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "一键保存到病程时间轴", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }

    // 自定义表现输入弹窗
    if (customTagDialogCategory != null) {
        AlertDialog(
            onDismissRequest = { customTagDialogCategory = null },
            title = { Text("添加自定义${customTagDialogCategory}", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = customTagInput,
                    onValueChange = { customTagInput = it },
                    placeholder = { Text("例如：犬吠样咳、嘴唇干燥") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val input = customTagInput.trim()
                        if (input.isNotBlank()) {
                            when (customTagDialogCategory) {
                                "咳嗽表现" -> if (!coughTags.contains(input)) coughTags.add(input)
                                "鼻腔与咽喉" -> if (!noseTags.contains(input)) noseTags.add(input)
                                "精神与食欲" -> if (!mentalTags.contains(input)) mentalTags.add(input)
                            }
                            if (!selectedTags.contains(input)) selectedTags.add(input)
                        }
                        customTagDialogCategory = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Teal600)
                ) {
                    Text("添加并选中")
                }
            },
            dismissButton = {
                TextButton(onClick = { customTagDialogCategory = null }) {
                    Text("取消")
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SymptomTagSection(
    title: String,
    tags: List<String>,
    selectedTags: MutableList<String>,
    onAddCustomClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
            Text(
                text = "+ 自定义",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Teal600,
                modifier = Modifier.clickable { onAddCustomClick() }
            )
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            tags.forEach { tag ->
                val isSelected = selectedTags.contains(tag)
                val isDanger = tag.contains("⚠️")
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) (if (isDanger) Color(0xFFFFF1F2) else Teal600) else Color(0xFFF8FAFC),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) (if (isDanger) Rose500 else Teal600) else (if (isDanger) Color(0xFFFECDD3) else Color(0xFFE2E8F0))
                    ),
                    modifier = Modifier.clickable {
                        if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                    }
                ) {
                    Text(
                        text = tag,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) (if (isDanger) Rose500 else Color.White) else (if (isDanger) Rose500 else Color(0xFF475569)),
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

private fun calculateTimeLabel(dayOffset: Int, period: Int, customTime: String): String {
    if (customTime.isNotBlank()) return customTime
    val dayPrefix = when (dayOffset) {
        0 -> "今天"
        1 -> "昨天"
        2 -> "前天"
        else -> "自定义"
    }
    val periodStr = when (period) {
        0 -> "刚才"
        1 -> "上午 09:00"
        2 -> "下午 15:00"
        3 -> "夜间 21:00"
        else -> ""
    }
    return "$dayPrefix $periodStr"
}

private fun calculateTimestamp(dayOffset: Int, period: Int, customTime: String): Long {
    if (customTime.isNotBlank()) {
        val trimmed = customTime.trim()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val formats = listOf(
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()),
            SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()),
            SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()),
            SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
        )
        for (sdf in formats) {
            try {
                val parsed = sdf.parse(trimmed)
                if (parsed != null) {
                    val cal = Calendar.getInstance().apply { time = parsed }
                    if (!trimmed.contains("202")) {
                        cal.set(Calendar.YEAR, currentYear)
                    }
                    return cal.timeInMillis
                }
            } catch (_: Exception) {}
        }
    }

    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -dayOffset)
    when (period) {
        0 -> { /* 当前时分秒保留 */ }
        1 -> { cal.set(Calendar.HOUR_OF_DAY, 9); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0) }
        2 -> { cal.set(Calendar.HOUR_OF_DAY, 15); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0) }
        3 -> { cal.set(Calendar.HOUR_OF_DAY, 21); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0) }
    }
    return cal.timeInMillis
}
