package com.mawsome20.aflam20;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.mediarouter.app.MediaRouteButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.inmobi.ads.InMobiBanner;
import com.mawsome20.aflam20.adapters.All_MoviesSeriesAdapter;
import com.mawsome20.aflam20.adapters.need_later.CastCrewAdapter;
import com.mawsome20.aflam20.adapters.need_later.CommentsAdapter;
import com.mawsome20.aflam20.adapters.EpisodeAdapter;
import com.mawsome20.aflam20.adapters.DownloadAdapter;
import com.mawsome20.aflam20.adapters.ServerApater;
import com.mawsome20.aflam20.models.CastCrew;
import com.mawsome20.aflam20.models.GetCommentsModel;
import com.mawsome20.aflam20.models.CommonModels;
import com.mawsome20.aflam20.models.Program;
import com.mawsome20.aflam20.models.SubtitleModel;
import com.mawsome20.aflam20.models.reports.reportMovie;
import com.mawsome20.aflam20.network.model.AdsConfig;
import com.mawsome20.aflam20.new_conception.models.Movies;
import com.mawsome20.aflam20.new_conception.models.Seasons;
import com.mawsome20.aflam20.new_conception.models.Series;
import com.mawsome20.aflam20.new_conception.models.mix_movies_series;
import com.mawsome20.aflam20.new_conception.models.strm_link;
import com.mawsome20.aflam20.utils.PreferenceUtils;
import com.mawsome20.aflam20.utils.ads.BannerAds;
import com.mawsome20.aflam20.utils.Constants;
import com.mawsome20.aflam20.utils.ads.PopUpAds;
import com.mawsome20.aflam20.utils.ToastMsg;
import com.mawsome20.aflam20.utils.Tools;
import com.noqoush.adfalcon.android.sdk.ADFView;
import com.squareup.picasso.Picasso;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;
/*
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DetailsActivity extends AppCompatActivity  {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PRELOAD_TIME_S = 20;
    public static final String TAG = DetailsActivity.class.getSimpleName();
    private TextView tvName, tvDirector, tvRelease, tvCast, tvDes, tvGenre, tvRelated;
    private RecyclerView rvDirector, rvServer, rvRelated, rvComment, castRv;
    private Spinner seasonSpinner;
    private LinearLayout seasonSpinnerContainer;
    public static RelativeLayout lPlay;
    private RelativeLayout contentDetails;
    private ImageButton backIv;
    List<Boolean> chanceToShowNewAd =  Arrays.asList(true,false);
    List<Boolean> chanceToShowNewAdFromPlayer =  Arrays.asList(true,false,false);
    StartAppAd startAppAd;


    private String finalTitle;
    private String finalPicProfile;
    private String finalGenres;
    private String finalYear;
    private String finalCategory;

    private  String user_charaf = "theegybestapp1013";
    private ServerApater serverAdapter;
    private DownloadAdapter internalDownloadAdapter, externalDownloadAdapter;
    private All_MoviesSeriesAdapter relatedAdapter;
  //  private LiveTvHomeAdapter relatedTvAdapter;
    private CastCrewAdapter castCrewAdapter;

    int start = 0;
    private List<strm_link> listServer = new ArrayList<>();
    private List<strm_link> listSeriesServer = new ArrayList<>();
    private List<mix_movies_series> listRelated = new ArrayList<>();
    private List<GetCommentsModel> listComment = new ArrayList<>();
    private List<CommonModels> listDownload = new ArrayList<>();
    private List<CommonModels> listInternalDownload = new ArrayList<>();
    public List<String> listExternalDownload = new ArrayList<>();
    private List<CastCrew> castCrews = new ArrayList<>();
    private String strDirector = "", strCast = "", strGenre = "";
    public static LinearLayout llBottom, llBottomParent, llcomment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String type = "", id = "";
    private ImageButton imgAddFav, shareIv2;
    public static ImageView imgBack, serverIv;
    private FloatingActionButton watchNowBt, downloadBt;
    private ImageView posterIv, thumbIv, descriptionBackIv;
    private String V_URL = "";
    public static WebView webView;
    public static ProgressBar progressBar;
    private boolean isFav = false;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Button btnComment;
    private EditText etComment;
    private CommentsAdapter commentsAdapter;
    private RelativeLayout adView;
    private InterstitialAd mInterstitialAd;
    private LinearLayout download_text;


    public static SimpleExoPlayer player;
    public static PlayerView simpleExoPlayerView;
    public static SubtitleView subtitleView;

    public static ImageView imgFull;
    public ImageView aspectRatioIv, externalPlayerIv, volumControlIv;
    private LinearLayout volumnControlLayout;
    private SeekBar volumnSeekbar;
    private TextView volumnTv;
    public MediaRouteButton mediaRouteButton;

    public static boolean isPlaying, isFullScr;
    public static View playerLayout;

    private int playerHeight;
    public static boolean isVideo = true;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String strSubtitle = "Null";
    public static MediaSource mediaSource = null;
    public static ImageView imgSubtitle;
    public ImageView radioPlayImage;
    private List<SubtitleModel> listSub = new ArrayList<>();
    private AlertDialog alertDialog;
    public String mediaUrl;
    private boolean tv = false;
    private String download_check = "";

    private String season;
    private String episod;
    private String movieTitle;
    private String seriesTitle;


    private String title;
    String castImageUrl;
    private FirebaseAuth mAuth;

    List<Program> programs = new ArrayList<>();

    private LinearLayout exoRewind, exoForward, seekbarLayout;
    ImageView exoDownloadIv;
    private TextView liveTv;


    boolean isDark;
    private OrientationEventListener myOrientationEventListener;

    private boolean fullScreenByClick;
    private String currentProgramTime;
    private String currentProgramTitle;
    private String userId;

    private String youtubeDownloadUr;
    private String urlType = "";
    private RelativeLayout descriptionLayout;
    private ImageView descriptionContatainer;
    private TextView dGenryTv;
    private RecyclerView internalServerRv, externalServerRv, serverRv;
    private LinearLayout internalDownloadLayout, externalDownloadLayout;
    private boolean activeMovie;

    private TextView sereisTitleTv;
    private RelativeLayout seriestLayout;
    private ImageView favIv;

    private RelativeLayout mRlTouch;
    private boolean intLeft, intRight;
    private int sWidth, sHeight;
    private long diffX, diffY;
    private Display display;
    private Point size;
    private float downX, downY;
    private AudioManager mAudioManager;
    private int aspectClickCount = 1;
    private Button btn_report;
    private DatabaseReference mRef;

     public Integer ep_postion=-1;
    private Integer season_position=-1;
    final List<String> seasonList = new ArrayList<>();
    String ReportDescription = "";
    EpisodeAdapter episodeAdapter;
    String TheURL = "";
    private Banner startAppBanner;
    private NestedScrollView sc_view;
    private FirebaseFunctions mFunctions;
    private PreferenceUtils mUtils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
         mUtils = new PreferenceUtils(DetailsActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        isDark = sharedPreferences.getBoolean("dark", false);
        mAuth=FirebaseAuth.getInstance();
        if (isDark) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mFunctions = FirebaseFunctions.getInstance();

        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        //---analytics-----------
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "details_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        try{
            FirebaseApp.initializeApp(this);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            mRef= FirebaseDatabase.getInstance().getReference();
            startAppBanner=findViewById(R.id.startAppBanner);
             sc_view = findViewById(R.id.scroll_view);

            btn_report=findViewById(R.id.btn_report_details);
            adView = findViewById(R.id.adView);
            llBottom = findViewById(R.id.llbottom);
            tvDes = findViewById(R.id.tv_details);
            tvCast = findViewById(R.id.tv_cast);
            tvRelease = findViewById(R.id.tv_release_date);
            tvName = findViewById(R.id.text_name);
            tvDirector = findViewById(R.id.tv_director);
            tvGenre = findViewById(R.id.tv_genre);
            swipeRefreshLayout = findViewById(R.id.swipe_layout);
            radioPlayImage = findViewById(R.id.radioPlayImage);
            webView = findViewById(R.id.webView);
            progressBar = findViewById(R.id.progressBar);
            llBottomParent = findViewById(R.id.llbottomparent);
            lPlay = findViewById(R.id.play);
            tvRelated = findViewById(R.id.tv_related);
            rvRelated = findViewById(R.id.rv_related);
            shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
            btnComment = findViewById(R.id.btn_comment);
            etComment = findViewById(R.id.et_comment);
            rvComment = findViewById(R.id.recyclerView_comment);
            llcomment = findViewById(R.id.llcomments);
            simpleExoPlayerView = findViewById(R.id.video_view);
            subtitleView = findViewById(R.id.subtitle);
            playerLayout = findViewById(R.id.player_layout);
            imgFull = findViewById(R.id.img_full_scr);
            aspectRatioIv = findViewById(R.id.aspect_ratio_iv);
            externalPlayerIv = findViewById(R.id.external_player_iv);
            volumControlIv = findViewById(R.id.volumn_control_iv);
            volumnControlLayout = findViewById(R.id.volumn_layout);
            volumnSeekbar = findViewById(R.id.volumn_seekbar);
            volumnTv = findViewById(R.id.volumn_tv);
            rvServer = findViewById(R.id.rv_server_list);
            seasonSpinner = findViewById(R.id.season_spinner);
            seasonSpinnerContainer = findViewById(R.id.spinner_container);
            imgSubtitle = findViewById(R.id.img_subtitle);
            download_text = findViewById(R.id.download_text);
            mediaRouteButton = findViewById(R.id.media_route_button);

            exoRewind = findViewById(R.id.rewind_layout);
            exoForward = findViewById(R.id.forward_layout);
            seekbarLayout = findViewById(R.id.seekbar_layout);
            castRv = findViewById(R.id.cast_rv);

            contentDetails = findViewById(R.id.content_details);
            backIv = findViewById(R.id.des_back_iv);

            descriptionLayout = findViewById(R.id.description_layout);
            descriptionContatainer = findViewById(R.id.cover_darkness);
            watchNowBt = findViewById(R.id.watch_now_bt);
            downloadBt = findViewById(R.id.download_bt);
            posterIv = findViewById(R.id.poster_iv);
            thumbIv = findViewById(R.id.image_thumb);
            dGenryTv = findViewById(R.id.genre_tv);
            serverIv = findViewById(R.id.img_server);

            seriestLayout = findViewById(R.id.series_layout);
            favIv = findViewById(R.id.add_fav2);
            sereisTitleTv = findViewById(R.id.seriest_title_tv);

            imgBack = findViewById(R.id.img_back);
            imgAddFav = findViewById(R.id.add_fav);
            shareIv2 = findViewById(R.id.share_iv2);

            if (isDark) {
                etComment.setBackground(ContextCompat.getDrawable(this, R.drawable.round_grey_transparent));
                btnComment.setTextColor(getResources().getColor(R.color.grey_20));
                descriptionContatainer.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient_black_transparent));
                btn_report.setBackgroundResource(R.drawable.btn_rounded_dark);
            }




            // start the shimmer effect
            shimmerFrameLayout.startShimmer();
            playerHeight = lPlay.getLayoutParams().height;
            progressBar.setMax(100); // 100 maximum value for the progress value
            progressBar.setProgress(50);



            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{

                        activeMovie=true;
                        if (activeMovie) {
                            setPlayerNormalScreen();
                            showDescriptionLayout();
                            activeMovie = false;
                            //hello

                            Log.e("POORME","now activeMovie is off from on click");

                        } else {
                            finish();
                            Log.e("PPORME","LOL");
                        }

                        if (!type.equals("movie")){
                            episodeAdapter.chanColor();
                        }
                        Collections.shuffle(chanceToShowNewAdFromPlayer);
                        if (chanceToShowNewAdFromPlayer.get(0)){
                            //loadAd();
                        }
                    }catch (Exception e){}

                }
            });


            type = getIntent().getStringExtra("vType");
            id = getIntent().getStringExtra("id");

            // getting user login info for favourite button visibility
         /*
        if (PreferenceUtils.isLoggedIn(DetailsActivity.this)) {
            imgAddFav.setVisibility(VISIBLE);
        } else {
            imgAddFav.setVisibility(GONE);
        }

          */

            commentsAdapter = new CommentsAdapter(this, listComment);
            rvComment.setLayoutManager(new LinearLayoutManager(this));
            rvComment.setHasFixedSize(true);
            rvComment.setNestedScrollingEnabled(false);

            rvComment.setAdapter(commentsAdapter);
            //getComments();
            imgFull.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    controlFullScreenPlayer();

                }
            });
            imgSubtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showDialog(DetailsActivity.this, listSub);

                }
            });
            /*
            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!PreferenceUtils.isLoggedIn(DetailsActivity.this)) {
                        startActivity(new Intent(DetailsActivity.this, FirebaseSignUpActivity.class));
                        new ToastMsg(DetailsActivity.this).toastIconError(getString(R.string.login_first));
                    } else if (etComment.getText().toString().equals("")) {
                        new ToastMsg(DetailsActivity.this).toastIconError(getString(R.string.comment_empty));
                    } else {
                        String comment = etComment.getText().toString();
                        addComment( id, PreferenceUtils.getUserId(DetailsActivity.this), comment);
                    }
                }
            });

             */

            imgAddFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUtils.isLoggedIn()) {
                        imgAddFav.setEnabled(false);
                        if (isFav) {
                            removeFromFavFC();
                        } else {
                            addToFavFC();
                        }
                        imgAddFav.setEnabled(true);
                    } else {
                        Toast.makeText(DetailsActivity.this, R.string.download_not_permitted, Toast.LENGTH_SHORT).show();
                        Handler hand = new Handler();
                        hand.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(DetailsActivity.this,LoginActivity.class));
                            }
                        }, 700);
                    }
                }
            });

            // its for tv series only when description layout visibility gone.
            favIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUtils.isLoggedIn()) {
                        if (isFav) {
                            removeFromFavFC();
                        } else {
                            addToFavFC();
                        }
                    } else {
                        Toast.makeText(DetailsActivity.this, R.string.download_not_permitted, Toast.LENGTH_SHORT).show();
                        Handler hand = new Handler();
                        hand.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(DetailsActivity.this,LoginActivity.class));
                            }
                        }, 700);
                    }
                }
            });


            if (!isNetworkAvailable()) {
                new ToastMsg(DetailsActivity.this).toastIconError(getString(R.string.no_internet));
            }
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    clear_previous();
                    initGetData();
                    Log.e("DETAILS_CATCH","REFRESHED");
                }
            });

            //loadAd();


            btn_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openReportDialog();
                }
            });
        }catch (Exception e){Log.e("DETAILS_CATCH",e.getMessage()); }

        initGetData();


    }

    public Map<String,String> getHeaders(){
        Map<String, String> extraHeaders = new HashMap<String, String>();
//        extraHeaders.put("Referer", "http://www.referer.tld/login.html");
        extraHeaders.put("Referer", "origin");
        return extraHeaders;
    }

    public String getHostName(String hostname)  {
        // to provide faultproof result, check if not null then return only hostname, without www.
        if (hostname != null) {
            return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
        }
        return hostname;
    }


    private class MyBrowser extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // Handle the error
            if (!isNetworkAvailable()) {
                loadErrorPage(view);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openWebActivity();
                    }
                },2000);
            }
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            if (!isNetworkAvailable()) {
                loadErrorPage(view);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openWebActivity();
                    }
                },2000);
            }
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(TheURL).getHost();
            return !url.contains(getHostName(host));
        }

    }

    private class MyGovidBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(TheURL).getHost();
            return !url.contains(getHostName(host));
        }

        // Handle API until level 21
        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return getNewResponse(url);
        }

        // Handle API 21+
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

            String url = request.getUrl().toString();

            return getNewResponse(url);
        }

        private WebResourceResponse getNewResponse(String url) {

            try {
                OkHttpClient httpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url.trim())
                        .addHeader("Referer", "origin") // Example header
//                        .addHeader("api-key", "YOUR_API_KEY") // Example header
                        .build();

                Response response = httpClient.newCall(request).execute();

                return new WebResourceResponse(
                        null,
                        response.header("content-encoding", "utf-8"),
                        response.body().byteStream()
                );

            } catch (Exception e) {
                return null;
            }

        }
        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // Handle the error
            if (!isNetworkAvailable()) {
                loadErrorPage(view);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openWebActivity();
                    }
                },2000);
            }
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            if (!isNetworkAvailable()) {
                loadErrorPage(view);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openWebActivity();
                    }
                },2000);
            }
        }
    }


