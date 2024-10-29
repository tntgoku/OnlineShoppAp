package com.example.onlineshopp.ActivityLayout;

import static com.example.onlineshopp.Database.ConnectFirebase.db;
import static com.example.onlineshopp.Database.ConnectFirebase.mAuth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshopp.Database.ConnectFirebase;
import com.example.onlineshopp.Database.ConnectSQLite;
import com.example.onlineshopp.FragmentLayout.Fragment_Home;
import com.example.onlineshopp.MainActivity;
import com.example.onlineshopp.R;
import com.example.onlineshopp.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.onlineshopp.temptlA;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Activity_login extends AppCompatActivity {
    String TAG=Activity_login.class.getName();
    ActivityLoginBinding binding;
    TextInputEditText  pwdtxt,usertxt;
    Button btnlogin;
    TextView signUpText,tv1;
    private final  int RC_SIGN_IN=234;
    boolean canLogin=false;
    String i,i1,i2,i3,i4;
    SharedPreferences sharedPreferences ;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        FirebaseApp.initializeApp(this);
        setContentView(binding.getRoot());
        // Cấu hình Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences= getSharedPreferences("MyPreferences", MODE_PRIVATE);
        setMapping();
        ConnectFirebase.setDb();
        eventCompment();

    }

    @SuppressLint("ResourceAsColor")
    private void eventCompment() {
    usertxt.setText(sharedPreferences.getString("t1",null));
    pwdtxt.setText(sharedPreferences.getString("t2",null));
    binding.rememberMeSwitch.setChecked(sharedPreferences.getBoolean("Save",false));
    String pwd=pwdtxt.getText().toString();
    String user=usertxt.getText().toString();
    if(pwd.isEmpty() || user.isEmpty()){
        btnlogin.setEnabled(false);
    }

        binding.rememberMeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Lưu t1 và t2
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("t1", binding.userNameLogin.getEditText().getText().toString());
                editor.putString("t2", binding.passwordLogin.getEditText().getText().toString());
                editor.putBoolean("Save",isChecked);
                editor.apply(); // Lưu dữ liệu

                Toast.makeText(this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(this, "Dữ liệu đã bị xóa", Toast.LENGTH_SHORT).show();
            }
        });
        binding.passwordLogin.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validatePassword(editable.toString());
                enabelbtnLogin(user,editable.toString());
            }
        });


        //BtnLogin
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFirebase(usertxt.getText().toString(),pwdtxt.getText().toString());
            }
        });



        //Listent Click
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Activity_login.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        //Login Google
        binding.googleSignInButton.setOnClickListener(view -> {
            signIn();
        });


        tv1.setOnClickListener(view -> {
            Intent i=new Intent(Activity_login.this, Activity_forget.class);
            startActivity(i);
        });
    }




    private void setMapping(){
        usertxt=binding.userNameLogin1;
        pwdtxt=binding.passwordLogin1;
        btnlogin=binding.loginButton;
        signUpText=binding.signUpText;
        tv1=binding.forgotPasswordText;

    }
    private void validatePassword(String password) {

        // Kiểm tra điều kiện mật khẩu
        if (password.length() < 5 || !temptlA.hasUpperCase(password)) {
            // Hiện thị biểu tượng lỗi
            pwdtxt.setError(getString(R.string.invalid_password));
            canLogin=false;
        } else {
            // Xóa thông báo lỗi
            pwdtxt.setError(null);
            canLogin=true;
        }
    }
    @SuppressLint("ResourceAsColor")
    void enabelbtnLogin(String user,String pwd){
        if (!canLogin && (user.isEmpty() || pwd.isEmpty()) ){
            btnlogin.setEnabled(false);
            btnlogin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrey));
        }else{
            btnlogin.setEnabled(true);
            btnlogin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }


    private void loginFirebase(String user, String pwd) {
        ConnectFirebase.setmAuth();
        mAuth.signInWithEmailAndPassword(user,pwd)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i(TAG, "signInWithEmail:success\n "+user.getUid()+"\n"+user.getTenantId()+"\n"+user.getDisplayName());
                            temptlA.checkProfileFireBase(user.getUid());
                            updateTimeFireBase(user.getUid());
                            Toast.makeText(Activity_login.this,"Đăng nhập thành công !!!!",Toast.LENGTH_SHORT).show();
                            pushdata(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Đăng nhập thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void updateTimeFireBase(String id){
        Map<String,Object> newdata=new HashMap<>();
        newdata.put("updateDate",temptlA.Datetimecurrent);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("accounts").document(id).update(newdata);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                }
            }else{
                Log.e(TAG,"Erroor");
            }
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    private void saveUserInfoToFirestore(@NonNull FirebaseUser user) {
        ConnectFirebase.setDb();
        String userId = user.getUid();
        DocumentReference userRef = db.collection("accounts").document(userId);

        Map<String, Object> userData = new HashMap<>();
        userData.put("fullName", user.getDisplayName());
        userData.put("email", user.getEmail());
        userData.put("photoUrl", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "");

        userRef.set(userData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("LoginActivity", "Dữ liệu người dùng đã được lưu vào Firestore"))
                .addOnFailureListener(e -> Log.w("LoginActivity", "Lỗi khi lưu dữ liệu người dùng vào Firestore", e));
    }
    private void pushdata(String id){
        Intent in1=new Intent();
        temptlA.IDuser=id;
        in1.putExtra("uid",id);
        temptlA.setIsLogin(true);
        setResult(RESULT_OK,in1);
        finish();
    }

}
