package com.loadmill.makerchecker.data

// --- Requests ---
data class LoginRequest(
    val username: String,
    val password: String,
    val role: String
)
data class InitiateRequest(
    val amount: String,        // keep as String to match the demo backend leniently
    val recipient: String
)
data class TxnIdRequest(
    val transactionId: Long
)

// --- Responses ---
data class LoginResponse(
    val token: String,
    val role: String,
    val username: String
)

data class Transfer(
    val transactionId: Long,
    val amount: String,
    val recipient: String,
    val status: String,
    val initiatedBy: String,
    val approvedBy: String?,
    val createdAt: String,
    val approvedAt: String?
)
