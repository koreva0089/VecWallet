package com.example.vecwallet.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vecwallet.ui.screens.CurrentCashScreen
import com.example.vecwallet.ui.theme.VecWalletTheme

@Composable
fun VecWalletApp(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        CurrentCashScreen(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Preview(
    showSystemUi = true,
    name = "VecWalletAppLightModePreview"
)
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "VecWalletAppDarkModePreview"
)
@Composable
private fun VecWalletAppPreview() {
    VecWalletTheme {
        VecWalletApp()
    }
}