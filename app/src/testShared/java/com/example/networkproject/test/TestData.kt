package com.example.networkproject.test

import com.example.networkproject.data.database.entities.Employee

object TestData {
    val employeeTestList = listOf(
        Employee(
            id = 1,
            name = "Leanne Graham",
            username = "Bret",
            email = "Sincere@april.biz",
        ),
        Employee(
            id = 2,
            name = "Ervin Howell",
            username = "Antonette",
            email = "Shanna@melissa.tv",
        ),
        Employee(
            id = 3,
            name = "Clementine Bauch",
            username = "Samantha",
            email = "Nathan@yesenia.net",
        ),
        Employee(
            id = 4,
            name = "Patricia Lebsack",
            username = "Karianne",
            email = "Julianne.OConner@kory.org",
        )
    )
}
