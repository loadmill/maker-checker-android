package com.loadmill.makerchecker.data

import com.loadmill.makerchecker.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {
    private val api = ApiClient.service
    private var token: String? = null

    // Helpers
    private fun auth(): String = token ?: ""

    suspend fun login(username: String, password: String, role: String): Result<LoginResponse> =
        runIoCatching {
            val res = api.login(LoginRequest(username, password, role))
            token = res.token
            res
        }

    suspend fun myTransfers(): Result<List<Transfer>> =
        runIoCatching { api.myTransfers(auth()) }

    suspend fun initiate(amount: String, recipient: String): Result<Transfer> =
        runIoCatching { api.initiate(auth(), InitiateRequest(amount, recipient)) }

    suspend fun pending(): Result<List<Transfer>> =
        runIoCatching { api.pending(auth()) }

    suspend fun approve(transactionId: Long): Result<Transfer> =
        runIoCatching { api.approve(auth(), TxnIdRequest(transactionId)) }

    suspend fun reject(transactionId: Long): Result<Transfer> =
        runIoCatching { api.reject(auth(), TxnIdRequest(transactionId)) }
}

/** runCatching on IO to keep the UI thread clean */
private suspend inline fun <T> runIoCatching(crossinline block: suspend () -> T): Result<T> =
    withContext(Dispatchers.IO) { runCatching { block() } }