//handle webview error
    public void loadErrorPage(WebView webview){
        try{
            if(webview!=null){
                // new ToastMsg(DetailsActivity.this).toastIconError("الرجاء تفقد إتصالك بالأنترنت");

                String htmlData ="<!DOCTYPE html>\n" +
                        "<html>" +
                        "   <head>" +
                        "      <title>Network Error</title>" +
                        "   </head>" +
                        "   <body style=\"background-color:grey;\">" +
                        "      <h1 align=\"right\">\n\n\nفشل الإتصال بالسيرفر</h1>\n" +
                        "      <p align=\"right\">\n\n...الرجاء الإنتظار جاري إعادة المحاولة</p>\n" +
                        "   </body>\n" +
                        "</html>";

                webview.loadUrl("about:blank");
                webview.loadDataWithBaseURL(null,htmlData, "text/html", "UTF-8",null);
                webview.invalidate();

            }
        }catch (Exception e){Log.e("DETAILS_CATCH",e.getMessage());}

    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void controlFullScreenPlayer() {
        try{
          if (isFullScr) {
              fullScreenByClick = false;
              isFullScr = false;
              swipeRefreshLayout.setVisibility(VISIBLE);
              getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
              setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

              if (isVideo) {
                  lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, playerHeight));
              } else {
                  lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, playerHeight));
              }

              // reset the orientation
              setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

          } else {

              fullScreenByClick = true;
              isFullScr = true;
              swipeRefreshLayout.setVisibility(GONE);
              getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
              setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

              if (isVideo) {
                  lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

              } else {
                  lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

              }

              // reset the orientation
              //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
              setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

          }
        }catch (Exception e){Log.e("CATCHED_ERROR",e.getMessage());}

    }


    @Override
    protected void onStart() {
        super.onStart();

        try {
            //lemme see

            if (mAudioManager != null) {
                volumnSeekbar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                int currentVolumn = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                volumnSeekbar.setProgress(currentVolumn);
            }

            volumnSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        //volumnTv.setText(i+"");
                        mAudioManager.setStreamVolume(player.getAudioStreamType(), i, 0);

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            volumControlIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    volumnControlLayout.setVisibility(VISIBLE);

                }
            });



            aspectRatioIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (aspectClickCount == 1) {
                        //Toast.makeText(DetailsActivity.this, "Fill", Toast.LENGTH_SHORT).show();
                        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                        aspectClickCount = 2;
                    } else if (aspectClickCount == 2) {
                        //Toast.makeText(DetailsActivity.this, "Fit", Toast.LENGTH_SHORT).show();
                        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                        aspectClickCount = 3;
                    } else if (aspectClickCount == 3) {
                        //Toast.makeText(DetailsActivity.this, "Zoom", Toast.LENGTH_SHORT).show();
                        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                        aspectClickCount = 1;
                    }

                }
            });

            externalPlayerIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mediaUrl != null) {
                        if (!tv) {
                            // set player normal/ potrait screen if not tv
                            descriptionLayout.setVisibility(VISIBLE);
                            setPlayerNormalScreen();
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(mediaUrl), "video/*");
                        startActivity(Intent.createChooser(intent, "Complete action using"));
                    }

                }
            });

            thumbIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!type.equals("tv") && !type.equals("tvseries")) {
                        openGoWatchDownDialog();
                    }
                }
            });

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!type.equals("tv") && !type.equals("tvseries")) {
                        openGoWatchDownDialog();
                    }
                }
            });

            watchNowBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickedWatchNow();
                }
            });

            downloadBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 OnClickedDownloadNow();
                }
            });

