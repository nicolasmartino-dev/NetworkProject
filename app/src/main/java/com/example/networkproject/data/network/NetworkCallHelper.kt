package com.example.networkproject.data.network

import com.example.networkproject.annotation.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

interface NetworkCallHelper {
    suspend fun getRequest(sUrl: String): String?
}

class NetworkCallHelperImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NetworkCallHelper {

    override suspend fun getRequest(sUrl: String): String? = withContext(ioDispatcher) {
        val inputStream: InputStream
        var result: String? = null

        try {
            // Create URL
            val url = URL(sUrl)

            // Create HttpURLConnection
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

            conn.setRequestProperty(
                "Accept",
                "application/json"
            )
            conn.requestMethod = "GET"
            conn.doInput = true
            conn.doOutput = false

            // Launch GET request
            conn.connect()

            // Receive response as inputStream
            inputStream = conn.inputStream

            result = // Convert input stream to string
                inputStream?.bufferedReader()?.use(BufferedReader::readText) ?: "error: inputStream is null"
        } catch (err: Error) {
            Timber.e("Error when executing get request: " + err.localizedMessage)
        }

        result
    }
}

data class Employee(
    val id: Int,
    val name: String,
    val username: String,
    val email: String
)
