package com.ankangcare.app.model

data class BristolStoolScale(
    val type: Int,
    val name: String,
    val icon: String,
    val description: String,
    val isWarning: Boolean
) {
    companion object {
        val ALL = listOf(
            BristolStoolScale(1, "硬球状", "🪨", "分散硬块，排便困难 (便秘)", true),
            BristolStoolScale(2, "香肠块状", "🥖", "团块粗糙结合 (轻微便秘)", false),
            BristolStoolScale(3, "有裂纹香肠", "🌭", "表面有裂纹 (健康正常)", false),
            BristolStoolScale(4, "光滑软蛇", "🐍", "光滑柔软，黄金便便 (最理想)", false),
            BristolStoolScale(5, "柔软小块", "☁️", "边缘清晰，容易排出 (轻度偏软)", false),
            BristolStoolScale(6, "糊状絮状", "🥣", "蓬松糊状，边缘不规则 (腹泻前期)", true),
            BristolStoolScale(7, "水样无固体", "🌊", "完全液体，无固体积聚 (严重腹泻)", true)
        )
    }
}
