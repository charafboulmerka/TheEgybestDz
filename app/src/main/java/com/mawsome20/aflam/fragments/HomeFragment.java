package com.mawsome20.aflam.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inmobi.ads.InMobiBanner;
import com.mawsome20.aflam.itemByGenreAndCategory;
import com.mawsome20.aflam.MainActivity;
import com.mawsome20.aflam.R;
import com.mawsome20.aflam.adapters.CountryAdapter;
import com.mawsome20.aflam.adapters.GenreCategoryAdapter;
import com.mawsome20.aflam.adapters.GenreHomeAdapter;
import com.mawsome20.aflam.adapters.HomePageMoviesAdapter;
import com.mawsome20.aflam.adapters.HomePageSeriesAdapter;
import com.mawsome20.aflam.adapters.SliderAdapter;
import com.mawsome20.aflam.models.CommonModels;
import com.mawsome20.aflam.models.GenreModel;
import com.mawsome20.aflam.nav_fragments.GenreFragment;
import com.mawsome20.aflam.new_conception.models.Movies;
import com.mawsome20.aflam.new_conception.models.Series;
import com.mawsome20.aflam.new_conception.models.mix_movies_series;
import com.mawsome20.aflam.utils.NetworkInst;
import com.mawsome20.aflam.utils.PreferenceUtils;
import com.mawsome20.aflam.utils.Tools;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.noqoush.adfalcon.android.sdk.ADFView;
import com.startapp.android.publish.ads.banner.Banner;
/*
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.StartAppAd;


 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;


public class HomeFragment extends Fragment {

    CardSliderViewPager cViewPager;
    private ArrayList<mix_movies_series> listSlider = new ArrayList<>();
    private Timer timer;

    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerViewMovie, recyclerViewTvSeries, recyclerViewGenre;
    private RecyclerView genreRv;
    private RecyclerView countryRv;
    private GenreCategoryAdapter genreAdapter;
    private CountryAdapter countryAdapter;
    private RelativeLayout genreLayout, countryLayout;
    private HomePageMoviesAdapter adapterMovie;
    private HomePageSeriesAdapter  adapterSeries;

    // private LiveTvHomeAdapter adapterTv;
    private List<Movies> listMovie = new ArrayList<>();
    private List<Series> listSeries = new ArrayList<>();
    private List<CommonModels> genreList = new ArrayList<>();
    private List<String> genreListNames = new ArrayList<>();
    private Button btnMoreMovie, btnMoreTv, btnMoreSeries;

    private TextView tvNoItem;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView scrollView;

    private RelativeLayout adView, adView1;
    private List<GenreModel> listGenre = new ArrayList<>();

    private GenreHomeAdapter genreHomeAdapter;
    private View sliderLayout;

    private MainActivity activity;
    private LinearLayout searchRootLayout;

    private CardView searchBar;
    private ImageView menuIv, searchIv;
    private TextView pageTitle;
    final List<String> categories_array =  Arrays.asList("أكشن","مغامرة","كوميدي","جريمة","دراما","خيال","رعب","لـغز","المزيد");
    final List<Integer> categories_icons_array =  Arrays.asList(R.drawable.ic_genre_action,R.drawable.ic_genre_adventurer,R.drawable.ic_genre_comedy,R.drawable.ic_genre_crime,R.drawable.ic_genre_drama,R.drawable.ic_genre_fantasy,R.drawable.ic_genre_horror,R.drawable.ic_genre_mystery,R.drawable.ic_genre_more);
    private DatabaseReference mRef;
    BoomMenuButton bmb;
    private Banner startAppBanner;
    private Banner startAppBanner1;
    private LinearLayout startAppLayoutBanner;
    private InMobiBanner inmobi_bannerAd;
    private InMobiBanner inmobi_bannerAd2;
    private ADFView AD_FALCON;
    private ADFView AD_FALCON2;
    SliderAdapter sliderAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();

        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRef = FirebaseDatabase.getInstance().getReference();
        bmb = view.findViewById(R.id.bmb);
        setFloatBtn();
        adView              = view.findViewById(R.id.adView);
        adView1             = view.findViewById(R.id.adView1);
        startAppBanner1     =view.findViewById(R.id.startAppBanner1);
        startAppBanner     =view.findViewById(R.id.startAppBanner);
        startAppLayoutBanner=view.findViewById(R.id.startapp_linearlayout);

        btnMoreSeries       = view.findViewById(R.id.btn_more_series);
        btnMoreMovie        = view.findViewById(R.id.btn_more_movie);
        shimmerFrameLayout  = view.findViewById(R.id.shimmer_view_container);
        tvNoItem            = view.findViewById(R.id.tv_noitem);
        coordinatorLayout   = view.findViewById(R.id.coordinator_lyt);
        swipeRefreshLayout  = view.findViewById(R.id.swipe_layout);
        scrollView          = view.findViewById(R.id.scrollView);
        sliderLayout        = view.findViewById(R.id.slider_layout);
        genreRv             = view.findViewById(R.id.genre_rv);
        countryRv           = view.findViewById(R.id.country_rv);
        genreLayout         = view.findViewById(R.id.genre_layout);
        countryLayout       = view.findViewById(R.id.country_layout);
        cViewPager          = view.findViewById(R.id.c_viewPager);
        searchRootLayout    = view.findViewById(R.id.search_root_layout);
        searchBar           = view.findViewById(R.id.search_bar);
        menuIv              = view.findViewById(R.id.bt_menu);
        pageTitle           = view.findViewById(R.id.page_title_tv);
        searchIv           = view.findViewById(R.id.search_iv);

        /*
        inmobi_bannerAd = (InMobiBanner)view.findViewById(R.id.banner);
        inmobi_bannerAd2 = (InMobiBanner)view.findViewById(R.id.banner2);
        AD_FALCON = (ADFView)view.findViewById(R.id.adFalconView);
        AD_FALCON2 = (ADFView)view.findViewById(R.id.adFalconView1);




        if (db.getConfigurationData().getAppConfig().getGenreVisible()) {
            genreLayout.setVisibility(View.VISIBLE);
        }
        if (db.getConfigurationData().getAppConfig().getCountryVisible()) {
            countryLayout.setVisibility(View.VISIBLE);
        }
*/
        pageTitle.setText(getResources().getString(R.string.home));

        if (activity.isDark) {
            pageTitle.setTextColor(activity.getResources().getColor(R.color.white));
            searchBar.setCardBackgroundColor(activity.getResources().getColor(R.color.black_window_light));
            menuIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_menu));
            searchIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_search_white));
        }

        //----init timer slider--------------------
        timer = new Timer();

        //----btn click-------------
        btnClick();



        //----movie's recycler view-----------------
        recyclerViewMovie = view.findViewById(R.id.recyclerView);
        recyclerViewMovie.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewMovie.setHasFixedSize(true);
        recyclerViewMovie.setNestedScrollingEnabled(false);
        adapterMovie = new HomePageMoviesAdapter(getContext(), listMovie);
        recyclerViewMovie.setAdapter(adapterMovie);

        //----series's recycler view-----------------
        recyclerViewTvSeries = view.findViewById(R.id.recyclerViewTvSeries);
        recyclerViewTvSeries.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTvSeries.setHasFixedSize(true);
        recyclerViewTvSeries.setNestedScrollingEnabled(false);
        adapterSeries = new HomePageSeriesAdapter(getActivity(), listSeries);
        recyclerViewTvSeries.setAdapter(adapterSeries);

        //----genre's recycler view--------------------
        recyclerViewGenre = view.findViewById(R.id.recyclerView_by_genre);
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewGenre.setHasFixedSize(true);
        recyclerViewGenre.setNestedScrollingEnabled(false);
        genreHomeAdapter = new GenreHomeAdapter(getContext(), listGenre);
        recyclerViewGenre.setAdapter(genreHomeAdapter);

        shimmerFrameLayout.startShimmer();


        getSlider();

        if (new NetworkInst(getContext()).isNetworkAvailable()) {
            getHomeContentViaFirebase();
           // getHomeContent();

        } else {
            tvNoItem.setText(getString(R.string.no_internet));
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclerViewMovie.removeAllViews();
                recyclerViewTvSeries.removeAllViews();
                recyclerViewGenre.removeAllViews();
                genreRv.removeAllViews();
                countryRv.removeAllViews();

                genreList.clear();
                genreListNames.clear();
                listMovie.clear();
                listSeries.clear();
                listSlider.clear();
                listGenre.clear();


                if (new NetworkInst(getContext()).isNetworkAvailable()) {
                   // getHomeContent();
                    getHomeContentViaFirebase();

                } else {
                    tvNoItem.setText(getString(R.string.no_internet));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
            }
        });


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateSearchBar(false);
                    if (bmb.getVisibility()!=View.VISIBLE)
                    bmb.setVisibility(View.VISIBLE);

                }
                if (scrollY > oldScrollY) { // down
                    animateSearchBar(true);
                    if (bmb.getVisibility()==View.VISIBLE)
                    bmb.setVisibility(View.GONE);

                }
            }
        });

        //getAdDetails();
    }

    private void getSlider(){
        sliderLayout.setVisibility(View.VISIBLE);
        com.mawsome20.aflam.new_conception.models.Config mConfig = (new PreferenceUtils(getContext())).getConfig();
        if (mConfig.getAuto_slider().equals("true")){
            new getMoviesInBackground().execute("");
            new getSeriesInBackground().execute("");
        }else {
            mRef.child("Slider").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        swipeRefreshLayout.setRefreshing(false);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        coordinatorLayout.setVisibility(View.GONE);

                        //latest movies data
                        for (DataSnapshot slider :  dataSnapshot.getChildren()){
                            mix_movies_series mSlider = slider.getValue(mix_movies_series.class);
                            if (mSlider.getId().contains("https://") || mSlider.getId().contains("http://")){
                                listSlider.add(0,mSlider);
                            }else {
                                listSlider.add(mSlider);
                            }

                            Log.e("CHARAF1013",mSlider.getId().toString());
                        }
                        Collections.reverse(listSlider);
                        sliderAdapter.notifyDataSetChanged();

                    }catch (Exception e){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    swipeRefreshLayout.setRefreshing(false);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
            });
        }
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
            mRef.child("Movies").limitToLast(Integer.valueOf(getString(R.string.limit_fc_data))).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                            for (DataSnapshot show :  dataSnapshot.getChildren()){
                                //oldKey = movie.getKey();
                                mix_movies_series mShow = show.getValue(mix_movies_series.class);
                                mShow.setName_class("movie");
                                listSlider.add(mShow);
                                Log.e("CHARAF2020",mShow.getId().toString());
                            }
                            sliderAdapter = new SliderAdapter(listSlider,getContext());
                            cViewPager.setAdapter(sliderAdapter);
                            sliderAdapter.notifyDataSetChanged();
                        }catch (Exception e){

                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
            mRef.child("Series").limitToLast(Integer.valueOf(getString(R.string.limit_fc_data))).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        for (DataSnapshot show :  dataSnapshot.getChildren()){
                            //oldKey = movie.getKey();
                            mix_movies_series mShow = show.getValue(mix_movies_series.class);
                            mShow.setName_class("tvseries");
                            listSlider.add(mShow);


                            Log.e("CHARAF1013",mShow.getId().toString());
                        }
                        sliderAdapter = new SliderAdapter(listSlider,getContext());
                        cViewPager.setAdapter(sliderAdapter);
                        sliderAdapter.notifyDataSetChanged();
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





    private void getHomeContentViaFirebase(){
        mRef.child("Movies").limitToLast(40).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    swipeRefreshLayout.setRefreshing(false);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    coordinatorLayout.setVisibility(View.GONE);

                    //latest movies data
                    for (DataSnapshot movie :  dataSnapshot.getChildren()){
                        Movies mMovie = movie.getValue(Movies.class);
                        listMovie.add(movie.getValue(Movies.class));
                        Log.e("CHARAF1013",mMovie.getId().toString());
                    }

                    adapterMovie.notifyDataSetChanged();
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }
        });


        mRef.child("Series").limitToLast(40).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    swipeRefreshLayout.setRefreshing(false);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    coordinatorLayout.setVisibility(View.GONE);



                    //latest movies data
                    for (DataSnapshot serire :  dataSnapshot.getChildren()){
                        Series mMovie = serire.getValue(Series.class);
                        listSeries.add(serire.getValue(Series.class));
                        Log.e("CHARAF1013",mMovie.getId().toString());
                    }

                    adapterSeries.notifyDataSetChanged();
                }catch (Exception e){

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }
        });

    }
