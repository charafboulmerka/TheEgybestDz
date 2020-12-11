package com.mawsome20.aflam.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inmobi.ads.InMobiBanner;
import com.mawsome20.aflam.MainActivity;
import com.mawsome20.aflam.R;
import com.mawsome20.aflam.adapters.All_SeriesAdapter;
import com.mawsome20.aflam.network.model.AdsConfig;
import com.mawsome20.aflam.new_conception.models.Series;
import com.mawsome20.aflam.utils.ApiResources;
import com.mawsome20.aflam.utils.ads.BannerAds;
import com.mawsome20.aflam.utils.Constants;
import com.mawsome20.aflam.utils.NetworkInst;
import com.mawsome20.aflam.utils.SpacingItemDecoration;
import com.mawsome20.aflam.utils.Tools;
import com.noqoush.adfalcon.android.sdk.ADFView;
import com.startapp.android.publish.ads.banner.Banner;
/*
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;

 */

import java.util.ArrayList;

public class TvSeriesFragment extends Fragment {

    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private All_SeriesAdapter mAdapter;
    private ArrayList<Series> mList =new ArrayList<>();

    private ApiResources apiResources;

    private boolean isLoading=false;
    private ProgressBar progressBar;
    private int pageCount=1;
    private SwipeRefreshLayout swipeRefreshLayout;

    private CoordinatorLayout coordinatorLayout;
    private TextView tvNoItem;
    //private RelativeLayout adView;
    private Banner startAppBanner;
    private LinearLayout startAppLayoutBanner;


    private LinearLayout searchRootLayout;

    private CardView searchBar;
    private ImageView menuIv, searchIv;
    private TextView pageTitle;

    private MainActivity activity;

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    private InMobiBanner bannerAd;
    private ADFView AD_FALCON;
    private RelativeLayout adView;
    private DatabaseReference mRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();
        mRef = FirebaseDatabase.getInstance().getReference();

        return inflater.inflate(R.layout.fragment_tvseries,null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle(getResources().getString(R.string.tv_series));

        initComponent(view);
        pageTitle.setText(getResources().getString(R.string.tv_series));

        if (activity.isDark) {
            pageTitle.setTextColor(activity.getResources().getColor(R.color.white));
            searchBar.setCardBackgroundColor(activity.getResources().getColor(R.color.black_window_light));
            menuIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_menu));
            searchIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_search_white));
        }


    }

    private void initComponent(View view) {

        apiResources=new ApiResources();

        startAppBanner=view.findViewById(R.id.startAppBanner);
        startAppLayoutBanner=view.findViewById(R.id.startapp_linearlayout);
        adView=view.findViewById(R.id.adView);

        progressBar=view.findViewById(R.id.item_progress_bar);
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        swipeRefreshLayout=view.findViewById(R.id.swipe_layout);
        coordinatorLayout=view.findViewById(R.id.coordinator_lyt);
        tvNoItem=view.findViewById(R.id.tv_noitem);
        //bannerAd = (InMobiBanner)view.findViewById(R.id.banner);
        AD_FALCON = view.findViewById(R.id.adFalconView);

        searchRootLayout    = view.findViewById(R.id.search_root_layout);
        searchBar           = view.findViewById(R.id.search_bar);
        menuIv              = view.findViewById(R.id.bt_menu);
        pageTitle           = view.findViewById(R.id.page_title_tv);
        searchIv            = view.findViewById(R.id.search_iv);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(getActivity(), 0), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new All_SeriesAdapter(getContext(), mList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !isLoading) {

                    coordinatorLayout.setVisibility(View.GONE);

                    pageCount=pageCount+1;
                    isLoading = true;

                    progressBar.setVisibility(View.VISIBLE);

                  //  getTvSeriesData(pageCount);
                    getSeriesDataOnScroll();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    animateSearchBar(true);
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    animateSearchBar(false);
                    controlsVisible = true;
                    scrolledDistance = 0;
                }

                if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                    scrolledDistance += dy;
                }


            }
        });

        if (new NetworkInst(getContext()).isNetworkAvailable()){
            //getTvSeriesData(pageCount);
            getSeriesDataFirstTime();
        }else {
            tvNoItem.setText(getResources().getString(R.string.no_internet));
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                pageCount=1;
                coordinatorLayout.setVisibility(View.GONE);
                mList.clear();
                recyclerView.removeAllViews();
                mAdapter.notifyDataSetChanged();
                if (new NetworkInst(getContext()).isNetworkAvailable()){
                    //getTvSeriesData(pageCount);
                    getSeriesDataFirstTime();
                }else {
                    tvNoItem.setText(getResources().getString(R.string.no_internet));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });

      //  loadAd();

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

    private void loadAd(){
        AdsConfig adsConfig = new AdsConfig();
        if (adsConfig.getAdsEnable().equals("1")) {

            if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADMOB)) {
                BannerAds.ShowAdmobBannerAds(activity, adView);

            } else if (adsConfig.getMobileAdsNetwork().equals(Constants.START_APP)) {
                startAppLayoutBanner.setVisibility(View.VISIBLE);
                BannerAds.showStartAppBanner(activity,null,"new_method",startAppBanner);
            } else if(adsConfig.getMobileAdsNetwork().equals(Constants.NETWORK_AUDIENCE)) {
                BannerAds.showFANBanner(getContext(), adView);
            }
            else if(adsConfig.getMobileAdsNetwork().equals(Constants.INMOBI)){
                BannerAds.showInmobiBanner(bannerAd);
            }
            else if(adsConfig.getMobileAdsNetwork().equals(Constants.ADFALCON)) {
                BannerAds.showAdFalconBanner(activity,AD_FALCON);
            }

        }
    }

    private String oldKey;
    private void getSeriesDataFirstTime(){
        mRef.child("Series").limitToLast(Integer.parseInt(getString(R.string.limit_fc_data))).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Integer order = 0;
                    for (DataSnapshot serie :  dataSnapshot.getChildren()){
                        if (order.equals(0)){
                            oldKey = serie.getKey();
                        }
                        Series mSerie = serie.getValue(Series.class);
                        mList.add(mSerie);
                        Log.e("CHARAF1013",mSerie.getId().toString());
                        order+=1;
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
    }

    private void getSeriesDataOnScroll(){
        Log.e("CHARAF1013","---------------------");
        Log.e("CHARAF1013",oldKey);
        mRef.child("Series").orderByKey().endAt(oldKey).limitToLast(Integer.parseInt(getString(R.string.limit_fc_data))).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    Integer order = 0;
                    for (DataSnapshot serie :  dataSnapshot.getChildren()) {
                        if (order<9){
                            if (order.equals(0)){
                                oldKey = serie.getKey();
                            }
                            Series mSerie = serie.getValue(Series.class);
                            mList.add(mSerie);
                            Log.e("CHARAF1013", mSerie.getId().toString());
                            order += 1;
                        }

                    }

                    mAdapter.notifyDataSetChanged();
                    isLoading=false;
                    progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
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
    }




    boolean isSearchBarHide = false;

    private void animateSearchBar(final boolean hide) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return;
        isSearchBarHide = hide;
        int moveY = hide ? -(2 * searchRootLayout.getHeight()) : 0;
        searchRootLayout.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }
}
