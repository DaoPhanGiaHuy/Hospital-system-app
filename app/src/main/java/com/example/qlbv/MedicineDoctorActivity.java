package com.example.qlbv;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MedicineDoctorActivity extends AppCompatActivity {

    LinearLayout medicineListLayout;
    Button btnPrescribe;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_doctor);

        medicineListLayout = findViewById(R.id.medicineListLayout);
        btnPrescribe = findViewById(R.id.btnPrescribe);
        dbHelper = new DatabaseHelper(this);

        // Lấy danh sách thuốc từ cơ sở dữ liệu
        List<Medicine> medicineList = dbHelper.getAllMedicines();

        if (medicineList.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("Chưa có thuốc nào được nhập.");
            medicineListLayout.addView(emptyView);
        } else {
            for (Medicine medicine : medicineList) {
                TextView tv = new TextView(this);
                tv.setText("- " + medicine.getName() + " | Số lượng: " + medicine.getQuantity());
                tv.setTextSize(16);
                tv.setPadding(0, 8, 0, 8);
                medicineListLayout.addView(tv);
            }
        }

        // Sự kiện nhấn "KÊ ĐƠN"
        btnPrescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MedicineDoctorActivity.this, "Xuất đơn thuốc thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
