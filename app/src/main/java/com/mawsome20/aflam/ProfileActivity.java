package com.mawsome20.aflam;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;


import com.mawsome20.aflam.new_conception.models.User;
import com.mawsome20.aflam.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPass, genderSpinner;
    private Button btnUpdate, deactivateBt;
    private ProgressDialog dialog;
    private String URL = "", strGender;
    private CircleImageView userIv;
    private static final int GALLERY_REQUEST_CODE = 1;
    private Uri imageUri;
    private ProgressBar progressBar;
    private String id;
    private boolean isDark;
    private String selectedGender = "Male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        isDark = sharedPreferences.getBoolean("dark", false);

        if (isDark) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (!isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("الحساب الشخصي");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---analytics-----------
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "profile_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);

        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPass = findViewById(R.id.password);
        btnUpdate = findViewById(R.id.signup);
        userIv = findViewById(R.id.user_iv);
        progressBar = findViewById(R.id.progress_bar);
        deactivateBt = findViewById(R.id.deactive_bt);
        genderSpinner = findViewById(R.id.gender_spinner);
        genderSpinner.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(this,R.drawable.ic_arrow_drop_down_white_24dp),null);

        id = ( new PreferenceUtils(ProfileActivity.this)).getUserId();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equals("")) {
                    Toast.makeText(ProfileActivity.this, "Please enter valid email", Toast.LENGTH_LONG).show();
                    return;
                } else if (etName.getText().toString().equals("")) {
                    Toast.makeText(ProfileActivity.this, "Please enter name", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                String name = etName.getText().toString();

                //updateProfile(id, email, name, pass);

            }
        });

        //gender spinner setup
        final String[] genderArray = new String[2];
        genderArray[0] = "ذكر";
        genderArray[1] = "انثى";
        genderSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("أختر الجنس");
                builder.setSingleChoiceItems(genderArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((TextView) v).setText(genderArray[i]);
                        selectedGender = genderArray[i];
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        getProfile();
    }

    void getProfile(){
        User mUser = (new PreferenceUtils(ProfileActivity.this)).getProfile();
        etName.setText(mUser.getName());
        etEmail.setText(mUser.getEmail());
        Picasso.get().load(R.drawable.ic_account_circle_black).centerCrop().fit().into(userIv);
    }

    @Override
    protected void onStart() {
        super.onStart();

        userIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openGallery();
            }
        });

        deactivateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showDeactivationDialog();
            }
        });
    }

    /*
    private void showDeactivationDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_deactivate, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText passEt = view.findViewById(R.id.pass_et);
        final EditText reasonEt = view.findViewById(R.id.reason_et);
        final Button okBt = view.findViewById(R.id.ok_bt);
        Button cancelBt = view.findViewById(R.id.cancel_bt);
        ImageView closeIv = view.findViewById(R.id.close_iv);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        LinearLayout topLayout = view.findViewById(R.id.top_layout);
        if (isDark) {
            topLayout.setBackgroundColor(getResources().getColor(R.color.overlay_dark_30));
        }

        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passEt.getText().toString();
                String reason = reasonEt.getText().toString();

                if (TextUtils.isEmpty(pass)) {
                    new ToastMsg(ProfileActivity.this).toastIconError("Please enter your password");
                    return;
                } else if (TextUtils.isEmpty(reason)) {
                    new ToastMsg(ProfileActivity.this).toastIconError("Please enter your reason");
                    return;
                }
                deactivateAccount(pass, reason, alertDialog, progressBar);
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }


     */



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
}
