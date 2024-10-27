package com.example.onlineshopp.ActivityLayout;

import static com.example.onlineshopp.Database.ConnectFirebase.db;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.onlineshopp.Adapter.foodAdapter;
import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.FragmentLayout.FragmentHomeViewModel;
import com.example.onlineshopp.FragmentLayout.Fragment_Home;
import com.example.onlineshopp.MainActivityModel;
import com.example.onlineshopp.R;
import com.example.onlineshopp.interface1.InterFace;
import com.example.onlineshopp.MainActivity;
import com.example.onlineshopp.Object.ItemFood;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.databinding.ItembuysBinding;
import com.example.onlineshopp.databinding.ItemfoodDeltaiBinding;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityDeltai_Item extends AppCompatActivity  implements InterFace {
    ItemfoodDeltaiBinding binding;
    ItemFood item;
    String TAG =ActivityDeltai_Item.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ItemfoodDeltaiBinding.inflate(getLayoutInflater());
        Intent i=getIntent();
        item=(ItemFood)i.getSerializableExtra("items");
        setContentView(binding.getRoot());
        setBottomMargin(49);
        eVentCompoment();
    }

    private void setBottomMargin(int dp) {
        // Lấy root view từ binding
        View mainLayout = binding.getRoot();

        // Chuyển đổi dp sang pixel
        float scale = getResources().getDisplayMetrics().density;
        int marginInPx = (int) (dp * scale + 0.5f);

        // Lấy các thông số layout và thiết lập margin bottom
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mainLayout.getLayoutParams();
        params.bottomMargin = marginInPx;
        mainLayout.setLayoutParams(params);
    }
    @Override
    public void setMapping() {

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void eVentCompoment() {

        binding.imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.textView4.setText(slProduct());

        binding.imagegotocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(ActivityDeltai_Item.this, MainActivity.class);
                startActivity(i);
            }
        });


        Glide.with(this).load(item.getPicURL()).into(binding.imageViewitems);
        binding.txtTitleitem.setText(item.getTitle());
        binding.detailitemsell.setText("SL ban ra: "+String.valueOf(item.getSell()));
        binding.txtratingitem.setText("5.5");
        binding.txtCostitem.setTextColor(getResources().getColor(R.color.red_price,null));
        binding.txtCostitem.setText(String.valueOf(item.getPrice())+"đ");
        binding.textdescriptionitem.setText(item.getDesc());


        binding.btnbuyitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!temptlA.isIsLogin()){
                    Toast.makeText(ActivityDeltai_Item.this,"Vui lòng đăng nhập để thực hiện các bước tiếp theo",Toast.LENGTH_LONG);
                    Log.v(TAG,"BTNBuyItem \n"+"Người dùng cố truy cập vao mua hàng");
                }else{
                    OpenDialogSelectItem();
                    Log.v(TAG,"User đang thực hiện mua hàng");
                }
            }
        });



        binding.itemorther.setLayoutManager(new GridLayoutManager(this,2,LinearLayoutManager.VERTICAL,
                false));
        foodAdapter adapter =new foodAdapter(MainActivityModel.mlistFood);
        binding.itemorther.setAdapter(adapter);


        binding.imageaddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!temptlA.isIsLogin()){
                    Toast.makeText(ActivityDeltai_Item.this,"Vui lòng đăng nhập để thực hiện các bước tiếp theo",Toast.LENGTH_LONG);
                    Log.v(TAG,"ImageaddtoCart \n"+"Người dùng cố truy cập vao mua hàng");
                }else {
                    addToCart();
                    binding.textView4.setText(slProduct());
                }
            }
        });

    }

    @Override
    public void onQuantityChanged() {

    }

    @Override
    public void getDataCheckBox(List<cartItem> mlistcart) {

    }
    //    public boolean checkProfile(String id){
//        ConnectSQLite connect=new ConnectSQLite(getApplicationContext());
//        SQLiteDatabase db=connect.getWritableDatabase();
//        Cursor cursor=db.rawQuery("SELECT * FROM "+ConnectSQLite.TABLE_6+" WHERE ID_Customer =?",new String[]{id});
//    }
    //dem so lg san pham
    public static  String slProduct(){
        int total=0;
        if(temptlA.listcart.size()>0){
            for( int i=0;i<temptlA.listcart.size();i++){
                cartItem item= temptlA.listcart.get(i);
                total=total+item.getQuantity();
            }
        }
        return String.valueOf(total);
    }

    private void OpenDialogSelectItem(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        // Sử dụng ViewBinding để lấy `ItembuysBinding`
        ItembuysBinding binding1 = ItembuysBinding.inflate(inflater);
        View mview = binding1.getRoot();
        final int[] i = {Integer.parseInt(binding1.editTextText.getText().toString())};
        binding1.textView.setText(item.getTitle());
        binding1.textView3.setText(String.valueOf(item.getPrice()));
        binding1.buttonminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i[0] >1 && i[0]<999){
                    i[0] = i[0] -1;
                    binding1.editTextText.setText(String.valueOf(i[0]));
                }
            }
        });

        binding1.buttonplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i[0]<=999){
                    i[0]=i[0]+1;
                    binding1.editTextText.setText(String.valueOf(i[0]));
                }
            }
        });
        builder.setView(mview);
