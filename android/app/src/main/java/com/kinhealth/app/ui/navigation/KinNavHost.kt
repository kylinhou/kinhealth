package com.kinhealth.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.kinhealth.app.ui.components.KinBottomBar
import com.kinhealth.app.ui.components.KinTab
import com.kinhealth.app.ui.dashboard.DashboardScreen
import com.kinhealth.app.ui.dossier.HealthDossierScreen
import com.kinhealth.app.ui.quickinput.QuickInputScreen
import com.kinhealth.app.ui.settings.FamilySettingsScreen

@Composable
fun KinHealthMainApp() {
    var currentTab by remember { mutableStateOf(KinTab.DASHBOARD) }

    Scaffold(
        bottomBar = {
            KinBottomBar(
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
                KinTab.DASHBOARD -> DashboardScreen(
                    onNavigateToQuickInput = { currentTab = KinTab.QUICK_INPUT }
                )
                KinTab.QUICK_INPUT -> QuickInputScreen(
                    onRecordSaved = { currentTab = KinTab.DASHBOARD }
                )
                KinTab.DOSSIER -> HealthDossierScreen()
                KinTab.SETTINGS -> FamilySettingsScreen()
            }
        }
    }
}

