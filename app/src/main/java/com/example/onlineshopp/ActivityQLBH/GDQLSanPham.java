package com.example.onlineshopp.ActivityQLBH;

import android.annotation.SuppressLint;
import android.content.ClipData;
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
import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.Object.ItemFood;
import com.example.onlineshopp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GDQLSanPham extends AppCompatActivity  {
    Button btnSearch, btnManageCategories, btnAddProduct, btnExit;
    ListView listView;
    TextView searchView;
    private StorageReference storageReference;
    List<ItemFood> getAllProductss= new ArrayList<>();
    SQLiteDatabase database;

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
        ConnectSQLite db = new ConnectSQLite(getApplicationContext());
         database= db.getReadableDatabase();

        LoadSQLite();
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




    }

    private void LoadSQLite(){
        //Get Data SQLite Products
        {
            Cursor cursor = database.rawQuery("SELECT * FROM " + ConnectSQLite.TABLE_1, null, null);
            if (cursor != null) {
                if(cursor.getCount()!=0) {
                    while (cursor.moveToNext()) {
                        @SuppressLint("Range") String id = cursor.getString(0);
                        @SuppressLint("Range") String name = cursor.getString(1);
                        int  sell = cursor.getInt(3);
                        @SuppressLint("Range") int Price = cursor.getInt(cursor.getColumnIndex("PriceOriginal"));
                        @SuppressLint("Range") int CateID = cursor.getInt(cursor.getColumnIndex("Cate_ID"));
                        @SuppressLint("Range") String Desc = cursor.getString(7);
                        @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(6);
                        Bitmap bitmap = byteArrayToBitmap(imageBytes);
                        uploadImage(bitmap, id,name,Price,sell,CateID,Desc);
                    }
                    Log.v("GDQLSanPham sdas", "SQlite 96 GDQLsanpham : co " + cursor.getCount() + " ban ghi");
                    cursor.close();
                }else{
                    ConnectFirebase.db= FirebaseFirestore.getInstance();
                    ConnectFirebase.db.collection("dishes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.getDocuments().isEmpty()){
                                for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()){
                                   String id= snapshot.getId();
                                    String name=snapshot.get("dishName",String.class);
                                    String desc=snapshot.get("description",String.class);
                                    String imgurl=snapshot.get("imageUrl",String.class);
                                    int idcate,price,status,sell;
                                    idcate= Math.toIntExact(snapshot.get("dishCategoryId", Long.class));
                                    price= Math.toIntExact(snapshot.get("price",Long.class));
                                    status= Math.toIntExact(snapshot.get("status", Long.class));
                                    sell=Math.toIntExact(snapshot.get("sell",Long.class));
                                    ItemFood its=new ItemFood(id,name,desc,price,idcate,imgurl,status,sell);
                                    getAllProductss.add(its);
                                }
                            }else{

                            }
                        }
                    }).addOnFailureListener(e -> {
                        Log.w("GDQLSan pham","Loi say ra o day"+e.getMessage());
                    });
                }
            }
            database.close();
        }

        listviewAdapter mlw=new listviewAdapter(this,getAllProductss);
        listView.setAdapter(mlw);
    }
        public  Bitmap byteArrayToBitmap(byte[] byteArray) {
            // Kiểm tra xem mảng byte không null và có độ dài lớn hơn 0
            if (byteArray != null && byteArray.length > 0) {
                return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
            return null; // Trả về null nếu mảng byte không hợp lệ
        }



    private void uploadImage(Bitmap bitmap,String id,String nameimg,int price,int sell,int cateid,String desc) {
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
                    ItemFood itemFs=new ItemFood(id,nameimg,desc,price,cateid,uri.toString(),1,sell);
                    getAllProductss.add(itemFs);
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


}



