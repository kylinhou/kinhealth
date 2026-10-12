package com.kinhealth.app.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import com.kinhealth.app.data.HealthRecordEntity
import com.kinhealth.app.data.MemberEntity
import com.kinhealth.app.ui.theme.Rose500
import com.kinhealth.app.ui.theme.Teal600
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    members: List<MemberEntity>,
    currentMemberId: String,
    records: List<HealthRecordEntity>,
    onSelectMember: (String) -> Unit,
    onNavigateToQuickInput: () -> Unit,
    onOpenBatchSymptomInput: (Int) -> Unit,
    onDeleteRecord: (Long) -> Unit,
    onAddMemberClick: () -> Unit
) {
    val activeMember = members.find { it.id == currentMemberId } ?: members.firstOrNull()

    // 病程日历天数选择（0=今天, 1=昨天, 2=前天, 3=大前天, 4=4天前）
    var selectedCalendarOffset by remember { mutableStateOf(0) }

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
                    text = "● ${members.size}人看护中",
                    color = Teal600,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // 空状态处理 (Zero Member Empty State)
        if (members.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(18.dp),
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
                    Text(text = "🌱", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "欢迎使用 KinHealth 安康记",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "建立一人一档后，系统将为您自适应构建专属的看护看板、用药罗盘与病程演变日历。",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = onAddMemberClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ 立即添加第一位家庭成员", fontWeight = FontWeight.Bold)
                    }
                }
            }
            return@Column
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
            members.forEach { m ->
                val isSelected = m.id == activeMember?.id
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) Color(0xFFF0FDFA) else Color.White,
                    shadowElevation = if (isSelected) 2.dp else 1.dp,
                    modifier = Modifier
                        .width(115.dp)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Teal600 else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onSelectMember(m.id) }
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "${m.avatar} ${m.name}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = m.currentTag,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (m.hasFeverAlarm) Rose500 else Teal600
                        )
                    }
                }
            }

            // 末尾添加家人入口
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier
                    .width(100.dp)
                    .clickable { onAddMemberClick() }
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "➕", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "添加家人", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Focused Context Card (基于成员病程状态自适应呈现)
        activeMember?.let { m ->
            val isFeverOrIllness = m.hasFeverAlarm || m.currentTag.contains("感冒") || m.currentTag.contains("发热")

            if (isFeverOrIllness) {
                // ================= 情境 A: 感冒发热病程看板 (含日历滑轨与当日全景) =================
                IllnessCareDashboardSection(
                    member = m,
                    records = records,
                    selectedDayOffset = selectedCalendarOffset,
                    onSelectDayOffset = { selectedCalendarOffset = it },
                    onRecordSymptomClick = { onOpenBatchSymptomInput(selectedCalendarOffset) },
                    onDeleteRecord = onDeleteRecord
                )
            } else {
                // ================= 情境 B: 日常健康监护看板 =================
                NormalCareDashboardSection(
                    member = m,
                    records = records,
                    onNavigateToQuickInput = onNavigateToQuickInput
                )
            }
        }
    }
}

/**
 * 感冒发热专属看板：顶部病程日历周滑轨 + 当日病情全景瀑布流 + 快捷记录
 */
