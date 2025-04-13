package com.example.qlbv;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MedicineActivity extends AppCompatActivity {

    EditText edtMedicineName, edtQuantity, edtPrice;
    Button btnAddMedicine;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        edtMedicineName = findViewById(R.id.edtMedicineName);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtPrice = findViewById(R.id.edtPrice);
        btnAddMedicine = findViewById(R.id.btnAddMedicine);

        dbHelper = new DatabaseHelper(this);

        btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtMedicineName.getText().toString().trim();
                String quantityStr = edtQuantity.getText().toString().trim();
                String priceStr = edtPrice.getText().toString().trim();

                if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(MedicineActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int quantity = Integer.parseInt(quantityStr);
                    double price = Double.parseDouble(priceStr);

                    boolean success = dbHelper.insertMedicine(name, quantity, price);

                    if (success) {
                        Toast.makeText(MedicineActivity.this, "Thêm thuốc thành công!", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại màn hình trước
                    } else {
                        Toast.makeText(MedicineActivity.this, "Lỗi khi thêm thuốc", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MedicineActivity.this, "Số lượng hoặc giá không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
