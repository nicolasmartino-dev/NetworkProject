package com.example.networkproject.data

import com.example.networkproject.annotation.IoDispatcher
import com.example.networkproject.data.database.entities.Employee
import com.example.networkproject.data.network.NetworkCallHelper
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


interface EmployeesRemoteApi {
    suspend fun fetchEmployees(): List<Employee>
}

class EmployeesRemoteApiImpl @Inject constructor(
    private val networkCallHelper: NetworkCallHelper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : EmployeesRemoteApi {
    override suspend fun fetchEmployees(): List<Employee> = withContext(ioDispatcher) {
        val result = networkCallHelper.getRequest("https://jsonplaceholder.typicode.com/users")
        val gson = GsonBuilder().create()
        val prettyJson = gson.toJson(JsonParser.parseString(result))
        Timber.d("Pretty Printed JSON :", prettyJson)
        try {
            val myType = object : TypeToken<List<Employee>>() {}.type
            val responseData: List<Employee>? = gson.fromJson(prettyJson, myType)
            responseData.orEmpty()
        } catch (ex: JsonSyntaxException) {
            Timber.e("NetworkCallHelper", "Gson exception", ex)
            emptyList()
        }
    }
}
