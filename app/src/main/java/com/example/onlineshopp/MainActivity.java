package com.example.onlineshopp;

import static com.example.onlineshopp.Database.ConnectFirebase.db;
import static com.example.onlineshopp.temptlA.REQUEST_CART;
import static com.example.onlineshopp.temptlA.REQUEST_GOHOME;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.FragmentLayout.FragmentHomeViewModel;
import com.example.onlineshopp.FragmentLayout.FragmentMeViewModel;
import com.example.onlineshopp.FragmentLayout.Fragment_Cart;
import com.example.onlineshopp.FragmentLayout.Fragment_Home;
import com.example.onlineshopp.FragmentLayout.Fragment_me;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.interface1.InterFace;
import com.example.onlineshopp.interface1.readjson;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements InterFace {
    private  String TAG= MainActivity.class.getName();
    public static final int REQUEST_CODE = 1;
    BottomNavigationView bottomNavigationView;
    FrameLayout mView;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        MainActivityModel.loadFood();//load full mon an
        MainActivityModel.loadFoodcheck("");//load full mon an
        MainActivityModel.loadBanner();

        //Khi Chay app  FrameLayout se load Fragment_Home dau` tien
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_view,new Fragment_Home()).commit();
        }
        eVentCompoment();

    }



    @Override
    public void setMapping() {
        //Ánh xạ tới  BottomNavigationView
        bottomNavigationView=findViewById(R.id.btngv);

        //Ánh xạ tới FrameLayout
        mView=findViewById(R.id.framelayout_view);
    }

    @Override
    public void eVentCompoment() {
        setMapping();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                            //Set idItem in Menu
                        int idItem = menuItem.getItemId();
                        if (idItem == R.id.navigation_home) {
//                            Load Fragment_Home
                            Fragment_Home fragmentHome=new Fragment_Home();
                            loadFragment(fragmentHome);
                        } else if (idItem == R.id.navigation_cart) {
//                            Load Fragment_Search
                            Fragment_Cart fragmentCart=new Fragment_Cart();
                            loadFragment(fragmentCart);
                        } else if (idItem == R.id.navigation_notifications) {
//                            Load Fragment_Notification
                            Fragment_me fragmentMe=new Fragment_me();
                            loadFragment(fragmentMe);
                        } else if (idItem == R.id.navigation_profile) {
//                            Load Fragment_me
                            Fragment_me fragmentMe=new Fragment_me();
                            loadFragment(fragmentMe);
                        }
                        return true;
                    }
        });
    }

    @Override
    public void onQuantityChanged() {

    }

    @Override
    public void getDataCheckBox(List<cartItem> mlistcart) {
    }


    //    Load FragmentLayout
    public void loadFragment(Fragment fragment){

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_view,fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String resultData = data.getStringExtra("uid");
                Log.v(TAG,resultData);
                // Xử lý dữ liệu nhận được
            }
        }
    }

}