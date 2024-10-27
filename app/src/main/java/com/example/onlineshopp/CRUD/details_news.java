package com.example.shoppe_food;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class details_news extends AppCompatActivity {
    TextView txt_details_NameNews, txt_details_idNews, txt_details_dateNews, txt_details_descNews;
    ImageView image_details;
    Button btn_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_news);

        // anh xa
        txt_details_NameNews = findViewById(R.id.txt_details_NameNews);
        txt_details_idNews = findViewById(R.id.txt_details_idNews);
        txt_details_dateNews = findViewById(R.id.txt_details_dateNews);
        txt_details_descNews = findViewById(R.id.txt_details_descNews);
        image_details = findViewById(R.id.image_details);
        btn_back = findViewById(R.id.btn_back);

        // lay du lieu tu intent

        String name = getIntent().getStringExtra("name");
        String id = getIntent().getStringExtra("id");
        String date = getIntent().getStringExtra("date");
        String desc = getIntent().getStringExtra("desc");
        String urlImage = getIntent().getStringExtra("urlImage");

        // gan du lieu vao

        txt_details_NameNews.setText(name);
        txt_details_idNews.setText("Mã bài viết: " + id);
        txt_details_dateNews.setText("Ngày đăng: " + date);
        txt_details_descNews.setText(desc);

        Glide.with(details_news.this)
                        .load(urlImage)
                        .placeholder(R.drawable.img)
                                .error(R.drawable.img)
                                        .into(image_details);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
