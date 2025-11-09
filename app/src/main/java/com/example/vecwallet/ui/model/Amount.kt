package com.example.vecwallet.ui.model

import com.example.vecwallet.ui.utils.OperationType

data class Amount(
    val differenceAmount: Double,
    val history: String,
    val operType: OperationType
)

val Amount.fullHistory: String
    get() = "$history: ${if (operType == OperationType.ADDITION) "+" else ""}" +
            "$differenceAmount"