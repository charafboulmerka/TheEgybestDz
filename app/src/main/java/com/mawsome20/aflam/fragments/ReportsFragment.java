package com.mawsome20.aflam.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inmobi.ads.InMobiBanner;
import com.mawsome20.aflam.MainActivity;
import com.mawsome20.aflam.R;
import com.mawsome20.aflam.models.reports.issue;
import com.mawsome20.aflam.models.reports.reportMovie;
import com.mawsome20.aflam.models.reports.requestAddMovie;
import com.mawsome20.aflam.models.reports.sugesstion;
import com.mawsome20.aflam.network.model.AdsConfig;
import com.mawsome20.aflam.utils.Constants;
import com.mawsome20.aflam.utils.ToastMsg;
import com.mawsome20.aflam.utils.Tools;
import com.mawsome20.aflam.utils.ads.BannerAds;
import com.mawsome20.aflam.utils.ads.PopUpAds;
import com.noqoush.adfalcon.android.sdk.ADFView;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.VideoListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;


 */
public class ReportsFragment extends Fragment {

    private MainActivity activity;

    private LinearLayout searchRootLayout;
    private CardView searchBar;
    private ImageView menuIv, searchIv;
    private TextView pageTitle;

    private CardView cardIssue;
    private CardView cardSuggestione;
    private CardView cardReportMovie;
    private CardView cardRequestAdd;

    private TextView issueDescription,suggestionDescription,reportMovieName,reportMovieDescription,requestAddTitle;
    private Button btnIssue,btnSuggestion,btnReportMovie,btnRequestAdd;
    private Spinner SpinnerReportType;
    private LinearLayout SpinnerLayout;



    private ProgressBar pr_bar;



    private RelativeLayout adView;
    private DatabaseReference mRef;
    private StartAppAd startAppAd;
    private LinearLayout startAppLayoutBanner;
    private Banner startAppBanner;
    private InMobiBanner bannerAd;
    private ADFView AD_FALCON;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();

        return inflater.inflate(R.layout.fragment_reports,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
            FirebaseApp.initializeApp(getActivity());
            mRef = FirebaseDatabase.getInstance().getReference();
        }catch (Exception e){ }
         //bannerAd = (InMobiBanner)view.findViewById(R.id.banner);

        searchRootLayout=view.findViewById(R.id.search_root_layout);
        searchBar           = view.findViewById(R.id.search_bar);
        menuIv              = view.findViewById(R.id.bt_menu);
        pageTitle           = view.findViewById(R.id.page_title_tv);
        searchIv            = view.findViewById(R.id.search_iv);
        adView=view.findViewById(R.id.adView);
        startAppBanner=view.findViewById(R.id.startAppBanner);
        startAppLayoutBanner=view.findViewById(R.id.startapp_linearlayout);
        AD_FALCON = view.findViewById(R.id.adFalconView);
        startAppAd = new StartAppAd(activity);
        pr_bar=view.findViewById(R.id.report_progress_bar);
        SpinnerLayout=view.findViewById(R.id.linear_layout_spinner);

        SpinnerReportType=view.findViewById(R.id.report_spinner);

        cardIssue=view.findViewById(R.id.card_report_issue);
        cardSuggestione=view.findViewById(R.id.card_report_suggestion);
        cardReportMovie=view.findViewById(R.id.card_report_movie);
        cardRequestAdd=view.findViewById(R.id.card_request_add);

        issueDescription=view.findViewById(R.id.text_report_issue);
        suggestionDescription=view.findViewById(R.id.text_suggestion);

        reportMovieName=view.findViewById(R.id.text_report_movie_name);
        reportMovieDescription=view.findViewById(R.id.text_report_movie_description);
        requestAddTitle=view.findViewById(R.id.text_request_add_movie_name);

        btnIssue=view.findViewById(R.id.btn_report_issue);
        btnSuggestion=view.findViewById(R.id.btn_sugesstion);
        btnReportMovie=view.findViewById(R.id.btn_report_movie);
        btnRequestAdd=view.findViewById(R.id.btn_request_add);

