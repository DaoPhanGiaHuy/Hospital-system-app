package com.example.qlbv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "qlbv.db";
    private static final int DATABASE_VERSION = 2;

    // Tên bảng và cột
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "fullname";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";

    private Context context; // <<< BỔ SUNG DÒNG NÀY

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // <<< GÁN GIÁ TRỊ VÀO BIẾN context
    }

    // Tạo bảng khi database được tạo
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(createUserTable);

        // Tạo bảng appointments
        db.execSQL("CREATE TABLE IF NOT EXISTS appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patient_email TEXT," +
                "doctor_name TEXT," +// Thêm cột doctor
                "date TEXT," +
                "time TEXT," +
                "symptoms TEXT)");

        // Tạo bảng clinic_rooms
        db.execSQL("CREATE TABLE IF NOT EXISTS clinic_rooms (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "doctor_name TEXT)");


        // Tạo bảng medicines
        db.execSQL("CREATE TABLE IF NOT EXISTS medicines (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "quantity INTEGER," +
                "price REAL)");

        // Tạo bảng prescriptions
        db.execSQL("CREATE TABLE IF NOT EXISTS prescriptions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "doctor_name TEXT," +
                "patient_email TEXT," +
                "medicine_id INTEGER," +
                "dose TEXT," +
                "FOREIGN KEY(medicine_id) REFERENCES medicines(id))");
    }

    // Nâng cấp database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS clinic_rooms");
        db.execSQL("DROP TABLE IF EXISTS medicines");
        db.execSQL("DROP TABLE IF EXISTS prescriptions");
        db.execSQL("DROP TABLE IF EXISTS appointments");


        onCreate(db);
        context.deleteDatabase(DATABASE_NAME); // Giúp xóa database cũ khi nâng cấp
    }




// Thêm người dùng mới
    public boolean insertUser(String name, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Trả về họ tên nếu đúng tài khoản, null nếu sai
    public String checkUserLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fullname FROM users WHERE email=? AND password=?", new String[]{email, password});
        if (cursor.moveToFirst()) {
            String fullname = cursor.getString(0);
            cursor.close();
            return fullname;
        }
        cursor.close();
        return null;
    }

    // Thêm lịch khám
    public boolean insertAppointment(String email, String doctor, String clinicRoom, String date, String time, String symptoms) {
        // Nếu doctor truyền vào có tiền tố "Bs. ", loại bỏ để lưu vào DB chuẩn
        if (doctor.startsWith("Bs. ")) {
            doctor = doctor.substring(4); // Xoá "Bs. " ở đầu
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_email", email);
        values.put("doctor_name", doctor);
         // values.put("clinic_room", clinicRoom); // Bổ sung dòng này
        values.put("date", date);
        values.put("time", time);
        values.put("symptoms", symptoms);

        long result = db.insert("appointments", null, values);
        return result != -1;
    }


    // Lấy danh sách bác sĩ
    public List<String> getDoctors() {
        List<String> doctorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fullname FROM users WHERE role = ?", new String[]{"Bác sĩ"});

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                doctorList.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return doctorList;
    }

    // Lấy role theo email
    public String getRoleByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE email=?", new String[]{email});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }

    // Lấy thông tin user theo email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fullname, email, role FROM users WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            String mail = cursor.getString(1);
            String role = cursor.getString(2);
            cursor.close();
            return new User(name, mail, role);
        }

        cursor.close();
        return null;
    }

    // Lấy danh sách lịch hẹn theo email
    public List<String> getAppointmentsByEmail(String email) {
        List<String> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT doctor_name, date, time, symptoms FROM appointments WHERE patient_email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                String doctor = cursor.getString(0);
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                String symptoms = cursor.getString(3);

                String record = "Bác sĩ: " + doctor + "\nNgày: " + date + " - " + time + "\nTriệu chứng: " + symptoms;
                records.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return records;
    }

    // Lấy danh sách lịch hẹn theo tên bác sĩ
    public List<String> getAppointmentsByDoctor(String doctorName) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT patient_email, date, time, symptoms FROM appointments WHERE doctor_name = ?",
                new String[]{doctorName}
        );

        if (cursor.moveToFirst()) {
            do {
                String patientEmail = cursor.getString(0);
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                String symptoms = cursor.getString(3);

                String patientName = getNameByEmail(patientEmail);

                list.add("Bệnh nhân: " + patientName + "\nThời gian: " + date + " " + time + "\nTriệu chứng: " + symptoms);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy tên bệnh nhân từ email
    public String getNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fullname FROM users WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        cursor.close();
        return email;
    }

    // Thêm phòng khám
    public boolean insertClinicRoom(String roomName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", roomName);  // Đảm bảo tên cột là "room_name"

        long result = db.insert("clinic_rooms", null, values);
        db.close();
        return result != -1; // Trả về true nếu việc thêm phòng thành công
    }

    // Xoá phòng khám
    public boolean deleteClinicRoom(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("clinic_rooms", "name = ?", new String[]{name});
        db.close();
        return result > 0;
    }

    // Cập nhật tên phòng khám
    public boolean updateClinicRoom(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);

        int result = db.update("clinic_rooms", values, "name = ?", new String[]{oldName});
        db.close();
        return result > 0;
    }

    // Lấy danh sách tất cả phòng khám
    public List<String> getAllClinicRooms() {
        List<String> rooms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Thay đổi "name" thành "room_name" để phù hợp với tên cột trong bảng
        Cursor cursor = db.rawQuery("SELECT name FROM clinic_rooms", null);

        if (cursor.moveToFirst()) {
            do {
                rooms.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return rooms;
    }

    // Thêm thuốc
// Thêm thuốc vào cơ sở dữ liệu
    public boolean insertMedicine(String name, int quantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("price", price);
        long result = db.insert("medicines", null, values);  // Sửa từ "medicine" thành "medicines"
        return result != -1;
    }


    public List<Medicine> getAllMedicines() {
        List<Medicine> medicineList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Medicines", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                medicineList.add(new Medicine(id, name, quantity));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return medicineList;
    }


    // Thêm đơn thuốc
    public boolean insertPrescription(String doctorName, String patientEmail, int medicineId, String dose) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("doctor_name", doctorName);
        values.put("patient_email", patientEmail);
        values.put("medicine_id", medicineId);
        values.put("dose", dose);

        long result = db.insert("prescriptions", null, values);
        db.close();
        return result != -1;
    }

    // Lấy ID của thuốc theo tên
    public int getMedicineIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM medicines WHERE name = ?", new String[]{name});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }

        cursor.close();
        return -1;  // Trả về -1 nếu không tìm thấy thuốc với tên này
    }

    // Trả về phòng khám tương ứng với bác sĩ
    public String getClinicRoomForDoctor(String doctorName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM clinic_rooms WHERE doctor_name = ?", new String[]{doctorName});
        if (cursor.moveToFirst()) {
            String room = cursor.getString(0);
            cursor.close();
            return room;
        }
        cursor.close();
        return "Không có phòng";
    }


}


