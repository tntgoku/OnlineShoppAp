package com.example.onlineshopp.ActivityLayout;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.Adapter.deltaioderAdapter;
import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.Object.order;
import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.ActivityDeltaisorderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Activity_deltaisorder extends AppCompatActivity {
ActivityDeltaisorderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityDeltaisorderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent i=getIntent();
        String name,phone,address,idoder;
        order orders= (order) i.getSerializableExtra("order");
        ConnectFirebase.db= FirebaseFirestore.getInstance();
        List<cartItem> lisst= orders.getCartItemList();
        binding.imageView7.setOnClickListener(view -> {
            finish();
        });
        deltaioderAdapter adapt= new deltaioderAdapter(this,lisst);
        binding.listest.setAdapter(adapt);
        binding.editTextaddress.setText(orders.getAddress());
        binding.editTextphone.setText(orders.getPhone());
        ConnectFirebase.db.collection("order").whereEqualTo("Idorder",orders.getIdoder()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
              DocumentSnapshot snapshot= task.getResult().getDocuments().get(0);
              binding.editTextTextname.setText(snapshot.get("Nameuser",String.class));
              binding.editTextmethodpayment.setText(snapshot.get("Payment",String.class)+"\n"+snapshot.get("Timer",String.class));
            }
        });
    }
}