@Composable
private fun IllnessCareDashboardSection(
    member: MemberEntity,
    records: List<HealthRecordEntity>,
    selectedDayOffset: Int,
    onSelectDayOffset: (Int) -> Unit,
    onRecordSymptomClick: () -> Unit,
    onDeleteRecord: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. 顶部病程日历周滑轨 (Timeline Weekstrip)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "📅", fontSize = 13.sp)
                        Text(text = "急性病程演变日历", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    }
                    Text(text = "近5天回溯", fontSize = 10.sp, color = Teal600)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 横向滑动卡片 (Day 0 到 Day 4)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val daysList = listOf(4, 3, 2, 1, 0) // 4天前 -> 今天
                    daysList.forEach { offset ->
                        val isSelected = selectedDayOffset == offset
                        val (dayLabel, dateStr) = getDayDateInfo(offset)
                        val dayRecords = filterRecordsByDayOffset(records, offset)

                        // 计算当日最高温与服药次数
                        val maxTemp = dayRecords.mapNotNull { extractTempNumber(it.valuePrimary) }.maxOrNull()
                        val medCount = dayRecords.count { it.valueSecondary != null || it.type == "MEDICATION" }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFFF0FDFA) else Color(0xFFF8FAFC),
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) Teal600 else Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier
                                .width(82.dp)
                                .clickable { onSelectDayOffset(offset) }
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = dayLabel,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Teal600 else Color(0xFF334155)
                                )
                                Text(
                                    text = dateStr,
                                    fontSize = 9.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (maxTemp != null) "${maxTemp}℃" else "--",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = when {
                                        maxTemp == null -> Color(0xFF94A3B8)
                                        maxTemp >= 38.5 -> Rose500
                                        maxTemp >= 37.3 -> Color(0xFFD97706)
                                        else -> Teal600
                                    }
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "💊 $medCount 次",
                                    fontSize = 9.sp,
                                    color = if (medCount > 0) Rose500 else Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. 选定日期的病情全景概览卡片
        val currentDayRecords = filterRecordsByDayOffset(records, selectedDayOffset)
        val (selectedDayLabel, selectedDateStr) = getDayDateInfo(selectedDayOffset)
        val selectedMaxTemp = currentDayRecords.mapNotNull { extractTempNumber(it.valuePrimary) }.maxOrNull()
        val selectedMedCount = currentDayRecords.count { it.valueSecondary != null || it.type == "MEDICATION" }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "${member.avatar} ${member.name} · $selectedDayLabel ($selectedDateStr)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (selectedMaxTemp != null && selectedMaxTemp >= 38.5) Color(0xFFFFF1F2) else Color(0xFFF0FDFA)
                    ) {
                        Text(
                            text = if (selectedMaxTemp != null && selectedMaxTemp >= 38.5) "🔥 处于发热期" else "平稳恢复期",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedMaxTemp != null && selectedMaxTemp >= 38.5) Rose500 else Teal600,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 三项关键数据指标
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(
                        title = "当日最高温",
                        value = if (selectedMaxTemp != null) "${selectedMaxTemp}℃" else "未测温",
                        valueColor = if (selectedMaxTemp != null && selectedMaxTemp >= 38.5) Rose500 else Teal600,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "退热给药",
                        value = "$selectedMedCount 次",
                        valueColor = if (selectedMedCount > 0) Rose500 else Color(0xFF0F172A),
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "打卡记录",
                        value = "${currentDayRecords.size} 条",
                        valueColor = Color(0xFF0F172A),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 快捷记录大按钮
                Button(
                    onClick = onRecordSymptomClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (selectedDayOffset == 0) "➕ 批量记录今日症状 (体温/给药/咳嗽)" else "➕ 补录 [${selectedDayLabel}] 症状记录",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // 3. 当日全景流水瀑布流
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "当日病程动态流水 (${currentDayRecords.size} 条)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )

                if (currentDayRecords.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "📋", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "该日暂无症状或用药打卡记录", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                    }
                } else {
                    currentDayRecords.forEach { record ->
                        TimelineEventCard(
                            record = record,
                            onDelete = { onDeleteRecord(record.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 单条事件卡片 (支持查看与删除撤销)
 */
@Composable
private fun TimelineEventCard(
    record: HealthRecordEntity,
    onDelete: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val timeStr = timeFormat.format(Date(record.timestamp))

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = timeStr, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Text(text = record.valuePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (record.valueSecondary != null) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFF1F2)
                        ) {
                            Text(
                                text = "💊 ${record.valueSecondary}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Rose500,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // 撤销/删除小图标
                    Text(
                        text = "✕",
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1),
                        modifier = Modifier
                            .clickable { onDelete() }
                            .padding(4.dp)
                    )
                }
            }

            if (record.notes != null) {
                Text(
                    text = record.notes,
                    fontSize = 11.sp,
                    color = Color(0xFF475569),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

/**
 * 日常健康监护看板
 */
@Composable
private fun NormalCareDashboardSection(
    member: MemberEntity,
    records: List<HealthRecordEntity>,
    onNavigateToQuickInput: () -> Unit
) {
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
                    text = "${member.avatar} ${member.name} · ${member.currentTag}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "${member.roleText} · ${member.ageText}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "体重基准: ${member.weightKg} kg (可用于剂量智能核算)",
                        fontSize = 11.sp,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "过敏红线: ${member.allergiesCsv}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (member.allergiesCsv != "无已知过敏") Rose500 else Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onNavigateToQuickInput,
                colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ 极速为 [${member.name}] 记录体温 / 用药 / 便便", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 9.sp, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = valueColor)
        }
    }
}

private fun getDayDateInfo(offset: Int): Pair<String, String> {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -offset)
    val month = cal.get(Calendar.MONTH) + 1
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val dateStr = "${month}/${day}"
    val label = when (offset) {
        0 -> "今天"
        1 -> "昨天"
        2 -> "前天"
        else -> "${offset}天前"
    }
    return Pair(label, dateStr)
}

private fun filterRecordsByDayOffset(records: List<HealthRecordEntity>, offset: Int): List<HealthRecordEntity> {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -offset)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    val start = cal.timeInMillis

    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 999)
    val end = cal.timeInMillis

    return records.filter { it.timestamp in start..end }
}

private fun extractTempNumber(text: String): Double? {
    val regex = Regex("""(\d{2}(?:\.\d)?)""")
    val match = regex.find(text)
    return match?.value?.toDoubleOrNull()
}
