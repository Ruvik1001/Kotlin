package com.example.kotlin.model

import com.google.firebase.database.PropertyName
import java.util.LinkedList

class TasksList(@PropertyName("lst") private var lst: LinkedList<Task> = LinkedList<Task>()) {
    @PropertyName("my_lst") private var my_lst = lst

    operator fun get(i: Int): Task {
        return my_lst[i]
    }

    fun add(task: Task) {
        my_lst.add(task)
    }

    fun get(): LinkedList<Task> {
        return my_lst
    }

    fun set(lst: LinkedList<Task>) {
        this.my_lst = lst
    }
}

