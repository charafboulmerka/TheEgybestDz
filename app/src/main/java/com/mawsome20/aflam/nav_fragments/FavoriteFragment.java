package com.mawsome20.aflam.nav_fragments;

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
import com.mawsome20.aflam.adapters.All_MoviesSeriesAdapter;
import com.mawsome20.aflam.network.model.AdsConfig;
import com.mawsome20.aflam.new_conception.models.mix_movies_series;
import com.mawsome20.aflam.utils.PreferenceUtils;
import com.mawsome20.aflam.utils.ApiResources;
import com.mawsome20.aflam.utils.ads.BannerAds;
import com.mawsome20.aflam.utils.Constants;
import com.mawsome20.aflam.utils.NetworkInst;
import com.mawsome20.aflam.utils.SpacingItemDecoration;
import com.mawsome20.aflam.utils.ToastMsg;
import com.mawsome20.aflam.utils.Tools;
import com.noqoush.adfalcon.android.sdk.ADFView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private All_MoviesSeriesAdapter mAdapter;
    private List<mix_movies_series> list =new ArrayList<>();

    private ApiResources apiResources;

    private boolean isLoading=false;
    private ProgressBar progressBar;
    private int pageCount=1,checkPass=0;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoItem;

    private MainActivity activity;
    private LinearLayout searchRootLayout;

    private CardView searchBar;
    private ImageView menuIv, searchIv;
    private TextView pageTitle;

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    private RelativeLayout adView;
    private String userId = "";
    InMobiBanner banner;
    ADFView AD_FALCON;
    private DatabaseReference mRef;
    private PreferenceUtils mUtils;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();

        return inflater.inflate(R.layout.fragment_movies,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setTitle(getResources().getString(R.string.favorite));
        mRef = FirebaseDatabase.getInstance().getReference();
        initComponent(view);

        pageTitle.setText(getResources().getString(R.string.favorite));


        if (activity.isDark) {
            pageTitle.setTextColor(activity.getResources().getColor(R.color.white));
            searchBar.setCardBackgroundColor(activity.getResources().getColor(R.color.black_window_light));
            menuIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_menu));
            searchIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_search_white));
        }

        //loadAd();

    }


    private void initComponent(View view) {

        apiResources=new ApiResources();
        //banner=(InMobiBanner) view.findViewById(R.id.banner);
        swipeRefreshLayout=view.findViewById(R.id.swipe_layout);
        coordinatorLayout=view.findViewById(R.id.coordinator_lyt);
        progressBar=view.findViewById(R.id.item_progress_bar);
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        tvNoItem=view.findViewById(R.id.tv_noitem);
        adView=view.findViewById(R.id.adView);
        AD_FALCON = view.findViewById(R.id.adFalconView);

        searchRootLayout    = view.findViewById(R.id.search_root_layout);
        searchBar           = view.findViewById(R.id.search_bar);
        menuIv              = view.findViewById(R.id.bt_menu);
        pageTitle           = view.findViewById(R.id.page_title_tv);
        searchIv            = view.findViewById(R.id.search_iv);


        userId = (new PreferenceUtils(getContext())).getUserId();

        //----favorite's recycler view-----------------
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(getActivity(), 0), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new All_MoviesSeriesAdapter(getContext(), list,"");
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !isLoading) {

                    pageCount=pageCount+1;
                    isLoading = true;

                    progressBar.setVisibility(View.VISIBLE);
                    getFavOnScroll();
                    //getData(userId,pageCount);
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.removeAllViews();
                pageCount=1;
                list.clear();
                mAdapter.notifyDataSetChanged();

                if (new NetworkInst(getContext()).isNetworkAvailable()){
                    getFavFirstTime();
                    //getData(userId,pageCount);
                }else {
                    tvNoItem.setText(getString(R.string.no_internet));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        if (new NetworkInst(getContext()).isNetworkAvailable()){
            getFavFirstTime();
           // getData(userId,pageCount);
        }else {
            tvNoItem.setText(getString(R.string.no_internet));
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
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
        AdsConfig adsConfig = new AdsConfig();
        if (adsConfig.getAdsEnable().equals("1")) {

            if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADMOB)) {
                BannerAds.ShowAdmobBannerAds(activity, adView);

            } else if (adsConfig.getMobileAdsNetwork().equals(Constants.START_APP)) {
                BannerAds.showStartAppBanner(activity, adView,"old_method",null);

            } else if(adsConfig.getMobileAdsNetwork().equals(Constants.NETWORK_AUDIENCE)) {
                BannerAds.showFANBanner(getContext(), adView);
            }
            else if(adsConfig.getMobileAdsNetwork().equals(Constants.INMOBI)){
                BannerAds.showInmobiBanner(banner);
            }
            else if(adsConfig.getMobileAdsNetwork().equals(Constants.ADFALCON)) {
                BannerAds.showAdFalconBanner(activity,AD_FALCON);
            }
        }
    }

    private String oldKey;

    private void getFavFirstTime(){
        String user_uid = (new  PreferenceUtils(getContext())).getUserId();
        mRef.child("Users").child(user_uid).child("Fav").limitToFirst(Integer.parseInt(getString(R.string.limit_fc_data))).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        tvNoItem.setText("لم يتم العثور على اي نتائج في قائمتك المفضلة");
                    }else {
                        coordinatorLayout.setVisibility(View.GONE);
                    }

                    for (DataSnapshot obj :  dataSnapshot.getChildren()){
                        oldKey = obj.getKey();
                        mix_movies_series mObj = obj.getValue(mix_movies_series.class);
                        list.add(obj.getValue(mix_movies_series.class));
                        Log.e("CHARAF1013",mObj.getId().toString());
                    }
                    mAdapter.notifyDataSetChanged();
                }catch (Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                isLoading=false;
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                new ToastMsg(activity).toastIconError("لم يتم العثور على اي نتائج في قائمتك المفضلة");
                if (pageCount==1){
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getFavOnScroll(){
        Log.e("CHARAF1013","---------------------");
        Log.e("CHARAF1013",oldKey);
        String user_uid =  (new PreferenceUtils(getContext())).getUserId();
        mRef.child("Users").child(user_uid).child("Fav").orderByKey().startAt(oldKey).limitToFirst(Integer.parseInt(getString(R.string.limit_fc_data))).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    Boolean goNext = true;
                    for (DataSnapshot obj :  dataSnapshot.getChildren()){
                        if (!goNext){
                            oldKey = obj.getKey();
                            mix_movies_series mObj = obj.getValue(mix_movies_series.class);
                            list.add(obj.getValue(mix_movies_series.class));
                            Log.e("CHARAF1013",mObj.getId().toString());
                        }
                        goNext = false;
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
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                new ToastMsg(activity).toastIconError("لم يتم العثور على اي نتائج في قائمتك المفضلة");

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