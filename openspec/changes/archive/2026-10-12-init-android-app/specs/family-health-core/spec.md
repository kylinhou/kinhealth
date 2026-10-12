## Purpose

《安康记》家庭健康管理核心业务能力，提供多家庭成员档案管理、全局成员上下文联动、儿童发热用药安全罗盘（防重复给药）、极速多模态记录以及一人一档严肃就诊数据资产归档与交接单生成。

## ADDED Requirements

### Requirement: Global Member Context Synchronization
系统 MUST 维护全局唯一的活跃家庭成员上下文（Current Member Context）。当用户在【今日看板】切换选中的家庭成员时，【健康档案】等核心视图 SHALL 同步自动更新为该成员的数据上下文，保证全应用状态一致性（Single Source of Truth）。

#### Scenario: Switching member on Dashboard reflects in Health Dossier
- **WHEN** 用户在今日看板顶部成员滑轨中将当前成员从“安安”切换为“张大爷”
- **THEN** 系统更新全局成员上下文为“张大爷”，且当用户切至健康档案 Tab 时，自动展示张大爷的专属慢病档案，顶部胶囊显示“张大爷的专属健康档案”。

### Requirement: Fever Medication Safety Compass
系统 MUST 提供针对儿童发热退热药（对乙酰氨基酚/布洛芬）的安全时间锁与 24 小时频次限制。当距离上次服药时间小于安全间隔（泰诺林 4 小时、美林 6 小时）时，系统 SHALL 锁定给药按钮并展示红色高危警示，防止多位家庭监护人之间发生重复喂药事故。

#### Scenario: Attempting to administer medication within locked window
- **WHEN** 距离安安上次服用对乙酰氨基酚（泰诺林）仅过去 2.5 小时，家长再次打开服药入口
- **THEN** 系统显示“安全锁生效中”，给药动作置灰禁用，并醒目提示“距离下次安全用药还需等待 1.5 小时，严防药物过量蓄积”。

### Requirement: One Dossier Per Person Architecture
健康档案模块 MUST 贯彻“一人一档（One Dossier Per Person）”原则，严禁在同一份就诊档案中混杂多位不同家庭成员的病历、指标与用药数据，确保医疗交接的纯净性与严肃性。

#### Scenario: Viewing dossier in isolation
- **WHEN** 选中“安安”进入健康档案与就诊长单生成视图
- **THEN** 页面只展示安安的发热热峰曲线、退热药已服时间清单、伴随症状与过敏红线（鸡蛋清/青霉素），绝不出现长辈张大爷的痛风尿酸指标或降压药。

### Requirement: Rapid Health Metric Entry
系统 MUST 提供 2-Tap 内即可完成的高频健康记录入口，包括体温数值快速步进调节、布里斯托 7 级排便卡通图谱点选、喂奶亲喂与瓶喂容量记录。

#### Scenario: Quick logging of temperature
- **WHEN** 用户从底部导航进入“极速记”，选中体温卡片并选择 38.8℃
- **THEN** 系统在 2 次轻触内保存该记录至本地数据库，并在今日看板时间流中实时呈现。
