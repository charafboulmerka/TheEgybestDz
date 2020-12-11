package com.mawsome20.aflam;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import com.mawsome20.aflam.utils.ToastMsg;

public class PassResetActivity extends AppCompatActivity {
    private EditText etEmail;
    private Button btnReset;
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
        setContentView(R.layout.activity_pass_reset);
        Toolbar toolbar = findViewById(R.id.toolbar);
        backgroundView = findViewById(R.id.background_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("إسترجاع كلمة المرور");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---analytics-----------
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "pass_reset_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        etEmail=findViewById(R.id.email);
        btnReset=findViewById(R.id.reset_pass);

        if (isDark) {
            backgroundView.setBackgroundColor(getResources().getColor(R.color.nav_head_bg));
            btnReset.setBackgroundResource(R.drawable.btn_rounded_dark);
        }

        dialog = new ProgressDialog(this);
        dialog.setMessage("الرجاء الإنتظار");
        dialog.setCancelable(false);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmailAddress(etEmail.getText().toString())){
                    new ToastMsg(PassResetActivity.this).toastIconError("الرجاء إدخال بريد إلكتروني صالح");
                    return;
                }else {
                    dialog.show();
                    FirebaseAuth.getInstance().sendPasswordResetEmail(etEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dialog.cancel();
                                    if (task.isSuccessful()){
                                    new ToastMsg(PassResetActivity.this).toastIconSuccess("تم إرسال رابط تغير كلمة المرور الى بريدك الإلكتروني بنجاح");
                                    }else {
                                        new ToastMsg(PassResetActivity.this).toastIconError("حصل خطأ الرجاء إعادة المحاولة");
                                    }
                                }
                            });
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


}
