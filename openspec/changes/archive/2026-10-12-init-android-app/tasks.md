## 1. 目录整理与规范固化

- [x] 1.1 将根目录原型文件归拢至 `design/prototypes/` 并验证页面间相对跳转正常
- [x] 1.2 编写 `docs/00_规范_开发流程与前置Demo机制.md`，确立“新功能必须先做原型 Demo 验证再编码”的开发规约
- [x] 1.3 更新根目录 `index.html` 与 `README.md`，提供工程全景与架构导航

## 2. Android 原生工程搭建

- [x] 2.1 创建 `android/` 目录结构与 Gradle 构建配置 (`build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`)，验证配置语法无误
- [x] 2.2 配置 `app/build.gradle.kts` 与 `AndroidManifest.xml`，引入 Kotlin、Jetpack Compose、Material 3 与 Room 依赖
- [x] 2.3 创建 `AnkangApplication.kt` 与 `MainActivity.kt` 入口，建立 Compose 主界面装载点

## 3. 领域模型与 Room 离线数据库

- [x] 3.1 编写 `Member.kt` 与 `MemberEntity.kt`，实现家庭成员档案与特异体质/过敏/6槽位模型
- [x] 3.2 编写 `HealthRecord.kt` 与 `HealthRecordEntity.kt`，涵盖体温、排便（布里斯托）、喂养、用药等指标
- [x] 3.3 编写 `MemberDao.kt` 与 `HealthRecordDao.kt`，实现本地优先离线 CRUD 操作
- [x] 3.4 编写 `AnkangDatabase.kt`，实现本地 SQLite 数据库实例与初始 Mock 数据（安安、张大爷、林女士）自动植入

## 4. 全局人员上下文与业务逻辑层

- [x] 4.1 编写 `MemberContextRepository.kt`，通过 `StateFlow<String>` 提供全局单一人选上下文（Single Source of Truth）
- [x] 4.2 编写 `MedicationCompassHelper.kt`，实现退热药（泰诺林/美林）安全时间锁（4-6h/6-8h）与防重复给药判定算法

## 5. Compose UI 设计系统与 4-Tab 框架

- [x] 5.1 编写 `Theme.kt` 与 `Color.kt`，定义暖愈医疗色彩系统（Teal 治愈青、Rose 警戒粉、Amber 慢病橙、Slate 沉静灰）
- [x] 5.2 编写 `AnkangBottomBar.kt`，实现标准 4-Tab 底部导航：今日看板、极速记、健康档案、家庭设置
- [x] 5.3 编写 `DashboardScreen.kt`（今日看板，顶部大滑轨 + 重点卡片）
- [x] 5.4 编写 `HealthDossierScreen.kt`（健康档案·一人一档，顶部微型身份胶囊 + 3大子资产视角）
- [x] 5.5 编写 `QuickInputScreen.kt`（极速录入，体温调节 + 布里斯托 7 级图谱点选）
- [x] 5.6 编写 `FamilySettingsScreen.kt`（家庭空间与系统设备设置）

## 6. OpenSpec 校验与集成验证

- [x] 6.1 运行 `openspec validate init-android-app`，确保所有 Proposal、Spec、Design、Tasks 严格通过规范校验
- [x] 6.2 验证 Android 源码语法完整性与目录结构组织规范
