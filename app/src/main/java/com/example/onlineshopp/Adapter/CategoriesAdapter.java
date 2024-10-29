package com.example.onlineshopp.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshopp.ActivityLayout.ActivityDeltai_Item;
import com.example.onlineshopp.ActivityLayout.Activity_cateItem;
import com.example.onlineshopp.ActivityLayout.Activity_login;
import com.example.onlineshopp.Object.ItemCat;
import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.RecyleItemCateBinding;
import com.example.onlineshopp.temptlA;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    public List<ItemCat> mlist;
    public CategoriesAdapter(List<ItemCat> mlist1){
        mlist=mlist1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyleItemCateBinding mview=RecyleItemCateBinding.inflate(LayoutInflater.from(parent.getContext()));
        return  new MyViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                ItemCat item=mlist.get(position);

                holder.binding.txtTitlecat.setText(item.getTitle());
        String imageUrl = item.getPicURL();
        if (imageUrl != null && !imageUrl.isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.binding.imgCart1);
        } else {
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temptlA.isLogin){
                    Intent i=new Intent(holder.itemView.getContext(), Activity_cateItem.class);
                    i.putExtra("cateID",item.getID());
                    holder.itemView.getContext().startActivity(i);
                }else{
                    Intent i=new Intent(holder.itemView.getContext(), Activity_login.class);
                    holder.itemView.getContext().startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size()>0?mlist.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         TextView tv;
         ImageView img;
         RecyleItemCateBinding binding;
        public MyViewHolder(RecyleItemCateBinding binding) {
            super(binding.getRoot());
           this.binding=binding;
        }
    }
}
