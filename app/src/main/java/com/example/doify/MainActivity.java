package com.example.doify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doify.adapter.TodoAdapter;
import com.example.doify.model.TodoItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private List<TodoItem> todoList = new ArrayList<>();
    private TodoAdapter todoAdapter;
    private String username;
    private SharedPreferences sharedPreferences;
    private static final String TODO_PREFS = "TodoData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button addButton = findViewById(R.id.addButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(todoAdapter);

        // Get username from intent
        username = getIntent().getStringExtra("username");
        sharedPreferences = getSharedPreferences(TODO_PREFS, MODE_PRIVATE);

        // Load tasks
        loadTasks();

        addButton.setOnClickListener(v -> showAddTaskDialog());

        // Set click listeners for edit and delete buttons in adapter
        todoAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                editTask(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteTask(position);
            }
        });
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String task = input.getText().toString().trim();
            if (!task.isEmpty()) {
                addTask(task);
            } else {
                Toast.makeText(MainActivity.this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addTask(String task) {
        TodoItem todoItem = new TodoItem(task);
        todoList.add(todoItem);
        todoAdapter.notifyItemInserted(todoList.size() - 1);
        saveTasks();
    }

    private void editTask(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(todoList.get(position).getTask());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTask = input.getText().toString().trim();
            if (!newTask.isEmpty()) {
                todoList.get(position).setTask(newTask);
                todoAdapter.notifyItemChanged(position); // Notify adapter for item change
                saveTasks(); // Save tasks after editing
            } else {
                Toast.makeText(MainActivity.this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void deleteTask(int position) {
        todoList.remove(position);
        todoAdapter.notifyItemRemoved(position);
        saveTasks();
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> taskSet = new HashSet<>();
        for (TodoItem item : todoList) {
            taskSet.add(item.getTask());
        }
        editor.putStringSet(username + "_tasks", taskSet);
        editor.apply();
    }

    private void loadTasks() {
        Set<String> taskSet = sharedPreferences.getStringSet(username + "_tasks", new HashSet<>());
        todoList.clear();
        for (String task : taskSet) {
            todoList.add(new TodoItem(task));
        }
        todoAdapter.notifyDataSetChanged();
    }
}
