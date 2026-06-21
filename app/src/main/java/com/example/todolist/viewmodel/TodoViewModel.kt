package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.db.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import com.example.todolist.db.ToDo

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val todoList: LiveData<List<ToDo>> = repository.allTodos

    fun addTodo(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(ToDo(title = title))
        }
    }


    fun deleteTodo(todo: ToDo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(todo)
        }
    }

    fun updateTodo(todo: ToDo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(todo)
        }
    }

    fun toggleDone(todo: ToDo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleDone(todo)
        }
    }
}
