package com.mawsome20.aflam.utils.ads;

import android.content.Context;
import android.os.Bundle;


import android.view.View;
import android.widget.RelativeLayout;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.inmobi.ads.InMobiBanner;
import com.ixidev.gdpr.GDPRChecker;
import com.mawsome20.aflam.R;
import com.mawsome20.aflam.network.model.AdsConfig;
import com.noqoush.adfalcon.android.sdk.ADFView;
import com.noqoush.adfalcon.android.sdk.constant.ADFAdSize;
import com.startapp.android.publish.ads.banner.Banner;
/*
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppSDK;


 */

public class BannerAds {

    public static void ShowAdmobBannerAds(Context context, RelativeLayout mAdViewLayout) {
        AdsConfig adsConfig = new AdsConfig();

        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(adsConfig.getAdmobBannerAdsId());
        AdRequest.Builder builder = new AdRequest.Builder();
        GDPRChecker.Request request = GDPRChecker.getRequest();

        if (request == GDPRChecker.Request.NON_PERSONALIZED) {
            // load non Personalized ads
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
        } // else do nothing , it will load PERSONALIZED ads
        mAdView.loadAd(builder.build());
        mAdViewLayout.addView(mAdView);

    }

    public static void showStartAppBanner(Context context, final RelativeLayout mainLayout,String method_type,Banner mBanner) {
        if (method_type.equals("old_method")){
            //startapp
            // StartAppSDK.init(context, new DatabaseHelper(context).getConfigurationData().getAdsConfig().getStartappAppId(), true);
            //StartAppSDK.setTestAdsEnabled(true);
            AdsConfig adsConfig = new AdsConfig();

            Banner startAppBanner = new Banner(context);
            RelativeLayout.LayoutParams bannerParameters =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
            bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
// Add to main Layout
            mainLayout.setVisibility(View.VISIBLE);
            mainLayout.addView(startAppBanner, bannerParameters);

        }
        else{
            mBanner.setVisibility(View.VISIBLE);
            mBanner.loadAd();
            mBanner.showBanner();
        }



    }

    public static void showFANBanner(Context context, RelativeLayout mAdViewLayout) {
        mAdViewLayout.setVisibility(View.VISIBLE);
        AdsConfig adsConfig = new AdsConfig();

        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, adsConfig.getFanBannerAdsPlacementId(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        mAdViewLayout.addView(adView);
        // Request an ad
        adView.loadAd();
    }

    public static void showInmobiBanner(InMobiBanner bannerAd){
        bannerAd.setVisibility(View.VISIBLE);
        bannerAd.setEnableAutoRefresh(true);
        bannerAd.load();
    }

    public static void showAdFalconBanner(Context mCtx ,ADFView adFalconView){

        try {
            adFalconView.setVisibility(View.VISIBLE);
            //Ensure test mode is set to false before your app is released
            adFalconView.setTestMode(false);
            // initialize the view by passing a publisher id, ad unit size, params,
            // listener and enable auto refresh.
            adFalconView.setEnableAutoRefresh(true);
            // then load first ad
            adFalconView.initialize(mCtx.getString(R.string.adfalcon_site_id), ADFAdSize.AD_UNIT_320x50, null, null, true);

        } catch (Exception ex) {

        }
    }


}
