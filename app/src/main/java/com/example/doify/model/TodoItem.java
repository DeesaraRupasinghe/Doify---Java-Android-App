package com.example.doify.model;

public class TodoItem {
    private String task;

    public TodoItem(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}