package com.example.doify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class settings extends AppCompatActivity {

    private EditText EditUsername, editEmail, currentPassword, editPassword;

    private TextView userNameTitle;
    private SharedPreferences sharedPreferences;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton backButton = findViewById(R.id.backBtn);

        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity to go back
                finish();
            }
        });

        userNameTitle = findViewById(R.id.userNameDis);
        EditUsername = findViewById(R.id.userNameSettings);
        editEmail = findViewById(R.id.userEmailSettings);
        currentPassword = findViewById(R.id.currentPassword);
        editPassword = findViewById(R.id.newPassword);


        Button saveChangesBtn = findViewById(R.id.SaveBtn);
        Button logOutBtn = findViewById(R.id.logOutBtn);
        Button ChangePass = findViewById(R.id.ChangePass);

        username = getIntent().getStringExtra("username");

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        loadUserDetails(username);

        saveChangesBtn.setOnClickListener(v -> saveChanges());
        logOutBtn.setOnClickListener(v -> logout());
        ChangePass.setOnClickListener(v -> changePassword());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_dev:
                        Intent intent = new Intent(settings.this, developer.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        return true;

                    case R.id.navigation_home:
                        Intent intent1 = new Intent(settings.this, MainActivity.class);
                        intent1.putExtra("username", username);
                        startActivity(intent1);
                        return true;

                    case R.id.navigation_settings:
                        Intent intent2 = new Intent(settings.this, settings.class);
                        intent2.putExtra("username", username);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadUserDetails(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String userKey = "user_" + username;
        String savedPassword = sharedPreferences.getString(userKey + "_password", null);
        String userEmail = sharedPreferences.getString(userKey  + "_email", "");

        userNameTitle.setText(username);
        EditUsername.setText(username);
        editEmail.setText(userEmail);

    }

    private void saveChanges() {
        // Retrieve updated values from the EditText fields
        String updatedUsername = EditUsername.getText().toString().trim();
        String updatedEmail = editEmail.getText().toString().trim();



        // Check if the username has changed
        if (!username.equals(updatedUsername)) {
            // Username has changed, update SharedPreferences with the new key
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Remove old user data
            String oldUserKey = "user_" + username;
            editor.remove(oldUserKey + "_userName");
            editor.remove(oldUserKey + "_email");
            editor.remove(oldUserKey + "_password");

            // Add new user data with the updated key
            String newUserKey = "user_" + updatedUsername;
            editor.putString(newUserKey + "_userName", updatedUsername);
            editor.putString(newUserKey + "_email", updatedEmail);
            editor.putString(newUserKey + "_password", sharedPreferences.getString(oldUserKey + "_password", null));

            editor.apply();

            // Update the username to the new one
            username = updatedUsername;
        } else {
            // Username has not changed, update email only
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String userKeyy = "user_" + username;
            editor.putString(userKeyy + "_email", updatedEmail);
            editor.apply();
        }



        // Update the UI with the new details
        userNameTitle.setText(updatedUsername);
        EditUsername.setText(updatedUsername);
        editEmail.setText(updatedEmail);

        // Notify the user that changes have been saved
        Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void changePassword(){

        String enteredCurrentPassword = currentPassword.getText().toString().trim();
        String newPassword = editPassword.getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String userKey = "user_" + username;
        String savedPassword = sharedPreferences.getString(userKey + "_password", null);

        if (!enteredCurrentPassword.isEmpty() && !newPassword.isEmpty()) {
            // Both current password and new password are not empty
            if (enteredCurrentPassword.equals(savedPassword)) {
                // Entered current password matches the saved password
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String userKeyyy = "user_" + username;
                editor.remove(userKeyyy + "_password");
                editor.putString(userKeyyy + "_password", newPassword);
                editor.apply();

                // Notify the user that password has been changed
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Entered current password does not match the saved password
                Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Either current password or new password (or both) is empty
            Toast.makeText(this, "Please enter both current and new password", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        Intent intent = new Intent(settings.this, login.class);
        startActivity(intent);
        finish();
    }
}
