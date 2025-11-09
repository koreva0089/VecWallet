package com.example.vecwallet.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vecwallet.R
import com.example.vecwallet.ui.model.Amount
import com.example.vecwallet.ui.model.fullHistory
import com.example.vecwallet.ui.theme.VecWalletTheme
import com.example.vecwallet.ui.utils.OperationType
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

private const val TAG = "CurrentCashScreen"

@Composable
fun CurrentCashScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: CurrentCashViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var showReduceDialog by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        CurrentCashContent(
            cash = uiState.currentCash,
            onAddClick = {
                showAddDialog = true
            },
            onReduceClick = {
                showReduceDialog = true
            },
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        if (uiState.historyList.isEmpty()) {
            Text(
                text = "There is no records",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .paddingFromBaseline(top = 24.dp)
                    .fillMaxWidth()
            )
        } else {
            CashHistoryList(
                historyList = uiState.historyList,
                contentPadding = PaddingValues(top = 24.dp)
            )
        }
    }
    if (showAddDialog) {
        AddCashDialog(
            cashAmount = uiState.currentCash,
            onCancelClick = { showAddDialog = false },
            onConfirmClick = {
                viewModel.changeCurrentCash(it)
                showAddDialog = false
            },
        )
    } else if (showReduceDialog) {
        ReduceCashDialog(
            cashAmount = uiState.currentCash,
            onCancelClick = { showReduceDialog = false },
            onConfirmClick = {
                viewModel.changeCurrentCash(-it)
                showReduceDialog = false
            }
        )
    }
}

@Composable
private fun CurrentCashContent(
    cash: Double,
    onAddClick: () -> Unit,
    onReduceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        CurrentCashText(
            cash = cash,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        CurrentCashActions(
            onAddClick = onAddClick,
            onReduceClick = onReduceClick
        )
    }
}

@Composable
private fun CurrentCashText(
    cash: Double,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY).apply {
        currency = Currency.getInstance("EUR")
    }
    Text(
        text = "Current cash: ${formatter.format(cash)}",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
    )
}

@Composable
private fun CurrentCashActions(
    onAddClick: () -> Unit,
    onReduceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        CurrentCashButton(
            onButtonClick = {
                Log.d(TAG, "CurrentCashActions: Adding to current cash")
                onReduceClick()
            },
            icon = '-',
            backgroundColor = Color.Red
        )
        Spacer(modifier = Modifier.width(4.dp))
        CurrentCashButton(
            onButtonClick = {
                Log.d(TAG, "CurrentCashActions: Reducing from current cash")
                onAddClick()
            },
            icon = '+',
            backgroundColor = Color.Green
        )
    }
}

@Composable
private fun CurrentCashButton(
    onButtonClick: () -> Unit,
    icon: Char,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onButtonClick,
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
    ) {
        Text(
            text = icon.toString(),
            color = Color.White,
            fontSize = 32.sp
        )
    }
}

@Composable
private fun AddCashDialog(
    cashAmount: Double,
    onCancelClick: () -> Unit,
    onConfirmClick: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var amountCashString by rememberSaveable { mutableStateOf("0.00") }
    AlertDialog(
        onDismissRequest = onCancelClick,
        dismissButton = {
            TextButton(onClick = onCancelClick) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmClick(amountCashString.toDoubleOrNull() ?: 0.0)
            }) {
                Text("Add")
            }
        },
        title = { Text("Add to cash") },
        text = {
            Column {
                Text(text = "$cashAmount + $amountCashString = ${cashAmount + amountCashString.toDouble()}")
                OutlinedTextField(
                    label = { Text("Add amount") },
                    value = amountCashString,
                    onValueChange = { amountCashString = it }
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun ReduceCashDialog(
    cashAmount: Double,
    onCancelClick: () -> Unit,
    onConfirmClick: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var amountCashString by rememberSaveable { mutableStateOf("0.00") }
    AlertDialog(
        onDismissRequest = onCancelClick,
        dismissButton = {
            TextButton(onClick = onCancelClick) {
                Text(stringResource(R.string.cancel_button_text))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmClick(amountCashString.toDoubleOrNull() ?: 0.0)
            }) {
                Text(stringResource(R.string.reduce_button_text))
            }
        },
        title = { Text("Reduce from cash") },
        text = {
            Column {
                Text(text = "$cashAmount - $amountCashString = ${cashAmount - amountCashString.toDouble()}")
                OutlinedTextField(
                    label = { Text(stringResource(R.string.reduce_text)) },
                    value = amountCashString,
                    onValueChange = { amountCashString = it }
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun CashHistoryList(
    historyList: List<Amount>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(historyList) { amount ->
            CashHistoryItem(amount = amount)
        }
    }
}

@Composable
fun CashHistoryItem(
    amount: Amount,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Text(
            text = amount.fullHistory,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun CashHistoryItemPreview() {
    VecWalletTheme {
        CashHistoryItem(
            amount = Amount(
                differenceAmount = 12.32,
                history = "Cash added",
                operType = OperationType.ADDITION
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CashHistoryPreview() {
    VecWalletTheme {
        CashHistoryList(
            historyList = listOf(
                Amount(
                    differenceAmount = 12.32,
                    history = "Cash added",
                    operType = OperationType.ADDITION
                ),
                Amount(
                    differenceAmount = 23.32,
                    history = "Cash reduced",
                    operType = OperationType.SUBSTRACTION
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CurrentCashScreenPreview() {
    VecWalletTheme {
        CurrentCashScreen()
    }
}