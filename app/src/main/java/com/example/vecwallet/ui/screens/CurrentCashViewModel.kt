package com.example.vecwallet.ui.screens

import androidx.lifecycle.ViewModel
import com.example.vecwallet.ui.model.Amount
import com.example.vecwallet.ui.utils.OperationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrentCashViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CurrentCashUiState())
    val uiState: StateFlow<CurrentCashUiState> = _uiState.asStateFlow()

    fun changeCurrentCash(difference: Double) {
        val list = _uiState.value.historyList.toMutableList().apply {
            add(
                Amount(
                    differenceAmount = difference,
                    history = if (difference > 0.0) "Cash added" else "Cash reduced",
                    operType = if (difference > 0.0) OperationType.ADDITION else OperationType.SUBSTRACTION
                )
            )
        }
        _uiState.update {
            it.copy(
                currentCash = it.currentCash + difference,
                historyList = list
            )
        }
    }
}

data class CurrentCashUiState(
    val currentCash: Double = 0.0,
    val historyList: List<Amount> = listOf()
)