/*
            shareIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tools.share(DetailsActivity.this, title);
                }
            });


 */
            backIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("IS_CLOSING","YES");
                    finish();
                    Collections.shuffle(chanceToShowNewAd);
                    if (chanceToShowNewAd.get(0)){
                      //  loadAd();
                    }
                }
            });

            shareIv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (title == null) {
                        new ToastMsg(DetailsActivity.this).toastIconError("Title should not be empty.");
                        return;
                    }
                    Tools.share(DetailsActivity.this, title);
                }
            });


            serverIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openServerDialog("movie");
                }
            });

            simpleExoPlayerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
                @Override
                public void onVisibilityChange(int visibility) {
                    Log.e("Visibil", String.valueOf(visibility));
                    if (visibility == 0) {
                        imgBack.setVisibility(VISIBLE);
                        imgFull.setVisibility(VISIBLE);


                        // invisible download icon for live tv
                        if (download_check.equals("1")) {
                            if (!tv) {
                                if (activeMovie) {
                                    serverIv.setVisibility(VISIBLE);
                                }
                            } else {
                            }
                        } else {
                        }

                        if (listSub.size() != 0) {
                            //imgSubtitle.setVisibility(VISIBLE);
                            imgSubtitle.setVisibility(GONE);
                        }
                        //imgSubtitle.setVisibility(VISIBLE);
                    } else {
                        imgBack.setVisibility(GONE);
                        imgFull.setVisibility(GONE);
                        imgSubtitle.setVisibility(GONE);
                        volumnControlLayout.setVisibility(GONE);
                    }
                }
            });




            if (player!=null){
                player.setPlayWhenReady(true);
            }
            // invisible control ui of exoplayer
            simpleExoPlayerView.setUseController(true);

        }catch (Exception e){Log.e("DETAILS_CATCH",e.getMessage());}




    }

    public void OnClickedDownloadNow(){
        try{
            if (!listInternalDownload.isEmpty() || !listExternalDownload.isEmpty()) {
                //if (PreferenceUtils.isLoggedIn(DetailsActivity.this)) {
                    openDownloadServerDialog();
            } else {
                Toast.makeText(DetailsActivity.this, R.string.no_download_server_found, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){Log.e("DETAILS_CATCH",e.getMessage());}

    }


    void OnClickedWatchNow(){
        try{
            if (!listServer.isEmpty()) {
                if (listServer.size() == 1) {
                    preparePlayer(listServer.get(0));
                }else {
                    openServerDialog("movie");
                }
            }else{
                Toast.makeText(DetailsActivity.this, R.string.no_video_found, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}

    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void setPlayerNormalScreen() {
        try{
            webView.loadUrl("about:blank");


            if (player != null) {
                player.setPlayWhenReady(false);
                player.stop();
                player.release();
            }

            if (swipeRefreshLayout!=null){
                swipeRefreshLayout.setVisibility(VISIBLE);
            }
            if (lPlay!=null){
                lPlay.setVisibility(GONE);
            }

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            if (isVideo) {
                lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, playerHeight));

            } else {
                lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, playerHeight));
            }
        }catch (Exception e){
            //hello

        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void setPlayerFullScreen(Context ctx) {

    try{
        activeMovie = true;
        if (swipeRefreshLayout!=null){
            swipeRefreshLayout.setVisibility(GONE);
        }

        getActivity(ctx).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity(ctx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (isVideo) {
            Log.e("isVideo","yes");
            lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        } else {
            Log.e("isVideo","no");
            lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }
    }catch (Exception e){
        //hello
    }
    }
    public static Activity getActivity(Context context) {

            if (context == null) return null;
            if (context instanceof Activity) return (Activity) context;
            if (context instanceof ContextWrapper)
                return getActivity(((ContextWrapper) context).getBaseContext());
            return null;

    }

    private void openReportDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.report_dialog, null);
        EditText DialogTitle = view.findViewById(R.id.dialog_report_title);
        EditText DialogDescription = view.findViewById(R.id.dialog_report_description);
        Button btnReportMovie = view.findViewById(R.id.dialog_report_button);
        Button btnClose = view.findViewById(R.id.dialog_close_button);
        ProgressBar pr_bar = view.findViewById(R.id.dialog_progress_bar);
        builder.setView(view);

        if (type.equals("tvseries")) {
            if ( ep_postion != -1) {
                ReportDescription = seasonList.get(season_position) + "\n" + "الحلقة " + ep_postion;
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogTitle.setText(finalTitle);
                DialogTitle.setEnabled(false);
                DialogDescription.setText(ReportDescription);
            }
        },500);
        final AlertDialog dialog = builder.create();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnReportMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DialogTitle.getText().toString().equals("")){
                    DialogTitle.setError("الرجاء إدخال اسم الفيلم او المسلسل");
                }
                else if(DialogDescription.getText().toString().equals("")){
                    DialogDescription.setError("الرجاء شرح مبسط حتى نفهم نوع المشكلة بالتحديد");
                }
                else{
                    pr_bar.setVisibility(View.VISIBLE);
                    btnReportMovie.setEnabled(false);
                    //btnClose.setEnabled(false);
                    DialogTitle.setEnabled(false);
                    DialogDescription.setEnabled(false);
                    String childName = "movie_name";
                    String childValue = "";
                    if (type.equals("movie")){
                        childName = "movie_name";
                         childValue = DialogTitle.getText().toString();
                    }
                    else{
                        childName = "report_description";
                        childValue = DialogDescription.getText().toString();
                    }
                    mRef.child("Reports").child("ReportedMovies").orderByChild(childName).equalTo(childValue)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        dialog.dismiss();
    Toast.makeText(DetailsActivity.this,"تم التبليغ عنه من قبل, سوف تقوم الإدارة بتصحيحه في اقرب وقت"
            ,Toast.LENGTH_LONG).show();
                                   //     showRewarded();
                                        return;
                                    }else {

                                        mRef.child("Reports").child("ReportedMovies").push().setValue(new reportMovie(DialogTitle.getText().toString(),DialogDescription.getText().toString(), Build.BRAND +" | "+ Build.MODEL,"ANDROID SDK "+Build.VERSION.SDK_INT+" ( "+Build.VERSION.RELEASE+" )", Tools.getTimeNow(),"true")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                pr_bar.setVisibility(View.GONE);
                                                btnReportMovie.setEnabled(true);
                                                DialogTitle.setEnabled(true);
                                                DialogDescription.setEnabled(true);
                                                if (task.isSuccessful()){
                                                    dialog.dismiss();
                                                    new ToastMsg(DetailsActivity.this).toastIconSuccess("تم إضافة التبليغ بنجـاح");
                                                    DialogTitle.setText("");
                                                    DialogDescription.setText("");
                                                }
                                                else{
                                                    new ToastMsg(DetailsActivity.this).toastIconError("حصل خطأ أثناء محاولة إضافة التبليغ الرجاء إعادة المحاولة");
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    try{
                                        btnReportMovie.setEnabled(true);
                                        btnClose.setEnabled(true);
                                        DialogTitle.setEnabled(true);
                                        DialogDescription.setEnabled(true);
                                        new ToastMsg(DetailsActivity.this).toastIconError("حصل خطأ أثناء محاولة إضافة التبليغ الرجاء إعادة المحاولة");

                                    }catch (Exception e){

                                    }

                                }
                            });

                }
            }
        });


        dialog.show();
    }

    void showRewarded(){
        if (Tools.getAdNetwork(this).equalsIgnoreCase(Constants.START_APP)){
            startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                @Override
                public void onReceiveAd(Ad ad) {
                    startAppAd.showAd();

                }

                @Override
                public void onFailedToReceiveAd(Ad ad) {
                    StartAppAd.showAd(DetailsActivity.this);
                }
            });
        }
        else {
            //loadAd();
        }




    }


    private void openGoWatchDownDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.watch_download_dialog, null);

        Button goWatch = view.findViewById(R.id.dialog_go_watch_btn);
        Button goDownload = view.findViewById(R.id.dialog_go_download_btn);
        TextView goClose = view.findViewById(R.id.dialog_go_close);

        builder.setView(view);


        final AlertDialog dialog = builder.create();


        goClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        goWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                OnClickedWatchNow();
            }
        });

        goDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                OnClickedDownloadNow();
            }
        });






        dialog.show();
    }

    private void openDownloadServerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_download_server_dialog, null);
        internalDownloadLayout = view.findViewById(R.id.internal_download_layout);

        externalDownloadLayout = view.findViewById(R.id.external_download_layout);

        if (listExternalDownload.isEmpty()) {
            externalDownloadLayout.setVisibility(GONE);
        }

       // if (listInternalDownload.isEmpty()) {
            internalDownloadLayout.setVisibility(GONE);
        //}

        /*
        internalServerRv = view.findViewById(R.id.internal_download_rv);
        internalDownloadAdapter = new DownloadAdapter(this, listInternalDownload, true);
        internalServerRv.setLayoutManager(new LinearLayoutManager(this));
        internalServerRv.setHasFixedSize(true);
        internalServerRv.setAdapter(internalDownloadAdapter);

         */
        externalServerRv = view.findViewById(R.id.external_download_rv);

        externalDownloadAdapter = new DownloadAdapter(this, listExternalDownload, true);
        externalServerRv.setLayoutManager(new LinearLayoutManager(this));
        externalServerRv.setHasFixedSize(true);
        externalServerRv.setAdapter(externalDownloadAdapter);

        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openServerDialog(String VideoType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_server_dialog, null);
        serverRv = view.findViewById(R.id.serverRv);
        // movies list is null here
        if (VideoType.equals("movie")){
            serverAdapter = new ServerApater(this, listServer, VideoType);
        }
        else{
            serverAdapter = new ServerApater(this, listSeriesServer, VideoType);
        }
        serverRv.setLayoutManager(new LinearLayoutManager(this));
        serverRv.setHasFixedSize(true);
        serverRv.setAdapter(serverAdapter);

        ImageView closeIv = view.findViewById(R.id.close_iv);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final ServerApater.OriginalViewHolder[] viewHolder = {null};
        serverAdapter.setOnItemClickListener(new ServerApater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, strm_link obj, int position, ServerApater.OriginalViewHolder holder) {
                preparePlayer(obj);
                serverAdapter.chanColor(viewHolder[0], position);
                holder.name.setTextColor(getResources().getColor(R.color.colorPrimary));
                viewHolder[0] = holder;
            }

            @Override
            public void getFirstUrl(String url) {
                mediaUrl = url;
            }

            @Override
            public void hideDescriptionLayoutEvent(String type) {
                dialog.dismiss();

            }
        });

    }

    public void setMediaUrlForTvSeries(ArrayList<strm_link> SeriesServers, String season, String episod) {
        //TODO CALL THE DIALOG SERVERS
     //   mediaUrl = url;
        this.listSeriesServer = SeriesServers;
        this.season = season;
        this.episod = episod;
        openServerDialog("tvserie");
    }