/*
    private void loadAd() {
        AdsConfig adsConfig = new DatabaseHelper(getContext()).getConfigurationData().getAdsConfig();
        if (adsConfig.getAdsEnable().equals("1")) {

            if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADMOB)) {

                BannerAds.ShowAdmobBannerAds(getContext(), adView);
                BannerAds.ShowAdmobBannerAds(getContext(), adView1);

            } else if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.START_APP)) {
                startAppLayoutBanner.setVisibility(View.VISIBLE);
                BannerAds.showStartAppBanner(activity,null,"new_method",startAppBanner);
                BannerAds.showStartAppBanner(activity,null,"new_method",startAppBanner1);



            } else if(adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.NETWORK_AUDIENCE)) {
                BannerAds.showFANBanner(getContext(), adView);
                BannerAds.showFANBanner(getContext(), adView1);
            }
            else if(adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.INMOBI)){

                BannerAds.showInmobiBanner(inmobi_bannerAd);
                BannerAds.showInmobiBanner(inmobi_bannerAd2);
            }
            else if(adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADFALCON)){
                BannerAds.showAdFalconBanner(getContext(),AD_FALCON);
                BannerAds.showAdFalconBanner(getContext(),AD_FALCON2);
            }
        }
    }


 */
protected void loadFragment(Fragment fragment) {
    // Begin the transaction
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    // Replace the contents of the container with the new fragment
    ft.replace(R.id.fragment_container, fragment);
    // or ft.add(R.id.your_placeholder, new ABCFragment());
    // Complete the changes added above
    ft.commit();
}

    private void btnClick() {

        btnMoreMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MoviesFragment());
                //Intent intent = new Intent(getContext(), ItemMovieActivity.class);
                // intent.putExtra("title", "Movies");
                //getActivity().startActivity(intent);
            }
        });


        btnMoreSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new TvSeriesFragment());
                // Intent intent = new Intent(getContext(), ItemSeriesActivity.class);
              //  intent.putExtra("title", "TV Series");
              //  getActivity().startActivity(intent);
            }
        });

    }
