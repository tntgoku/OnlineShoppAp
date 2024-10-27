package com.example.onlineshopp.ActivityLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.MainActivity;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.databinding.ActivityPaymentBinding;
import com.example.onlineshopp.databinding.LayoutnotificationBinding;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_notifaction extends AppCompatActivity {
    LayoutnotificationBinding binding;
    private final String TAG="Activity_payment";

    private ArrayList<cartItem> receivedList = new ArrayList<>();
    private  String name,address,phone;
    private  int selectionPosition =0,freeship=30000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutnotificationBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            for (int i = 0; i < intent.getIntExtra("size", 0); i++) {
                receivedList.add((cartItem) intent.getSerializableExtra("Items" + String.valueOf(i)));
            }
            Log.d(TAG, "Kich thuoc cua list nay:"+String.valueOf(intent.getIntExtra("size", 0)));
            name=intent.getStringExtra("name");
            phone=intent.getStringExtra("phone");
            address=intent.getStringExtra("address");
            selectionPosition=intent.getIntExtra("payment",0);
            freeship=intent.getIntExtra("freeship",30000);
        }else{
            Log.w(TAG,"nuull");
        }


        binding.gomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Activity_notifaction.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        createOder(temptlA.user.getID(),name,address,phone,selectionPosition);
        updateProducts();
        removeItemincart();
    }


    private void createOder(String iduser,String name,String address,String phone,int methodpayment){
        ConnectFirebase.setDb();
        String idoder;
        Map<String,Object> newData=new HashMap<>();
        newData.put("CustomersID",iduser);
        newData.put("Address",address);
        newData.put("Nameuser",name);
        newData.put("orderStatus","PENDING");
        String payment="COD";
        if(methodpayment ==0){
            payment="COD";
        }else if(methodpayment==1) {
            payment="Zalopay";
        }
        else if(methodpayment==2) {
            payment="MoMo";
        }else{
            payment="COD";
        }
        newData.put("Payment",payment);
        newData.put("Phone",phone);
        newData.put("Timer", temptlA.Datetimecurrent);
        newData.put("Amount: ", temptlA.getAmountItem(receivedList));
        newData.put("Freeship: ", freeship);
        newData.put("Total", temptlA.getTotalBillcartitem(receivedList)+freeship);
        List<Map<String,Object>> listoder=new ArrayList<>();
        for(cartItem items: receivedList){
            Map<String,Object> neworder=new HashMap<>();
            neworder.put("ID",items.getItemID());
            neworder.put("Quantity",items.getQuantity());
            neworder.put("Name",items.getItem().getTitle());
            neworder.put("Total",items.getItem().getPrice()*items.getQuantity());
            listoder.add(neworder);
        }
        newData.put("ItemsOrder",listoder);
       DocumentReference document= ConnectFirebase.db.collection("order").document();
             idoder=document.getId();
             newData.put("Idorder",document.getId());
            document.set(newData).addOnSuccessListener(aVoid->{
                Log.i(TAG,"Tạo thành công order: "+document.getId()+"\nTimer: "+temptlA.Datetimecurrent);
            });


    }
    private void removeItemincart(){
        ConnectFirebase.setDb();
        ConnectFirebase.db.collection("cart").document(temptlA.IDCART).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   List<Map<String, Object>> items = (List<Map<String, Object>>) task.getResult().get("items");
                                                   Log.v("Sau khi xoa ","trc khi xoa"+items.size());
                                                   for(int i=0;i<receivedList.size();i++){
                                                           removeItemFromList(items,receivedList.get(i).getItemID());
                                                   }
                                               }
                                           }
                                       });
        //Remove in cart
    }
    private void removeItemFromList(List<Map<String, Object>> items, String itemID){
        for(int i=0;i< items.size();i++){
            if (items.get(i).get("ID").equals(itemID)){
                items.remove(i);
            };
        }
        ConnectFirebase.db=FirebaseFirestore.getInstance();

            ConnectFirebase.db.collection("cart").document(temptlA.IDCART)
                    .update("items", items)
                    .addOnSuccessListener(aVoid -> {
                        Log.i("Notification", "Xóa thành công, số lượng items còn lại: " + items.size());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Error", "Error updating items: " + e.getMessage());
                    });
            for(cartItem itemremove:receivedList){
                    for(int i=0;i<temptlA.listcart.size();i++){
                        if (itemremove.getItemID().equals(temptlA.listcart.get(i).getItemID())){
                            temptlA.listcart.remove(i);
                        }
            }
        }
    }
    private void updateProducts(){
       ConnectFirebase.db= FirebaseFirestore.getInstance();
       for(cartItem items: receivedList){
           ConnectFirebase.db.collection("dishes").whereEqualTo("dishId",items.getItemID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
               @Override
               public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                  DocumentSnapshot snapshot= queryDocumentSnapshots.getDocuments().get(0);
                      int sell= Math.toIntExact(snapshot.get("sell", Long.class));
                   ConnectFirebase.db.collection("dishes").document(snapshot.getId())
                           .update("sell",sell+items.getQuantity()).addOnSuccessListener( aVoid->{
                       Log.v(TAG,"Cap nhat lai so luong ban hang");
                   }).addOnFailureListener(e -> {
                       Log.w(TAG+" 149","Ko cap nhat dc");
                           });
               }
           });

        }

    }
}
