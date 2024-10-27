package com.example.onlineshopp.ActivityLayout;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.ActivityprofileuserBinding;
import com.example.onlineshopp.interface1.InterFace;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_profileuser extends AppCompatActivity implements InterFace {
    ActivityprofileuserBinding binding;
    private  final String TAG="Activity_profileuser";
    private  Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityprofileuserBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        setMapping();
        eVentCompoment();
            if(temptlA.IDuser!=null) {

            }else{
                Log.w(TAG, "No user is signed in");
                Toast.makeText(getApplicationContext(), "Vui lòng đăng nhập để xem thông tin cá nhân", Toast.LENGTH_SHORT).show();
            }
    }
    @Override
    public void setMapping() {
    }

    @Override
    public void eVentCompoment() {
            setProfile();
            binding.imgback.setOnClickListener(
                    view -> {finish();});
            binding.btnsaveprofile.setOnClickListener(view -> {
                String name,phone,address,date,gender;
                name=binding.userNameTextView.getText().toString();
                phone=binding.phoneNumberEditText.getText().toString();
                address=binding.addressEditText.getText().toString();
                date=binding.birthdayEditText.getText().toString();
                gender=binding.gender.getText().toString();
                updateProfile(name,phone,address,gender,date);
                finish();
            });


            binding.calendarButton.setOnClickListener(view -> {openDialogDate();});


            binding.profileImageView.setOnClickListener(view -> {
                    String path= Environment.getExternalStorageDirectory()+"/Download";
                        Uri path1=Uri.parse(path);
                        Intent i=new Intent(Intent.ACTION_PICK);
                        i.setDataAndType(path1,"*/*");
                        startActivity(i);
            });
            binding.changeimgprofile.setOnClickListener(view -> {

            });
    }


    private void setProfile(){
        binding.userNameTextView.setText(temptlA.user.getName());
        binding.phoneNumberEditText.setText(temptlA.user.getPhone());
        binding.addressEditText.setText(temptlA.user.getAddress());
        binding.birthdayEditText.setText(temptlA.user.getDate());
        binding.gender.setText(temptlA.user.getGender());
    }


    @Override
    public void onQuantityChanged() {


    }

    @Override
    public void getDataCheckBox(List<cartItem> mlistcart) {

    }




    private void getDatauserFirebase(){
        binding.fullNameEditText.setText(temptlA.user.getName());
        binding.userNameTextView.setText(temptlA.user.getName());
        binding.addressEditText.setText(temptlA.user.getAddress());
        binding.gender.setText(temptlA.user.getGender());
        binding.phoneNumberEditText.setText(temptlA.user.getPhone());
        binding.birthdayEditText.setText(temptlA.user.getDate());
        ConnectFirebase.db =FirebaseFirestore.getInstance();

        ConnectFirebase.db.collection("accounts").whereEqualTo("uid",temptlA.user.getID()).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            binding.emailEditText.setText(task.getResult().getDocuments().get(0).get("email",String.class));
                        }
                    }
                }
        );
    }
    private void updateProfile(String name,String phone,String address,String gender,String datebirth){
        Map<String,Object> newdata=new HashMap<>();

        newdata.put("fullName",name);
        newdata.put("Phone",phone);
        newdata.put("address",address);
        newdata.put("birthday",datebirth);
        newdata.put("gender",gender);
        temptlA.user.setDate(datebirth);
        temptlA.user.setGender(gender);
        temptlA.user.setName(name);
        temptlA.user.setPhone(phone);
        temptlA.user.setAddress(address);

        FirebaseFirestore db=FirebaseFirestore.getInstance();

        DocumentReference document=db.collection("customers").document(temptlA.user.getID());

        document.update(newdata).addOnSuccessListener(aVoid->{
            Log.v(TAG,"Sua thanh cong");
            Toast.makeText(getApplicationContext(),"Cập nhật lại thông thành công!!!!",Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e ->{
            Log.w(TAG,"Lỗi khi cập nhật"+e.getMessage());
            Toast.makeText(getApplicationContext(),"Cập nhật thất bại!!!!",Toast.LENGTH_SHORT).show();
        });

    }

    private void openDialogDate(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String years=String.valueOf(i);
                String months=String.valueOf(i1);
                String dates=String.valueOf(i2);
                binding.birthdayEditText.setText(i2+"/"+i1+"/"+i);
            }
        },year,month,day);

        datePicker.show();
    }

    private void openCamera(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA},temptlA.REQUEST_CAMERA);
        }else{
            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,temptlA.REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Xử lý mở camera
            if (requestCode == temptlA.REQUEST_CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                binding.profileImageView.setImageBitmap(bitmap);
            }
        }
    }

}