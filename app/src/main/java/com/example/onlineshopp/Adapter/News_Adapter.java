package com.example.onlineshopp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshopp.CRUD.DataBase;
import com.example.onlineshopp.CRUD.details_news;
import com.example.onlineshopp.CRUD.update_news;
import com.example.onlineshopp.Object.news;
import com.example.onlineshopp.R;

import java.util.ArrayList;
import java.util.List;

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.MyViewHolder>{
    Context context;

    Activity activity;
    ArrayList<Integer> news_id;
    ArrayList<String> news_name;
    ArrayList<String> news_date;
    ArrayList<String> news_desc;
    ArrayList<String> news_img;
    List<news> mlistt;

    public News_Adapter(Context context, Activity activity,
                        ArrayList<Integer> news_id, ArrayList<String> news_name,
                        ArrayList<String> news_date, ArrayList<String> news_desc,
                        ArrayList<String> news_img) {
        this.context = context;
        this.activity = activity;
        this.news_id = news_id;
        this.news_name = news_name;
        this.news_date = news_date;
        this.news_desc = news_desc;
        this.news_img = news_img;
    }
    public News_Adapter(List<news> mlist){
        this.mlistt=mlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_news, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.news_name.setText(news_name.get(position));
        holder.news_date.setText(news_date.get(position));
        holder.news_desc.setText(news_desc.get(position));
        holder.news_id.setText("Mã bài viết: " + news_id.get(position));
        Glide.with(context)
                .load(news_img.get(position))  // URL của ảnh
                .placeholder(R.drawable.img) // ảnh chờ trong khi tải ảnh
                .error(R.drawable.img) // ảnh hiển thị khi có lỗi
                .into(holder.news_img);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, details_news.class);
                intent.putExtra("urlImage", news_img.get(position));
                intent.putExtra("name", news_name.get(position));
                intent.putExtra("date", news_date.get(position));
                intent.putExtra("desc", news_desc.get(position));
                intent.putExtra("id", String.valueOf(news_id.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });

        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, update_news.class);
                intent.putExtra("urlImage", news_img.get(position));
                intent.putExtra("name", news_name.get(position));
                intent.putExtra("date", news_date.get(position));
                intent.putExtra("desc", news_desc.get(position));
                intent.putExtra("id", String.valueOf(news_id.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa bài viết này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataBase db = new DataBase(context);
                                db.DeleteNews(news_id.get(position));
                                news_id.remove(position);
                                news_img.remove(position);
                                news_name.remove(position);
                                news_date.remove(position);
                                news_desc.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, news_id.size());
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return news_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView news_name, news_date, news_desc, news_id;
        ImageView news_img;
        Button btn_update, btn_delete;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            news_name = itemView.findViewById(R.id.tv_news_name);
            news_date = itemView.findViewById(R.id.tv_date_post);
            news_desc = itemView.findViewById(R.id.tv_news_desc);
            news_img = itemView.findViewById(R.id.img_news);
            news_id = itemView.findViewById(R.id.tv_id_news);
            linearLayout = itemView.findViewById(R.id.main_item);
            btn_update = itemView.findViewById(R.id.btn_update);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }





}