        if (activity.isDark) {
            pageTitle.setTextColor(activity.getResources().getColor(R.color.white));
            searchBar.setCardBackgroundColor(activity.getResources().getColor(R.color.black_window_light));
            menuIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_menu));
            searchIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_search_white));
            SpinnerLayout.setBackgroundResource(R.drawable.btn_rounded_dark);

        }


       String[] report_array=getResources ().getStringArray (R.array.report_types_array);
        ArrayAdapter<String> adapter= new ArrayAdapter<String> (getContext(),R.layout.spinner_item,report_array);
        adapter.setDropDownViewResource (android.R.layout.simple_dropdown_item_1line);
        SpinnerReportType.setAdapter (adapter);

        SpinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!activity.isDark){
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                }
                switch (i){
                    case 2:
                      cardIssue.setVisibility(View.VISIBLE);
                      cardSuggestione.setVisibility(View.GONE);
                      cardReportMovie.setVisibility(View.GONE);
                      cardRequestAdd.setVisibility(View.GONE);
                    break;

                    case 1:
                        cardIssue.setVisibility(View.GONE);
                        cardSuggestione.setVisibility(View.GONE);
                        cardReportMovie.setVisibility(View.VISIBLE);
                        cardRequestAdd.setVisibility(View.GONE);
                        break;

                    case 0:
                        cardIssue.setVisibility(View.GONE);
                        cardSuggestione.setVisibility(View.GONE);
                        cardReportMovie.setVisibility(View.GONE);
                        cardRequestAdd.setVisibility(View.VISIBLE);
                        break;




                    case 3:
                        cardIssue.setVisibility(View.GONE);
                        cardSuggestione.setVisibility(View.VISIBLE);
                        cardReportMovie.setVisibility(View.GONE);
                        cardRequestAdd.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (issueDescription.getText().toString().equals("")){
                    issueDescription.setError("الرجاء إدخال تفاصيل");
                }
                else{
                    pr_bar.setVisibility(View.VISIBLE);
                    btnIssue.setEnabled(false);
                    issueDescription.setEnabled(false);
                    mRef.child("Reports").child("Issue").orderByChild("issue_description").equalTo(issueDescription.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                btnIssue.setEnabled(true);
                                issueDescription.setEnabled(true);
                                pr_bar.setVisibility(View.GONE);
                             Toast.makeText(getContext(),"تم تسجيل هذا التبليغ من قبل",Toast.LENGTH_LONG).show();
                            }else {
                                mRef.child("Reports").child("Issue").push().setValue(new issue(issueDescription.getText().toString(), Build.BRAND +" | "+ Build.MODEL,"ANDROID SDK "+Build.VERSION.SDK_INT+" ( "+Build.VERSION.RELEASE+" )", Tools.getTimeNow(),"true")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        btnIssue.setEnabled(true);
                                        issueDescription.setEnabled(true);
                                        pr_bar.setVisibility(View.GONE);
                                        if (task.isSuccessful()){
                                            new ToastMsg(getActivity()).toastIconSuccess("تم إضافة التلبيغ بنجاح");
                                            issueDescription.setText("");
                                            //showRewarded();
                                        }
                                        else{
                                            new ToastMsg(getActivity()).toastIconError("حصل خطأ أثناء إضافة التبليغ الرجاء إعادة المحاولة");
                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            btnIssue.setEnabled(true);
                            issueDescription.setEnabled(true);
                            pr_bar.setVisibility(View.GONE);
                            new ToastMsg(getActivity()).toastIconError("حصل خطأ أثناء محاولة إضافة التبليغ الرجاء إعادة المحاولة");

                        }
                    });

                }
            }
        });

        btnSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (suggestionDescription.getText().toString().equals("")){
                    suggestionDescription.setError("الرجاء إدخال تفاصيل");
                }
                else{
                    pr_bar.setVisibility(View.VISIBLE);
                    btnSuggestion.setEnabled(false);
                    suggestionDescription.setEnabled(false);
                    mRef.child("Reports").child("Suggestion").push().setValue(new sugesstion(suggestionDescription.getText().toString(), Build.BRAND +" | "+ Build.MODEL,"ANDROID SDK "+Build.VERSION.SDK_INT+" ( "+Build.VERSION.RELEASE+" )", Tools.getTimeNow(),"true")).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pr_bar.setVisibility(View.GONE);
                            btnSuggestion.setEnabled(true);
                            suggestionDescription.setEnabled(true);
                            if (task.isSuccessful()){
                                new ToastMsg(getActivity()).toastIconSuccess("تم إضافة الإقتراح بنجاح");
                                suggestionDescription.setText("");
                            }
                            else{
                                new ToastMsg(getActivity()).toastIconError("حصل خطأ أثناء محاولة إضافة الإقتراح الرجاء إعادة المحاولة");
                            }
                        }
                    });
                }
            }
        });

        btnReportMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reportMovieName.getText().toString().equals("")){
                    reportMovieName.setError("الرجاء إدخال اسم الفيلم او المسلسل");
                }
                else if(reportMovieDescription.getText().toString().equals("")){
                    reportMovieDescription.setError("الرجاء شرح مبسط حتى نفهم نوع المشكلة بالتحديد");
                }
                else{
                    pr_bar.setVisibility(View.VISIBLE);
                    btnReportMovie.setEnabled(false);
                    reportMovieName.setEnabled(false);
                    reportMovieDescription.setEnabled(false);
                    mRef.child("Reports").child("ReportedMovies").orderByChild("movie_name").equalTo(reportMovieName.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        pr_bar.setVisibility(View.GONE);
                                        btnReportMovie.setEnabled(true);
                                        reportMovieName.setEnabled(true);
                                        reportMovieDescription.setEnabled(true);
                                        Toast.makeText(getActivity(),"تم التبليغ عنه من قبل, سوف تقوم الإدارة بتصحيحه في اقرب وقت",Toast.LENGTH_LONG).show();
                                        return;
                                    }else{
                                        mRef.child("Reports").child("ReportedMovies").push().setValue(new reportMovie(reportMovieName.getText().toString(),reportMovieDescription.getText().toString(), Build.BRAND +" | "+ Build.MODEL,"ANDROID SDK "+Build.VERSION.SDK_INT+" ( "+Build.VERSION.RELEASE+" )", Tools.getTimeNow(),"true")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                pr_bar.setVisibility(View.GONE);
                                                btnReportMovie.setEnabled(true);
                                                reportMovieName.setEnabled(true);
                                                reportMovieDescription.setEnabled(true);
                                                if (task.isSuccessful()){
                                                    new ToastMsg(getActivity()).toastIconSuccess("تم إضافة التبليغ بنجـاح");
                                                    reportMovieName.setText("");
                                                    reportMovieDescription.setText("");
                                                   // showRewarded();

                                                }
                                                else{
                                                    new ToastMsg(getActivity()).toastIconError("حصل خطأ أثناء محاولة إضافة التبليغ الرجاء إعادة المحاولة");
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    pr_bar.setVisibility(View.GONE);
                                    btnReportMovie.setEnabled(true);
                                    reportMovieName.setEnabled(true);
                                    reportMovieDescription.setEnabled(true);
                                    new ToastMsg(getActivity()).toastIconError("حصل خطأ أثناء محاولة إضافة التبليغ الرجاء إعادة المحاولة");
                                }
                            });

                }
            }
        });

        btnRequestAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestAddTitle.getText().toString().equals("")){
                    requestAddTitle.setError("الرجاء إدخال اسم الفيلم او المسلسل");
                }
                else{
                    pr_bar.setVisibility(View.VISIBLE);
                    btnRequestAdd.setEnabled(false);
                    requestAddTitle.setEnabled(false);
                    mRef.child("Reports").child("RequestAddMovie").orderByChild("request_name").equalTo(requestAddTitle.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        pr_bar.setVisibility(View.GONE);
                                        btnRequestAdd.setEnabled(true);
                                        requestAddTitle.setEnabled(true);
                                       Toast.makeText(getActivity(),"تم تسجيل هذا الطلب من قبل, سوف تعمل الإدارة على إضافته في اقرب وقت",Toast.LENGTH_LONG).show();
                                        return;
                                    }else {
                                        mRef.child("Reports").child("RequestAddMovie").push().setValue(new requestAddMovie(requestAddTitle.getText().toString(), Build.BRAND +" | "+ Build.MODEL,"ANDROID SDK "+Build.VERSION.SDK_INT+" ( "+Build.VERSION.RELEASE+" )", Tools.getTimeNow(),"true")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                pr_bar.setVisibility(View.GONE);
                                                btnRequestAdd.setEnabled(true);
                                                requestAddTitle.setEnabled(true);
                                                if (task.isSuccessful()){
                                                    new ToastMsg(getActivity()).toastIconSuccess("تم تسجيل طلبك بنجـاح");
                                                    requestAddTitle.setText("");
                                                    //showRewarded();

                                                }
                                                else{
                                                    new ToastMsg(getActivity()).toastIconError("حصل خطأ أثناء محاولة إضافة طلبك");
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    pr_bar.setVisibility(View.GONE);
                                    btnRequestAdd.setEnabled(true);
                                    requestAddTitle.setEnabled(true);
                                    new ToastMsg(getActivity()).toastIconError("حصل خطأ أثناء محاولة إضافة طلبك");

                                }
                            });

                }
            }
        });

       // loadAd();

    }

    void showRewarded(){
       if (Tools.getAdNetwork(activity).equalsIgnoreCase(Constants.START_APP)){
           startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
               @Override
               public void onReceiveAd(Ad ad) {
                   startAppAd.showAd();

               }

               @Override
               public void onFailedToReceiveAd(Ad ad) {
                   StartAppAd.showAd(activity);
               }
           });

           startAppAd.setVideoListener(new VideoListener() {
               @Override
               public void onVideoCompleted() {
                   // Grant user with the reward
                   //Toast.makeText(activity,"fsqfqsf",Toast.LENGTH_SHORT).show();
               }
           });
       }
       else if(Tools.getAdNetwork(activity).equalsIgnoreCase(Constants.INMOBI)){
           PopUpAds.showInmobiInterstitialAds(activity);
       }
       else if(Tools.getAdNetwork(activity).equalsIgnoreCase(Constants.ADFALCON)){
           PopUpAds.showAdFalconInterstitialAds(activity);
       }
       else if(Tools.getAdNetwork(activity).equalsIgnoreCase(Constants.ADFALCON)) {
           PopUpAds.showFANInterstitialAds(getContext());
       }
       }







    @Override
    public void onStart() {
        super.onStart();

        menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.openDrawer();
            }
        });
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.goToSearchActivity();
            }
        });

    }

    private void loadAd() {
        List<Boolean> chanceToShowAd =  Arrays.asList(true,false,false);
        Collections.shuffle(chanceToShowAd);
        Boolean chosenChance = chanceToShowAd.get(0);
        AdsConfig adsConfig = new AdsConfig();
        if (adsConfig.getAdsEnable().equals("1")) {
            if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADMOB)) {
                BannerAds.ShowAdmobBannerAds(activity, adView);
                if (chosenChance){
                    PopUpAds.ShowAdmobInterstitialAds(getContext());
                }
            } else if (adsConfig.getMobileAdsNetwork().equals(Constants.START_APP)) {
                startAppLayoutBanner.setVisibility(View.VISIBLE);
                BannerAds.showStartAppBanner(activity,null,"new_method",startAppBanner);
                if (chosenChance){
                    PopUpAds.showStartappInterstitialAds(getContext());
                }

            } else if(adsConfig.getMobileAdsNetwork().equals(Constants.NETWORK_AUDIENCE)) {
                BannerAds.showFANBanner(getContext(), adView);
                if (chosenChance){
                    PopUpAds.showFANInterstitialAds(getContext());
                }
            }
            else if(adsConfig.getMobileAdsNetwork().equals(Constants.INMOBI)){
                BannerAds.showInmobiBanner(bannerAd);
                if (chosenChance){
                    PopUpAds.showInmobiInterstitialAds(getContext());
                }
            }
            else if(adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADFALCON)){
                BannerAds.showAdFalconBanner(activity,AD_FALCON);
                if (chosenChance){
                    PopUpAds.showAdFalconInterstitialAds(getContext());
                }
            }
        }
    }



}