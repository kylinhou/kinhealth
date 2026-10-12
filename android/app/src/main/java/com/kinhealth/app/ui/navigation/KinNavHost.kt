package com.kinhealth.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kinhealth.app.KinHealthApplication
import com.kinhealth.app.data.HealthRecordEntity
import com.kinhealth.app.repository.MemberContextRepository
import com.kinhealth.app.ui.components.AddMemberDialog
import com.kinhealth.app.ui.components.BatchSymptomDialog
import com.kinhealth.app.ui.components.KinBottomBar
import com.kinhealth.app.ui.components.KinTab
import com.kinhealth.app.ui.dashboard.DashboardScreen
import com.kinhealth.app.ui.dossier.HealthDossierScreen
import com.kinhealth.app.ui.quickinput.QuickInputScreen
import com.kinhealth.app.ui.settings.FamilySettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun KinHealthMainApp() {
    val context = LocalContext.current.applicationContext as KinHealthApplication
    val coroutineScope = rememberCoroutineScope()

    // 观察本地 Room 数据库的在册家庭成员列表 (响应式 Single Source of Truth)
    val members by context.database.memberDao().getAllMembers().collectAsState(initial = emptyList())
    val currentMemberId by MemberContextRepository.currentMemberId.collectAsState()

    // 全局成员上下文自愈：若列表非空，且当前成员不在列表中，自动选中首位成员
    LaunchedEffect(members, currentMemberId) {
        if (members.isNotEmpty()) {
            val exists = members.any { it.id == currentMemberId }
            if (!exists) {
                MemberContextRepository.setCurrentMember(members.first().id)
            }
        }
    }

    // 响应式观察当前成员关联的所有健康记录
    val currentMemberRecords by remember(currentMemberId) {
        if (currentMemberId != null) {
            context.database.healthRecordDao().getRecordsByMember(currentMemberId!!)
        } else {
            flowOf(emptyList())
        }
    }.collectAsState(initial = emptyList())

    val activeMember = members.find { it.id == currentMemberId } ?: members.firstOrNull()

    var currentTab by remember { mutableStateOf(KinTab.DASHBOARD) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showBatchSymptomDialog by remember { mutableStateOf(false) }
    var batchDialogInitialOffset by remember { mutableStateOf(0) }

    // 持久化保存记录的统一回调
    val onSaveHealthRecord: (HealthRecordEntity) -> Unit = { record ->
        coroutineScope.launch(Dispatchers.IO) {
            context.database.healthRecordDao().insertRecord(record)
        }
    }

    // 单条记录删除撤回
    val onDeleteHealthRecord: (Long) -> Unit = { recordId ->
        coroutineScope.launch(Dispatchers.IO) {
            context.database.healthRecordDao().deleteRecordById(recordId)
        }
    }

    Scaffold(
        bottomBar = {
            KinBottomBar(
                currentRoute = currentTab.route,
                onTabSelected = { tab -> currentTab = tab }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                KinTab.DASHBOARD -> DashboardScreen(
                    members = members,
                    currentMemberId = currentMemberId ?: "",
                    records = currentMemberRecords,
                    onSelectMember = { MemberContextRepository.setCurrentMember(it) },
                    onNavigateToQuickInput = { currentTab = KinTab.QUICK_INPUT },
                    onOpenBatchSymptomInput = { offset ->
                        batchDialogInitialOffset = offset
                        showBatchSymptomDialog = true
                    },
                    onDeleteRecord = onDeleteHealthRecord,
                    onAddMemberClick = { showAddDialog = true }
                )
                KinTab.QUICK_INPUT -> QuickInputScreen(
                    members = members,
                    currentMemberId = currentMemberId ?: "",
                    onSaveRecord = onSaveHealthRecord,
                    onAddMemberClick = { showAddDialog = true }
                )
                KinTab.DOSSIER -> HealthDossierScreen(
                    members = members,
                    currentMemberId = currentMemberId ?: "",
                    onSelectMember = { MemberContextRepository.setCurrentMember(it) },
                    onAddMemberClick = { showAddDialog = true }
                )
                KinTab.SETTINGS -> FamilySettingsScreen(
                    members = members,
                    onAddMemberClick = { showAddDialog = true },
                    onUpdateMember = { updatedMember ->
                        coroutineScope.launch(Dispatchers.IO) {
                            context.database.memberDao().insertOrUpdate(updatedMember)
                        }
                    },
                    onDeleteMember = { id ->
                        coroutineScope.launch(Dispatchers.IO) {
                            context.database.memberDao().deleteMemberById(id)
                            if (currentMemberId == id) {
                                val remaining = members.filter { it.id != id }
                                MemberContextRepository.setCurrentMember(remaining.firstOrNull()?.id)
                            }
                        }
                    }
                )
            }

            // 1. 添加家庭成员交互弹窗
            if (showAddDialog) {
                AddMemberDialog(
                    onDismissRequest = { showAddDialog = false },
                    onConfirm = { newEntity ->
                        coroutineScope.launch(Dispatchers.IO) {
                            context.database.memberDao().insertOrUpdate(newEntity)
                            MemberContextRepository.setCurrentMember(newEntity.id)
                        }
                        showAddDialog = false
                    }
                )
            }

            // 2. 首页唤起的感冒病程批量记录交互抽屉 (支持所选日期联动)
            if (showBatchSymptomDialog && activeMember != null) {
                BatchSymptomDialog(
                    member = activeMember,
                    initialDateOffsetDays = batchDialogInitialOffset,
                    onDismissRequest = { showBatchSymptomDialog = false },
                    onConfirm = { newRecord ->
                        onSaveHealthRecord(newRecord)
                        showBatchSymptomDialog = false
                    }
                )
            }
        }
    }
}
