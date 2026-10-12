package com.kinhealth.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.kinhealth.app.data.MemberEntity
import com.kinhealth.app.ui.components.DeleteMemberConfirmDialog
import com.kinhealth.app.ui.components.MemberFormDialog
import com.kinhealth.app.ui.theme.Rose500
import com.kinhealth.app.ui.theme.Teal600

enum class SettingsTab {
    FAMILY_MEMBERS,
    SYSTEM_PREFERENCES
}

@Composable
fun FamilySettingsScreen(
    members: List<MemberEntity>,
    onAddMemberClick: () -> Unit,
    onUpdateMember: (MemberEntity) -> Unit,
    onDeleteMember: (String) -> Unit
) {
    var activeTab by remember { mutableStateOf(SettingsTab.FAMILY_MEMBERS) }
    var editingMember by remember { mutableStateOf<MemberEntity?>(null) }
    var deletingMember by remember { mutableStateOf<MemberEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        // 顶部大标题
        Text(text = "⚙️ 家庭空间与系统设置", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
        Text(text = "家庭在册档案 · 发热专病监护 · 软硬协同设置", fontSize = 11.sp, color = Color(0xFF64748B))

        Spacer(modifier = Modifier.height(14.dp))

        // 顶部分段控制器：解耦「家庭成员」与「系统设置」
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE2E8F0),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (activeTab == SettingsTab.FAMILY_MEMBERS) Color.White else Color.Transparent,
                    shadowElevation = if (activeTab == SettingsTab.FAMILY_MEMBERS) 2.dp else 0.dp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeTab = SettingsTab.FAMILY_MEMBERS }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(vertical = 9.dp)
                    ) {
                        Text(
                            text = "👨‍👩‍👧‍👦 家庭成员档案 (${members.size})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activeTab == SettingsTab.FAMILY_MEMBERS) Teal600 else Color(0xFF64748B)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (activeTab == SettingsTab.SYSTEM_PREFERENCES) Color.White else Color.Transparent,
                    shadowElevation = if (activeTab == SettingsTab.SYSTEM_PREFERENCES) 2.dp else 0.dp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeTab = SettingsTab.SYSTEM_PREFERENCES }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(vertical = 9.dp)
                    ) {
                        Text(
                            text = "⚙️ 系统与设备设置",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activeTab == SettingsTab.SYSTEM_PREFERENCES) Teal600 else Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (activeTab) {
            SettingsTab.FAMILY_MEMBERS -> {
                FamilyMembersPanel(
                    members = members,
                    onAddMemberClick = onAddMemberClick,
                    onEditMember = { editingMember = it },
                    onDeletePrompt = { deletingMember = it },
                    onToggleFever = { member ->
                        val updated = member.copy(hasFeverAlarm = !member.hasFeverAlarm)
                        onUpdateMember(updated)
                    }
                )
            }
            SettingsTab.SYSTEM_PREFERENCES -> {
                SystemPreferencesPanel()
            }
        }
    }

    // 编辑成员弹窗
    editingMember?.let { target ->
        MemberFormDialog(
            initialMember = target,
            onDismissRequest = { editingMember = null },
            onConfirm = { updated ->
                onUpdateMember(updated)
                editingMember = null
            }
        )
    }

    // 删除防误触确认弹窗
    deletingMember?.let { target ->
        DeleteMemberConfirmDialog(
            member = target,
            onDismissRequest = { deletingMember = null },
            onConfirmDelete = {
                onDeleteMember(target.id)
                deletingMember = null
            }
        )
    }
}

