# 🌿 安康记 (KinHealth) - 全家健康管理系统

> **产品定位**：面向中国家庭多人口（婴幼儿·老年长辈·中坚成人）的暖愈极简健康监护应用。  
> **核心架构**：标准 4-Tab 极简框架 (`[ 🏠 今日看板 ]` - `[ ➕ 极速记 ]` - `[ 📁 健康档案 ]` - `[ ⚙️ 家庭设置 ]`)，贯彻“一人一档（One Dossier Per Person）”与“发热退热药防重复给药安全罗盘”。

---

## 🏗️ 规范化工程目录体系

本项目遵循严谨的工业级软件工程与 Spec-driven 规范，目录划分如下：

```
c:\AIHome\安康记\
├── android/            # 🤖 Android 原生工程 (Kotlin 2.0+ · Jetpack Compose · Room 离线优先 · MVI)
├── design/             # 🎨 设计与高保真原型中心
│   ├── prototypes/     # 📱 HTML5/JS 交互原型库与体验大盘 (index.html, page_1 ~ page_6)
│   └── assets/         # 🖼️ 设计切图与矢量图标资产
├── docs/               # 📑 产品 PRD、医学规则算法与交互说明书
│   └── 00_规范_开发流程与前置Demo机制.md # 📌 核心开发铁律
├── openspec/           # 📐 OpenSpec 规格驱动中心 (specs/ · changes/ · config.yaml)
├── index.html          # 🌐 工程全景导览门户 (一键通往原型/规范/代码)
└── README.md           # 📘 本文档
```

---

## 📌 核心开发铁律（前置 Demo 与 OpenSpec）

1. **原型前置（Prototype First）**：
   - **严禁未见 Demo 直接编写 Android 代码**；
   - 任何新功能或复杂交互改造，必须**首先在 `design/prototypes/` 产出对应的高保真 HTML 交互原型**，挂载至原型大盘，并在手机真实视口下完成触控与状态验证。
2. **OpenSpec 规范驱动（Spec-Driven）**：
   - 任何开发变更必须通过 `openspec new change "<name>"` 创建提案并遵循规范工件：
     - `proposal.md`：核心诉求与动机；
     - `specs/<capability>/spec.md`：规范与测试场景（SHALL/MUST 与 WHEN/THEN）；
     - `design.md`：架构设计与权衡；
     - `tasks.md`：原子任务清单；
   - 通过 `openspec validate` 校验后推进实施。
3. **Android 现代原生技术栈**：
   - **语言**：Kotlin 2.0+
   - **UI**：Jetpack Compose + Material 3 暖愈医疗主题（Teal 治愈青 / Rose 警示粉 / Amber 慢病橙）
   - **架构**：MVI / UDF 单向数据流，全局单一人选上下文（`MemberContextRepository`）
   - **存储**：Room SQLite 本地优先离线数据库

---

## 🔗 快速直达

- 🌐 [全景导览门户 (index.html)](file:///c:/AIHome/%E5%AE%89%E5%BA%B7%E8%AE%B0/index.html)
- 🎨 [高保真原型交互大厅 (design/prototypes/index.html)](file:///c:/AIHome/%E5%AE%89%E5%BA%B7%E8%AE%B0/design/prototypes/index.html)
- 📌 [开发流程与前置 Demo 规范说明](file:///c:/AIHome/%E5%AE%89%E5%BA%B7%E8%AE%B0/docs/00_%E8%A7%84%E8%8C%83_%E5%BC%80%E5%8F%91%E6%B5%81%E7%A8%8B%E4%B8%8E%E5%89%8D%E7%BD%AEDemo%E6%9C%BA%E5%88%B6.md)
- 🤖 [Android 工程架构与源码说明](file:///c:/AIHome/%E5%AE%89%E5%BA%B7%E8%AE%B0/android/README.md)

