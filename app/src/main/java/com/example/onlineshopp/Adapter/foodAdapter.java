package com.example.onlineshopp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshopp.ActivityLayout.ActivityDeltai_Item;
import com.example.onlineshopp.ActivityLayout.Activity_login;
import com.example.onlineshopp.Object.ItemCat;
import com.example.onlineshopp.Object.ItemFood;
import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.ActivityLoginBinding;
import com.example.onlineshopp.databinding.ItemfoodBinding;
import com.example.onlineshopp.interface1.InterFace;
import com.example.onlineshopp.temptlA;

import java.util.List;

public class foodAdapter extends RecyclerView.Adapter<foodAdapter.myViewHolder> {
        private List<ItemFood> mlist;
    private List<ItemFood> mlistfilter;
    public foodAdapter(List<ItemFood> mlist) {
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemfoodBinding binding= ItemfoodBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        ItemFood food=mlist.get(position);
        holder.binding.texttitlefood.setText(food.getTitle());
        holder.binding.textcost.setText(String.valueOf(food.getPrice())+"đ");
        holder.binding.textsell.setText(String.valueOf(food.getSell()));
        Glide.with(holder.itemView.getContext()).
                load(food.getPicURL())
                .into(holder.binding.imageViewfood);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i=new Intent(holder.itemView.getContext(), ActivityDeltai_Item.class);
                    i.putExtra("items",food);
                    holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size()>0?mlist.size():0;
    }

    public class myViewHolder extends  RecyclerView.ViewHolder{

        ItemfoodBinding binding;
        public myViewHolder(ItemfoodBinding binding) {
            super(binding.getRoot());

            this.binding=binding;

        }

    }
}
