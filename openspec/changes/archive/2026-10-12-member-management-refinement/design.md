## Context

见 `proposal.md`。当前应用已构建了基于 Room SQLite 的 `MemberEntity` 与 `MemberDao`，但界面层仅支持初次新增与直接删除，缺少修改编辑能力、删除二次确认防误触机制、以及原型中的选项卡解耦。

## Goals / Non-Goals

**Goals:**
- 提供无缝的家庭成员编辑对话框（回填初始值、更新姓名、角色、年龄、体重、过敏标签）。
- 提供 Material 3 规范的删除成员二次确认对话框，防止看护人家长误触丢数据。
- 成员卡片支持一键切换发热监护状态（`hasFeverAlarm`）并实时刷新徽章。
- 将设置界面重构为顶部分段选项卡：【家庭在册档案】与【系统与设备设置】，实现信息架构的彻底解耦。

**Non-Goals:**
- 本阶段不引入多端云端后端同步（维持本地优先 Local-First 架构）。
- 不破坏现有 `MemberEntity` 的 Room 表结构，字段完全向前兼容。

## Decisions

### 1. 成员编辑与新增表单复用 (`MemberFormDialog`)
- **方案决策**：将原有 `AddMemberDialog.kt` 扩展升级为通用的 `MemberFormDialog.kt`，支持 `initialMember: MemberEntity?` 参数。
  - 当 `initialMember == null` 时为“添加新成员”模式，标题为“➕ 3 步向导添加新家人”，ID 自动生成 UUID；
  - 当 `initialMember != null` 时为“编辑成员档案”模式，标题为“✏️ 修改家庭成员档案”，回填原角标、姓名、年龄、体重及已选过敏项，保存时保留原 ID。
- **替代方案对比**：分开写两个 Dialog 会产生大量冗余的标签点选与校验代码；复用通用组件保证了添加和编辑体验与设计的一致性。

### 2. 删除防误触设计
- 使用 Material 3 标准 `AlertDialog`，当用户点击“删除”时暂存待删除目标 ID 与名称，弹出醒目警告：“确定要删除【XXX】的档案吗？此操作将同步移除其健康与用药数据且不可恢复”。

### 3. 架构解耦与顶部 Segmented Tabs
- 在 `FamilySettingsScreen.kt` 顶部引入 Tab 状态 `selectedTab: SettingsTab`（`MEMBERS` vs `SYSTEM`）。
  - `MEMBERS`：展示家庭在册概览、成员卡片（含发热开关、编辑、删除）以及添加新成员按钮；
  - `SYSTEM`：展示高频设置组（用药复测闹钟、蓝牙硬件协同、一人一档数据导出与隐私权限），与交互原型 `page_6_member_settings.html` 保持一致。

## Risks / Trade-offs

- **[风险 1: 当前活跃成员被删除]** → 若用户删除了当前正在查看的成员，系统必须自动 fallback 到列表剩余成员中的第一个，若全部删空则重置活跃上下文为空，触发全局空状态保护。
- **[风险 2: 体重浮点数输入格式错误]** → 表单对体重输入严格执行 `toDoubleOrNull()` 校验，默认回退安全值（如 10.0kg），避免发生崩溃。
