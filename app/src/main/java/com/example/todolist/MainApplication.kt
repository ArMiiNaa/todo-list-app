package com.example.todolist

import android.app.Application
import com.example.todolist.db.TodoDatabase

class MainApplication : Application() {
    companion object {
        lateinit var todoDatabase: TodoDatabase
    }

    override fun onCreate() {
        super.onCreate()
        todoDatabase = TodoDatabase.get(applicationContext)
    }
}
