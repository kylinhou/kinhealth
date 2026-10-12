## Context

参考 proposal.md 与 specs/family-health-core/spec.md。目前《安康记》原先在 `KinHealthDatabase` 中注册了 `populateInitialData` 种子注入，导致安装后写死安安等假数据；且 `FamilySettingsScreen.kt` 中的添加成员向导仅为静态描述占位符，未连接真实输入和 Dao 存储。

## Goals / Non-Goals

**Goals:**
- 移除 `KinHealthDatabase` 中的任何测试假数据插入回调，实现 100% 干净的数据库初态；
- 封装高交互性的 `AddMemberDialog`（含角色预设图标、姓名、年龄/月龄、体重、过敏红线输入表单）；
- 贯通数据流：点击保存后调用协程 `memberDao.insertOrUpdate(entity)`，并触发 `MemberContextRepository.setCurrentMember(id)`；
- 在【今日看板】、【极速记】与【健康档案】中提供统一的优雅空状态（Empty State），并在各页面提供呼出添加成员对话框的入口；
- 列表非空时，自动将有效成员渲染至滑轨与微型胶囊中。

**Non-Goals:**
- 本阶段不引入远程多端账号鉴权，坚持本地优先（Local-First）离线存储；
- 不引入照片拍照剪裁库，角色头像采用高辨识度的 Emoji/矢量风格预设。

## Decisions

### 1. 弹窗表单组件架构 (AddMemberDialog)
- **设计**：在 `ui/components/AddMemberDialog.kt` 中设计 Material 3 规范的表单对话框；
- **角色预设支持**：
  - 👶 婴儿/幼儿 (自带“儿科监护”场景标签与发热监护建议)
  - 👴 爷爷/长辈 (自带“慢病专科”场景标签)
  - 👵 奶奶/长辈 (自带“慢病专科”场景标签)
  - 👩 妈妈/女性 (自带“健康成人”场景标签)
  - 👨 爸爸/男性 (自带“健康成人”场景标签)
  - 🧑 自己/中坚
- **数据校验**：姓名非空校验，体重与年龄合理性格式转换。

### 2. 全局成员上下文自愈机制 (Context Auto-Healing)
- **设计**：在 UI 根层监听 `membersFlow`：
  - 当 `members` 为空时，`currentMemberId` 保持为可空或空状态；
  - 当 `members` 非空且当前选中的 ID 不在列表内时，自动纠偏选中列表第一位成员；
  - 用户新增成员成功后，主动将上下文定向至该新成员。

## Risks / Trade-offs

- **[风险] 空状态下如果用户进入极速记或健康档案，可能由于缺少成员 ID 引发异常**
  - **缓解方案**：在极速记与健康档案中增加判空逻辑，若无成员则展示“请先添加家庭成员”友好引导卡片，禁用保存动作并提供一键添加按钮。
