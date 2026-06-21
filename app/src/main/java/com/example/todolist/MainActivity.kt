package com.example.todolist

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.db.TodoRepository
import com.example.todolist.ui.TodoListPage
import com.example.todolist.TodoViewModelFactory.kt.TodoViewModelFactory
import com.example.todolist.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val todoDao = MainApplication.todoDatabase.todoDao()
        val repository = TodoRepository(todoDao)
        val factory = TodoViewModelFactory(repository)
        val todoViewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            ToDoListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {

                    TodoListPage(todoViewModel)
                }
            }
        }
    }
}
