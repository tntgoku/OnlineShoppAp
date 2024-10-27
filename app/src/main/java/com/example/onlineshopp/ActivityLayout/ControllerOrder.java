package com.example.onlineshopp.ActivityLayout;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.MainActivityModel;
import com.example.onlineshopp.Object.ItemFood;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.Object.order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ControllerOrder {

    public static List<order> mlistOrder= new ArrayList<>();
    public static List<order> getOrderList() {
        return mlistOrder;
    }

    public static void loadOder(String id){
        //Id ở đây là id của User truy vấn lấy Order mà User đã mua
        //Truy vấn tới Collection Order với đk Where CustomersID= (ID User)
        ConnectFirebase.db.collection("order")
                .whereEqualTo("CustomersID", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Log.w("OrderFirebase", "User này chưa có Order trên Firebase.....");
                            } else {
                                List<List<cartItem>> lists=new ArrayList<>();

                                for (DocumentSnapshot snapshot : task.getResult()) {
                                    String address = snapshot.getString("Address");
                                    String idcs = snapshot.getString("CustomersID");
                                    String idOrder = snapshot.getString("Idorder");
                                    String phone = snapshot.getString("Phone");
                                    String payment = snapshot.getString("Payment");
                                    String nameUser = snapshot.getString("Nameuser");
                                    String timer = snapshot.getString("Timer");
                                    String orderStatus = snapshot.contains("orderStatus") ? snapshot.getString("orderStatus") : "PENDING";
                                    Long amountLong = snapshot.getLong("Amount");
                                    Long totalLong = snapshot.getLong("Total");
                                    Long freeshipLong = snapshot.getLong("Freeship");
                                    int amount = amountLong != null ? amountLong.intValue() : 0;
                                    int total = totalLong != null ? totalLong.intValue() : 0;
                                    int freeship = freeshipLong != null ? freeshipLong.intValue() : 0;

                                    List<Map<String, Object>> itemsOrderList = (List<Map<String, Object>>) snapshot.get("ItemsOrder");
                                    List<cartItem> cartItemList = new ArrayList<>();
                                    // Lay san pham da mua trong array ItemsOrder cua Collision Order
                                    if (itemsOrderList != null) {
                                        for (int j=0;j<itemsOrderList.size();j++) {
                                            Map<String, Object> item = itemsOrderList.get(j);
                                            String itemId = (String) item.get("ID");
                                            String itemName = (String) item.get("Name");
                                            Long quantityLong = (Long) item.get("Quantity");
                                            Long itemTotalLong = (Long) item.get("Total");
                                            int quantity = quantityLong != null ? quantityLong.intValue() : 0;
                                            int itemTotal = itemTotalLong != null ? itemTotalLong.intValue() : 0;
                                            for(ItemFood i1: MainActivityModel.mlistFood){
                                                if(i1.getID().equals(itemId)){
                                                    cartItemList.add(new cartItem(itemId, i1, quantity));
                                                    Log.v("Them thanh cong","Load thanh cong i1.ID: "+i1.getID()+"\nIDitem: "+itemId);
                                                }
                                            }
                                        }
                                        mlistOrder.add(new order(idOrder,idcs,amount,total,cartItemList,payment,orderStatus,address,phone,timer));
                                    }
                                }

                            }
                        } else {
                            Log.e("OrderFirebase", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private static List<cartItem> loaditemdish(List<Map<String, Object>> mlistOdersitem) {
        ConnectFirebase.db = FirebaseFirestore.getInstance();
        List<cartItem> mlisttemp = new ArrayList<>();
        for (Map<String, Object> i : mlistOdersitem) {
            ConnectFirebase.db.collection("dishes").whereEqualTo("dishId", i.get("dishId")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot snapshot = task.getResult().getDocuments().get(0);
                            String id, name, desc, imgurl;
                            id = snapshot.getId();
                            name = snapshot.get("dishName", String.class);
                            desc = snapshot.get("description", String.class);
                            imgurl = snapshot.get("imageUrl", String.class);
                            int idcate, price, status, sell;
                            idcate = Math.toIntExact(snapshot.get("dishCategoryId", Long.class));
                            price = Math.toIntExact(snapshot.get("price", Long.class));
                            status = Math.toIntExact(snapshot.get("status", Long.class));
                            sell = Math.toIntExact(snapshot.get("sell", Long.class));
                            Log.v("TAG", id + "\n" + sell + "\n" + name + "\n");
                            ItemFood items = new ItemFood(id, name, desc, price, idcate, imgurl, status, sell);
                            String ids = (String) i.get("dishId");
                            Long quantityLong = (Long) i.get("Quantity");
                            Integer quantity = quantityLong != null ? quantityLong.intValue() : 0;
                            mlisttemp.add(new cartItem(ids, items, quantity));
                        }
                    }
                }
            });
        }
        return mlisttemp;
    }


}
