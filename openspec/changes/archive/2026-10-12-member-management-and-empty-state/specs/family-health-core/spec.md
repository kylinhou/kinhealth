## ADDED Requirements

### Requirement: Dynamic Family Member Creation
系统 MUST 支持用户通过界面动态添加家庭成员，提供角色预设选择（如宝宝、长辈、妈妈、爸爸、自己）、姓名输入、月龄/年龄输入、体重输入以及过敏红线说明，并将新成员数据安全持久化到本地 Room 数据库中。新增完成后，系统 SHALL 自动将该成员激活为全局当前活跃成员（Current Member Context）。

#### Scenario: User successfully creates a new family member
- **WHEN** 用户点击“添加新成员”，输入姓名“小宝”、选择角色“宝宝”、输入体重“9.5kg”并点击“保存”
- **THEN** 系统将新成员插入本地数据库，关闭添加向导，全局成员上下文自动切换至“小宝”，且今日看板与家庭设置列表立即呈现小宝的档案信息。

### Requirement: Zero Member Empty State Handling
当本地数据库中家庭成员列表为空（Zero Members）时，系统 MUST 呈现清晰友好的空状态（Empty State）引导，严禁展示虚假的内置人员或发生空指针异常。各核心 Tab（今日看板、极速记、健康档案）SHALL 明确提示无成员，并提供直接跳转添加成员的行动点（CTA）。

#### Scenario: Launching application with empty database
- **WHEN** 新用户初次启动应用且数据库无任何在册成员
- **THEN** 今日看板与健康档案不显示任何虚假测试人员，而是展示“暂无家庭成员”友好占位卡片，并展示“➕ 添加第一位家庭成员”按钮引导用户建立档案。
