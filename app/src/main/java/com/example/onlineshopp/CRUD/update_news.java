package com.example.onlineshopp.CRUD;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlineshopp.R;

import java.time.Instant;
import java.util.Calendar;

public class update_news  extends AppCompatActivity {
        DataBase db;
        EditText text_NameNews_update, txt_DatePost_update, txt_desc_update, txt_ImageURL_update;
        TextView txt_idNews_update;
        ImageView image_update;
        String id,name,date,desc, urlImage;
        Button btn_cancel, btn_update, datePicker, btn_loadImage_update;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_news);

        txt_idNews_update = findViewById(R.id.txt_idNews_update);
        image_update = findViewById(R.id.image_update);
        txt_ImageURL_update = findViewById(R.id.txt_ImageURL_update);
        btn_loadImage_update = findViewById(R.id.btn_loadImage_update);
        text_NameNews_update = findViewById(R.id.text_NameNews_update);
        txt_DatePost_update = findViewById(R.id.txt_DatePost_update);
        txt_desc_update = findViewById(R.id.txt_desc_update);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_update = findViewById(R.id.btn_update);
        datePicker = findViewById(R.id.date_picker);

        db = new DataBase(update_news.this);
        getandsetInten();

        btn_loadImage_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageUrl = txt_ImageURL_update.getText().toString().trim();

                if(!imageUrl.isEmpty()){
                    Glide.with(update_news.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.img)
                            .error(R.drawable.img_3)
                            .into(image_update);
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlImage = txt_ImageURL_update.getText().toString();
                name = text_NameNews_update.getText().toString();
                desc = txt_desc_update.getText().toString();
                date = txt_DatePost_update.getText().toString();

                DataBase d_b = new DataBase(update_news.this);
                d_b.UpdateNews(Integer.parseInt(id), urlImage, name, date, desc);
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        update_news.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                txt_DatePost_update.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });

    }

    void getandsetInten() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("urlImage") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("date") && getIntent().hasExtra("desc")){
            id = getIntent().getStringExtra("id");
            urlImage = getIntent().getStringExtra("urlImage");
            name = getIntent().getStringExtra("name");
            date = getIntent().getStringExtra("date");
            desc = getIntent().getStringExtra("desc");

            txt_idNews_update.setText(id);
            txt_ImageURL_update.setText(urlImage);
            text_NameNews_update.setText(name);
            txt_desc_update.setText(desc);
            txt_DatePost_update.setText(date);
            Log.d("aaa", "Lấy dữ liệu thành công");
        } else {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }
}

