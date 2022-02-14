package com.example.networkproject.data.network.retrofit

import com.example.networkproject.data.Employee
import retrofit2.Call
import retrofit2.http.GET

interface UsersService {
    @GET("users")
    fun fetchUsers(): Call<List<Employee>>
}
