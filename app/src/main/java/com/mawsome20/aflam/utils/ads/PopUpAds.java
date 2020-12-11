package com.mawsome20.aflam.utils.ads;

import android.content.Context;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.inmobi.ads.AdMetaInfo;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.mawsome20.aflam.R;
import com.mawsome20.aflam.network.model.AdsConfig;
import com.noqoush.adfalcon.android.sdk.ADFAd;
import com.noqoush.adfalcon.android.sdk.ADFInterstitial;
import com.noqoush.adfalcon.android.sdk.ADFListener;
import com.noqoush.adfalcon.android.sdk.constant.ADFErrorCode;
import com.startapp.android.publish.adsCommon.StartAppAd;

import java.util.Map;
/*
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

 */

public class PopUpAds {

    public static void ShowAdmobInterstitialAds(Context context) {
        AdsConfig adsConfig = new AdsConfig();
        final InterstitialAd mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(adsConfig.getAdmobInterstitialAdsId());
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                    mInterstitialAd.show();


                /*Random rand = new Random();
                int i = rand.nextInt(10)+1;

                Log.e("INTER AD:", String.valueOf(i));

                if (i%2==0){
                    mInterstitialAd.show();
                }*/
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

            }
        });
    }

    public static void showFANInterstitialAds(Context context){
       // DatabaseHelper db = new DatabaseHelper(context);
        String placementId = "";

        final com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(context, placementId);
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        interstitialAd.loadAd();
    }

    public static void showStartappInterstitialAds(Context context){
        //startapp
       // StartAppSDK.init(context, new DatabaseHelper(context).getConfigurationData().getAdsConfig().getStartappAppId(), true);
        //StartAppSDK.setTestAdsEnabled(true);

       // StartAppAd startAppAd = new StartAppAd(context);
    //    startAppAd.loadAd(StartAppAd.AdMode.VIDEO);
       // startAppAd.loadAd(StartAppAd.AdMode.VIDEO);
        //startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);

        StartAppAd.showAd(context);
        //startAppAd.showAd(); // show the ad
    }

    public static void showInmobiInterstitialAds(Context context){
        Long full_page_id = 1593723218210L;
        InMobiInterstitial interstitialAd = new InMobiInterstitial(context,full_page_id, new InterstitialInmobiAdEventListener());
        interstitialAd.load();

    }


    public static void showAdFalconInterstitialAds(Context context){
            ADFInterstitial adfInterstitial = new ADFInterstitial(context, context.getString(R.string.adfalcon_site_id),new  mInterstitialAdFalcon());
        adfInterstitial.setTestMode(false);

        adfInterstitial.loadInterstitialAd();

    }

    public static class mInterstitialAdFalcon implements ADFListener {
        @Override
        public void onLoadAd(ADFAd adfAd) {
            if(adfAd instanceof ADFInterstitial){
                ((ADFInterstitial)adfAd).showInterstitialAd();
            }
        }

        @Override
        public void onError(ADFAd adfAd, ADFErrorCode adfErrorCode, String s) {

        }

        @Override
        public void onPresentAdScreen(ADFAd adfAd) {

        }

        @Override
        public void onDismissAdScreen(ADFAd adfAd) {

        }

        @Override
        public void onLeaveApplication() {

        }
    }


    public static class InterstitialInmobiAdEventListener extends com.inmobi.ads.listeners.InterstitialAdEventListener {

        public void onAdLoadSucceeded(@NonNull InMobiInterstitial ad, @NonNull AdMetaInfo info) {ad.show();
        }

        public void onAdLoadFailed(@NonNull InMobiInterstitial ad, @NonNull InMobiAdRequestStatus status) {}

        public void onAdFetchSuccessful(@NonNull InMobiInterstitial ad, @NonNull AdMetaInfo info) {}


        public void onAdClicked(@NonNull InMobiInterstitial ad, Map<Object, Object> params) {}

        public void onAdWillDisplay(@NonNull InMobiInterstitial ad) {}

        public void onAdDisplayed(@NonNull InMobiInterstitial ad, @NonNull AdMetaInfo info) {}

        public void onAdDisplayFailed(@NonNull InMobiInterstitial ad) {}

        public void onAdDismissed(@NonNull InMobiInterstitial ad) {}

        public void onUserLeftApplication(@NonNull InMobiInterstitial ad) {}

        public void onRewardsUnlocked(@NonNull InMobiInterstitial ad, Map<Object, Object> rewards) {}
    }


}
