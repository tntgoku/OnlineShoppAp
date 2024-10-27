package com.example.onlineshopp.ActivityLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.ActivityprofileuserBinding;
import com.example.onlineshopp.temptlA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_change_pass extends AppCompatActivity {
    String confirmNewPass, newPass, oldPass;

    ActivityprofileuserBinding binding;

    private EditText edtOldPass, edtNewPass, edtConfirmNewPass;
    private Button btnSavePass;
    private  final String TAG="Activity_change_pass";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_pass);
        if(temptlA.IDuser!=null) {
        }else{
            Log.w(TAG, "No user is signed in");
            Toast.makeText(getApplicationContext(), "Vui lòng đăng nhập để xem thông tin cá nhân", Toast.LENGTH_SHORT).show();
        }
        setmaping();

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPass = "check";
                newPass = edtNewPass.getText().toString().trim();
                confirmNewPass = edtConfirmNewPass.getText().toString().trim();
                oldPass = edtOldPass.getText().toString().trim();
                checkEnteredPassword(oldPass);

            }
        });


    }
    public void setmaping(){
        edtOldPass = findViewById(R.id.edtold_pass);
        edtNewPass = findViewById(R.id.edtnew_pass);
        edtConfirmNewPass = findViewById(R.id.edtconfirm_new_pass);
        btnSavePass = findViewById(R.id.btnsave_pass);
         oldPass = edtOldPass.getText().toString().trim();
         newPass = edtNewPass.getText().toString().trim();
         confirmNewPass = edtConfirmNewPass.getText().toString().trim();
    }
    public void checkEnteredPassword(String enteredPassword) {
        if (user != null && user.getEmail() != null && !enteredPassword.isEmpty()) {
            String email = user.getEmail();

            AuthCredential credential = EmailAuthProvider.getCredential(email, enteredPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //mk cũ đúng
                                if (newPass.equals(confirmNewPass)) {
                                    user.updatePassword(newPass)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User password updated.");
                                                    }
                                                }
                                            });

                                    Toast.makeText(Activity_change_pass.this, "Mật khẩu đã được cập nhật!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    //mk moi ko trung nhau
                                    Toast.makeText(Activity_change_pass.this, "Mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //sai mk cu
                                Log.d("PasswordCheck", "Mật khẩu nhập vào không đúng.");
                                Toast.makeText(getApplicationContext(), "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Log.d("PasswordCheck", "Không có người dùng đăng nhập.");
            Toast.makeText(getApplicationContext(), "Không có người dùng đăng nhập!", Toast.LENGTH_SHORT).show();
        }
    }
}