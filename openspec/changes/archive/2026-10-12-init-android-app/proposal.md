## Why

目前《安康记》已完成全套产品需求文档（PRD）与高保真交互原型设计，并确立了 4-Tab 极简架构与“一人一档”就诊数据中枢。为了将原型落地为真实可用的 Android 移动端应用，需要在当前项目中建立标准规范的工程组织架构，采用现代 Android 原生技术栈（Kotlin + Jetpack Compose + Material Design 3 + Room 离线优先数据库），并确立“后续任何新功能开发前必须先在 design/prototypes 产出高保真 demo”的开发流水线规范。

## What Changes

- **工程目录规范化与重构**：
  - 将所有 HTML 交互 Demo 与设计大盘归拢至 `design/prototypes/` 目录；
  - 确立新功能开发流水线规范：先交互原型 Demo ➔ 再 OpenSpec 规格 ➔ 后 Android 端编码；
  - 更新根目录导航入口与 `README.md` 工程架构全景。
- **Android 原生项目初始化**：
  - 在 `android/` 目录下初始化标准 Android Gradle 工程结构；
  - 技术选型：Kotlin 2.0+、Jetpack Compose、Material 3 暖愈医疗主题设计系统、Navigation Compose、MVI/MVVM 架构（UDF 状态流）、Room 离线优先本地数据库。
- **核心数据模型与架构搭建**：
  - 建立家庭成员实体（Member: 姓名、角色、月龄/年龄、体重、过敏红线、6槽位配置）；
  - 建立健康流水记录表（Record: 体温、排便、喂奶、用药、尿酸、睡眠）；
  - 建立全局人员上下文状态流（Global Member Context: Single Source of Truth，保证今日看板与健康档案严格联动一致）。

## Capabilities

### New Capabilities
- `family-health-core`: 涵盖家庭多成员管理与全局上下文联动、儿童发热用药安全罗盘（防误服重复服）、极速记录（布里斯托/喂养/指标）、一人一档健康档案与就诊长单生成的核心业务规则与数据规范。

### Modified Capabilities
（无现有已发布 capability，本次为初始构建）

## Impact

- **文件系统影响**：HTML 原型文件整理至 `design/prototypes/`；新增 `android/` 工程代码根目录；新增 `openspec/` 规范目录。
- **技术栈依赖**：Android SDK (API 34/35)、Kotlin、Jetpack Compose、Room SQLite。
- **破坏性变更**：无代码破坏性变更，原型文件相对路径在 `design/prototypes/` 内部自包含保持可用。