// Tạo AlertDialog từ builder
        AlertDialog dialog = builder.create();

        binding1.imageButtonexits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        binding.btnbuyitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đi đến thanh toán với Product này...
                Intent i=new Intent(ActivityDeltai_Item.this,Activity_payment.class);
                cartItem items=new cartItem( String.valueOf(item.getID()),item
                        ,Integer.parseInt(binding1.editTextText.getText().toString()));
                i.putExtra("Item0",items);
                i.putExtra("sizecart","1");
                builder.getContext().startActivity(i);
            }
        });

        // Hiển thị dialog
        dialog.show();
    }



    private void addToCart(){

        if(!temptlA.IDuser.isEmpty()) {
            cartItem itemFake = new cartItem(String.valueOf(item.getID()), item, 1);
            if (temptlA.addProduct(itemFake)) {
                Log.v(TAG, "Cap nhat lai gio hang thanh cong");
            } else {
                temptlA.listcart.add(itemFake);
                Log.v(TAG, "Them sp moi vao gio hang voi id:" + String.valueOf(itemFake.getItem().getID()));
            }

            for (int i = 0; i < temptlA.listcart.size(); i++) {
                Log.v(TAG, "addCart:" + temptlA.listcart.get(i).getQuantity());
            }
            checkadd(itemFake,temptlA.IDuser);
        }

    }

    private void checkadd(cartItem itemFake, String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo truy vấn để kiểm tra xem tài liệu có tồn tại không
        Query db123 = db.collection("cart").whereEqualTo("customerId", temptlA.IDuser);
        db123.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && !task.getResult().isEmpty()) {
                        // Nếu tài liệu tồn tại, lấy IDcart
                        String existingId = task.getResult().getDocuments().get(0).getId();
                        temptlA.IDCART=existingId;
                        Log.d(TAG, "day la id Cart\n" + existingId);
                        updateCart(existingId,itemFake);
                        return;
                    } else {
                        creatednewCart(itemFake);
                    }
                } else {
                    Log.w("Firestore", "Error getting documents.", task.getException());
                }
            }
        });
    }
    private void creatednewCart(cartItem itemfake){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docnew=db.collection("cart").document();
        //Item cart
        Map<String, Object> item=new HashMap<>();
        item.put("ID",itemfake.getItemID());
        item.put("Quantity",itemfake.getQuantity());
        item.put("Name",itemfake.getItem().getTitle());

        List<Map<String, Object>> items=new ArrayList<>();
        items.add(item);
        //create  Field
        Map<String, Object> maps = new HashMap<>();

        maps.put("cartId", docnew.getId());
        maps.put("customerId", temptlA.IDuser);
        maps.put("items", items);
        maps.put("createdAt", temptlA.Datetimecurrent);
        maps.put("updatedAt", temptlA.Datetimecurrently());
        docnew.set(maps).addOnSuccessListener(aVoid -> {
            Log.v(TAG, "Them oi 1 document \n ID: " + docnew.getId());
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error: " + e.getMessage());
        });
    }

    private void updateCart(String cartId,cartItem itemFake){
        db.collection("cart").document(cartId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    //Field Items
                    List<Map<String, Object>> items;
                    if(documentSnapshot.contains("items")) {
                        items = (List<Map<String, Object>>) documentSnapshot.get("items");
                        boolean found = false;

                        for (Map<String, Object> item : items) {
                            // Item int Items have ID= ItemFake  found==true
                            if (item.get("ID").equals(itemFake.getItemID())) {
                                int existingQuantity = ((Long) item.get("Quantity")).intValue();
                                item.put("Quantity", existingQuantity + itemFake.getQuantity());
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            Map<String, Object> newItem = new HashMap<>();
                            newItem.put("ID", itemFake.getItemID());
                            newItem.put("Quantity", itemFake.getQuantity());
                            items.add(newItem);
                        }
                    }else{
                        items= new ArrayList<>();
                        Map<String, Object> newItem = new HashMap<>();
                        newItem.put("ID", itemFake.getItemID());
                        newItem.put("Quantity", itemFake.getQuantity());
                        items.add(newItem);
                    }

                    //Update time when User select
                    db.collection("cart").document(cartId)
                            .update("items", items)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Đã cập nhật giỏ hàng", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Lỗi khi cập nhật giỏ hàng: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Lỗi khi đọc giỏ hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
