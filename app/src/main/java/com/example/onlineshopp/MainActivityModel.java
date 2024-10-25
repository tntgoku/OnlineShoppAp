package com.example.onlineshopp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.onlineshopp.Object.ItemCat;
import com.example.onlineshopp.Object.ItemFood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivityModel {
    public static  FirebaseFirestore db=FirebaseFirestore.getInstance();

    public static List<ItemFood> mlistFood=new ArrayList<>();
    public static List<ItemCat> mlistcat=new ArrayList<>();
    public static void loadFood(){

        db.collection("dishes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                        for(QueryDocumentSnapshot snapshot:task.getResult()){
                            String id,name,desc,imgurl;
                            id=snapshot.getId();
                            name=snapshot.get("dishName",String.class);
                            desc=snapshot.get("description",String.class);
                            imgurl=snapshot.get("imageUrl",String.class);
                            int idcate,price,status,sell;
                            idcate= Math.toIntExact(snapshot.get("dishCategoryId", Long.class));
                            price= Math.toIntExact(snapshot.get("price",Long.class));
                            status= Math.toIntExact(snapshot.get("status", Long.class));
                            sell=Math.toIntExact(snapshot.get("sell",Long.class));
                            Log.v("TAG",id+"\n"+sell+"\n"+name+"\n");
                            ItemFood item=new ItemFood(id,name,desc,price,idcate,imgurl,status,sell);
                        mlistFood.add(item);
                        }
                }
            }
        });
    }
    public static  void loadBanner() {
        db.collection("Banner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Get the data from the document
                        String id = document.getString("Id");
                        String imagePath = document.getString("ImagePath");
                        String name = document.getString("Name");
                        //Khoi tao
                        ItemCat item = new ItemCat(Integer.parseInt(id), imagePath, name);
                        Log.d("BannerData", "Id: " + id + ", ImagePath: " + imagePath + ", Name: " + name);
                        mlistcat.add(item);
                    }
                }
            }
        });
    }

}
