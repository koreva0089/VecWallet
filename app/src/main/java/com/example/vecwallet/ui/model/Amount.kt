package com.example.vecwallet.ui.model

import com.example.vecwallet.ui.utils.OperationType

data class Amount(
    val differenceAmount: Double,
    val history: String,
    val operType: OperationType
)
