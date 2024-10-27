package com.example.onlineshopp.ActivityQLBH;

import android.content.Intent;
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

public class Update_dmsp extends AppCompatActivity {

    private EditText etCategoryName, etCategoryDescription;
    private Button btnUpdateCategory, btnDeleteDm, btnExit;
    private String currentCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_dmsp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các view
        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        btnUpdateCategory = findViewById(R.id.btnUpdateCategory);
        btnDeleteDm = findViewById(R.id.btnDeleteDm);
        btnExit = findViewById(R.id.btnExit);

        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        currentCategoryName = intent.getStringExtra("CATEGORY_NAME");
        etCategoryName.setText(currentCategoryName);

        // Sự kiện cập nhật danh mục
        btnUpdateCategory.setOnClickListener(view -> updateCategory());

        // Sự kiện xóa danh mục
        btnDeleteDm.setOnClickListener(view -> deleteCategory());

        // Sự kiện thoát
        btnExit.setOnClickListener(view -> finish());
    }

    private void updateCategory() {
        String updatedCategoryName = etCategoryName.getText().toString();
        String updatedDescription = etCategoryDescription.getText().toString();

        if (!updatedCategoryName.isEmpty()) {
            ConnectSQLite dbHelper = new ConnectSQLite(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Kiểm tra xem tên danh mục đã tồn tại hay chưa
            String checkQuery = "SELECT Name_Cate FROM CATEGORY WHERE Name_Cate = ?";
            Cursor cursor = db.rawQuery(checkQuery, new String[]{updatedCategoryName});


            if (cursor.moveToFirst()) {
                Toast.makeText(this, "Tên danh mục đã tồn tại!", Toast.LENGTH_SHORT).show();
            } else {

                String updateQuery = "UPDATE CATEGORY SET Name_Cate = ?, Description_Cate = ? WHERE Name_Cate = ?";
                db.execSQL(updateQuery, new Object[]{updatedCategoryName, updatedDescription, currentCategoryName});

                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
            cursor.close();
            db.close();
        } else {
            Toast.makeText(this, "Tên danh mục không được để trống", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteCategory() {
        ConnectSQLite dbHelper = new ConnectSQLite(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Xóa danh mục trong SQLite
        String deleteQuery = "DELETE FROM CATEGORY WHERE Name_Cate = ?";
        db.execSQL(deleteQuery, new Object[]{currentCategoryName});

        Toast.makeText(this, "Danh mục đã được xóa!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
