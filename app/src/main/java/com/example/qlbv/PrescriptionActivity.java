package com.example.qlbv;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class PrescriptionActivity extends AppCompatActivity {

    Spinner spinnerMedicine;
    EditText edtDosage;
    Button btnPrescribe;
    DatabaseHelper dbHelper;
    String currentDoctorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        spinnerMedicine = findViewById(R.id.spinnerMedicine);
        edtDosage = findViewById(R.id.edtDosage);
        btnPrescribe = findViewById(R.id.btnAddPrescription);

        dbHelper = new DatabaseHelper(this);
        currentDoctorEmail = getIntent().getStringExtra("email");

        // Lấy danh sách thuốc từ database dưới dạng List<Medicine>
        List<Medicine> medicines = dbHelper.getAllMedicines();
        List<String> medicineNames = new ArrayList<>();

        // Chuyển danh sách Medicine thành danh sách tên thuốc
        for (Medicine medicine : medicines) {
            medicineNames.add(medicine.getName());  // Giả sử Medicine có phương thức getName()
        }

        // Tạo ArrayAdapter với danh sách tên thuốc
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medicineNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicine.setAdapter(adapter);

        btnPrescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicineName = spinnerMedicine.getSelectedItem().toString();
                String dosageStr = edtDosage.getText().toString().trim();

                if (dosageStr.isEmpty()) {
                    Toast.makeText(PrescriptionActivity.this, "Vui lòng nhập liều lượng", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy medicineId từ tên thuốc
                int medicineId = dbHelper.getMedicineIdByName(medicineName);
                if (medicineId == -1) {
                    Toast.makeText(PrescriptionActivity.this, "Thuốc không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển đổi liều lượng thành số nguyên
                int dosage = Integer.parseInt(dosageStr);

                // Thực hiện chèn đơn thuốc
                boolean success = dbHelper.insertPrescription(currentDoctorEmail, "patientEmail", medicineId, dosageStr);

                if (success) {
                    Toast.makeText(PrescriptionActivity.this, "Kê đơn thuốc thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PrescriptionActivity.this, "Lỗi khi kê đơn thuốc", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
