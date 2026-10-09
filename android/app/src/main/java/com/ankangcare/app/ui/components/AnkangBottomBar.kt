package com.ankangcare.app.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ankangcare.app.ui.theme.Teal600

enum class AnkangTab(val route: String, val title: String, val icon: String) {
    DASHBOARD("dashboard", "今日看板", "🏠"),
    QUICK_INPUT("quick_input", "极速记", "➕"),
    DOSSIER("dossier", "健康档案", "📁"),
    SETTINGS("settings", "家庭设置", "⚙️")
}

@Composable
fun AnkangBottomBar(
    currentRoute: String,
    onTabSelected: (AnkangTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Teal600
    ) {
        AnkangTab.values().forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Text(text = tab.icon)
                },
                label = {
                    Text(
                        text = tab.title,
                        color = if (selected) Teal600 else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFFCCFBF1)
                )
            )
        }
    }
}
