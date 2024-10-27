package com.example.onlineshopp.ActivityQLBH;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.Adapter.listviewAdapter;
import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.Object.ItemFood;
import com.example.onlineshopp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GDQLSanPham extends AppCompatActivity  implements uploadimage{
    Button btnSearch, btnManageCategories, btnAddProduct, btnExit;
    ListView listView;
    TextView searchView;
    private StorageReference storageReference;

    private SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdqlsan_pham);

        // Khởi tạo các view
        btnSearch = findViewById(R.id.btnSearch);
        btnManageCategories = findViewById(R.id.btnmmanageCategories);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnExit = findViewById(R.id.btnExit);
        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.lvProductList);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Thiết lập sự kiện cho các nút
        btnManageCategories.setOnClickListener(view -> {
            Intent intent = new Intent(GDQLSanPham.this, QLDanhMucSP.class);
            startActivity(intent);
        });

        btnAddProduct.setOnClickListener(view -> {
            Intent intent = new Intent(GDQLSanPham.this, Create_sp.class);
            startActivity(intent);
        });

        btnExit.setOnClickListener(view -> finish());

        ConnectSQLite db = new ConnectSQLite(getApplicationContext());
        SQLiteDatabase  datahelps=db.getReadableDatabase();
        List<Bitmap> mlist=new ArrayList<>();

        //Get Data SQLite Products
        {
            Cursor cursor = datahelps.rawQuery("SELECT * FROM " + ConnectSQLite.TABLE_1, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String id = cursor.getString(0);
                    @SuppressLint("Range") String name = cursor.getString(1);

                    @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("PriceOriginal"));
                    @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(6);
                    Log.v("TAG","Price : "+ price+"\nId : "+id);
                    Bitmap bitmap = byteArrayToBitmap(imageBytes);
//                    uploadImage(bitmap,name,this);
                    mlist.add(bitmap);
                }
                cursor.close();
            }
            db.close();
        }

            listviewAdapter mlw=new listviewAdapter(this,mlist);
        listView.setAdapter(mlw);
    }


    public List<ItemFood> getAllProductss() {
        List<ItemFood> Productss = new ArrayList<>();

        return Productss;
    }
    private void LoadSQLite(){

    }
        public  Bitmap byteArrayToBitmap(byte[] byteArray) {
            // Kiểm tra xem mảng byte không null và có độ dài lớn hơn 0
            if (byteArray != null && byteArray.length > 0) {
                return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
            return null; // Trả về null nếu mảng byte không hợp lệ
        }



    private void uploadImage(Bitmap bitmap,String nameimg, uploadimage callback) {
        storageReference = FirebaseStorage.getInstance().getReference();
        String result=null;
        if (bitmap != null) {
            // Chuyển đổi bitmap thành mảng byte
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Chọn định dạng và độ nén
            byte[] data = baos.toByteArray();

            // Tạo một tham chiếu đến vị trí nơi bạn muốn lưu trữ hình ảnh trong Firebase Storage
            StorageReference imageRef = storageReference.child( nameimg + ".jpg");

            // Tải lên dữ liệu
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Tải lên thành công
                Log.d("FirebaseStorage", "Upload successful!");
                // Lấy URL của hình ảnh vừa tải lên
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        callback.onSuccess(uri.toString());
                    Log.d("FirebaseStorage", "Download URL: " + uri.toString());
                });
            }).addOnFailureListener(e -> {
                // Tải lên thất bại
                Log.e("FirebaseStorage", "Upload failed: " + e.getMessage());
            });
        } else {
            Log.e("UploadImage", "Bitmap is null");
        }
    }

    @Override
    public void onSuccess(String url) {

    }

    @Override
    public void onFailure(Exception e) {

    }
}



