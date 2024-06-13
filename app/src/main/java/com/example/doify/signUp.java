package com.example.doify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signUp extends AppCompatActivity {

    private EditText userNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Find views
        userNameEditText = findViewById(R.id.userNameSignUp);
        emailEditText = findViewById(R.id.signUpEmail);
        passwordEditText = findViewById(R.id.signUpPass);
        confirmPasswordEditText = findViewById(R.id.signUpPassConfirm);
        Button signUpBtn = findViewById(R.id.SignUpBtn);
        Button logBtn = findViewById(R.id.logBtn);

        // Apply insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set click listeners
        signUpBtn.setOnClickListener(v -> signUp());
        logBtn.setOnClickListener(v -> login());
    }

    private void login() {
        Intent intent = new Intent(signUp.this, login.class);
        startActivity(intent);
    }

    private void signUp() {
        String userName = userNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save user details using SharedPreferences with unique key
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userKey = "user_" + userName; // Unique key for each user
        editor.putString(userKey + "_userName", userName);
        editor.putString(userKey + "_email", email);
        editor.putString(userKey + "_password", password);
        editor.apply();

        Intent intent = new Intent(signUp.this, login.class);
        startActivity(intent);

        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
        finish();
    }
}
