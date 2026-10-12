## ADDED Requirements

### Requirement: Dynamic Family Member Modification
系统 MUST 支持对已存在的家庭成员档案进行完整编辑与修改。用户在家庭设置页面轻触成员卡片或点击“编辑”按钮时，系统 SHALL 唤起信息编辑弹窗，回填该成员当前的姓名、角色模板、年龄/月龄、体重（用于儿科剂量换算）以及已选过敏红线标签。当用户修改完成并确认后，系统 SHALL 立即更新本地 Room 数据库并刷新全局当前上下文展示。

#### Scenario: User edits baby weight for pediatric dosing accuracy
- **WHEN** 伴随宝宝生长发育，家长进入家庭设置点击“编辑”小宝的档案，将体重从 9.5kg 修改为 10.2kg 并保存
- **THEN** 系统更新本地数据库中小宝的记录，卡片显示最新体重 10.2kg，用药安全计算罗盘即刻基于最新体重作为换算基准。

### Requirement: Member Deletion Safety Guard
系统 MUST 在执行删除家庭成员操作前展示二次确认警示对话框（Confirmation Dialog），严禁未经确认的单触即删。对话框 SHALL 明确警示“删除该成员将同步移除其专属健康档案与历史记录”，并提供“取消”与“确认删除”选项，防止看护人家长误触造成家庭健康数据不可逆丢失。

#### Scenario: User clicks delete but cancels on confirmation
- **WHEN** 用户误触某个家庭成员卡片上的“删除”按钮，系统弹出“确定删除该家庭成员档案吗？”确认框
- **THEN** 用户点击“取消”，删除动作中止，该成员档案与关联数据完好保留。

#### Scenario: User confirms member deletion
- **WHEN** 用户点击“删除”并在警示确认框中明确点击“确认删除”
- **THEN** 系统从本地数据库彻底删除该成员，并在被删除成员为当前活跃上下文时，自动切换或重置全局上下文。

### Requirement: Fever and Chronic Condition Toggle
系统 MUST 在家庭成员卡片上提供专病与发热监护状态的快捷切换功能。当启用“发热监护中”时，成员卡片与主页滑轨 SHALL 呈现高对比度发热警戒徽章（如 🔴 发热监护），提醒家庭全体成员关注体温复测与退热间隔。

#### Scenario: Activating fever monitoring mode
- **WHEN** 用户在小宝的成员档案卡片上开启“发热监护”开关
- **THEN** 系统将该成员状态更新为发热警示状态，并在卡片和全局上下文呈现醒目的发热监护标记。

### Requirement: Family Settings and System Preferences Segregation
家庭空间与设置页面 MUST 在视觉与交互上将【👨‍👩‍👧‍👦 家庭成员档案】与【⚙️ 系统与设备设置】进行解耦分离，通过顶部分段选项卡（Segmented Tabs）让用户可清晰聚焦成员档案管理或系统级配置（如用药提醒、蓝牙协同、权限设置）。

#### Scenario: Switching between family members tab and system settings tab
- **WHEN** 用户在设置页面点击顶部的【系统设置】分段按钮
- **THEN** 页面平滑切换至系统设置面板，展示用药安全闹钟、智能体温计直连与数据备份配置，而不再混杂在成员列表中。
