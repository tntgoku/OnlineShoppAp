package com.example.onlineshopp.ActivityQLBH;

import static com.example.onlineshopp.Database.ConnectSQLite.TABLE_1;
import static com.example.onlineshopp.Database.ConnectSQLite.TABLE_2;
import static com.example.onlineshopp.temptlA.REQUEST_CAMERA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.R;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Create_sp extends AppCompatActivity implements uploadimage {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;

    Button btncamera, btnUploadImage, btnAddProduct, btnthoat;
    EditText etProductName, etProductDescription, etProductPrice, etProductQuantity;
    Spinner spinnerCategories;
    ConnectSQLite databaseHelper;
    ImageView ivProductImage;
    byte[] imageBytes;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_sp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần
        btncamera = findViewById(R.id.btncamera);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnthoat = findViewById(R.id.btnthoat);

        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductQuantity = findViewById(R.id.etProductQuantity);

        spinnerCategories = findViewById(R.id.spinnerCategories);
        ivProductImage = findViewById(R.id.ivProductImage);

        // Khởi tạo đối tượng databaseHelper
        databaseHelper = new ConnectSQLite(this);

        // Tải dữ liệu danh mục vào Spinner
        loadCategoriesIntoSpinner();

        // Xử lý sự kiện nút camera
        btncamera.setOnClickListener(v -> openCamera());

        // Xử lý sự kiện nút upload ảnh
        btnUploadImage.setOnClickListener(v -> openGallery());

        // Xử lý sự kiện thêm sản phẩm
        btnAddProduct.setOnClickListener(view -> {
            // Lấy thông tin từ EditText
            String name = etProductName.getText().toString();
            String description = etProductDescription.getText().toString();
            Double price = null;
            Integer quantity = null;
            int Cateid=spinnerCategories.getSelectedItemPosition();
            Log.i("TAG Click spinner","Spinner Position : "+spinnerCategories.getSelectedItemPosition());
            // Kiểm tra và chuyển đổi giá trị
            try {
                price = Double.valueOf(etProductPrice.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(Create_sp.this, "Giá không hợp lệ!", Toast.LENGTH_SHORT).show();
                return; // Ngưng thực hiện nếu giá không hợp lệ
            }

            try {
                quantity = Integer.valueOf(etProductQuantity.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(Create_sp.this, "Số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
                return; // Ngưng thực hiện nếu số lượng không hợp lệ
            }

            // Lấy danh mục đã chọn từ Spinner
            String selectedCategory = spinnerCategories.getSelectedItem().toString();

            // Lấy ID của danh mục từ cơ sở dữ liệu
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT ID_Cate FROM CATEGORY WHERE Name_Cate = ?", new String[]{selectedCategory});
            int cateId = -1; // Giá trị mặc định nếu không tìm thấy

            if (cursor.moveToFirst()) {
                cateId = cursor.getInt(cursor.getColumnIndexOrThrow("ID_Cate"));
            }
            cursor.close();

            // Kiểm tra nếu cateId hợp lệ
            if (cateId == -1) {
                Toast.makeText(Create_sp.this, "Danh mục không hợp lệ!", Toast.LENGTH_SHORT).show();
                return; // Ngưng thực hiện nếu danh mục không hợp lệ
            }

            // Chuyển đổi hình ảnh từ ImageView thành byte[]
            imageBytes = imageview_to_bye(ivProductImage);
            insertDataSQLite(name,1,quantity,price,description,cateId);


            // Xóa dữ liệu trong các EditText và ImageView sau khi thêm sản phẩm
            etProductName.setText("");
            etProductDescription.setText("");
            etProductPrice.setText("");
            etProductQuantity.setText("");
            ivProductImage.setImageDrawable(null); // Đặt lại hình ảnh
        });

        btnthoat.setOnClickListener(view -> {
            finish();
        });


    }
    public  Bitmap byteArrayToBitmap(byte[] byteArray) {
        // Kiểm tra xem mảng byte không null và có độ dài lớn hơn 0
        if (byteArray != null && byteArray.length > 0) {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        return null; // Trả về null nếu mảng byte không hợp lệ
    }



    private void uploadImage(Bitmap bitmap,String nameimg) {

    }
    private void insertDataSQLite(String name, int type, int quantity, Double price, String description, int cateId){
        // Thêm sản phẩm vào cơ sở dữ liệu
        SQLiteDatabase writeDb = databaseHelper.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_1 + " (Product_name, ProductTypeID, Inventory, PriceOriginal, DiscountPrice, Images, Descr, Cate_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            writeDb.execSQL(sql, new Object[]{name, type, quantity, price, 0, imageBytes, description, cateId}); // ProductTypeID là 1 nếu không có thông tin nào khác
            Toast.makeText(Create_sp.this, "Sản phẩm đã được thêm thành công!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(Create_sp.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        insertfirebase(name,type,quantity,price,description,cateId);
    }

    private void insertfirebase(String name, int type, int quantity, Double price, String description, int cateI) {
        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference();
        Bitmap bitmap = byteArrayToBitmap(imageBytes);
        if (bitmap != null) {
            // Chuyển đổi bitmap thành mảng byte
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Chọn định dạng và độ nén
            byte[] data = baos.toByteArray();
            // Tạo một tham chiếu đến vị trí nơi bạn muốn lưu trữ hình ảnh trong Firebase Storage
            StorageReference imageRef = storageReference.child( name + ".jpg");
            // Tải lên dữ liệu
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Lấy URL của hình ảnh vừa tải lên
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.d("FirebaseStorage", "Download URL: " + uri.toString());
                    insetProduct(name,quantity,price,description,cateI,uri.toString());
                });
            }).addOnFailureListener(e -> {
                // Tải lên thất bại
                Log.e("FirebaseStorage", "Upload failed: " + e.getMessage());
            });
        } else {
            Log.e("UploadImage", "Bitmap is null");
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.img);
            Bitmap bitmap1=((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] datas=baos.toByteArray();
            StorageReference imageRef = storageReference.child( name + ".jpg");
            // Tải lên dữ liệu
            UploadTask uploadTask = imageRef.putBytes(datas);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Lấy URL của hình ảnh vừa tải lên
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.d("FirebaseStorage", "Download URL: " + uri.toString());
                    insetProduct(name,quantity,price,description,cateI,uri.toString());
                });
            }).addOnFailureListener(e -> {
                // Tải lên thất bại
                Log.e("FirebaseStorage", "Upload failed: " + e.getMessage());
            });
        }
    }
    private void insetProduct(String name,  int quantity, Double price, String description, int cateI,String url){
        Map<String,Object> data=new HashMap<>();
        data.put("dishName",name);
        int prics;
        data.put("price",(int) Math.round(price));
        data.put("sell",quantity);
        data.put("description",description);
        data.put("imageUrl",url);
        data.put("dishCategoryId",cateI);
        data.put("createdAt", temptlA.Datetimecurrent);
        data.put("updatedAt", temptlA.Datetimecurrent);
        data.put("status", 1);
        ConnectFirebase.db= FirebaseFirestore.getInstance();
        DocumentReference reference = ConnectFirebase.db.collection("dishes").document();
        reference.set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Create_sp.this, "Thêm lên Firebase thành công", Toast.LENGTH_SHORT).show();

                // Update the document
                reference.update("dishId", reference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Do something after successfully updating dishId
                        Log.d("Firestore", "Dish ID updated successfully.");
                        Toast.makeText(Create_sp.this, "Cập nhật dishId thành công", Toast.LENGTH_SHORT).show();
                        // Additional code here if needed
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Firestore", "Failed to update dishId: " + e.getMessage());
                    Toast.makeText(Create_sp.this, "Cập nhật dishId thất bại", Toast.LENGTH_SHORT).show();
                });
            } else {
                Log.e("Firestore", "Failed to set data: " + task.getException().getMessage());
                Toast.makeText(Create_sp.this, "Thêm lên Firebase thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategoriesIntoSpinner() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<String> categoryList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT Name_Cate FROM CATEGORY", null);
        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("Name_Cate"));
                categoryList.add(categoryName);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "Không có danh mục nào!", Toast.LENGTH_SHORT).show();
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);



        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.v("TAG", "Position: " + position + ", ID: " + id);
                // You can also retrieve the selected item as a string, if needed
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                Log.v("TAG", "Selected item: " + selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case where no item is selected, if necessary
            }
        });
    }


    private void openCamera() {


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the camera permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            // Permission is already granted, open the camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ivProductImage.setImageBitmap(photo);
                imageBytes = imageview_to_bye(ivProductImage); // Lưu trữ imageBytes ngay sau khi lấy ảnh
                Toast.makeText(this, "Ảnh đã được chụp!", Toast.LENGTH_SHORT).show();
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ivProductImage.setImageBitmap(bitmap);
                    imageBytes = imageview_to_bye(ivProductImage); // Lưu trữ imageBytes ngay sau khi lấy ảnh
                    Toast.makeText(this, "Ảnh đã được chọn!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Lỗi khi lấy ảnh!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public byte[] imageview_to_bye(ImageView hinh) {
        BitmapDrawable drawable = (BitmapDrawable) hinh.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onSuccess(String url) {

    }

    @Override
    public void onFailure(Exception e) {

    }
}
