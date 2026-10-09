# 🤖 安康记 Android 客户端工程说明

> 基于现代 Android 原生架构与规范打造的家庭健康管理移动应用。

---

## 🛠️ 技术栈选型

- **开发语言**: Kotlin 2.0+ (严格空安全与协程并发)
- **UI 框架**: Jetpack Compose + Material Design 3 (声明式暖愈医疗设计系统)
- **架构模式**: MVI / UDF (单向数据流与不可变状态驱动)
- **数据持久化**: Room SQLite Database (本地优先 Local-First 离线存储，家庭健康隐私本地自愈)
- **异步处理**: Kotlin Coroutines + StateFlow
- **导航组件**: Jetpack Navigation Compose (标准 4-Tab 底部路由)

---

## 📂 源码目录结构

```
android/app/src/main/java/com/KinHealth/app/
├── KinHealthApplication.kt            # 应用生命周期基类，初始化本地 Room 数据库
├── MainActivity.kt                 # 单 Activity 声明式 Compose 主容器
├── data/                           # 数据层 (Room)
│   ├── KinHealthDatabase.kt           # 数据库配置与预置种子数据 (安安、张大爷、林女士)
│   ├── MemberEntity.kt             # 家庭成员数据实体
│   ├── HealthRecordEntity.kt       # 健康流水记录实体
│   ├── MemberDao.kt                # 成员数据访问接口
│   └── HealthRecordDao.kt          # 健康记录访问接口
├── model/                          # 领域模型
│   ├── Member.kt                   # 家庭成员模型
│   ├── HealthRecord.kt             # 健康流水模型
│   ├── RecordType.kt               # 记录类型枚举 (体温/用药/排便/喂养/尿酸/血压)
│   └── BristolStoolScale.kt        # 布里斯托 7 级排便分级模型
├── repository/                     # 业务与状态中枢
│   └── MemberContextRepository.kt  # 全局人员上下文单一数据源 (Single Source of Truth)
├── util/                           # 医学工具类
│   └── MedicationCompassHelper.kt  # 退热药安全时间锁计算器 (4-6h/6-8h 防重复服药)
└── ui/                             # 表现层 (Compose UI)
    ├── theme/                      # 暖愈医疗配色规范 (Teal/Rose/Amber/Slate)
    ├── components/                 # 通用组件 (4-Tab栏/微型身份胶囊/体温步进器/布里斯托点选)
    ├── navigation/                 # 4-Tab 框架与状态调度 (KinHealthMainApp)
    ├── dashboard/                  # Tab 1: 今日看板 (大头像滑轨 + 情境聚焦卡片)
    ├── quickinput/                 # Tab 2: 极速记 (2-Tap 体温/排便保存)
    ├── dossier/                    # Tab 3: 健康档案·一人一档 (三大资产视角 + 门诊长单)
    └── settings/                   # Tab 4: 家庭设置 (3人协同 + 添加家人向导)
```

---

## 🚀 核心架构亮点

1. **一人一档全局联动 (Single Source of Truth)**:
   - 全局由 `MemberContextRepository` 集中管理 `currentMemberId`。
   - 用户在首页选择“安安”，切到“健康档案”时自动呈现安安的专属发热交接单与过敏红线；切换至“张大爷”时，档案与首页均自适应切换为痛风尿酸与慢病用药。
2. **用药安全罗盘防重复给药**:
   - `MedicationCompassHelper` 精确按毫秒时间戳计算服药安全窗口，严格阻止多位家长重叠喂药。
3. **布里斯托 7 级图谱极速记**:
   - 2 次轻触内完成大便性状与体温数值保存，0 毫秒完成本地持久化。

