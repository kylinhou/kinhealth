## 1. 清理数据层假数据

- [x] 1.1 改造 `KinHealthDatabase.kt`，彻底移除初始假数据种子注入回调，保证数据库初始状态纯净为 0 人
- [x] 1.2 改造 `MemberContextRepository.kt`，初始默认成员 ID 设为空字符串，并支持空上下文与自愈机制

## 2. 交互式添加家庭成员组件实现

- [x] 2.1 编写 `ui/components/AddMemberDialog.kt`，支持角色 Emoji/预设网格选择、姓名输入、年龄/月龄、体重与过敏红线输入
- [x] 2.2 在 Dialog 确认事件中，生成唯一 ID 并构造 `MemberEntity` 传递给回调，支持轻量校验（姓名必填）

## 3. 页面级数据驱动与空状态处理

- [x] 3.1 改造 `FamilySettingsScreen.kt`，通过 Room Dao 真实加载家庭成员列表，点击“添加家人”唤起 `AddMemberDialog` 并持久化入库
- [x] 3.2 改造 `DashboardScreen.kt`，动态从数据库加载成员列表；当成员为空时展示友好空状态与“添加家人”引导
- [x] 3.3 改造 `HealthDossierScreen.kt` 与 `QuickInputScreen.kt`，在无成员时安全展示空状态引导，避免无目标记录异常
- [x] 3.4 改造 `KinNavHost.kt` 与 `MemberCapsulePill.kt`，实现全 App 成员增删与上下文无缝联动

## 4. 验证与 OpenSpec 校验

- [x] 4.1 运行 `openspec validate member-management-and-empty-state`，确保变更规范严格合法
- [x] 4.2 验证各界面代码逻辑与空状态交互完整性
