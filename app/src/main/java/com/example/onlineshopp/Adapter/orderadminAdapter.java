package com.example.onlineshopp.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopp.ActivityLayout.Activity_deltaisorder;
import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Object.order;
import com.example.onlineshopp.databinding.ItemorderfragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orderadminAdapter extends RecyclerView.Adapter<orderadminAdapter.MyviewHolder> {
    private List<order> mlistt;

    public orderadminAdapter(List<order> mlistt) {
        this.mlistt = mlistt;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemorderfragmentBinding binding=ItemorderfragmentBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new MyviewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, @SuppressLint("RecyclerView") int position) {
        order items=mlistt.get(position);

        holder.binding.textname.setText(items.getName());
        holder.binding.textViewphone.setText(items.getPhone());
        holder.binding.textViewaddress.setText(items.getAddress());
        holder.binding.textViewtotal.setText(items.getTotal());
        holder.binding.textViewfreeship.setText(items.getPayment());
        String values;
        if(items.getOrderstatus().equals("PENDING")) {
            values = "Đang chờ xử lý";
        }
        else if (items.getOrderstatus().equals("PROCESSING")){
            values="Đang xử lý";
        }
        else if (items.getOrderstatus().equals("CANCEL")) {
            values="HỦY ĐƠN HÀNG";
        }else{
            values="Đã giao hàng";
        }
        holder.binding.textViewstatusorder.setText(values);
        holder.itemView.setOnClickListener(view -> {
            Intent i=new Intent(holder.itemView.getContext(), Activity_deltaisorder.class);
            i.putExtra("order",items);
            holder.itemView.getContext().startActivity(i);
        });
        if(items.getOrderstatus().equals("PENDING")){
            holder.binding.button8.setOnClickListener(view -> {
                ConnectFirebase.db= FirebaseFirestore.getInstance();
                ConnectFirebase.db.collection("order").whereEqualTo("Idorder",items.getIdUser()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                         String s=   task.getResult().getDocuments().get(0).getId();
                            ConnectFirebase.db.collection("order").document(s)
                                    .update("orderStatus","PROCESSING")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.v("TaG","Them thanh cong");
                                                mlistt.get(position).setOrderstatus("PROCESSING");
                                                Toast.makeText(holder.itemView.getContext(),"Cap nhat lai trang thai don hang thanh cong",Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                            }
                                        }
                                    }
                            );
                        }
                    }
                });
            });
            holder.binding.button9.setOnClickListener(view -> {
                mlistt.remove(position);
                ConnectFirebase.db= FirebaseFirestore.getInstance();
                ConnectFirebase.db.collection("order").whereEqualTo("Idorder",items.getIdUser()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                                DocumentSnapshot snapshot=task.getResult().getDocuments().get(0);
                                Map<String,Object> dataToMove=snapshot.getData();
                                dataToMove.put("orderStatus","CANCEL");
                            ConnectFirebase.db.collection("cancelorder")
                                    .document(snapshot.getId()) // Use the same ID in the new collection
                                    .set(dataToMove)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Successfully copied document, now delete the original
                                            ConnectFirebase.db.collection("order")
                                                    .document(snapshot.getId())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("Firestore", "Đã xóa hơn đơn này ");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e("Firestore", "Error deleting original document", e);
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });

                            notifyDataSetChanged();
                Toast.makeText(holder.itemView.getContext()," xóa thành công hóa đơn này",Toast.LENGTH_SHORT).show();
            });
        } else if (items.getOrderstatus().equals("PROCESSING") || items.getOrderstatus().equals("DELIVERED")) {
            holder.binding.button8.setOnClickListener(view -> {
                Toast.makeText(holder.itemView.getContext(),"Đơn hàng đang trong trag thái Đang xử lý, Đang Giao hàng",Toast.LENGTH_SHORT).show();
            });
            holder.binding.button9.setOnClickListener(view -> {
                Toast.makeText(holder.itemView.getContext(),"Không thể xóa hóa đơn này",Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return mlistt.size()>0?mlistt.size():0;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{
        ItemorderfragmentBinding binding;
        public MyviewHolder(ItemorderfragmentBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
