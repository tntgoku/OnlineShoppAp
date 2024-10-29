package com.example.onlineshopp.ActivityLayout;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.interface1.InterFace;
import com.example.onlineshopp.R;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements InterFace {
    private static final String TAG = "SignupActivity";

    private TextInputLayout  userNameInputLayout,emailInputLayout, mobileNumberInputLayout, passwordInputLayout,
            confirmPasswordInputLayout;

    private TextView loginText;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setMapping();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        eVentCompoment();
    }

    @Override
    public void setMapping() {
        loginText=findViewById(R.id.loginText);
        signupButton=findViewById(R.id.signupButton);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        mobileNumberInputLayout = findViewById(R.id.mobileNumberInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout=findViewById(R.id.confirmPasswordInputLayout);
        signupButton = findViewById(R.id.signupButton);
        loginText = findViewById(R.id.loginText);
        userNameInputLayout=findViewById(R.id.nameInputLayout);
    }

    @Override
    public void eVentCompoment() {
        //Btn Register
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        //Btn Login

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        passwordInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkpass(editable.toString());
                Log.v("Ky tu nhap ao",passwordInputLayout.getEditText().getText().toString()+"\n"+confirmPasswordInputLayout.getEditText().getText().toString());
            }
        });
        confirmPasswordInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkpass(editable.toString());
            }
        });
    }

    private void checkpass(String password){
        if (password.length()<5 || !temptlA.hasUpperCase(password)){
            passwordInputLayout.setError(getString(R.string.invalid_password));
        }else{
            passwordInputLayout.setError(null);
        }
    }

    @Override
    public void onQuantityChanged() {

    }

    @Override
    public void getDataCheckBox(List<cartItem> mlistcart) {

    }


    private void registerUser() {
        String email = emailInputLayout.getEditText().getText().toString().trim();
        String mobileNumber = mobileNumberInputLayout.getEditText().getText().toString().trim();
        String password = passwordInputLayout.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordInputLayout.getEditText().getText().toString().trim();
        String user= userNameInputLayout.getEditText().getText().toString();
        if (email.isEmpty() || mobileNumber.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }else{
            Log.v("RegisterActivity", "Email này đã tồn tại!!.");
            checkEmail(email,password,mobileNumber,user);
        }

    }

    private  void checkEmail(String email, String pass, String moblieNumber, String user){

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"createUserWithEmail:success");
                    FirebaseUser user1=mAuth.getCurrentUser();
                    Log.i("RegisterActivity_Customer","ID: "+user1.getUid()+"\nName_cus: "+user+"\nPhone: "+moblieNumber+"\nEmail: "+email+"\nTime: "+temptlA.Datetimecurrent);
                    saveUsertoFireBase(user1,user,moblieNumber,email,pass);
                    Toast.makeText(getApplicationContext(),"Đăng ký thành công, Vui lòng đăng nhập lại!!!",Toast.LENGTH_LONG).show();
                }else{
                    Log.e(TAG,"createUserWithEmail:failure");
                    Toast.makeText(getApplicationContext(), "Đăng ký thất bại: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void saveUsertoFireBase(FirebaseUser user1,String user,String moblieNumber,String email,String pass){
        Map<String,Object> newData=new HashMap<>();
        Map<String,Object> newData1=new HashMap<>();
        newData.put("uid",user1.getUid());
        newData.put("fullName",user);
        newData.put("Phone",moblieNumber);
        newData.put("email",email);
        newData.put("updatedAt", temptlA.Datetimecurrent);
        newData.put("roleid", 2);

        //Account
        newData1.put("uid",user1.getUid());
        newData1.put("email",email);
        newData1.put("password",pass);
        newData1.put("roleid",2);
        newData1.put("UpdateAt",temptlA.Datetimecurrent);
        DocumentReference userRef =db.collection("customers").document(user1.getUid());
        DocumentReference accRef =db.collection("accounts").document(user1.getUid());
        db.runTransaction(transaction -> {
            transaction.set(accRef, newData1);
            transaction.set(userRef, newData);
            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error writing user data", e);
            String errorMessage = "Lỗi khi lưu thông tin tài khoản: ";
            if (e instanceof FirebaseFirestoreException) {
                FirebaseFirestoreException ffe = (FirebaseFirestoreException) e;
                errorMessage += ffe.getCode().toString();
            } else {
                errorMessage += e.getMessage();
            }
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            // Xóa tài khoản khỏi Authentication nếu lưu Firestore thất bại
            user1.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User account deleted from Authentication.");
                } else {
                    Log.w(TAG, "Failed to delete user account from Authentication.", task.getException());
                }
            });
        });
    }





}