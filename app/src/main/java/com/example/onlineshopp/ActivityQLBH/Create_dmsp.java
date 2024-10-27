package com.example.onlineshopp.ActivityQLBH;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.R;

public class Create_dmsp extends AppCompatActivity {

    EditText etCategoryName, etCategoryDescription;
    Button btnAddCategory, btnExit, btnDelete;
    ConnectSQLite databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_dmsp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện và kết nối với SQLite
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnExit = findViewById(R.id.btnExit);
        btnDelete = findViewById(R.id.btnDeleteDm);

        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        etCategoryName = findViewById(R.id.etCategoryName);

        databaseHelper = new ConnectSQLite(this); // Khởi tạo đối tượng ConnectSQLite

        btnExit.setOnClickListener(view -> finish());

        btnAddCategory.setOnClickListener(view -> {
            String mota = etCategoryDescription.getText().toString();
            String tenDM = etCategoryName.getText().toString();

            if (mota.isEmpty()) {
                etCategoryDescription.setError("Cannot be empty");
            }
            if (tenDM.isEmpty()) {
                etCategoryName.setError("Cannot be empty");
            } else {
                addDMSP(tenDM, mota);
            }
        });
    }

    private void addDMSP(String tenDM, String mota) {
        // Mở kết nối với cơ sở dữ liệu và chuẩn bị dữ liệu
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name_Cate", tenDM);
        values.put("Description_Cate", mota); // Sử dụng cột Description_Cate

        String checkQuery = "SELECT Name_Cate FROM CATEGORY WHERE Name_Cate = ?";
        Cursor cursor = db.rawQuery(checkQuery, new String[]{tenDM});
        if(cursor.moveToFirst()){
            Toast.makeText(this, "Tên danh mục đã tồn tại", Toast.LENGTH_SHORT).show();
        }else {

            // Thêm dữ liệu vào bảng CATEGORY
            long result = db.insert("CATEGORY", null, values);
            if (result != -1) {
                Toast.makeText(this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show();
            }
            db.close(); // Đóng kết nối sau khi thêm xong
        }
    }
}
