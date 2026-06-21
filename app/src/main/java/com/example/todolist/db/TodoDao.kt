package com.example.todolist.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Update

@Dao
interface TodoDao {


    @Query("SELECT * FROM todos ORDER BY id DESC")
    fun getAllTodos(): LiveData<List<ToDo>>

    @Insert
    suspend fun addTodo(todo: ToDo)

    @Delete
    suspend fun deleteTodo(todo: ToDo)

    @Update
    suspend fun update(todo: ToDo)

    @Query("UPDATE todos SET isDone = :done WHERE id = :id") suspend fun setDone(id: Int, done: Boolean)
    }

