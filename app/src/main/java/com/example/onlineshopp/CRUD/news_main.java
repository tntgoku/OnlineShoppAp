package com.example.onlineshopp.CRUD;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopp.Adapter.News_Adapter;
import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class news_main  extends AppCompatActivity {
        private static final int REQUEST_CODE_ADD = 1;
        RecyclerView recyclerView;
        Button btn_add_news,btngobackhome;
        DataBase db;
        ArrayList<String> news_name, news_date, news_desc, news_img;
        ArrayList<Integer> news_id;
        News_Adapter adapter;
        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.news_main);
            recyclerView = findViewById(R.id.recyclerView_news);
            btn_add_news = findViewById(R.id.btn_add_news);
            btngobackhome=findViewById(R.id.btngotohome);
            btn_add_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(news_main.this, add_news.class);
                    startActivityForResult(intent, REQUEST_CODE_ADD);
                }
            });
            db = new DataBase(news_main.this);
            news_id = new ArrayList<>();
            news_name = new ArrayList<>();
            news_date = new ArrayList<>();
            news_desc = new ArrayList<>();
            news_img = new ArrayList<>();

            btngobackhome.setOnClickListener(view -> {
                finish();
            });

            display();
            adapter = new News_Adapter(this, news_main.this, news_id, news_name, news_date, news_desc, news_img);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
                news_id.clear();
                news_img.clear();
                news_name.clear();
                news_desc.clear();
                news_date.clear();
                recreate();
                display();
                adapter.notifyDataSetChanged();
            }
        }


        @SuppressLint("NotifyDataSetChanged")
        public void display() {
            Cursor cs = db.SelectData();
            if (cs.getCount() == 0) {
                Log.d("aaa", "Display_data: Không có dữ liệu");
                // go to firebase get data

                return;
            } else {
                while (cs.moveToNext()) {
                    news_id.add(Integer.valueOf((cs.getString(0))));
                    news_img.add(cs.getString(1));
                    news_name.add(cs.getString(2));
                    news_desc.add(cs.getString(3));
                    news_date.add(cs.getString(4));
                }
            }
        }

        private void getdataFirebase(){
            ConnectFirebase.db= FirebaseFirestore.getInstance();
            ConnectFirebase.db.collection("news").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){

                        }
                    }
                }
            });

        }



}
