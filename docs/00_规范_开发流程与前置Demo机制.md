# 🌿 《安康记》工程开发规范：OpenSpec 驱动与前置 Demo 设计机制

> **核心原则**：规范先导 · 体验前置 · 严谨医疗 · 离线优先

---

## 1. 软件开发总工作流（三步闭环）

为了确保每一项功能都经过充分的人机工程学推敲、医学逻辑验证以及规范化架构演进，本项目严格执行**三步闭环开发机制**：

```
┌─────────────────────────────────┐
│ Step 1: 前置高保真交互 Demo 设计 │ ➔ 在 design/prototypes/ 下开发 HTML/JS 原型并真机评审
└────────────────┬────────────────┘
                 ▼
┌─────────────────────────────────┐
│ Step 2: OpenSpec 规格与变更管理  │ ➔ openspec new change 制定 spec/design/tasks
└────────────────┬────────────────┘
                 ▼
┌─────────────────────────────────┐
│ Step 3: Android 原生工程编码与测试│ ➔ 在 android/ 下编写 Kotlin + Compose + Room 实现
└─────────────────────────────────┘
```

---

## 2. 详细规约

### 规约 1：新功能必须“原型前置（Prototype First）”
- **禁止未见 Demo 直接写 Android 生产代码**。
- 任何新增功能模块、涉及紧急发热用药、排便图谱、老人慢病、门诊长单或复杂手势变动的需求，必须首先在 `design/prototypes/` 目录下创建独立的 HTML5/CSS3/JS 高保真原型（命名遵循 `page_X_xxx.html`），并同步挂载至 `design/prototypes/index.html`。
- 原型需在手机端真实视口尺寸（390px~412px）下完成**触控点按、动画过渡、极速录入步骤**的人机工学验证，经评审确认无误后方可推进。

### 规约 2：严格遵循 OpenSpec 规范
- 任何架构或业务变更，必须使用项目内置的 `openspec` CLI 进行全生命周期跟踪：
  1. `openspec new change "<change-name>"`：创建变更提案；
  2. 严守四部曲工件：
     - `proposal.md`：核心诉求与动机（Why & What Changes & Capabilities）；
     - `specs/<capability>/spec.md`：严格的规范要求与测试场景（SHALL/MUST 与 WHEN/THEN）；
     - `design.md`：架构设计、选型原因与风险权衡（Context, Decisions, Trade-offs）；
     - `tasks.md`：原子化拆解的任务清单（必须采用 `- [ ] X.Y` 格式）；
  3. `openspec validate <change-name>`：校验规范完整性；
  4. 实施完成并通过测试后，使用 `openspec archive <change-name>` 将变更归档至主干 specs。

### 规约 3：Android 原生技术栈标准
- **语言**：Kotlin 2.0+（严格空安全、类型推断、协程并发）
- **UI 框架**：Jetpack Compose + Material Design 3（声明式组件、主题动态切换、无感重组优化）
- **架构模式**：MVI / UDF（单向数据流），全局维护唯一人员上下文（`currentMemberId`），杜绝看板与档案状态分裂
- **数据层**：Room SQLite 本地持久化，贯彻**本地优先（Local-First）**，确保夜间弱网或无网络环境下 0 毫秒极速记录
- **配色系统**：
  - Teal（#0D9488）：治愈青，主色调与日常健康态
  - Rose（#F43F5E）：警戒粉，发热超温、过敏红线警示
  - Amber（#F59E0B / #D97706）：慢病橙，长辈尿酸、高血压管理
  - Slate（#0F172A / #F8FAFC）：沉静灰，背景与次要阅读文本

---

## 3. 目录结构职责划分

| 目录路径 | 职责与内容 | 维护规范 |
| :--- | :--- | :--- |
| `android/` | Android 原生应用程序源码工程（Gradle、Compose、Room） | 遵循官方 Clean Architecture 与 Kotlin 代码风格 |
| `design/prototypes/` | 所有高保真交互 Demo、HTML 原型、测试脚本与索引大盘 | **开发前置必须产出**，保持内部相对链接健康 |
| `design/assets/` | 原型设计资产、SVG 图标、调色板切图 | 保持轻量矢量化 |
| `docs/` | 业务 PRD、医学规则、用药罗盘防重服算法等深度规范说明 | 医疗专业性与逻辑算法说明 |
| `openspec/` | OpenSpec 规范中心（`specs/`, `changes/`, `config.yaml`） | 由 `openspec` CLI 驱动与验证 |
