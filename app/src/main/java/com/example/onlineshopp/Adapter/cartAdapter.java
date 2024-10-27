package com.example.onlineshopp.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshopp.ActivityLayout.ActivityDeltai_Item;
import com.example.onlineshopp.R;
import com.example.onlineshopp.interface1.InterFace;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.databinding.ItemCartBinding;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.myItemHolder> {
    private List<cartItem> mlist;
    private   boolean isVisibility=false;
    private InterFace ls;
    private  List<cartItem> mlistcheckbox =new ArrayList<>();
    public cartAdapter(List<cartItem> mlist, InterFace ls1) {
        this.mlist = mlist;
        isVisibility=false;
        ls=ls1;
    }

    @NonNull
    @Override
    public myItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCartBinding binding = ItemCartBinding.inflate(inflater, parent, false);


        return new myItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myItemHolder holder, @SuppressLint("RecyclerView") int position) {
        cartItem item =mlist.get(position);

        holder.tv.setText(item.getItem().getTitle());
        holder.tv1.setText(String.valueOf(item.getItem().getPrice())+"đ");
        holder.tv2.setText("So luong: "+String.valueOf(item.getItem().getSell()));
        Glide.with(holder.itemView.getContext()).load(item.getItem().getPicURL()).into(holder.img);


        holder.binding.imageViewminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getQuantity()>0){
                    item.setQuantity(item.getQuantity()-1);
                    Updateitem(item);
                    holder.binding.editTextText2.setText(String.valueOf(item.getQuantity()));
                notifyDataSetChanged();
                }else{
                    item.setQuantity(1);
                    holder.binding.editTextText2.setText(String.valueOf(item.getQuantity()));
                }
                ls.onQuantityChanged();
            }
        });

        holder.binding.imageView3plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getQuantity()>0){
                    item.setQuantity(item.getQuantity()+1);
                    Updateitem(item);
                    holder.binding.editTextText2.setText(String.valueOf(item.getQuantity()));
                    notifyDataSetChanged();
                }else{
                    item.setQuantity(1);
                    holder.binding.editTextText2.setText(String.valueOf(item.getQuantity()));
                }
                ls.onQuantityChanged();
            }
        });


        if(!isVisibility){
            holder.binding.imageView4.setVisibility(View.GONE);
            holder.binding.cartItemImage.setMaxWidth(120);
            holder.binding.cartItemName.setMaxWidth(430);
        }else{
            holder.binding.imageView4.setVisibility(View.VISIBLE);
            holder.binding.cartItemImage.setMaxWidth(60);
            holder.binding.imageView4.setImageResource(R.drawable.recycle_bin);
            holder.binding.cartItemName.setMaxWidth(250);
        }

        holder.binding.editTextText2.setText(String.valueOf(item.getQuantity()));
        holder.binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mlist.get(position).getItemID().equals(item.getItemID());
                deleteitem(item);
                Toast.makeText(holder.itemView.getContext(), "Xoa thanh cong ",Toast.LENGTH_SHORT).show();
                mlist.remove(item);
                notifyDataSetChanged();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(holder.itemView.getContext(), ActivityDeltai_Item.class);
                i.putExtra("items",item.getItem());
                holder.itemView.getContext().startActivity(i);
            }
        });

        holder.checkbox.setOnClickListener(view -> {
            boolean check=holder.checkbox.isChecked();
            if(check){
                mlistcheckbox.add(item);
                ls.getDataCheckBox(mlistcheckbox);
            }else {
                mlistcheckbox.remove(item);
                ls.getDataCheckBox(mlistcheckbox);
            }
        });
    }
    private void Updateitem(cartItem item){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo truy vấn để kiểm tra xem tài liệu có tồn tại không
        db.collection("cart").document(temptlA.IDCART).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    List<Map<String, Object>> items = (List<Map<String, Object>>)task.getResult().get("items");

                    for (Map<String, Object> items1 : items) {
                        if(items1.get("ID").equals(item.getItemID())){
                            items1.put("Quantity",item.getQuantity());
                            break;
                        }
                    }
                    db.collection("cart")
                            .document(temptlA.IDCART)
                            .update("items", items)
                            .addOnSuccessListener(aVoid -> {
                                System.out.println("Quantity updated successfully for ID " + item.getItemID());
                            })
                            .addOnFailureListener(e -> {
                                System.err.println("Error updating quantity: " + e);
                            });
                }
            }
        });
    }

    private void deleteitem(cartItem item){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

      db.collection("cart").document(temptlA.IDCART).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
              List<Map<String, Object>> items = (List<Map<String, Object>>) documentSnapshot.get("items");
                for(Map<String, Object> item1:items){
                    if(item1.get("ID").equals(item.getItemID())){
                        items.remove(item1);
                    }
                }

                //Update lai Items
              Map<String,Object> Updateitems=new HashMap<>();
                Updateitems.put("items",items);
              db.collection("cart").document(temptlA.IDCART).update(Updateitems).addOnSuccessListener(aVoid->{
                  Log.d("Remove items","Xoa thanh cong");
              });
          }
      }).addOnFailureListener(e -> {
          Log.e("cartAdapter 185","Loi say ra o day");
      });
    }

    public void toggleI3Visibility(){
            isVisibility = !isVisibility;
            notifyDataSetChanged();
        }
    @Override
    public int getItemCount() {
        return mlist.size()>0?mlist.size():0;
    }

    public class myItemHolder extends RecyclerView.ViewHolder {
        TextView tv,tv1,tv2;
        ImageView img;
        CheckBox checkbox;
        ItemCartBinding binding;
        public myItemHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            tv=binding.cartItemName;
            tv1=binding.cartItemPrice;
            tv2=binding.cartItemQuantity;
            img=binding.cartItemImage;
            checkbox=binding.checkBox2;
        }
    }

}
