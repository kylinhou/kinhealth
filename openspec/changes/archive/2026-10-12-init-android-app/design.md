## Context

《安康记》已完成 PRD 与高保真交互原型设计。为了将交互规范转化为高质量、安全严谨的 Android 应用程序，需要采用现代 Android 原生架构，并建立与“前置 Demo 设计流程”紧密结合的规范化工程体系。

## Goals / Non-Goals

**Goals:**
- 搭建标准的 Android Gradle 原生工程结构（`android/`）；
- 确立统一的技术架构：Kotlin 2.0+、Jetpack Compose、Material 3 暖愈医疗配色、MVI/UDF 状态流、Room 本地离线优先存储；
- 贯彻“一人一档（One Dossier Per Person）”与“全局人员上下文”底层数据驱动；
- 固化“新功能前置高保真 Demo 设计”机制，确保每一个进入开发的模块均在 `design/prototypes/` 中有可闭环检验的交互原型。

**Non-Goals:**
- 本阶段不引入第三方过度复杂的云端微服务同步，坚持以本地优先（Local-First）离线可用为主；
- 本阶段不开发跨平台 iOS/Web 版本，聚焦打磨极致体验的 Android 原生应用。

## Decisions

### 1. 技术栈选型：Kotlin + Jetpack Compose + Material 3
- **决策**：采用 Google 官方推荐的纯原生声明式 UI 框架 Jetpack Compose，配合 Material Design 3 暖愈医疗设计系统。
- **原因**：发热罗盘、夜间双药安全锁、布里斯托图谱等包含大量高灵敏度手势与实时动画；Compose 拥有优异的状态驱动重组性能与无缝暗黑模式适配。
- **备选方案比较**：
  - Flutter/React Native：增加多层桥接与打包体积，且在医疗设备硬件联动（蓝牙耳温枪/血压计）上原生效能更高。

### 2. 状态架构：MVI / UDF 单向数据流
- **决策**：应用内所有状态变更通过统一的 `Intent` 触发，由 `ViewModel` 结合 Kotlin `StateFlow` 输出不可变 `UiState`。
- **原因**：核心业务强调“全局成员上下文一致性（Single Source of Truth）”。通过全局 `MemberContextRepository` 集中派发 `currentMemberId`，无论用户在今日看板、极速记或健康档案，均严格消费同一人员上下文。

### 3. 持久化存储：Room 本地优先数据库 (Local-First)
- **决策**：采用 Android 官方 Room ORM。
- **数据表划分**：
  - `MemberEntity`: 成员档案（id, name, avatar, role, birthDate, weight, allergies, quick_slots）
  - `HealthRecordEntity`: 综合流水（id, memberId, timestamp, type, valueNumeric, valueJson, notes）
  - `MedicationPlanEntity`: 用药计划与安全锁间隔配置
- **原因**：夜间发热与紧急就诊场景往往处于离线或地下室弱网环境，本地数据库能保障 0 毫秒保存与绝对隐私安全。

### 4. 流程规范：前置 Demo 设计流水线
- **规范**：以后任何新增业务功能或复杂交互改造，遵循以下三步法闭环：
  1. **Step 1 (前置 Demo)**: 在 `design/prototypes/` 下开发对应功能的高保真 HTML/JS 原型，并集成至 `index.html` 原型大盘进行评审；
  2. **Step 2 (OpenSpec 规范)**: 运行 `openspec new change` 编写能力变更规范（spec.md、design.md、tasks.md）；
  3. **Step 3 (Android 编码与测试)**: 依据已验证的 Demo 交互逻辑，在 `android/` 中编写 Kotlin/Compose 实现与单元测试。

## Risks / Trade-offs

- **[风险 1: 内存与重组开销]** → **缓解方案**: 对趋势折线图与日历打卡列表使用 `remember` 与 Canvas 纯矢量绘制，避免产生冗余 Compose 节点。
- **[风险 2: 医疗时间计算越界]** → **缓解方案**: 泰诺林与美林的安全时间锁计算统一采用 Java `Instant`/`java.time` 标准时间戳计算，杜绝跨时区或夏令时计算偏移。
