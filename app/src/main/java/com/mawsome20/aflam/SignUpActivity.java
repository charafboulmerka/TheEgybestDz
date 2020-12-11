package com.mawsome20.aflam;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mawsome20.aflam.utils.PreferenceUtils;
import com.mawsome20.aflam.utils.ToastMsg;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName,etEmail,etPass;
    private Button btnSignup;
    private ProgressDialog dialog;
    private View backgorundView;

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
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (!isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("فتح حساب جديد");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---analytics-----------
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "sign_up_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);

        etName=findViewById(R.id.name);
        etEmail=findViewById(R.id.email);
        etPass=findViewById(R.id.password);
        btnSignup=findViewById(R.id.signup);
        backgorundView=findViewById(R.id.background_view);
        if (isDark) {
            backgorundView.setBackgroundColor(getResources().getColor(R.color.nav_head_bg));
            btnSignup.setBackgroundResource(R.drawable.btn_rounded_dark);
        }
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmailAddress(etEmail.getText().toString())){
                    new ToastMsg(SignUpActivity.this).toastIconError("please enter valid email");
                }else if(etPass.getText().toString().equals("")){
                    new ToastMsg(SignUpActivity.this).toastIconError("please enter password");
                }else if (etName.getText().toString().equals("")){
                    new ToastMsg(SignUpActivity.this).toastIconError("please enter name");
                }else {
                    String email = etEmail.getText().toString();
                    String pass = etPass.getText().toString();
                    String name = etName.getText().toString();
                    signUpFC(email, pass, name);
                }
            }
        });
    }

    private void signUpFC(String email, String pass, String name){
        dialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            new ToastMsg(SignUpActivity.this).toastIconSuccess("تم فتح الحساب بنجاح");
                            // save user info to sharedPref
                            saveUserInfo(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, email);
                        }else {
                            new ToastMsg(SignUpActivity.this).toastIconError("حصل خطأ الرجاء تفقد إتصالك بالإنترنت و إعادة المحاولة");
                            dialog.cancel();
                        }
                    }
                });
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

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void saveUserInfo(String uid, String name, String email) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(new com.mawsome20.aflam.new_conception.models.User(uid,name,email))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            new PreferenceUtils(SignUpActivity.this).setProfile(new com.mawsome20.aflam.new_conception.models.User(uid,name,email));
                            Intent it = new Intent(SignUpActivity.this,MainActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(it);
                        }
                        else {
                            Toast.makeText(SignUpActivity.this,"حصل خطأ الرجاء إعادة المحاولة",Toast.LENGTH_LONG).show();
                        }
                    }
                });
        new PreferenceUtils(SignUpActivity.this).setProfile(new com.mawsome20.aflam.new_conception.models.User(uid,name,email));
        finish();
    }



}
