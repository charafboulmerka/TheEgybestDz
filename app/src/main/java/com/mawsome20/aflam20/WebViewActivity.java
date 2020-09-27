package com.mawsome20.aflam20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

//import com.startapp.sdk.adsbase.StartAppAd;

import com.mawsome20.aflam20.utils.Constants;
import com.mawsome20.aflam20.utils.Tools;
import com.mawsome20.aflam20.utils.ads.PopUpAds;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar mPr;
    List<Boolean> chanceToShowAd =  Arrays.asList(true,false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        webView = findViewById(R.id.webView);
        mPr = findViewById(R.id.mProgressBar);
        Button btClose = findViewById(R.id.btn_close);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.shuffle(chanceToShowAd);
                if (chanceToShowAd.get(0)){
                    if (Tools.getAdNetwork(getApplicationContext()).equalsIgnoreCase(Constants.START_APP)) {
                        PopUpAds.showStartappInterstitialAds(WebViewActivity.this);
                    }
                    else if (Tools.getAdNetwork(getApplicationContext()).equalsIgnoreCase(Constants.INMOBI)) {
                        PopUpAds.showInmobiInterstitialAds(WebViewActivity.this);
                    }
                    else if(Tools.getAdNetwork(getApplicationContext()).equalsIgnoreCase(Constants.ADFALCON)){
                        PopUpAds.showAdFalconInterstitialAds(WebViewActivity.this);
                    }
                }
                finish();
            }
        });
        btClose.setCompoundDrawables(ContextCompat.getDrawable(this,R.drawable.ic_action_back_arrow),null,null,null);


        String s = getIntent().getStringExtra("url");
        /*
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

         */

        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && mPr.getVisibility() == ProgressBar.GONE) {
                    mPr.setVisibility(ProgressBar.VISIBLE);
                }

                mPr.setProgress(progress);
                if (progress == 100) {
                    mPr.setVisibility(ProgressBar.GONE);
                }
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowContentAccess(true);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.loadUrl(s);

    }
}
