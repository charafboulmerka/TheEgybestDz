package com.mawsome20.aflam20;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inmobi.ads.InMobiBanner;
import com.mawsome20.aflam20.adapters.All_MoviesSeriesAdapter;
import com.mawsome20.aflam20.network.model.AdsConfig;
import com.mawsome20.aflam20.new_conception.models.mix_movies_series;
import com.mawsome20.aflam20.utils.ads.BannerAds;
import com.mawsome20.aflam20.utils.Constants;
import com.mawsome20.aflam20.utils.NetworkInst;
import com.mawsome20.aflam20.utils.SpacingItemDecoration;
import com.mawsome20.aflam20.utils.Tools;
import com.noqoush.adfalcon.android.sdk.ADFView;

import java.util.ArrayList;
import java.util.List;

public class itemByGenreAndCategory extends AppCompatActivity {


    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private All_MoviesSeriesAdapter mAdapter;
    private List<mix_movies_series> list =new ArrayList<>();

    private boolean isLoading=false;
    private ProgressBar progressBar;
    private int pageCount=1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String id="",type="";
    private CoordinatorLayout coordinatorLayout;
    private TextView tvNoItem;
    private RelativeLayout adView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<String> moviesNames = new ArrayList<String>();
    private DatabaseReference mRef;
    InMobiBanner bannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("dark", false);
        mRef = FirebaseDatabase.getInstance().getReference();

        if (isDark) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (!isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        setSupportActionBar(toolbar);

        //---analytics-----------
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "movie_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adView=findViewById(R.id.adView);
        coordinatorLayout=findViewById(R.id.coordinator_lyt);
        progressBar=findViewById(R.id.item_progress_bar);
        shimmerFrameLayout=findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
         //bannerAd = (InMobiBanner)findViewById(R.id.banner);

        swipeRefreshLayout=findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"الرجاء السحب للأسفل",Toast.LENGTH_LONG).show();
                }

            }
        });

        tvNoItem=findViewById(R.id.tv_noitem);


        //----movie's recycler view-----------------
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        mAdapter = new All_MoviesSeriesAdapter(this, list,"");
        recyclerView.setAdapter(mAdapter);
        /*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !isLoading) {

                    pageCount=pageCount+1;
                    isLoading = true;

                    progressBar.setVisibility(View.VISIBLE);
                    initData();
                   // getMovieByGenreId(id,pageCount);
                }
            }
        });


         */
        id = getIntent().getStringExtra("id");
        type =getIntent().getStringExtra("type");

        if (new NetworkInst(this).isNetworkAvailable()){
            initData();
        }else {
            tvNoItem.setText(getString(R.string.no_internet));
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
        }

        /*
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(),"REFRESHED",Toast.LENGTH_LONG).show();
                coordinatorLayout.setVisibility(View.GONE);

                pageCount=1;

                list.clear();
               // moviesNames.clear();
                recyclerView.removeAllViews();
                mAdapter.notifyDataSetChanged();

                if (new NetworkInst(ItemMovieActivity.this).isNetworkAvailable()){
                    initData();
                }else
                    {
                    tvNoItem.setText(getString(R.string.no_internet));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });

         */

      //  loadAd();
    }


    private void initData(){
        new getMoviesInBackground().execute("");
        new getSeriesInBackground().execute("");
        /*
        if (id == null){
            Log.e("HERE_ID","CALL 1");

            getMovie(pageCount);
        }else if (type.equals("country")){
            Log.e("HERE_ID","CALL 2");

            getMovieByCountryId(id, pageCount);
        }else {
            Log.e("HERE_ID","CALL 3");

            getMovieByGenreId(id, pageCount);
        }

         */

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
            String myString = params[0];
            mRef.child("Movies").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        isLoading=false;
                        progressBar.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                        if (!dataSnapshot.exists() && pageCount==1){
                            coordinatorLayout.setVisibility(View.VISIBLE);
                        }else {
                            coordinatorLayout.setVisibility(View.GONE);
                        }
                        for (DataSnapshot show :  dataSnapshot.getChildren()){
                            //oldKey = movie.getKey();
                            mix_movies_series mShow = show.getValue(mix_movies_series.class);
                            if (type.equals("genre")){
                                if (mShow.getGeners().contains(id)){
                                    mShow.setName_class("movie");
                                    list.add(mShow);
                                }
                            }else {
                                if (mShow.getType().contains(id)){
                                    mShow.setName_class("movie");
                                    list.add(mShow);
                                }
                            }


                            Log.e("CHARAF1013",mShow.getId().toString());
                        }
                        mAdapter.notifyDataSetChanged();
                    }catch (Exception e){}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    isLoading=false;
                    progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    if (pageCount==1){
                        coordinatorLayout.setVisibility(View.VISIBLE);
                    }
                }
            });


            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
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
            String myString = params[0];
            mRef.child("Series").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        isLoading=false;
                        progressBar.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                        if (!dataSnapshot.exists() && pageCount==1){
                            coordinatorLayout.setVisibility(View.VISIBLE);
                        }else {
                            coordinatorLayout.setVisibility(View.GONE);
                        }
                        for (DataSnapshot show :  dataSnapshot.getChildren()){
                            //oldKey = movie.getKey();
                            mix_movies_series mShow = show.getValue(mix_movies_series.class);
                            if (type.equals("genre")){
                                if (mShow.getGeners().contains(id)){
                                    mShow.setName_class("tvseries");
                                    list.add(mShow);
                                }
                            }else {
                                if (mShow.getType().contains(id)){
                                    mShow.setName_class("tvseries");
                                    list.add(mShow);
                                }
                            }


                            Log.e("CHARAF1013",mShow.getId().toString());
                        }
                        mAdapter.notifyDataSetChanged();
                    }catch (Exception e){}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    isLoading=false;
                    progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    if (pageCount==1){
                        coordinatorLayout.setVisibility(View.VISIBLE);
                    }
                }
            });


            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }



    private void loadAd(){
        AdsConfig adsConfig = new AdsConfig();
        if (adsConfig.getAdsEnable().equals("1")) {

            if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADMOB)) {
                BannerAds.ShowAdmobBannerAds(itemByGenreAndCategory.this, adView);

            } else if (adsConfig.getMobileAdsNetwork().equals(Constants.START_APP)) {
                BannerAds.showStartAppBanner(itemByGenreAndCategory.this, adView,"old_method",null);

            } else if(adsConfig.getMobileAdsNetwork().equals(Constants.NETWORK_AUDIENCE)) {
                BannerAds.showFANBanner(itemByGenreAndCategory.this, adView);
            }
            else if(adsConfig.getMobileAdsNetwork().equals(Constants.INMOBI)){
                BannerAds.showInmobiBanner(bannerAd);
            }
            else if(adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADFALCON)){
                ADFView AD_FALCON = findViewById(R.id.adFalconView);
                BannerAds.showAdFalconBanner(this,AD_FALCON);
            }

        }
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

}