/*
    private void getAdDetails() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        AdsConfig adsConfig = db.getConfigurationData().getAdsConfig();

        new GDPRChecker()
                .withContext(activity)
                .withPrivacyUrl(Config.TERMS_URL) // your privacy url
                .withPublisherIds(adsConfig.getAdmobAppId()) // your admob account Publisher id
                //.withTestMode("9424DF76F06983D1392E609FC074596C") // remove this on real project
                .check();

     //   loadAd();
    }

 */

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

        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    boolean isSearchBarHide = false;

    private void animateSearchBar(final boolean hide) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return;
        isSearchBarHide = hide;
        int moveY = hide ? -(2 * searchRootLayout.getHeight()) : 0;
        searchRootLayout.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();

    }

    void setFloatBtn(){
        Integer img_id=R.drawable.ic_play;
        String txt="";
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {

            img_id=categories_icons_array.get(i);
            txt=categories_array.get(i);

            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(img_id)
                   // .textSize(17)
                    .typeface(Typeface.DEFAULT_BOLD)
                    .normalText(txt);


            bmb.addBuilder(builder.listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    if (index==8){
                        Fragment f = new GenreFragment();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,f)
                                .commit();
                    }
                    else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String mId="";
                                if (index==0){
                                    mId="action";
                                }else if(index==1){
                                    mId="adventure";
                                }else if(index==2){
                                    mId="comedy";
                                }else if(index==3){
                                    mId="crime";
                                }else if(index==4){
                                    mId="drama";
                                }else if(index==5){
                                    mId="fantasy";
                                }else if(index==6){
                                    mId="horror";
                                }else if(index==7){
                                    mId="mystery";
                                }

                                Intent intent=new Intent(getContext(), itemByGenreAndCategory.class);
                                intent.putExtra("id",mId);

                                intent.putExtra("title",new Tools().getArName(mId));
                                intent.putExtra("type","genre");

                                getContext().startActivity(intent);

                            }
                        },500);

                    }
                }
            }));
        }
    }


}