public void preparePlayer(strm_link obj){

    String mediaType = "hls";
    mediaUrl = obj.getUrl();
    mediaType = obj.getType();
    /*
    //object is a movie
    if (obj!=null){
        mediaUrl = obj.getUrl();
        mediaType = obj.getType();
    }
    else{
        mediaUrl = serie_obj.getUrl();
        mediaType = serie_obj.getType();
    }



     */
    iniMoviePlayer(mediaUrl.trim(),mediaType.trim(), DetailsActivity.this);

}

    void clear_previous() {

        strCast = "";
        strDirector = "";
        strGenre = "";
        listDownload.clear();
        listInternalDownload.clear();
        listExternalDownload.clear();
        programs.clear();
        castCrews.clear();
    }

    public void showDialog(Context context, List<SubtitleModel> list) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_subtitle, viewGroup, false);
        ImageView cancel = dialogView.findViewById(R.id.cancel);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        //SubtitleAdapter adapter = new SubtitleAdapter(context, list);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        alertDialog = builder.create();
        alertDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }


/*
    private class SubtitleAdapter extends RecyclerView.Adapter<SubtitleAdapter.OriginalViewHolder> {

        private List<SubtitleModel> items = new ArrayList<>();
        private Context ctx;

        public SubtitleAdapter(Context context, List<SubtitleModel> items) {
            this.items = items;
            ctx = context;
        }


        @Override
        public SubtitleAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SubtitleAdapter.OriginalViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_subtitle, parent, false);
            vh = new SubtitleAdapter.OriginalViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(SubtitleAdapter.OriginalViewHolder holder, final int position) {

            final SubtitleModel obj = items.get(position);
            holder.name.setText(obj.getLang());

            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setSelectedSubtitle(mediaSource, obj.getUrl(), ctx);
                    alertDialog.cancel();

                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class OriginalViewHolder extends RecyclerView.ViewHolder {

            public TextView name;
            private View lyt_parent;


            public OriginalViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.name);
                lyt_parent = v.findViewById(R.id.lyt_parent);
            }
        }

    }


 */
    private void loadAd() {
        try{
            AdsConfig adsConfig = new AdsConfig();
            if (adsConfig.getAdsEnable().equals("1")) {

                if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADMOB)) {
                    BannerAds.ShowAdmobBannerAds(this, adView);
                    PopUpAds.ShowAdmobInterstitialAds(this);

                } else if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.START_APP)) {
                    LinearLayout startAppLayoutBanner = findViewById(R.id.startapp_linearlayout);
                    startAppLayoutBanner.setVisibility(VISIBLE);
                   // BannerAds.showStartAppBanner(DetailsActivity.this, adView);
                    //Banner
                    //StartAppSDK.init(this, new DatabaseHelper(this).getConfigurationData().getAdsConfig().getStartappAppId(), false);
                    BannerAds.showStartAppBanner(this,null,"new_method",startAppBanner);
                    startAppAd = new StartAppAd(this);

                    //FULL SCREEN
                    Collections.shuffle(chanceToShowNewAdFromPlayer);
                    if (chanceToShowNewAdFromPlayer.get(0)){
                        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC,new AdEventListener() {
                            @Override
                            public void onReceiveAd(Ad ad) {
                                System.out.println("Ad received");
                                startAppAd.showAd();
                            }

                            @Override
                            public void onFailedToReceiveAd(Ad ad) {

                            }
                        });
                    }
                    else{
                        StartAppAd.showAd(this);
                    }

                } else if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.NETWORK_AUDIENCE)) {
                    BannerAds.showFANBanner(this, adView);
                    PopUpAds.showFANInterstitialAds(DetailsActivity.this);
                }
                else if(adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.INMOBI)) {
                    PopUpAds.showInmobiInterstitialAds(this);
                    InMobiBanner bannerAd = (InMobiBanner)findViewById(R.id.banner);
                    BannerAds.showInmobiBanner(bannerAd);
                }
                else if(adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADFALCON)){
                    PopUpAds.showAdFalconInterstitialAds(this);
                    ADFView AD_FALCON = findViewById(R.id.adFalconView);
                    BannerAds.showAdFalconBanner(this,AD_FALCON);
                }

            }

        }catch (Exception e){ Log.e("CATCHED_ERROR", e.getMessage()); }

    }



    private void initGetData() {
            //----related rv----------
            relatedAdapter = new All_MoviesSeriesAdapter(this, listRelated,"related");
           rvRelated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
            rvRelated.setHasFixedSize(true);
            rvRelated.setAdapter(relatedAdapter);

            if (type.equals("tvseries")) {

                seasonSpinnerContainer.setVisibility(VISIBLE);


                rvServer.setVisibility(VISIBLE);
                serverIv.setVisibility(GONE);

                rvRelated.removeAllViews();
                listRelated.clear();
                rvServer.removeAllViews();
                listServer.clear();

                findViewById(R.id.float_btns).setVisibility(GONE);
                //downloadBt.setVisibility(GONE);
               // watchNowBt.setVisibility(GONE);

                // cast & crew adapter
                castCrewAdapter = new CastCrewAdapter(this, castCrews);
                castRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                castRv.setHasFixedSize(true);
                castRv.setAdapter(castCrewAdapter);

                //getSeriesData(type, id);
                getSerieFC();
                if (listSub.size() == 0) {
                    imgSubtitle.setVisibility(GONE);
                }

            } else {
                imgFull.setVisibility(GONE);
                listServer.clear();
                rvRelated.removeAllViews();
                listRelated.clear();
                if (listSub.size() == 0) {
                    imgSubtitle.setVisibility(GONE);
                }

                // cast & crew adapter
                castCrewAdapter = new CastCrewAdapter(this, castCrews);
                castRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                castRv.setHasFixedSize(true);
                castRv.setAdapter(castCrewAdapter);

                //getData(type, id);
                Log.e("charaf2020",id);
                getMovieFC();
/*
                getDataFc(type, id)
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                        Object details = ffe.getDetails();
                                        Toast.makeText(DetailsActivity.this,"NO:"+e.getMessage().toString(),Toast.LENGTH_LONG).show();

                                    }
                                    else{
                                        Toast.makeText(DetailsActivity.this,"YES:",Toast.LENGTH_LONG).show();
                                      //  Log.e("charaf1013",task.getResult());

                                    }

                                    // ...
                                }

                                // ...
                            }
                        });


 */


                final ServerApater.OriginalViewHolder[] viewHolder = {null};
            }

            if (mUtils.isLoggedIn()) {
              //  getFavStatus();
                getFavStatusFC();
            }



    }

    @SuppressLint("SetJavaScriptEnabled")
    private void openWebActivity() {
        if (isPlaying) {
            player.release();
        }
        progressBar.setVisibility(GONE);
        playerLayout.setVisibility(GONE);
        WebSettings webSettings = webView.getSettings();
        //webSettings.setUserAgentString(user_charaf);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setUseWideViewPort(true);
        //webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        webView.setWebViewClient(new MyBrowser());
/*
        if(TheURL.contains("govid")) {
            Log.i("info", "Playing a govid video: " + TheURL);
            webView.setWebViewClient(new MyGovidBrowser());
        } else {
            webView.setWebViewClient(new MyBrowser());
        }

 */

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(TheURL);


        webView.setVisibility(VISIBLE);
    }

    public void iniMoviePlayer(String url, String type, Context context) {
        Log.e("vTYpe :: ", type);
        urlType = type;

        if (type.equals("embed") || type.equals("vimeo") || type.equals("gdrive") || type.equals("youtube-live")) {
            changeDesignForType();
            activeMovie = true;
            isVideo = false;
            setPlayerFullScreen(context);
            TheURL=url;
            openWebActivity();
        }
        else {
            if (!type.equals("youtube") ){
                openPlayersChoicesDialog(url, type,context);
            }
            else{
                changeDesignForType();
                isVideo = true;
                initVideoPlayer(url, context, type);
            }
        }
    }



    public void changeDesignForType(){
        activeMovie = true;
        if (type.equals("movie")){
            setPlayerFullScreen(this);
            descriptionLayout.setVisibility(GONE);
            lPlay.setVisibility(VISIBLE);
        }else{
            hideDescriptionLayout();
            showSeriesLayout();
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void openPlayersChoicesDialog(String c_url, String c_type, Context c_context){
        alertDialog = new AlertDialog.Builder(this).create();


        alertDialog.setMessage("المشاهدة عن طريق؟"+"\n\n"+"أحيانا يتعذر التطبيق عن تشغيل بعد الروابط لهذا ننصحك بالمشغل VLC");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "VLC  PLAYER", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (isPackageInstalled("org.videolan.vlc",c_context.getPackageManager())){
                    Uri uri = Uri.parse(c_url);
                    Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
                    vlcIntent.setPackage("org.videolan.vlc");
                    vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
                    vlcIntent.putExtra("title", title);
                    vlcIntent.putExtra("from_start", false);
                    startActivity(vlcIntent);
                }
                else{
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=org.videolan.vlc");
                    Intent vlcIntent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(vlcIntent);
                }


            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "الإعتيادي", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                changeDesignForType();
                isVideo = true;
                initVideoPlayer(c_url, c_context, c_type);
                //AfterSerieServerChoice(obj,viewHolder,holder,pos);

            }});

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "اخرى", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (mediaUrl != null) {
                    if (!tv) {
                        // set player normal/ potrait screen if not tv
                        descriptionLayout.setVisibility(VISIBLE);
                        setPlayerNormalScreen();
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(c_url), "video/*");
                    startActivity(Intent.createChooser(intent, "إكمال المشاهدة عن طريق"));
                }

            }});
        alertDialog.show();
    }



    public void initVideoPlayer(String url, Context context, String type) {
        try{
            progressBar.setVisibility(VISIBLE);

            if (player != null) {
                player.release();
            }

            webView.setVisibility(GONE);
            playerLayout.setVisibility(VISIBLE);
/*
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new
                    AdaptiveTrackSelection.Factory(bandwidthMeter);

            DefaultTrackSelector trackSelector = new
                    DefaultTrackSelector(videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

 */
             player = new SimpleExoPlayer.Builder(context).build();
            player.setPlayWhenReady(true);
            //simpleExoPlayerView = findViewById(R.id.video_view);
            if(simpleExoPlayerView!=null){
                simpleExoPlayerView.setPlayer(player);
            }
            Uri uri = Uri.parse(url);

            if (type.equals("hls") || type.equals("m3u8")) {
                mediaSource = hlsMediaSource(uri, context);

            } else if (type.equals("youtube")) {
                Log.e("youtube url  :: ", url);
                extractYoutubeUrl(url, context, 18);
            } else if (type.equals("youtube-live")) {
                Log.e("youtube url  :: ", url);
                extractYoutubeUrl(url, context, 133);
            } else if (type.equals("rtmp")) {
                mediaSource = rtmpMediaSource(uri);
            } else {
                mediaSource = mediaSource(uri, context);
            }

            //Toast.makeText(context, "castSession:"+getCastSessionObj()+"", Toast.LENGTH_SHORT).show();
            player.prepare(mediaSource, true, false);

            player.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playWhenReady && playbackState == Player.STATE_READY) {

                        isPlaying = true;
                        progressBar.setVisibility(View.GONE);
                    } else if (playbackState == Player.STATE_READY) {
                        progressBar.setVisibility(View.GONE);
                        isPlaying = false;
                    } else if (playbackState == Player.STATE_BUFFERING) {
                        isPlaying = false;
                        progressBar.setVisibility(VISIBLE);
                    } else {
                        // player paused in any state
                        isPlaying = false;
                    }
                }
            });
        }catch (Exception e){}

    }

    @SuppressLint("StaticFieldLeak")
    private void extractYoutubeUrl(String url, final Context context, final int tag) {
        new YouTubeExtractor(context) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    int itag = tag;
                    String downloadUrl = ytFiles.get(itag).getUrl();
                    youtubeDownloadUr = downloadUrl;
                    Log.e("YOUTUBE::", String.valueOf(downloadUrl));
                    try {

                        MediaSource mediaSource = mediaSource(Uri.parse(downloadUrl), context);
                        player.prepare(mediaSource, true, false);
                        if (Config.YOUTUBE_VIDEO_AUTO_PLAY) {
                            player.setPlayWhenReady(true);
                        } else {
                            player.setPlayWhenReady(false);
                        }

                        player.addListener(new Player.DefaultEventListener() {
                            @Override
                            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                if (playWhenReady && playbackState == Player.STATE_READY) {

                                    isPlaying = true;
                                    progressBar.setVisibility(View.GONE);
                                } else if (playbackState == Player.STATE_READY) {
                                    progressBar.setVisibility(View.GONE);
                                    isPlaying = false;
                                } else if (playbackState == Player.STATE_BUFFERING) {
                                    isPlaying = false;
                                    progressBar.setVisibility(VISIBLE);
                                } else {
                                    // player paused in any state
                                    isPlaying = false;
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }.extract(url, true, true);
    }

    private MediaSource rtmpMediaSource(Uri uri) {
        MediaSource videoSource = null;
        RtmpDataSourceFactory dataSourceFactory = new RtmpDataSourceFactory();
        videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);

        return videoSource;
    }

    private MediaSource hlsMediaSource(Uri uri, Context context) {
        /*
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, user_charaf), bandwidthMeter);

        MediaSource videoSource = new HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
         */
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(context, user_charaf));
// Create a HLS media source pointing to a playlist uri.
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
// Create a player instance.

        return hlsMediaSource;
    }

    private MediaSource mediaSource(Uri uri, Context context) {

        return new ProgressiveMediaSource.Factory(
                new DefaultHttpDataSourceFactory(user_charaf)).
                createMediaSource(uri);

    }

    public void setSelectedSubtitle(MediaSource mediaSource, String subtitle, Context context) {
        MergingMediaSource mergedSource;
        if (subtitle != null) {
            Uri subtitleUri = Uri.parse(subtitle);

            Format subtitleFormat = Format.createTextSampleFormat(
                    null, // An identifier for the track. May be null.
                    MimeTypes.TEXT_VTT, // The mime type. Must be set correctly.
                    Format.NO_VALUE, // Selection flags for the track.
                    "en"); // The subtitle language. May be null.
/*
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, CLASS_NAME), new DefaultBandwidthMeter());

 */

            DataSource.Factory dataSourceFactory =
                    new DefaultHttpDataSourceFactory(Util.getUserAgent(context, user_charaf));


            MediaSource subtitleSource = new SingleSampleMediaSource
                    .Factory(dataSourceFactory)
                    .createMediaSource(subtitleUri, subtitleFormat, C.TIME_UNSET);


            mergedSource = new MergingMediaSource(mediaSource, subtitleSource);
            player.prepare(mergedSource, false, false);
            //resumePlayer();

        } else {
            Toast.makeText(context, "لا توجد ترجمة", Toast.LENGTH_SHORT).show();
        }
    }


    private void getFavStatusFC(){
        String user_uid = mUtils.getUserId();
        mRef.child("Users").child(user_uid).child("Fav").orderByChild("id").equalTo(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            isFav = true;
                            //here too
                            imgAddFav.setBackgroundResource(R.drawable.ic_favorite_white);
                            imgAddFav.setVisibility(VISIBLE);
                            if (seriestLayout.getVisibility()==VISIBLE){
                                favIv.setBackgroundResource(R.drawable.ic_favorite_white);
                            }
                        } else {
                            isFav = false;
                            imgAddFav.setBackgroundResource(R.drawable.ic_favorite_border_white);
                            imgAddFav.setVisibility(VISIBLE);
                            if (seriestLayout.getVisibility()==VISIBLE){
                                favIv.setBackgroundResource(R.drawable.ic_favorite_border_white);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void addToFavFC(){
        String user_uid = mAuth.getUid();
        DatabaseReference mKey = mRef.child("Users").child(user_uid).child("Fav").push();
        mKey.setValue(new mix_movies_series(id,finalTitle,finalPicProfile,finalGenres,finalYear,finalCategory,type))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            new ToastMsg(DetailsActivity.this).toastIconSuccess("تم الإضافة الى المفضلة بنجاح");
                            isFav = true;
                            imgAddFav.setBackgroundResource(R.drawable.ic_favorite_white);
                            //here fav
                            if (seriestLayout.getVisibility()==VISIBLE){
                                favIv.setBackgroundResource(R.drawable.ic_favorite_white);
                            }
                        }else {
                            new ToastMsg(DetailsActivity.this).toastIconError("حصل خطأ اثناء محاولة الإضافة الى المفضلة الرجاء إعادة المحاولة");
                        }
                    }
                });

    }

    private void removeFromFavFC(){
        String user_uid = mAuth.getUid();
        mRef.child("Users").child(user_uid).child("Fav").orderByChild("id").equalTo(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            HashMap<String,Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                            List<String> keys = new ArrayList<>(td.keySet());
                                //Log.e("NEWDAY",String.valueOf(keys.get(0)));
                                mRef.child("Users").child(user_uid).child("Fav").child(keys.get(0)).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                              if (task.isSuccessful()){
                                                  isFav = false;
                                                  new ToastMsg(DetailsActivity.this).toastIconSuccess("تتم إزالته من المفضلة بنجاح");
                                                  imgAddFav.setBackgroundResource(R.drawable.ic_favorite_border_white);
                                                  //here fav
                                                  if (seriestLayout.getVisibility()==VISIBLE){
                                                      favIv.setBackgroundResource(R.drawable.ic_favorite_border_white);
                                                  }
                                              }
                                              else {
                                                  isFav = true;
                                                  new ToastMsg(DetailsActivity.this).toastIconError("حصل خطأ الرجاء إعادة المحاولة");
                                                  imgAddFav.setBackgroundResource(R.drawable.ic_favorite_white);
                                                  if (seriestLayout.getVisibility()==VISIBLE){
                                                      favIv.setBackgroundResource(R.drawable.ic_favorite_white);
                                                  }
                                              }
                                            }
                                        });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public void setSeasonData(List<String> seasonData, ArrayList<Seasons> season) {
        //Collections.reverse(seasonData);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, seasonData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        seasonSpinner.setAdapter(aa);


        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                season_position = i;
                rvServer.removeAllViewsInLayout();
                rvServer.setLayoutManager(new LinearLayoutManager(DetailsActivity.this,
                        RecyclerView.HORIZONTAL, false));

                if (mUtils.isLoggedIn()){
                    mRef.child("Users").child(mUtils.getUserId()).child("Watched").child(id)
                            .child(season_position.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String data = (String) dataSnapshot.getValue();
                                List<String> watchedList = Arrays.asList(data.split(","));
                                episodeAdapter = new EpisodeAdapter(DetailsActivity.this,
                                        season.get(season_position).getEpisodes(),id,season_position
                                        ,season.get(season_position).getSeason_pic(),watchedList);
                                rvServer.setAdapter(episodeAdapter);
                            }else {
                                ArrayList<String> watchedList = new ArrayList<String>();
                                episodeAdapter = new EpisodeAdapter(DetailsActivity.this,
                                        season.get(season_position).getEpisodes(),id,season_position
                                        ,season.get(season_position).getSeason_pic(),watchedList);
                                rvServer.setAdapter(episodeAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    ArrayList<String> watchedList = new ArrayList<String>();
                    episodeAdapter = new EpisodeAdapter(DetailsActivity.this,
                            season.get(season_position).getEpisodes(),id,season_position
                            ,season.get(season_position).getSeason_pic(),watchedList);
                    rvServer.setAdapter(episodeAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setGenreText() {

        tvGenre.setText(strGenre);

        dGenryTv.setText(strGenre);

    }



    private void getSerieFC(){
        strGenre = "";
        mRef.child("Series").orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(GONE);
                /*
                            if (download_check.equals("1")) {
                        download_text.setVisibility(VISIBLE);
                        downloadBt.setVisibility(VISIBLE);
                    } else {
                        download_text.setVisibility(GONE);
                        downloadBt.setVisibility(GONE);
                    }
                 */
                Series serie = dataSnapshot.child(id).getValue(Series.class);
                finalTitle = serie.getTitle();
                finalGenres = serie.getGeners();
                finalYear = serie.getYear();
                finalCategory = serie.getType();
                finalPicProfile = serie.getPic_profile();

                seriesTitle = serie.getTitle();
                finalTitle = seriesTitle;
                sereisTitleTv.setText(seriesTitle);
                tvName.setText(seriesTitle);

                tvRelease.setText("نشر في " + serie.getYear());
                tvDes.setText(serie.getDescription().replace("&nbsp;",""));
                tvDirector.setText(serie.getDirector());


                Picasso.get().load(serie.getPic_profile()).fit().centerCrop().placeholder(R.drawable.album_art_placeholder_large)
                        .into(posterIv);
                Picasso.get().load(serie.getPic_cover()).fit().centerCrop().placeholder(R.drawable.poster_placeholder)
                        .into(thumbIv);

                List<String> gr = Arrays.asList(serie.getGeners().split("\\|"));
                if (gr.size()!=0){
                    if (gr.size()==1){
                        tvGenre.setText(Tools.getArName(gr.get(0)));
                        dGenryTv.setText(Tools.getArName(gr.get(0)));
                    }else {
                        for (String i:gr){
                            if (strGenre.equals("")){
                                strGenre+=i;
                            }else {
                                strGenre=strGenre+","+i;
                            }
                        }
                    }
                }

                tvGenre.setText(strGenre);
                dGenryTv.setText(strGenre);


                seasonList.clear();
                for (Integer i=0;i<serie.getSeasons().size();i++){
                    seasonList.add(serie.getSeasons().get(i).getSeason_title());
                }
                setSeasonData(seasonList,serie.getSeasons());
                new getSeriesInBackground().execute(serie.getGeners());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getMovieFC(){
        strGenre = "";
        mRef.child("Movies").orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(GONE);
                swipeRefreshLayout.setRefreshing(false);
                /*
                            if (download_check.equals("1")) {
                        download_text.setVisibility(VISIBLE);
                        downloadBt.setVisibility(VISIBLE);
                    } else {
                        download_text.setVisibility(GONE);
                        downloadBt.setVisibility(GONE);
                    }
                 */
                Movies movie = dataSnapshot.child(id).getValue(Movies.class);
                finalTitle = movie.getTitle();
                finalGenres = movie.getGeners();
                finalYear = movie.getYear();
                finalCategory = movie.getType();
                finalPicProfile = movie.getPic_profile();

                movieTitle = movie.getTitle();

                tvName.setText(movieTitle);
                tvRelease.setText("نشر في " + movie.getYear());
                tvDes.setText(movie.getDescription().replace("&nbsp;",""));
                tvDirector.setText(movie.getDirector());

                Picasso.get().load(movie.getPic_profile()).fit().centerCrop().placeholder(R.drawable.album_art_placeholder_large)
                        .into(posterIv);
                Picasso.get().load(movie.getPic_cover()).fit().centerCrop().placeholder(R.drawable.poster_placeholder)
                        .into(thumbIv);

                List<String> gr = Arrays.asList(movie.getGeners().split("\\|"));
                if (gr.size()!=0){
                    if (gr.size()==1){
                        tvGenre.setText(Tools.getArName(gr.get(0)));
                        dGenryTv.setText(Tools.getArName(gr.get(0)));
                    }else {
                    for (String i:gr){
                        if (strGenre.equals("")){
                            strGenre+=i;
                        }else {
                            strGenre=strGenre+","+i;
                        }
                    }
                    }
                }

                tvGenre.setText(strGenre);
                dGenryTv.setText(strGenre);
                listServer=movie.getStream_links();
                listExternalDownload=movie.getDownload_links();
                new getMoviesInBackground().execute(movie.getGeners());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class getMoviesInBackground extends AsyncTask<String, Integer, String> {
        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            String myGenres = params[0];
            mRef.child("Movies").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        for (DataSnapshot show :  dataSnapshot.getChildren()){

                            mix_movies_series mShow = show.getValue(mix_movies_series.class);

                            List<String> relatedGenres = Arrays.asList(mShow.getGeners().split("\\|"));
                            List<String> selectedSerieGenres = Arrays.asList(myGenres.split("\\|"));

                            for (String gr : selectedSerieGenres){
                                if (relatedGenres.contains(gr)){
                                    mShow.setName_class("movie");
                                    listRelated.add(mShow);
                                    break;
                                }
                            }
                        }
                        Collections.shuffle(listRelated);
                        relatedAdapter.notifyDataSetChanged();
                    }catch (Exception e){

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return "this string is passed to onPostExecute";
        }


        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }


    private class getSeriesInBackground extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
        }
        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {

            // get the string from params, which is an array
            String myGenres = params[0];
            mRef.child("Series").limitToFirst(50).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        for (DataSnapshot show :  dataSnapshot.getChildren()){
                            mix_movies_series mShow = show.getValue(mix_movies_series.class);
                            List<String> relatedGenres = Arrays.asList(mShow.getGeners().split("\\|"));
                            List<String> selectedSerieGenres = Arrays.asList(myGenres.split("\\|"));

                            for (String gr : selectedSerieGenres){
                                if (relatedGenres.contains(gr)){
                                    mShow.setName_class("tvseries");
                                    listRelated.add(mShow);
                                    break;
                                }
                            }


                            Log.e("CHARAF1013",mShow.getId().toString());
                        }
                        Collections.shuffle(listRelated);
                        relatedAdapter.notifyDataSetChanged();
                    }catch (Exception e){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


            return "this string is passed to onPostExecute";
        }


        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }
/*
    private void getData(String vtype, String vId) {
        strCast = "";
        strDirector = "";
        strGenre = "";

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        SingleDetailsApi api = retrofit.create(SingleDetailsApi.class);
        Call<SingleDetails> call = api.getSingleDetails(Config.API_KEY, vtype, vId);
        call.enqueue(new Callback<SingleDetails>() {
            @Override
            public void onResponse(Call<SingleDetails> call, retrofit2.Response<SingleDetails> response) {
                if (response.code() == 200){
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    SingleDetails singleDetails = response.body();
                   // mRef.child("test").setValue(singleDetails);
                    //paidControl(singleDetails.getIsPaid());
                    Log.e("Download", "size: " + singleDetails.getDownloadLinks().size());
                    Log.e("Download", "size: " + singleDetails.getTitle());
                    download_check = singleDetails.getEnableDownload();
                    castImageUrl = singleDetails.getThumbnailUrl();
                    if (download_check.equals("1")) {
                        download_text.setVisibility(VISIBLE);
                        downloadBt.setVisibility(VISIBLE);
                    } else {
                        download_text.setVisibility(GONE);
                        downloadBt.setVisibility(GONE);
                    }
                    title = singleDetails.getTitle();
                    movieTitle = title;

                    tvName.setText(title);
                    tvRelease.setText("نشر في " + singleDetails.getRelease());
                    tvDes.setText(singleDetails.getDescription().replace("&nbsp;",""));



                    Picasso.get().load(singleDetails.getPosterUrl()).fit().centerCrop().placeholder(R.drawable.album_art_placeholder_large)
                            .into(posterIv);
                    Picasso.get().load(singleDetails.getThumbnailUrl()).fit().centerCrop().placeholder(R.drawable.poster_placeholder)
                            .into(thumbIv);

                    //----director---------------
                    for (int i = 0; i < singleDetails.getDirector().size(); i++) {
                        Director director = response.body().getDirector().get(i);
                        if (i == singleDetails.getDirector().size() - 1) {
                            strDirector = strDirector + director.getName();
                        } else {
                            strDirector = strDirector + director.getName() + ", ";
                        }
                    }
                    tvDirector.setText(strDirector);

                    //----cast---------------
                    for (int i = 0; i < singleDetails.getCast().size(); i++) {
                        Cast cast = singleDetails.getCast().get(i);

                        CastCrew castCrew = new CastCrew();
                        castCrew.setId(cast.getStarId());
                        castCrew.setName(cast.getName());
                        castCrew.setUrl(cast.getUrl());
                        castCrew.setImageUrl(cast.getImageUrl());

                        castCrews.add(castCrew);

                    }
                    castCrewAdapter.notifyDataSetChanged();

                    //---genre---------------
                    /*
                    Tools t = new Tools();
                    for (int i = 0; i < singleDetails.getGenre().size(); i++) {
                        Genre genre = singleDetails.getGenre().get(i);
                        if (i == singleDetails.getCast().size() - 1) {
                            strGenre = strGenre + t.getArName(genre.getName());
                        } else {
                            if (i == singleDetails.getGenre().size() - 1) {
                                strGenre = strGenre + t.getArName(genre.getName());
                            } else {
                                strGenre = strGenre + t.getArName(genre.getName()) + " ,";
                            }
                        }
                    }


                    Tools t = new Tools();
                    for (int i = 0; i < singleDetails.getGenre().size(); i++) {
                        Genre genre = singleDetails.getGenre().get(i);
                        if (i == singleDetails.getCast().size() - 1) {
                            strGenre = strGenre + Tools.getArName(genre.getName())+", ";
                        } else {
                            if (i == singleDetails.getGenre().size() - 1) {
                                strGenre = strGenre + Tools.getArName(genre.getName()) + "";
                            } else {
                                strGenre = strGenre + Tools.getArName(genre.getName()) + ", ";
                            }
                        }
                    }
                    tvGenre.setText(strGenre);
                    dGenryTv.setText(strGenre);

                    //-----server----------
                    List<Video> serverList = new ArrayList<>();
                    serverList.addAll(singleDetails.getVideos());
                    for (int i = 0; i < serverList.size(); i++){
                        Video video = serverList.get(i);

                        CommonModels models = new CommonModels();
                        models.setTitle(video.getLabel());
                        models.setStremURL(video.getFileUrl());
                        models.setServerType(video.getFileType());

                        if (video.getFileType().equals("mp4")) {
                            V_URL = video.getFileUrl();
                        }

                        //----subtitle-----------
                        List<Subtitle> subArray = new ArrayList<>();
                        subArray.addAll(singleDetails.getVideos().get(i).getSubtitle());
                        if (subArray.size() != 0) {

                            List<SubtitleModel> list = new ArrayList<>();
                            for (int j = 0; j < subArray.size(); j++) {
                                Subtitle subtitle = subArray.get(j);
                                SubtitleModel subtitleModel = new SubtitleModel();
                                subtitleModel.setUrl(subtitle.getUrl());
                                subtitleModel.setLang(subtitle.getLanguage());
                                list.add(subtitleModel);
                            }
                            if (i == 0) {
                                listSub.addAll(list);
                            }
                            models.setListSub(list);
                        } else {
                            models.setSubtitleURL(strSubtitle);
                        }



                     //   listServer.add(models);
                    }


                    Collections.reverse(listServer);
                    if (serverAdapter != null) {
                        serverAdapter.notifyDataSetChanged();
                    }

                    //----related post---------------
                    for (int i = 0; i < singleDetails.getRelatedMovie().size(); i++) {
                        RelatedMovie relatedMovie = singleDetails.getRelatedMovie().get(i);
                        CommonModels models = new CommonModels();
                        models.setTitle(relatedMovie.getTitle());
                        models.setImageUrl(relatedMovie.getThumbnailUrl());
                        models.setId(relatedMovie.getVideosId());
                        models.setVideoType("movie");
                        models.setIsPaid(relatedMovie.getIsPaid());
                        models.setIsPaid(relatedMovie.getIsPaid());
                        listRelated.add(models);
                        Collections.shuffle(listRelated);
                    }

                    if (listRelated.size() == 0) {
                        tvRelated.setVisibility(GONE);
                    }
                    relatedAdapter.notifyDataSetChanged();

                    //----download list---------
                    listExternalDownload.clear();
                    listInternalDownload.clear();
                    for (int i = 0; i < singleDetails.getDownloadLinks().size(); i++) {
                        DownloadLink downloadLink = singleDetails.getDownloadLinks().get(i);

                        CommonModels models = new CommonModels();
                        models.setTitle(downloadLink.getLabel());
                        models.setStremURL(downloadLink.getDownloadUrl());
                        models.setFileSize(downloadLink.getFileSize());
                        models.setResulation(downloadLink.getResolution());
                        models.setInAppDownload(downloadLink.isInAppDownload());
                        if (downloadLink.isInAppDownload()) {
                            listInternalDownload.add(models);
                        } else {
                           // listExternalDownload.add(models);
                        }
                    }

                }else {
                    swipeRefreshLayout.setRefreshing(false);
                }
                sc_view.scrollTo(0, 0);
                sc_view.fullScroll(View.FOCUS_UP);
            }

            @Override
            public void onFailure(Call<SingleDetails> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    */

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
/*
    private void addComment(String videoId, String userId, final String comments) {

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        CommentApi api = retrofit.create(CommentApi.class);
        Call<PostCommentModel> call = api.postComment(Config.API_KEY, videoId, userId, comments);
        call.enqueue(new Callback<PostCommentModel>() {
            @Override
            public void onResponse(Call<PostCommentModel> call, retrofit2.Response<PostCommentModel> response) {
                if (response.body().getStatus().equals("success")){
                    rvComment.removeAllViews();
                    listComment.clear();
                    getComments();
                    etComment.setText("");
                    new ToastMsg(DetailsActivity.this).toastIconSuccess(response.body().getMessage());
                }else {
                    new ToastMsg(DetailsActivity.this).toastIconError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<PostCommentModel> call, Throwable t) {

            }
        });
    }

    private void getComments() {

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        CommentApi api = retrofit.create(CommentApi.class);
        Call<List<GetCommentsModel>> call = api.getAllComments(Config.API_KEY, id);
        call.enqueue(new Callback<List<GetCommentsModel>>() {
            @Override
            public void onResponse(Call<List<GetCommentsModel>> call, retrofit2.Response<List<GetCommentsModel>> response) {
                if (response.code() == 200) {
                    listComment.addAll(response.body());

                    commentsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<GetCommentsModel>> call, Throwable t) {

            }
        });

    }


 */
    public void hideDescriptionLayout() {
        descriptionLayout.setVisibility(GONE);
        lPlay.setVisibility(VISIBLE);
    }

    public void showSeriesLayout() {
        seriestLayout.setVisibility(VISIBLE);
    }

    public void showDescriptionLayout() {
        descriptionLayout.setVisibility(VISIBLE);
        lPlay.setVisibility(GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("ACTIVITY:::", "PAUSE" + isPlaying);

        //if (isPlaying && player != null) {
        if (player != null) {
            //Log.e("PLAY:::","PAUSE");
            player.setPlayWhenReady(false);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //castManager.removeProgressWatcher(this);

        Log.e("ACTIVITY:::", "STOP" + isPlaying);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onBackPressed() {
        //activeMovie = true;
        if (descriptionLayout.getVisibility()== GONE) {
            setPlayerNormalScreen();
            showDescriptionLayout();
            activeMovie = false;
            if (player != null) {
                player.setPlayWhenReady(false);
                player.stop();
            }
            Log.e("POORME","now activeMovie is off");

        } else {
            Log.e("POORME","else on back preesed");
            releasePlayer();
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //startPlayer();
        if (player != null) {
            if (type.equals("youtube") || type.equals("youtube-live")) {
                if (Config.YOUTUBE_VIDEO_AUTO_PLAY) {
                    player.setPlayWhenReady(true);
                } else {
                    player.setPlayWhenReady(false);
                }
            } else {
                player.setPlayWhenReady(true);
            }

        }

    }

    public void releasePlayer() {
        try{
            if (player != null) {
                player.setPlayWhenReady(true);
                player.stop();
                player.release();
                player = null;
                simpleExoPlayerView.setPlayer(null);
                simpleExoPlayerView = null;
            }
        }catch (Exception e){
            Log.e("CATCHED_error",e.getMessage());
        }

    }






/*

    public void downloadVideo(final String url) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    public void run() {
                        downloadFile(url);
                    }
                };
                handler.post(runnable);

            } else {
                requestPermission(); // Code for permission
            }
        } else {

            // Code for Below 23 API Oriented Device
            // Do next code

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                public void run() {
                    downloadFile(url);
                }
            };
            handler.post(runnable);
        }


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new ToastMsg(DetailsActivity.this).toastIconSuccess("Now You can download.");
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public void downloadFile(String url) {
        String fileName = "";
        int notificationId = new Random().nextInt(100 - 1) - 1;
        Log.d("id:", notificationId + "");

        if (url == null || url.isEmpty()) {
            return;
        }

        if (type.equals("movie")) {
            fileName = tvName.getText().toString();
        } else {
            fileName = seriesTitle + "_" + season + "_" + episod;
        }

        String path = Constants.getDownloadDir(DetailsActivity.this);

        String fileExt = url.substring(url.lastIndexOf('.')); // output like .mkv
        fileName = fileName + fileExt;

        fileName = fileName.replaceAll(" ", "_");
        fileName = fileName.replaceAll(":", "_");

        File file = new File(path, "e_" + fileName); // e_ for encode
        if (file.exists()) {
            new ToastMsg(DetailsActivity.this).toastIconError(getString(R.string.file_already_downloaded));
            return;
        }

        //download with workManager
        String dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
        Data data = new Data.Builder()
                .putString("url", url)
                .putString("dir", dir)
                .putString("fileName", fileName)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DownloadWorkManager.class)
                .setInputData(data)
                .build();

        String workId = request.getId().toString();
        Constants.workId = workId;
        WorkManager.getInstance().enqueue(request);
    }


 */

    public void hideExoControlForTv() {
        exoRewind.setVisibility(GONE);
        exoForward.setVisibility(GONE);
        liveTv.setVisibility(VISIBLE);
        seekbarLayout.setVisibility(GONE);
    }



    private void getScreenSize() {
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        sWidth = size.x;
        sHeight = size.y;
        //Toast.makeText(this, "fjiaf", Toast.LENGTH_SHORT).show();
    }

    public class RelativeLayoutTouchListener implements View.OnTouchListener {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    //touch is start
                    downX = event.getX();
                    downY = event.getY();
                    if (event.getX() < (sWidth / 2)) {

                        //here check touch is screen left or right side
                        intLeft = true;
                        intRight = false;

                    } else if (event.getX() > (sWidth / 2)) {

                        //here check touch is screen left or right side
                        intLeft = false;
                        intRight = true;
                    }
                    break;

                case MotionEvent.ACTION_UP:

                case MotionEvent.ACTION_MOVE:

                    //finger move to screen
                    float x2 = event.getX();
                    float y2 = event.getY();

                    diffX = (long) (Math.ceil(event.getX() - downX));
                    diffY = (long) (Math.ceil(event.getY() - downY));

                    if (Math.abs(diffY) > Math.abs(diffX)) {
                        if (intLeft) {
                            //if left its for brightness

                            if (downY < y2) {
                                //down swipe brightness decrease
                            } else if (downY > y2) {
                                //up  swipe brightness increase
                            }

                        } else if (intRight) {

                            //if right its for audio
                            if (downY < y2) {
                                //down swipe volume decrease
                                mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

                            } else if (downY > y2) {
                                //up  swipe volume increase
                                mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            }
                        }
                    }
            }
            return true;
        }


    }
/*
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        startAppAd.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        startAppAd.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);

    }

 */
}

