package com.example.qlbv;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;
public class ClinicRoomActivity extends AppCompatActivity {

    private Button btnAdd, btnDelete, btnUpdate, btnGetList;
    private ListView listView;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;
    private List<String> clinicRoomsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_room);

        btnAdd = findViewById(R.id.btnAddClinicRoom);
        btnDelete = findViewById(R.id.btnDeleteClinicRoom);
        btnUpdate = findViewById(R.id.btnUpdateClinicRoom);
        btnGetList = findViewById(R.id.btnGetClinicRooms);
        listView = findViewById(R.id.listViewClinicRooms);

        databaseHelper = new DatabaseHelper(this);
        clinicRoomsList = new ArrayList<>();

        // Lấy danh sách phòng khám
        loadClinicRooms();

        // Thêm phòng khám
        btnAdd.setOnClickListener(v -> {
            showAddClinicRoomDialog();
        });

        // Xóa phòng khám
        btnDelete.setOnClickListener(v -> {
            showDeleteClinicRoomDialog();
        });

        // Cập nhật phòng khám
        btnUpdate.setOnClickListener(v -> {
            showUpdateClinicRoomDialog();
        });

        // Lấy danh sách phòng khám
        btnGetList.setOnClickListener(v -> {
            loadClinicRooms();
        });
    }

    private void loadClinicRooms() {
        clinicRoomsList.clear();
        clinicRoomsList.addAll(databaseHelper.getAllClinicRooms());
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clinicRoomsList);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddClinicRoomDialog() {
        // Tạo một dialog để nhập tên phòng khám mới
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Thêm Phòng Khám")
                .setMessage("Nhập tên phòng khám:")
                .setView(input)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = input.getText().toString();
                    if (!name.isEmpty()) {
                        if (databaseHelper.insertClinicRoom(name)) {
                            Toast.makeText(this, "Phòng khám đã được thêm", Toast.LENGTH_SHORT).show();
                            loadClinicRooms();
                        } else {
                            Toast.makeText(this, "Lỗi khi thêm phòng khám", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showDeleteClinicRoomDialog() {
        // Tạo một dialog để chọn phòng khám cần xóa
        final String[] rooms = clinicRoomsList.toArray(new String[0]);
        new AlertDialog.Builder(this)
                .setTitle("Xóa Phòng Khám")
                .setItems(rooms, (dialog, which) -> {
                    String roomToDelete = rooms[which];
                    if (databaseHelper.deleteClinicRoom(roomToDelete)) {
                        Toast.makeText(this, "Phòng khám đã được xóa", Toast.LENGTH_SHORT).show();
                        loadClinicRooms();
                    } else {
                        Toast.makeText(this, "Lỗi khi xóa phòng khám", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showUpdateClinicRoomDialog() {
        // Tạo một dialog để chọn phòng khám cần cập nhật và nhập tên mới
        final String[] rooms = clinicRoomsList.toArray(new String[0]);
        new AlertDialog.Builder(this)
                .setTitle("Cập Nhật Phòng Khám")
                .setItems(rooms, (dialog, which) -> {
                    String oldName = rooms[which];
                    EditText input = new EditText(this);
                    new AlertDialog.Builder(this)
                            .setTitle("Cập Nhật Tên Phòng Khám")
                            .setMessage("Nhập tên mới:")
                            .setView(input)
                            .setPositiveButton("Cập nhật", (dialog1, which1) -> {
                                String newName = input.getText().toString();
                                if (!newName.isEmpty()) {
                                    if (databaseHelper.updateClinicRoom(oldName, newName)) {
                                        Toast.makeText(this, "Phòng khám đã được cập nhật", Toast.LENGTH_SHORT).show();
                                        loadClinicRooms();
                                    } else {
                                        Toast.makeText(this, "Lỗi khi cập nhật phòng khám", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                })
                .show();
    }
}

