package com.example.onlineshopp.ActivityLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.ActivityForgetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_forget extends AppCompatActivity {
        ActivityForgetBinding binding;
        FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityForgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String email= binding.userNameLogin.getEditText().getText().toString();

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.v("TAG","WD'awd'awd");
                            Toast.makeText(Activity_forget.this,"Tin nhan da dc gui den Email cua ban, Vui long kiem tra Email ma chung toi da cung cap",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Log.e("TAG","Say ra Loi~ r");
                        }
                    }
                });
            }
        });
    }
}