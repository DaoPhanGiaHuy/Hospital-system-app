package com.example.qlbv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnSchedule, btnManageMedicine;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnManageMedicine = findViewById(R.id.btnManageMedicine);  // Khai báo nút Quản lý thuốc

        // Nhận email và role từ LoginActivity
        email = getIntent().getStringExtra("email");
        String name = getIntent().getStringExtra("fullname");
        String role = getIntent().getStringExtra("role");
        tvWelcome.setText("Xin chào, " + (name != null ? name : ""));

        // Hiển thị hoặc ẩn nút "Quản lý đơn thuốc" nếu là Admin
        if ("Admin".equalsIgnoreCase(role) || "Bác sĩ".equalsIgnoreCase(role)) {
            btnManageMedicine.setVisibility(View.VISIBLE);  // Hiển thị cho Admin
        } else {
            btnManageMedicine.setVisibility(View.GONE);  // Ẩn cho các vai trò khác
        }

        // Sự kiện nút "Quản lý thuốc"
        btnManageMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Bác sĩ".equalsIgnoreCase(role)) {
                    // Chuyển sang Activity dành cho bác sĩ
                    Intent intent = new Intent(DashboardActivity.this, MedicineDoctorActivity.class);
                    startActivity(intent);
                } else {
                    // Chuyển sang Activity quản lý thuốc của Admin
                    Intent intent = new Intent(DashboardActivity.this, MedicineActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Sự kiện nút "Đặt lịch khám"
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ScheduleActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        // Ẩn nút "Đặt lịch khám" cho người không phải Bệnh nhân
        if (!"Bệnh nhân".equalsIgnoreCase(role)) {
            btnSchedule.setVisibility(View.GONE); // Hoặc setEnabled(false) để vô hiệu hóa
        }

        // Sự kiện nút "Thông tin cá nhân"
        Button btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        // Sự kiện nút "Đăng xuất"
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trở về LoginActivity và xóa ngăn xếp (clear task)
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Kết thúc activity hiện tại
            }
        });

        // Sự kiện nút "Hồ sơ bệnh án"
        Button btnMedicalRecord = findViewById(R.id.btnMedicalRecord);
        btnMedicalRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MedicalRecordActivity.class);
                intent.putExtra("email", email); // Gửi email để truy vấn lịch sử khám
                intent.putExtra("role", role);   // Truyền role từ trước đó
                startActivity(intent);
            }
        });

        // Sự kiện nút "Phòng khám"
        Button btnClinicRoom = findViewById(R.id.btnClinicRoom);
        btnClinicRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Bác sĩ".equalsIgnoreCase(role)) {
                    // Chuyển sang Activity quản lý phòng khám dành cho bác sĩ
                    Intent intent = new Intent(DashboardActivity.this, ClinicRoomActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Hiển thị hoặc ẩn nút "Phòng khám" nếu là Bác sĩ
        if ("Bác sĩ".equalsIgnoreCase(role)) {
            btnClinicRoom.setVisibility(View.VISIBLE);  // Hiển thị cho Bác sĩ
        } else {
            btnClinicRoom.setVisibility(View.GONE);  // Ẩn cho các vai trò khác
        }
    }
}
