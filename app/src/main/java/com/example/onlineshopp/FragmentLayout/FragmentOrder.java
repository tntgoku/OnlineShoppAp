package com.example.onlineshopp.FragmentLayout;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlineshopp.ActivityLayout.ControllerOrder;
import com.example.onlineshopp.Adapter.orderadminAdapter;
import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.FragmentCartBinding;
import com.example.onlineshopp.databinding.FragmentorderBinding;

public class FragmentOrder extends Fragment {

    private FragmentOrderViewModel mViewModel;

    public static FragmentOrder newInstance() {
        return new FragmentOrder();
    }

    private View mview;
    FragmentorderBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentorderBinding.inflate(getLayoutInflater());

        orderadminAdapter ll=new orderadminAdapter(ControllerOrder.mlistOrderadmin);

        binding.orderRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        binding.orderRecycleView.setAdapter(ll);

//        binding.orderRecycleView
        return binding.getRoot();
    }
}
