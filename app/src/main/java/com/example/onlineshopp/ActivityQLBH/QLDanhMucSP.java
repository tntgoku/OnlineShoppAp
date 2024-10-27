package com.example.onlineshopp.ActivityQLBH;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.R;

import java.util.ArrayList;
import java.util.List;

public class QLDanhMucSP extends AppCompatActivity {

    private Button btnAddCategory, btnExitCategory, btnSearchCategory;
    private ListView listViewCategories;
    private ArrayAdapter<String> adapter;
    private List<String> categoryList;
    private EditText editTextSearchCategory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qldanh_muc_sp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnExitCategory = findViewById(R.id.btnExitCategory);
        btnSearchCategory = findViewById(R.id.btnSearchCategory);
        listViewCategories = findViewById(R.id.lvCategoryList);
        editTextSearchCategory = findViewById(R.id.editTextSearchCategory);

        btnAddCategory.setOnClickListener(view -> {
            Intent intent = new Intent(QLDanhMucSP.this, Create_dmsp.class);
            startActivity(intent);
        });

        btnExitCategory.setOnClickListener(view -> finish());

        btnSearchCategory.setOnClickListener(view -> {
            String searchText = editTextSearchCategory.getText().toString().trim();
            if (!searchText.isEmpty()) {
                loadCategoriesFromSQLite(searchText);
            } else {
                Toast.makeText(this, "Vui lòng nhập tên danh mục để tìm kiếm", Toast.LENGTH_SHORT).show();
            }
        });

        // Tải dữ liệu từ SQLite và hiển thị lên ListView
        loadCategoriesFromSQLite(null);
    }

    private void loadCategoriesFromSQLite(String searchText) {
        ConnectSQLite db = new ConnectSQLite(this);
        Cursor cursor;

        // Nếu có từ khóa tìm kiếm, sử dụng câu lệnh SQL để tìm kiếm
        if (searchText != null) {
            cursor = db.getReadableDatabase().rawQuery(
                    "SELECT Name_Cate, Description_Cate FROM CATEGORY WHERE Name_Cate LIKE ?",
                    new String[]{"%" + searchText + "%"});
        } else {
            cursor = db.getReadableDatabase().rawQuery("SELECT Name_Cate, Description_Cate FROM CATEGORY", null);
        }

        categoryList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("Name_Cate"));
                String categoryDescription = cursor.getString(cursor.getColumnIndexOrThrow("Description_Cate"));
                // Kết hợp tên danh mục và mô tả để hiển thị
                String displayText = categoryName + " - " + categoryDescription;
                categoryList.add(displayText);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "Không có danh mục nào!", Toast.LENGTH_SHORT).show();
        }
        cursor.close();

        // Sử dụng ArrayAdapter để hiển thị danh sách lên ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryList);
        listViewCategories.setAdapter(adapter);

        listViewCategories.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedCategory = categoryList.get(position).split(" - ")[0]; // Lấy tên danh mục

            // Tạo AlertDialog với hai lựa chọn: Sửa và Xóa
            AlertDialog.Builder builder = new AlertDialog.Builder(QLDanhMucSP.this);
            builder.setTitle("Chọn thao tác");
            builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, (dialog, which) -> {
                if (which == 0) {
                    // Người dùng chọn "Sửa" -> Chuyển đến trang Update_dmsp
                    Intent intent = new Intent(QLDanhMucSP.this, Update_dmsp.class);
                    intent.putExtra("CATEGORY_NAME", selectedCategory);
                    startActivity(intent);
                } else if (which == 1) {
                    // Người dùng chọn "Xóa" -> Xóa danh mục khỏi cơ sở dữ liệu
                    deleteCategory(selectedCategory);
                }
            });
            builder.show();
            return true;
        });
    }
    // Hàm xóa danh mục
    private void deleteCategory(String name) {
        ConnectSQLite dbHelper = new ConnectSQLite(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Thực hiện xóa danh mục trong SQLite
        int result = db.delete("CATEGORY", "Name_Cate = ?", new String[]{name});
        if (result > 0) {
            Toast.makeText(this, "Xóa danh mục thành công", Toast.LENGTH_SHORT).show();
            loadCategoriesFromSQLite(null); // Tải lại dữ liệu sau khi xóa
        } else {
            Toast.makeText(this, "Xóa danh mục thất bại", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadCategoriesFromSQLite(null);
    }
}
