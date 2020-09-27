package com.mawsome20.aflam20.nav_fragments;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.Login;
import com.mawsome20.aflam20.LoginActivity;
import com.mawsome20.aflam20.MainActivity;
import com.mawsome20.aflam20.R;
import com.mawsome20.aflam20.fragments.HomeFragment;
import com.mawsome20.aflam20.fragments.MoviesFragment;
import com.mawsome20.aflam20.fragments.TvSeriesFragment;
import com.mawsome20.aflam20.network.model.AdsConfig;
import com.mawsome20.aflam20.utils.Constants;
import com.mawsome20.aflam20.utils.PreferenceUtils;
import com.mawsome20.aflam20.utils.ads.PopUpAds;
//import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.volcaniccoder.bottomify.BottomifyNavigationView;
import com.volcaniccoder.bottomify.OnNavigationItemChangeListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MainHomeFragment extends Fragment {
    private MainActivity activity;
    private BottomifyNavigationView bottomifyNavigationViewDark, bottomifyNavigationViewLight;
    LinearLayout searchRootLayout;
    List<Boolean> chanceToShowHomeAd =  Arrays.asList(true,false,false,false);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_main_home, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomifyNavigationViewDark = view.findViewById(R.id.bottomify_nav);
        bottomifyNavigationViewLight = view.findViewById(R.id.bottomify_nav_light);
        searchRootLayout = view.findViewById(R.id.search_root_layout);

        SharedPreferences sharedPreferences = activity.getSharedPreferences("push", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("dark", false);

        if (isDark) {
            //bottomifyNavigationView
            bottomifyNavigationViewDark.setVisibility(View.VISIBLE);
            bottomifyNavigationViewDark.setBackgroundColor(getResources().getColor(R.color.black_window_light));
        } else {
            //bottomifyNavigationView light
            bottomifyNavigationViewLight.setVisibility(View.VISIBLE);
            bottomifyNavigationViewLight.setBackgroundColor(getResources().getColor(R.color.white));
        }

        //bottomifyNavigationView
        bottomifyNavigationViewDark.setActiveNavigationIndex(0);
        bottomifyNavigationViewDark.setOnNavigationItemChangedListener(new OnNavigationItemChangeListener() {
            @Override
            public void onNavigationItemChanged(@NotNull BottomifyNavigationView.NavigationItem navigationItem) {
                switch (navigationItem.getPosition()) {
                    case 0:
                        loadFragment(new HomeFragment());
                        break;
                    case 1:
                        loadFragment(new MoviesFragment());
                        break;
                    case 2:
                        loadFragment(new TvSeriesFragment());
                        break;
                    case 3:
                        if (new PreferenceUtils(getContext()).isLoggedIn()){
                            loadFragment(new FavoriteFragment());
                        }else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            Toast.makeText(getContext(),"يجب عليك تسجيل الدخول للإطلاع على المفضلة",Toast.LENGTH_LONG).show();
                        }
                        break;

                }
                Collections.shuffle(chanceToShowHomeAd);
                if (chanceToShowHomeAd.get(0)){
                   // loadAd();
                }
            }
        });

        //bottomify light
        bottomifyNavigationViewLight.setActiveNavigationIndex(0);
        bottomifyNavigationViewLight.setOnNavigationItemChangedListener(new OnNavigationItemChangeListener() {
            @Override
            public void onNavigationItemChanged(@NotNull BottomifyNavigationView.NavigationItem navigationItem) {
                switch (navigationItem.getPosition()){
                    case 0:
                        loadFragment(new HomeFragment());
                        break;
                    case 1:
                        loadFragment(new MoviesFragment());
                        break;
                    case 2:
                        loadFragment(new TvSeriesFragment());
                        break;
                    case 3:
                        loadFragment(new FavoriteFragment());
                        break;

                }
                Collections.shuffle(chanceToShowHomeAd);
                if (chanceToShowHomeAd.get(0)){
                    //loadAd();
                }
            }
        });


        loadFragment(new HomeFragment());

    }

    private void loadAd() {
        AdsConfig adsConfig = new AdsConfig();
        if (adsConfig.getAdsEnable().equals("1")) {

            if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADMOB)) {
                PopUpAds.ShowAdmobInterstitialAds(activity);

            } else if (adsConfig.getMobileAdsNetwork().equals(Constants.START_APP)) {
                StartAppAd.showAd(activity);

            } else if(adsConfig.getMobileAdsNetwork().equals(Constants.NETWORK_AUDIENCE)) {
                PopUpAds.showFANInterstitialAds(getContext());
            }
            else if(adsConfig.getMobileAdsNetwork().equals(Constants.NETWORK_AUDIENCE)) {
                PopUpAds.showInmobiInterstitialAds(getContext());
            }
            else if(adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADFALCON)){
                PopUpAds.showAdFalconInterstitialAds(getContext());
            }
        }
    }

    //----load fragment----------------------
    private boolean loadFragment(Fragment fragment){
        if (fragment!=null){
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();

            return true;
        }
        return false;

    }


}