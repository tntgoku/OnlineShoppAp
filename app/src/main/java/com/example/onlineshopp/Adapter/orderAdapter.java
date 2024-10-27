package com.example.onlineshopp.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshopp.ActivityLayout.Activity_deltaisorder;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.Object.order;
import com.example.onlineshopp.R;

import java.util.List;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.MyviewHolder>{
    private List<order> mlist;
    public  orderAdapter(List<order> mlist){
       this.mlist=mlist;
    }
    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mview= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemhistory,parent,false);


        return new MyviewHolder(mview);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(holder.itemView.getContext())
                .load(mlist.get(position).getCartItemList().get(0).getItem().getPicURL())
                .into(holder.img);

        if(mlist.get(position).getOrderstatus().equals("PENDING")){
            holder.tv3.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.primaryy));
            holder.tv3.setText("Dang cho xu ly");
        }else if(mlist.get(position).getOrderstatus().equals("PROCESSING")){
            holder.tv3.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.secondary));
            holder.tv3.setText("Dang xu ly");
        }else if(mlist.get(position).getOrderstatus().equals("DELIVERED")){
            holder.tv3.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.success));
            holder.tv3.setText("Da giao hang");
        }else{
            holder.tv3.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.cancel));

            holder.tv3.setText("Don hang da bi huy");
        }
        holder.tv1.setText("x"+String.valueOf(mlist.get(position).getAmount()));
        holder.tv2.setText(String.valueOf(mlist.get(position).getTotal())+"đ");
        StringBuilder stringBuilder=new StringBuilder();
        for(cartItem item:mlist.get(position).getCartItemList()){
            stringBuilder.append(item.getItem().getTitle()+" x"+String.valueOf(item.getQuantity())+" ");
        }
        holder.tv.setText(stringBuilder.toString());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(holder.itemView.getContext(), Activity_deltaisorder.class);
                i.putExtra("order",mlist.get(position));
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.isEmpty()?0:mlist.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tv,tv1,tv2,tv3;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imageViewitems1);
            tv=itemView.findViewById(R.id.texttitlecart);
            tv1=itemView.findViewById(R.id.textViewQuantity);
            tv2=itemView.findViewById(R.id.texttotalbillh);
            tv3=itemView.findViewById(R.id.textstatus);
        }
    }
}
