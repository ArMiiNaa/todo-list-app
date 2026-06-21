package com.example.todolist.db

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {

    val allTodos: LiveData<List<ToDo>> = todoDao.getAllTodos()

    suspend fun add(todo: ToDo) {
        todoDao.addTodo(todo)
    }

    suspend fun delete(todo: ToDo) {
        todoDao.deleteTodo(todo)
    }

    suspend fun update(todo: ToDo) {
        todoDao.update(todo)
    }

    suspend fun toggleDone(todo: ToDo) {
        todoDao.setDone(todo.id, !todo.isDone)
    }
}
