package com.example.kotlinpract1.UI.View

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.kotlinpract1.R
import com.example.kotlinpract1.UI.StateHolder.ViewModel.MyViewModel

class MainActivity : AppCompatActivity() {
    // init pract88

    private lateinit var navController: NavController

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val students = listOf(
            Student("Криштофор Андрей", "ИКБО-25-21"),
            Student("Вяся Пупкин", "ИКБО-100-23"),
            Student("П Пупкин", "ИКБО-100-23"),
            Student("Вяся Пупкин", "ИКБО-100-23"),
            // Добавьте других студентов по аналогии
        )
        setContent {
            MyApp(students)
        }
    }
}