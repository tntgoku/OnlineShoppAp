package com.example.onlineshopp.CRUD;

import static java.lang.System.load;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.R;
import com.example.onlineshopp.temptlA;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class add_news extends AppCompatActivity {
    EditText id, name, date, desc, txt_ImageURL;
    ImageView image;
    Button add, exit, datePicker, btn_loadImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_news);

        date = findViewById(R.id.txt_DatePost);
        name = findViewById(R.id.text_NameNews);
        desc = findViewById(R.id.txt_desc);
        image = findViewById(R.id.image);
        txt_ImageURL = findViewById(R.id.txt_ImageURL);
        btn_loadImage = findViewById(R.id.btn_loadImage);
        add = findViewById(R.id.btn_them);
        exit = findViewById(R.id.btn_huy);
        datePicker = findViewById(R.id.date_picker);


        btn_loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageUrl = txt_ImageURL.getText().toString().trim();
                if (!imageUrl.isEmpty()) {
                   Glide.with(add_news.this)
                           .load(imageUrl)
                           .placeholder(R.drawable.img)
                           .error(R.drawable.img)
                           .into(image);
                } else {
                    Toast.makeText(add_news.this, "Vui lòng nhập đường dẫn hình ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageNews = txt_ImageURL.getText().toString();
                String nameNews = name.getText().toString();
                String datePost = date.getText().toString();
                String descNews = desc.getText().toString();
                addNews(imageNews,nameNews,datePost,descNews);
                DataBase db = new DataBase(add_news.this);
                db.addNews(imageNews, nameNews, datePost, descNews);
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        add_news.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                date.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });
    }
    public  void addNews(String Image_News, String Name_News, String Date_Post, String Desc_News) {

        ConnectFirebase.db = FirebaseFirestore.getInstance();
        Map<String, Object> newsData = new HashMap<>();
        // Add data to Firestore
        DocumentReference reference=ConnectFirebase.db.collection("news").document();
        newsData.put("Image_News", Image_News);
        newsData.put("Id_mAuth", temptlA.IDuser);
        newsData.put("Name_News", Name_News);
        newsData.put("Date_Post", Date_Post);
        newsData.put("Desc_News", Desc_News);
        newsData.put("ID_News", reference.getId());
        reference.set(newsData)
                .addOnSuccessListener(aVoid ->{
                        Toast.makeText(add_news.this,"Thêm thành công bài viết mới ",Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Log.i("Firestore", "Failed to add data to Firestore: " + e.getMessage()));
    }
}
