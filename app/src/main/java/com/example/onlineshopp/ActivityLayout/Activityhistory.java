package com.example.onlineshopp.ActivityLayout;

import static com.example.onlineshopp.ActivityLayout.ControllerOrder.loadOder;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.onlineshopp.Adapter.orderAdapter;
import com.example.onlineshopp.Adapter.viewFragmentAdapter;
import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.MainActivityModel;
import com.example.onlineshopp.Object.ItemFood;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.Object.order;
import com.example.onlineshopp.databinding.HistorybuyitemBinding;
import com.example.onlineshopp.interface1.InterFace;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Activityhistory extends AppCompatActivity implements InterFace {
    HistorybuyitemBinding binding;
    private static List<order> mlistOrder1= new ArrayList<>();
    private static String TAG="Activityhistory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
            ConnectFirebase.db = FirebaseFirestore.getInstance();

        setMapping();
        eVentCompoment();
    }


    @Override
    public void setMapping() {

    }

    @Override
    public void eVentCompoment() {
        binding.imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.itemhasorder.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        mlistOrder1=ControllerOrder.mlistOrder;
        orderAdapter ordera=new orderAdapter(mlistOrder1);

        binding.itemhasorder.setAdapter(ordera);
    }

    @Override
    public void onQuantityChanged() {

    }

    @Override
    public void getDataCheckBox(List<cartItem> mlistcart) {

    }
}
