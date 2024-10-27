package com.example.onlineshopp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.Object.ItemFood;
import com.example.onlineshopp.Object.User;
import com.example.onlineshopp.Object.cartItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class temptlA {
    public static boolean isLogin= false;
    public static String IDuser =null;
    public static String IDCART =null;
    static LocalDate date=LocalDate.now();
    static LocalTime date1 = LocalTime.now();
    public static final int REQUEST_CART = 203;
    public static String Datetimecurrent=String.valueOf(date)+" Timer: "+String.valueOf(date1);
    public static boolean isIsLogin() {
        return isLogin;
    }
    public static String Datetimecurrently(){
        Calendar calendar = Calendar.getInstance();
        // Lấy thời gian hiện tại
        Date currentDate = calendar.getTime();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR_OF_DAY, 7);

        // Định dạng thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    public static void setIsLogin(boolean isLogin) {
        temptlA.isLogin = isLogin;
    }

    public static List<cartItem> listcart=new ArrayList<>();


    public static boolean hasUpperCase(String str) {
        // Kiểm tra xem chuỗi có ký tự in hoa không
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }
    public static boolean addProduct(cartItem cartfake){
        for(int i=0;i<listcart.size();i++){
            if(listcart.get(i).getItemID().equals(String.valueOf(cartfake.getItemID())) ){
                int currentQuantity = listcart.get(i).getQuantity();
                int newQuantity = currentQuantity + cartfake.getQuantity();
                listcart.get(i).setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }
    public static User user;
    public static FirebaseFirestore db;

    @SuppressLint("SuspiciousIndentation")
    public static void checkProfileFireBase(String id){
     db =FirebaseFirestore.getInstance();

        db.collection("customers").document(id).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document!=null && document.exists()){
                                String id,name,address,phone,gender,dateb;
                                int role=2;
                                id=document.getId();
                                temptlA.IDuser=id;
                                name=document.get("fullName",String.class);
                                phone=document.get("Phone",String.class);
                                address = document.contains("address") ? document.getString("address") : null;
                                gender = document.contains("gender") ? document.getString("gender") : "Nam";
                                dateb = document.contains("birthday") ? document.getString("birthday") : null;
                                role = document.contains("roleid") ? Math.toIntExact(document.getLong("roleid")) : role;
                                String img1 = document.contains("URLimg") ? document.getString("URLimg") : null;
                                user=new User(id,name,gender,dateb,address,phone,role,img1);

                            }
                        }
                    }
                });
    }


    public static void checkCartUse(String id){
        db=FirebaseFirestore.getInstance();

        db.collection("cart").whereEqualTo("customerId",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               if(!queryDocumentSnapshots.isEmpty()){
                DocumentSnapshot documentSnapshot =queryDocumentSnapshots.getDocuments().get(0);
                List<Map<String, Object>> items = (List<Map<String, Object>>) documentSnapshot.get("items");
                //Kiểm tra xem items có khác rỗng không và có khác null
                if (items != null && !items.isEmpty()) {
                    //Tạo vòng lặp để lấy item
                    for (Map<String, Object> item : items) {
                        String id = item.get("ID").toString();
                        Long quantityLong = (Long) item.get("Quantity");
                        Integer quantity = quantityLong != null ? quantityLong.intValue() : 0;
                        db.collection("dishes").whereEqualTo("dishId",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                DocumentSnapshot document=task.getResult().getDocuments().get(0);
                                String id= document.get("dishId",String.class);
                                String name = document.get("dishName",String.class);
                                String desc = document.get("description",String.class);
                                String imgurl= document.get("imageUrl",String.class);
                                int idcate,price,status,sell;
                                idcate= Math.toIntExact(document.get("dishCategoryId", Long.class));
                                price= Math.toIntExact(document.get("price",Long.class));
                                status= Math.toIntExact(document.get("status", Long.class));
                                sell=Math.toIntExact(document.get("sell",Long.class));
                                Log.v("TAG",id+"\n"+sell+"\n"+name+"\n");
                                ItemFood item=new ItemFood(id,name,desc,price,idcate,imgurl,status,sell);
                                listcart.add(new cartItem(id,item,quantity));
                            }
                        });
                        // Log or use the retrieved values
                        Log.d("Item Info", "ID: " + id + ", Quantity: " + quantity);
                    }
                    Log.d("TAG","ID cart la: " +documentSnapshot.getId());
                    temptlA.IDCART=documentSnapshot.getId();
                } else {
                    Log.w("Firestore", "Không có item nào trong mảng");
                }
               }else{
                   Log.w("Firestore Line 144", "Không Ko có items");
               }
            }
        }).addOnFailureListener(e -> {
            Log.e("Firebase temptlA Line 109",e.getMessage());
        });
    }

    private static void getitem(String id){
        db=FirebaseFirestore.getInstance();

    }
    public static String getTotalBillCart(){
        String result="0";
        if(listcart.isEmpty()){
            return result;
        }
        int sum=0;
        for(int i=0;i<listcart.size();i++){
            sum=sum+(listcart.get(i).getQuantity()*listcart.get(i).getItem().getPrice());
        }
        result= String.valueOf(sum);
        return "Tổng cộng: "+result+ " đ";
    }
    public  static int getAmountItem(List<cartItem> mlist){
        if(mlist==null||mlist.isEmpty()){
            return -1;
        }else{
            int total=0;
            for(cartItem item:mlist){
                total+=item.getQuantity();
            }
            return total;
        }
    }
    public static float getTotalBillcartitem(List<cartItem> mlist){
        if(mlist==null||mlist.isEmpty()){
            return -1;
        }
        int total=0;
        for(cartItem item:mlist){
            total+=(item.getItem().getPrice()*item.getQuantity());
        }
        return total;
    }
    public static final  int REQUEST_GOHOME=198;
    public static final  int REQUEST_BUYP=197;
    public static final  int REQUEST_CAMERA=200;



}