@Composable
private fun FamilyMembersPanel(
    members: List<MemberEntity>,
    onAddMemberClick: () -> Unit,
    onEditMember: (MemberEntity) -> Unit,
    onDeletePrompt: (MemberEntity) -> Unit,
    onToggleFever: (MemberEntity) -> Unit
) {
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
                Column {
                    Text(
                        text = "👨‍👩‍👧‍👦 家庭在册成员档案",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "一人一档 · 剂量安全算药 · 专病警戒",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B)
                    )
                }
                if (members.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF0FDFA),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Room 本地加密存储",
                            fontSize = 10.sp,
                            color = Teal600,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (members.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🏠", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "暂无在册家庭成员",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )
                        Text(
                            text = "点击下方按钮启动向导，建立第一个家庭档案",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                members.forEach { m ->
                    MemberItemCard(
                        member = m,
                        onEdit = { onEditMember(m) },
                        onDelete = { onDeletePrompt(m) },
                        onToggleFever = { onToggleFever(m) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddMemberClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ 启动向导添加新家人", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MemberItemCard(
    member: MemberEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFever: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (member.hasFeverAlarm) Color(0xFFFECDD3) else Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // 头部：头像 + 姓名角色 + 状态徽章
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = member.avatar, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = member.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${member.roleText} · ${member.ageText})",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Text(
                            text = "最新体重: ${if (member.weightKg > 0) "${member.weightKg} kg" else "未录入"} | 状态: ${member.currentTag}",
                            fontSize = 11.sp,
                            color = Color(0xFF475569)
                        )
                    }
                }

                // 专病/发热监护徽章
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (member.hasFeverAlarm) Color(0xFFFFF1F2) else Color(0xFFF0FDFA),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (member.hasFeverAlarm) Color(0xFFFDA4AF) else Color(0xFFCCFBF1))
                ) {
                    Text(
                        text = if (member.hasFeverAlarm) "🔴 发热监护中" else "🟢 状态稳定",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (member.hasFeverAlarm) Rose500 else Teal600,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 过敏红线提示条
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (member.allergiesCsv != "无已知过敏" && member.allergiesCsv.isNotBlank()) Color(0xFFFFF7ED) else Color(0xFFF1F5F9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚠️ 过敏红线: ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (member.allergiesCsv != "无已知过敏" && member.allergiesCsv.isNotBlank()) Color(0xFFC2410C) else Color(0xFF64748B)
                    )
                    Text(
                        text = member.allergiesCsv,
                        fontSize = 10.sp,
                        color = Color(0xFF334155)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 底部操作栏：发热模式切换 + 编辑按钮 + 删除按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 发热开关
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onToggleFever() }
                        .padding(vertical = 4.dp)
                ) {
                    Switch(
                        checked = member.hasFeverAlarm,
                        onCheckedChange = { onToggleFever() },
                        modifier = Modifier.height(28.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "发热警戒",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (member.hasFeverAlarm) Rose500 else Color(0xFF64748B)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onEdit,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("✏️ 编辑档案", fontSize = 11.sp, color = Teal600, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(32.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECDD3))
                    ) {
                        Text("删除", fontSize = 11.sp, color = Rose500)
                    }
                }
            }
        }
    }
}

@Composable
private fun SystemPreferencesPanel() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // 1. 用药与复测闹钟
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "⏰ 智能复测与用药闹钟", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "基于医学安全间隔（美林6h/非布司他每日晚）下发全家协同提醒", fontSize = 11.sp, color = Color(0xFF64748B))

                Spacer(modifier = Modifier.height(8.dp))

                SettingRowItem(
                    title = "退热药 4-6h 安全锁提醒",
                    desc = "严防多位长辈或看护人重复喂药过量",
                    checked = true
                )
                SettingRowItem(
                    title = "高热 1 小时复测体温提醒",
                    desc = "服药后 60 分钟自动推送复测提醒",
                    checked = true
                )
                SettingRowItem(
                    title = "慢病每日用药定时闹钟",
                    desc = "血压降压药、痛风降尿酸药早晚提醒",
                    checked = false
                )
            }
        }

        // 2. 智能硬件直连
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "📶 智能硬件协同 (蓝牙 / NFC)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "直连家用健康仪器，免手动输入自动入库", fontSize = 11.sp, color = Color(0xFF64748B))

                Spacer(modifier = Modifier.height(8.dp))

                SettingRowItem(
                    title = "蓝牙持续体温贴",
                    desc = "贴附测温，夜间高热超阈值蜂鸣报警 (已就绪)",
                    checked = false
                )
                SettingRowItem(
                    title = "智能臂式血压计同步",
                    desc = "长辈测完血压自动同步高压/低压/脉搏",
                    checked = false
                )
                SettingRowItem(
                    title = "指尖血尿酸检测仪",
                    desc = "蓝牙即测即传，痛风发作期指标追踪",
                    checked = false
                )
            }
        }

        // 3. 数据安全与隐私
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "🔒 一人一档与数据安全", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "纯本地存储（Local-First SQLite），零未经授权云端上传", fontSize = 11.sp, color = Color(0xFF64748B))

                Spacer(modifier = Modifier.height(8.dp))

                SettingRowItem(
                    title = "脱敏就诊交接长图导出",
                    desc = "一键生成发热热峰与用药交接单提供给接诊医生",
                    checked = true
                )
                SettingRowItem(
                    title = "本地数据库自动安全快照",
                    desc = "每日静默备份健康历史至本地存储",
                    checked = true
                )
            }
        }
    }
}

@Composable
private fun SettingRowItem(
    title: String,
    desc: String,
    checked: Boolean
) {
    var isChecked by remember { mutableStateOf(checked) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Text(text = desc, fontSize = 10.sp, color = Color(0xFF64748B))
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            modifier = Modifier.height(28.dp)
        )
    }
}
