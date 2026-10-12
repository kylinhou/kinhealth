package com.kinhealth.app.ui.quickinput

import androidx.compose.foundation.BorderStroke
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
import com.kinhealth.app.data.HealthRecordEntity
import com.kinhealth.app.data.MemberEntity
import com.kinhealth.app.model.BristolStoolScale
import com.kinhealth.app.ui.components.BatchSymptomDialog
import com.kinhealth.app.ui.components.BristolPicker
import com.kinhealth.app.ui.components.TemperatureStepper
import com.kinhealth.app.ui.theme.Rose500
import com.kinhealth.app.ui.theme.Teal600

enum class QuickInputMode {
    ILLNESS_BATCH, // 感冒病程批量记录
    DAILY_FEEDING   // 日常生理/便便记录
}

@Composable
fun QuickInputScreen(
    members: List<MemberEntity>,
    currentMemberId: String,
    onSaveRecord: (HealthRecordEntity) -> Unit,
    onAddMemberClick: () -> Unit
) {
    val activeMember = members.find { it.id == currentMemberId } ?: members.firstOrNull()

    // 默认如果成员处于感冒监护中，优先展示感冒批量录入
    var inputMode by remember(activeMember) {
        val isIll = activeMember?.hasFeverAlarm == true || activeMember?.currentTag?.contains("感冒") == true
        mutableStateOf(if (isIll) QuickInputMode.ILLNESS_BATCH else QuickInputMode.DAILY_FEEDING)
    }

    var showBatchDialog by remember { mutableStateOf(false) }

    // 日常模式下的状态
    var currentTemp by remember { mutableStateOf(37.2) }
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "➕ 极速健康录入中心", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
            if (activeMember != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0FDFA),
                    border = BorderStroke(1.dp, Color(0xFFCCFBF1))
                ) {
                    Text(
                        text = "${activeMember.avatar} ${activeMember.name}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Teal600,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        if (members.isEmpty() || activeMember == null) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📝", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "暂无健康看护对象",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "健康指标（体温、排便、用药、症状）需关联具体家庭成员。请先建立家庭档案。",
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

        Spacer(modifier = Modifier.height(12.dp))

        // 顶层双模式切换 Segmented Control
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE2E8F0),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(3.dp)) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (inputMode == QuickInputMode.ILLNESS_BATCH) Color.White else Color.Transparent,
                    shadowElevation = if (inputMode == QuickInputMode.ILLNESS_BATCH) 2.dp else 0.dp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { inputMode = QuickInputMode.ILLNESS_BATCH }
                ) {
                    Text(
                        text = "🤒 感冒急性病程 (批量记录)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (inputMode == QuickInputMode.ILLNESS_BATCH) Rose500 else Color(0xFF64748B),
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (inputMode == QuickInputMode.DAILY_FEEDING) Color.White else Color.Transparent,
                    shadowElevation = if (inputMode == QuickInputMode.DAILY_FEEDING) 2.dp else 0.dp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { inputMode = QuickInputMode.DAILY_FEEDING }
                ) {
                    Text(
                        text = "🍼 日常生理记录 (体温/便便)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (inputMode == QuickInputMode.DAILY_FEEDING) Teal600 else Color(0xFF64748B),
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (inputMode == QuickInputMode.ILLNESS_BATCH) {
            // ================= 模式 1: 感冒病程批量录入入口卡片 =================
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "🤒", fontSize = 24.sp)
                        Column {
                            Text(text = "感冒病程 · 批量表现极速打卡", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(text = "支持快捷/精确时间补录、体温、给药及咳嗽体征一屏勾选", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "• 快捷时间胶囊：支持补录今天、昨天、前天任意时段", fontSize = 10.sp, color = Color(0xFF475569))
                            Text(text = "• 呼吸道症状：频繁阵咳、流清鼻涕、嗜睡萎靡一键多选", fontSize = 10.sp, color = Color(0xFF475569))
                            Text(text = "• 退热药用药：记录美林、泰诺林或手输备药用量", fontSize = 10.sp, color = Color(0xFF475569))
                        }
                    }

                    Button(
                        onClick = { showBatchDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Rose500),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "➕ 立即打开批量记录面板", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        } else {
            // ================= 模式 2: 日常生理记录 (体温调节 + 布里斯托便便) =================
            // 1. 体温调节卡片
            Text(text = "1. 体温记录 (微调至当前度数)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
            Spacer(modifier = Modifier.height(6.dp))
            TemperatureStepper(
                initialValue = currentTemp,
                onValueChange = { currentTemp = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 2. 布里斯托排便卡片
            Text(text = "2. 排便状态 (布里斯托 7 级图谱)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
            Spacer(modifier = Modifier.height(6.dp))
            BristolPicker(
                selectedType = selectedBristol,
                onSelectType = { item -> selectedBristol = item.type }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. 极速保存按钮 (真实落库)
            Button(
                onClick = {
                    val tempStr = String.format("%.1f℃", currentTemp)
                    val isFever = currentTemp >= 38.5
                    val record = HealthRecordEntity(
                        memberId = activeMember.id,
                        timestamp = System.currentTimeMillis(),
                        type = "DAILY_PHYSIOLOGY",
                        valuePrimary = tempStr,
                        valueSecondary = "布里斯托 ${selectedBristol} 型",
                        notes = "体温: $tempStr | 便便: 布里斯托 ${selectedBristol} 型",
                        isAlert = isFever
                    )
                    onSaveRecord(record)
                    saveMessage = "✅ 已成功为 [${activeMember.name}] 保存到本地数据库！(体温 $tempStr)"
                },
                colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("立即保存到本地数据库 (0ms 本地持久化)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
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

        // 弹出的批量记录弹窗
        if (showBatchDialog && activeMember != null) {
            BatchSymptomDialog(
                member = activeMember,
                onDismissRequest = { showBatchDialog = false },
                onConfirm = { record ->
                    onSaveRecord(record)
                    showBatchDialog = false
                    saveMessage = "✅ 已成功批量保存病程表现到时间轴！"
                }
            )
        }
    }
}
