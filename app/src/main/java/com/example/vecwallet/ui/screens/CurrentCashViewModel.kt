package com.example.vecwallet.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrentCashViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CurrentCashUiState())
    val uiState: StateFlow<CurrentCashUiState> = _uiState.asStateFlow()

    fun changeCurrentCash(difference: Double) {
        _uiState.update {
            it.copy(currentCash = it.currentCash + difference)
        }
    }
}

data class CurrentCashUiState(
    val currentCash: Double = 0.0
)