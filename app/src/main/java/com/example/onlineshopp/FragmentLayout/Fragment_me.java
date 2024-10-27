package com.example.onlineshopp.FragmentLayout;

import static com.example.onlineshopp.temptlA.checkProfileFireBase;
import static com.example.onlineshopp.temptlA.user;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopp.ActivityLayout.ActivityDeltai_Item;
import com.example.onlineshopp.ActivityLayout.Activity_change_pass;
import com.example.onlineshopp.ActivityLayout.Activity_login;
import com.example.onlineshopp.ActivityLayout.Activity_profileuser;
import com.example.onlineshopp.ActivityLayout.Activityhistory;
import com.example.onlineshopp.ActivityLayout.ControllerOrder;
import com.example.onlineshopp.CRUD.news_main;
import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.MainActivity;
import com.example.onlineshopp.Object.User;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.interface1.InterFace;
import com.example.onlineshopp.R;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fragment_me extends Fragment implements InterFace {

    private FragmentMeViewModel mViewModel;
    TextView nameuser,tv2,tv3,tv4,tv5;
    ImageView btnimg;
    View mview;
    Button btn,btn1,btn2,btn3,btn4, btn_chage_password;
    String TAG="Fragment_me";
    FirebaseFirestore firebaseFirestore;

    public static Fragment_me newInstance() {
        return new Fragment_me();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         mview=inflater.inflate(R.layout.fragment_me, container, false);
        mViewModel = new ViewModelProvider(this).get(FragmentMeViewModel.class);
        // TODO: Use the ViewModel
        setMapping();
        eVentCompoment();

        return mview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void setMapping() {
        btnimg=mview.findViewById(R.id.imgCart);
        tv2=mview.findViewById(R.id.totalitemincart);
        nameuser=mview.findViewById(R.id.userNameTextView);
        btn=mview.findViewById(R.id.editProfileButton);
        tv3=mview.findViewById(R.id.gotoorder);
        btn2=mview.findViewById(R.id.editgotoadmin);
        btn3=mview.findViewById(R.id.manageButton);
        btn4=mview.findViewById(R.id.logOutButton);
        tv4=mview.findViewById(R.id.countoderbending);
        tv5=mview.findViewById(R.id.countoderPROCESS);
        btn_chage_password=mview.findViewById(R.id.changePasswordButton);

    }


    @Override
    public void eVentCompoment() {
        tv2.setText(ActivityDeltai_Item.slProduct());
        if(!temptlA.isLogin){
            Intent i =new Intent(getActivity(),Activity_login.class);
            startActivityForResult(i, MainActivity.REQUEST_CODE);
        }else{
            //Logout
            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logout();
                }
            });
        }
        if(user==null ){
            Log.e(TAG,"Ko co dang nhap");
            Log.v(TAG,"ID la "+temptlA.IDuser);
        }
        else {
            if(!user.getID().isEmpty()){
                    nameuser.setText(user.getName());
                    countoder();
                checkRole(user.getRole());
                if(temptlA.IDCART!=null){
                    Log.v(TAG,temptlA.IDCART);
                }
            }
        }

        btn3.setOnClickListener(v ->{
            MainActivity main=new MainActivity();
            main.loadFragment(new Fragment_Cart());
        });


        //goto Activity_profile
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), Activity_profileuser.class);
                startActivity(i);
            }
        });



        //goto Activityhistory
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), Activityhistory.class);
                startActivity(i);
            }
        });


        //Go to news
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), news_main.class);
                startActivity(i);
            }
        });
       btn_chage_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), Activity_change_pass.class);
                mview.getContext().startActivity(i);
            }
        });

    }

    public void passData(String data) {
        mViewModel.setuata(data);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK ) {
             if(user==null){
                 temptlA.checkProfileFireBase(data.getStringExtra("uid"));
             }
            Log.v(TAG,data.getStringExtra("uid"));
                 temptlA.checkCartUse(data.getStringExtra("uid"));
                countoder();
                 ControllerOrder.loadOder(temptlA.IDuser);

             update(data.getStringExtra("uid"));
            }

    }

    private void logout(){
        temptlA.setIsLogin(false);temptlA.listcart.clear();
        user=null;
        temptlA.IDuser=null;
        Intent intent = new Intent(getActivity(), Activity_login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(user==null){
            Log.v(TAG,"User da Null");
        }
            Log.d(TAG,"Ban da chon Logout");
            startActivity(intent);
    }
    @Override
    public void onQuantityChanged() {

    }
    private void countoder(){
        if(temptlA.IDuser==null){
            tv4.setText(0);
            tv4.setVisibility(View.INVISIBLE);
            tv5.setText(0);
            tv5.setVisibility(View.INVISIBLE);
        }else{
            if (!temptlA.IDuser.isEmpty()){
                ConnectFirebase.db.collection("order")
                        .whereEqualTo("CustomersID",temptlA.IDuser)
                        .whereEqualTo("orderStatus","PENDING").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.size()==0){
                                    tv4.setVisibility(View.GONE);
                                }else {
                                    tv4.setVisibility(View.VISIBLE);
                                    tv4.setText(String.valueOf(queryDocumentSnapshots.size()));

                                }
                            }
                        });
                ConnectFirebase.db.collection("order")
                        .whereEqualTo("CustomersID",temptlA.IDuser)
                        .whereEqualTo("orderStatus","DELIVERED").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.size()==0){
                                    tv5.setVisibility(View.GONE);
                                }else {
                                tv5.setVisibility(View.VISIBLE);
                                tv5.setText(queryDocumentSnapshots.size());

                                }
                            }
                        });
            }
        }
    }

    @Override
    public void getDataCheckBox(List<cartItem> mlistcart) {

    }
    private void checkRole(String id) {
        //admin
        if (Integer.parseInt(id) == 1) {
            btn3.setVisibility(View.VISIBLE);
            Log.v("Fragment_me", "Admin da dang nhap , " + temptlA.Datetimecurrent + "\n" + user.getID());
        } else {
            btn3.setVisibility(View.GONE);
        }

    }

    private void update(String id){
        ConnectFirebase.db =FirebaseFirestore.getInstance();

        ConnectFirebase.db.collection("customers").document(id).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document!=null && document.exists()){
                                String id,name,address,phone,gender,dateb;
                                name=document.get("fullName",String.class);
                                nameuser.setText(name);
                                int role= Math.toIntExact(document.get("roleid", Long.class));
                                checkRole(String.valueOf(role));
                            }
                        }
                    }
                });
    }


}