package com.ankangcare.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.ankangcare.app.ui.components.AnkangBottomBar
import com.ankangcare.app.ui.components.AnkangTab
import com.ankangcare.app.ui.dashboard.DashboardScreen
import com.ankangcare.app.ui.dossier.HealthDossierScreen
import com.ankangcare.app.ui.quickinput.QuickInputScreen
import com.ankangcare.app.ui.settings.FamilySettingsScreen

@Composable
fun AnkangMainApp() {
    var currentTab by remember { mutableStateOf(AnkangTab.DASHBOARD) }

    Scaffold(
        bottomBar = {
            AnkangBottomBar(
                currentRoute = currentTab.route,
                onTabSelected = { tab -> currentTab = tab }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AnkangTab.DASHBOARD -> DashboardScreen(
                    onNavigateToQuickInput = { currentTab = AnkangTab.QUICK_INPUT }
                )
                AnkangTab.QUICK_INPUT -> QuickInputScreen(
                    onRecordSaved = { currentTab = AnkangTab.DASHBOARD }
                )
                AnkangTab.DOSSIER -> HealthDossierScreen()
                AnkangTab.SETTINGS -> FamilySettingsScreen()
            }
        }
    }
}
