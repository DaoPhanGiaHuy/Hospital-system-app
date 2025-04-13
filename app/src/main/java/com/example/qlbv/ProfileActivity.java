package com.example.qlbv;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvFullName, tvEmail, tvRole;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);

        dbHelper = new DatabaseHelper(this);

        String email = getIntent().getStringExtra("email");

        if (email != null) {
            User user = dbHelper.getUserByEmail(email);
            if (user != null) {
                tvFullName.setText("Họ tên: " + user.getFullname());
                tvEmail.setText("Email: " + user.getEmail());
                tvRole.setText("Vai trò: " + user.getRole());
            }
        }
    }
}
