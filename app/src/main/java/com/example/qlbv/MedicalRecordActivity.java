package com.example.qlbv;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MedicalRecordActivity extends AppCompatActivity {

    ListView listMedicalRecords;
    DatabaseHelper dbHelper;
    String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);

        listMedicalRecords = findViewById(R.id.listMedicalRecords);
        dbHelper = new DatabaseHelper(this);

        currentUserEmail = getIntent().getStringExtra("email");

        // Lấy vai trò của người dùng
        String role = dbHelper.getRoleByEmail(currentUserEmail);

        List<String> appointments;

        if (role != null && role.equals("Bác sĩ")) {
            // Lấy tên bác sĩ từ email
            String doctorName = dbHelper.getNameByEmail(currentUserEmail);
            appointments = dbHelper.getAppointmentsByDoctor(doctorName);
        } else {
            // Nếu là bệnh nhân, lấy lịch hẹn theo email
            appointments = dbHelper.getAppointmentsByEmail(currentUserEmail);
        }

        // Hiển thị lên ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointments);
        listMedicalRecords.setAdapter(adapter);
    }
}
