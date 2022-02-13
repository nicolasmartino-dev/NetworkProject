package com.example.networkproject.data

import com.example.networkproject.annotation.IoDispatcher
import com.example.networkproject.data.network.Employee
import com.example.networkproject.data.network.NetworkCallHelper
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeesApiImpl @Inject constructor(
    private val networkCallHelper: NetworkCallHelper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : EmployeesApi {
    override suspend fun fetchEmployees(): List<Employee> = withContext(ioDispatcher) {
        val result = networkCallHelper.getRequest("https://jsonplaceholder.typicode.com/users")
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(JsonParser.parseString(result))
        //Log.d("Pretty Printed JSON :", prettyJson)
        try {
            val myType = object : TypeToken<List<Employee>>() {}.type
            val responseData: List<Employee>? = gson.fromJson(prettyJson, myType)
            responseData.orEmpty()
        } catch (ex: JsonSyntaxException) {
            //Log.e("NetworkCallHelper", "Gson exception", ex)
            emptyList()
        }
    }
}
