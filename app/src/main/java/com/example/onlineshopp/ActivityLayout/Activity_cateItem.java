package com.example.onlineshopp.ActivityLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopp.Adapter.foodAdapter;
import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Object.ItemFood;
import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.FragmentCateidBinding;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Activity_cateItem extends AppCompatActivity {
    private RecyclerView rec;
    private ImageView item;
    private  int ID;
    FragmentCateidBinding binding;
    private List<ItemFood> mlistfillter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding=FragmentCateidBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        Intent i=getIntent();
        if(i!=null){
           ID= i.getIntExtra("cateID",0);
        }
        if(ID>3){
            ID=3;
        }else{
            mlistfillter=Load(ID);

        }
        QueryCount(ID);
        binding.imageView9.setOnClickListener(view -> {
            finish();
        });

        setRecycle(mlistfillter);

    }
    private List<ItemFood> Load(int ID){
        ConnectFirebase.db= FirebaseFirestore.getInstance();
        List<ItemFood> templta =new ArrayList<>();
        ConnectFirebase.db.collection("dishes").whereEqualTo("dishCategoryId",ID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                         for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
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
                             templta.add(item);
                         }
                }
            }
        });
        return templta;
    }
    private void QueryCount(int ID){

        ConnectFirebase.db.collection("dishes")
                .whereEqualTo("dishCategoryId", ID).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            int count=queryDocumentSnapshots.getDocuments().size();
                        binding.nameidcate.setText("Có tổng là "+count+" với loại sản phẩm này");
                        }
                    }
                });
    }
    private void setRecycle(List<ItemFood> mlistfillters){
        foodAdapter foods=new foodAdapter(mlistfillters);
        binding.recycleCate.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        binding.recycleCate.setAdapter(foods);
    }

}
