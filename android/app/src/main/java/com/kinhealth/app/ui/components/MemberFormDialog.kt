package com.kinhealth.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.kinhealth.app.data.MemberEntity
import com.kinhealth.app.ui.theme.Rose500
import com.kinhealth.app.ui.theme.Teal600
import java.util.UUID

data class RolePreset(
    val avatar: String,
    val roleName: String,
    val defaultTag: String
)

val PRESETS = listOf(
    RolePreset("👶", "宝宝", "儿科监护"),
    RolePreset("👧", "女孩", "健康成长"),
    RolePreset("👦", "男孩", "健康成长"),
    RolePreset("👵", "奶奶/外婆", "慢病健康"),
    RolePreset("👴", "爷爷/外公", "慢病健康"),
    RolePreset("👩", "妈妈", "健康成人"),
    RolePreset("👨", "爸爸", "健康成人"),
    RolePreset("🧑", "自己", "日常体检")
)

@Composable
fun MemberFormDialog(
    initialMember: MemberEntity? = null,
    onDismissRequest: () -> Unit,
    onConfirm: (MemberEntity) -> Unit
) {
    val isEditMode = initialMember != null

    var selectedPreset by remember(initialMember) {
        val matched = PRESETS.firstOrNull { it.avatar == initialMember?.avatar }
            ?: PRESETS.firstOrNull { it.roleName == initialMember?.roleText }
            ?: PRESETS[0]
        mutableStateOf(matched)
    }

    var name by remember(initialMember) {
        mutableStateOf(initialMember?.name.orEmpty())
    }
    var ageText by remember(initialMember) {
        val initialAge = initialMember?.ageText.orEmpty()
        mutableStateOf(if (initialAge == "未知年龄") "" else initialAge)
    }
    var weightText by remember(initialMember) {
        val w = initialMember?.weightKg ?: 0.0
        mutableStateOf(if (w > 0) w.toString() else "")
    }
    var allergiesText by remember(initialMember) {
        val a = initialMember?.allergiesCsv.orEmpty()
        mutableStateOf(if (a == "无已知过敏") "" else a)
    }
    var hasError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (isEditMode) "✏️ 编辑家庭成员档案" else "➕ 添加家庭成员档案",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = if (isEditMode) "更新生长月龄、体重与特异体质红线" else "建立一人一档，专属性监护全家健康",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 1. 角色预设与头像选择
                Text(
                    text = "1. 选择身份预设",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PRESETS.forEach { preset ->
                        val isSelected = preset.avatar == selectedPreset.avatar
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFFF0FDFA) else Color(0xFFF8FAFC),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Teal600 else Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier
                                .clickable { selectedPreset = preset }
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = preset.avatar, fontSize = 22.sp)
                                Text(
                                    text = preset.roleName,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Teal600 else Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. 姓名输入 (必填)
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (it.isNotBlank()) hasError = false
                    },
                    label = { Text("家人姓名 / 昵称 (必填)") },
                    placeholder = { Text("例如：小安 / 爸爸") },
                    isError = hasError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (hasError) {
                    Text(
                        text = "请输入家人姓名",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 3. 年龄/月龄与体重
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = ageText,
                        onValueChange = { ageText = it },
                        label = { Text("年龄 / 月龄") },
                        placeholder = { Text("8个月 / 65岁") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text("体重 (kg)") },
                        placeholder = { Text("如 9.2") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 4. 特殊体质与过敏红线
                OutlinedTextField(
                    value = allergiesText,
                    onValueChange = { allergiesText = it },
                    label = { Text("过敏红线 / 慢病备注") },
                    placeholder = { Text("例如：鸡蛋清、青霉素禁忌、高血压 (无则留空)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 5. 底部操作按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("取消", color = Color(0xFF64748B))
                    }

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                hasError = true
                                return@Button
                            }
                            val weight = weightText.toDoubleOrNull() ?: (initialMember?.weightKg ?: 0.0)
                            val targetId = initialMember?.id ?: UUID.randomUUID().toString().substring(0, 8)
                            val newEntity = MemberEntity(
                                id = targetId,
                                name = name.trim(),
                                avatar = selectedPreset.avatar,
                                roleText = selectedPreset.roleName,
                                ageText = if (ageText.isBlank()) "未知年龄" else ageText.trim(),
                                weightKg = weight,
                                currentTag = initialMember?.currentTag ?: selectedPreset.defaultTag,
                                hasFeverAlarm = initialMember?.hasFeverAlarm ?: false,
                                allergiesCsv = if (allergiesText.isBlank()) "无已知过敏" else allergiesText.trim()
                            )
                            onConfirm(newEntity)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isEditMode) "保存修改" else "保存入库", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteMemberConfirmDialog(
    member: MemberEntity,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "确认删除家庭成员？", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Text(
                text = "您即将删除【${member.name} (${member.roleText})】的家庭档案。\n删除后，该成员关联的所有专属健康记录与就诊病历将同步移除且无法恢复，请确认是否继续？",
                fontSize = 13.sp,
                color = Color(0xFF475569)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                colors = ButtonDefaults.buttonColors(containerColor = Rose500),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("确认删除", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismissRequest,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("取消", color = Color(0xFF64748B))
            }
        }
    )
}

/**
 * 兼容旧调用的 AddMemberDialog 包装器
 */
@Composable
fun AddMemberDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (MemberEntity) -> Unit
) {
    MemberFormDialog(
        initialMember = null,
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}
