package com.mawsome20.aflam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mawsome20.aflam.utils.PreferenceUtils;
import com.mawsome20.aflam.utils.ToastMsg;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail,etPass;
    private TextView tvSignUp,tvReset;
    private Button btnLogin;
    private ProgressDialog dialog;
    private View backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("dark", false);

        if (isDark) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (!isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("تسجيل الدخول");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---analytics-----------
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "login_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);

        etEmail=findViewById(R.id.email);
        etPass=findViewById(R.id.password);
        tvSignUp=findViewById(R.id.signup);
        btnLogin=findViewById(R.id.signin);
        tvReset=findViewById(R.id.reset_pass);
        backgroundView=findViewById(R.id.background_view);
        if (isDark) {
            backgroundView.setBackgroundColor(getResources().getColor(R.color.nav_head_bg));
            btnLogin.setBackgroundResource(R.drawable.btn_rounded_dark);
        }

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,PassResetActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValidEmailAddress(etEmail.getText().toString())){
                    new ToastMsg(LoginActivity.this).toastIconError("Please enter valid email");
                }
                else if(etPass.getText().toString().equals("")){
                    new ToastMsg(LoginActivity.this).toastIconError("Please enter password");
                }else {
                    String email = etEmail.getText().toString();
                    String pass = etPass.getText().toString();
                    loginFC(email,pass);
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }

        });

    }


    private void loginFC(String email,final String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    new ToastMsg(LoginActivity.this).toastIconSuccess("تم تسجيل الدخول بنجاح");
                    getUserFC(FirebaseAuth.getInstance().getCurrentUser().getUid());

                }else {
                    new ToastMsg(LoginActivity.this).toastIconError("حصل خطأ اثناء محاولة تسجيل الدخول");
                    dialog.dismiss();
                }
            }
        });
    }

    private void getUserFC(String uid){
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                            if (dataSnapshot.exists()){
                                com.mawsome20.aflam.new_conception.models.User mUser =
                                        dataSnapshot.getValue(com.mawsome20.aflam.new_conception.models.User.class);
                                new PreferenceUtils(LoginActivity.this).setProfile(mUser);
                                finish();
                            }else {
                                saveUserInfo(uid,etEmail.getText().toString().split("@")[0],etEmail.getText().toString());
                            }
                        }catch (Exception e){

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void saveUserInfo(String uid, String name, String email) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(new com.mawsome20.aflam.new_conception.models.User(uid,name,email))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            new PreferenceUtils(LoginActivity.this).setProfile(new com.mawsome20.aflam.new_conception.models.User(uid,name,email));
                            Intent it = new Intent(LoginActivity.this,MainActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(it);
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"حصل خطأ الرجاء إعادة المحاولة",Toast.LENGTH_LONG).show();
                        }
                    }
                });
        new PreferenceUtils(LoginActivity.this).setProfile(new com.mawsome20.aflam.new_conception.models.User(uid,name,email));
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }



}
