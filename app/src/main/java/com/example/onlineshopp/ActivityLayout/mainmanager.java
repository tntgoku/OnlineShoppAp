package com.example.onlineshopp.ActivityLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.onlineshopp.ActivityQLBH.GDQLSanPham;
import com.example.onlineshopp.CRUD.news_main;
import com.example.onlineshopp.FragmentLayout.FragmentOrder;
import com.example.onlineshopp.FragmentLayout.Fragment_Cart;
import com.example.onlineshopp.FragmentLayout.Fragment_Home;
import com.example.onlineshopp.FragmentLayout.Fragment_me;
import com.example.onlineshopp.FragmentLayout.Fragment_orderadmin;
import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.ActivityForgetBinding;
import com.example.onlineshopp.databinding.MainadminBinding;
import com.example.onlineshopp.databinding.ManagerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class mainmanager extends AppCompatActivity {
MainadminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = MainadminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutadmin,new Fragment_orderadmin()).commit();
        }
        binding.imageView8.setOnClickListener(view -> {
            finish();
        });

        binding.navigationbottom.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        //Set idItem in Menu
                        int idItem = menuItem.getItemId();
                        if (idItem == R.id.navigation_order) {
//                            Load Fragment_Home
                            FragmentOrder fragmentHome=new FragmentOrder();
                            loadFragment(fragmentHome);
                        } else if (idItem == R.id.navigation_cart) {
                            Intent t=new Intent(mainmanager.this, GDQLSanPham.class);
                            startActivity(t);
                            Fragment_Cart fragmentCart=new Fragment_Cart();
                        } else if (idItem == R.id.navigation_news) {
                            Intent t=new Intent(mainmanager.this, news_main.class);
                            startActivity(t);
                        }
                        return true;
                    }
                });

    }

    private void loadFragment(Fragment Fragments) {

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutadmin,Fragments).commit();
    }
}
