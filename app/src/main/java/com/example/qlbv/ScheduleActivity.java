package com.example.qlbv;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    Spinner spinnerDoctor;
    DatePicker datePicker;
    TimePicker timePicker;
    EditText edtSymptoms;
    Button btnSubmit;
    TextView txtClinicRoom;

    DatabaseHelper dbHelper;
    String currentUserEmail;

    // BỔ SUNG: Spinner phòng khám
    Spinner spinnerClinicRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        edtSymptoms = findViewById(R.id.edtSymptoms);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtClinicRoom = findViewById(R.id.txtClinicRoom); // Hiển thị phòng khám

        // BỔ SUNG: Ánh xạ spinner phòng khám
        spinnerClinicRoom = findViewById(R.id.spinnerClinicRoom);

        timePicker.setIs24HourView(true);
        dbHelper = new DatabaseHelper(this);
        currentUserEmail = getIntent().getStringExtra("email");

        List<String> doctors = dbHelper.getDoctors();
        if (doctors.isEmpty()) {
            doctors.add("Không có bác sĩ");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctor.setAdapter(adapter);

        // Khi chọn bác sĩ, hiển thị phòng khám
        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDoctor = doctors.get(position).replace("Bs. ", "").trim();
                String room = dbHelper.getClinicRoomForDoctor(selectedDoctor);
                txtClinicRoom.setText("Phòng khám: " + (room != null ? room : "Chưa cập nhật"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtClinicRoom.setText("Phòng khám: Chưa chọn bác sĩ");
            }
        });

        // BỔ SUNG: Đổ dữ liệu vào spinner phòng khám
        List<String> clinicRooms = dbHelper.getAllClinicRooms();
        if (clinicRooms.isEmpty()) {
            clinicRooms.add("Không có phòng khám");
        }

        ArrayAdapter<String> clinicRoomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clinicRooms);
        clinicRoomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClinicRoom.setAdapter(clinicRoomAdapter);

        btnSubmit.setOnClickListener(v -> {
            String doctor = spinnerDoctor.getSelectedItem().toString().replace("Bs. ", "").trim();
            String symptoms = edtSymptoms.getText().toString().trim();

            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            String date = String.format("%02d/%02d/%04d", day, month, year);

            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String time = String.format("%02d:%02d", hour, minute);

            // BỔ SUNG: Lấy phòng khám được chọn từ spinner
            String selectedRoom = spinnerClinicRoom.getSelectedItem().toString();

            if (symptoms.isEmpty()) {
                Toast.makeText(ScheduleActivity.this, "Vui lòng nhập triệu chứng", Toast.LENGTH_SHORT).show();
                return;
            }

            // BỔ SUNG: Truyền thêm phòng khám khi đặt lịch
            boolean success = dbHelper.insertAppointment(currentUserEmail, doctor, selectedRoom, date, time, symptoms);
            if (success) {
                Toast.makeText(ScheduleActivity.this, "Đặt lịch khám thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ScheduleActivity.this, "Lỗi khi đặt lịch", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
