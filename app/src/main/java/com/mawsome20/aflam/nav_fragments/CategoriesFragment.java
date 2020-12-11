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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mawsome20.aflam.adapters.GenreCategoryAdapter;
import com.mawsome20.aflam.network.model.AdsConfig;
import com.mawsome20.aflam.new_conception.models.genres;
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

public class CategoriesFragment extends Fragment {

    ShimmerFrameLayout shimmerFrameLayout;
    private ApiResources apiResources;
    private RecyclerView recyclerView;
    private List<genres> list = new ArrayList<>();
    private List<String> listNames = new ArrayList<>();

    private GenreCategoryAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;

    private TextView tvNoItem;
    private RelativeLayout adView;

    private MainActivity activity;

    private LinearLayout searchRootLayout;

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    private CardView searchBar;
    private ImageView menuIv, searchIv;
    private TextView pageTitle;
    InMobiBanner banner ;
    ADFView AD_FALCON;
    private DatabaseReference mRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.layout_country,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.country));

        mRef= FirebaseDatabase.getInstance().getReference();
        adView=view.findViewById(R.id.adView);
        coordinatorLayout=view.findViewById(R.id.coordinator_lyt);
        swipeRefreshLayout=view.findViewById(R.id.swipe_layout);
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
        tvNoItem=view.findViewById(R.id.tv_noitem);
        searchRootLayout=view.findViewById(R.id.search_root_layout);
        searchBar           = view.findViewById(R.id.search_bar);
        menuIv              = view.findViewById(R.id.bt_menu);
        pageTitle           = view.findViewById(R.id.page_title_tv);
        searchIv            = view.findViewById(R.id.search_iv);
        banner = view.findViewById(R.id.banner);
        AD_FALCON = view.findViewById(R.id.adFalconView);
        pageTitle.setText(getContext().getResources().getString(R.string.country));

        if (activity.isDark) {
            pageTitle.setTextColor(activity.getResources().getColor(R.color.white));
            searchBar.setCardBackgroundColor(activity.getResources().getColor(R.color.black_window_light));
            menuIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_menu));
            searchIv.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_search_white));
        }

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(getActivity(), 10), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new GenreCategoryAdapter(activity, list, "category","");
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

        shimmerFrameLayout.startShimmer();


        if (new NetworkInst(getContext()).isNetworkAvailable()){
            getAllCategorieseFC();
        }else {
            tvNoItem.setText(getString(R.string.no_internet));
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                coordinatorLayout.setVisibility(View.GONE);

                recyclerView.removeAllViews();
                list.clear();
                listNames.clear();
                mAdapter.notifyDataSetChanged();

                if (new NetworkInst(getContext()).isNetworkAvailable()){
                    getAllCategorieseFC();
                }else {
                    tvNoItem.setText(getString(R.string.no_internet));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //loadAd();
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
                BannerAds.ShowAdmobBannerAds(getContext(), adView);

            } else if (adsConfig.getMobileAdsNetwork().equals(Constants.START_APP)) {
                BannerAds.showStartAppBanner(getContext(), adView,"old_method",null);

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

    private void getAllCategorieseFC(){
        mRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    if (!dataSnapshot.exists()){
                        coordinatorLayout.setVisibility(View.VISIBLE);
                    }else {
                        coordinatorLayout.setVisibility(View.GONE);
                    }
                    for (DataSnapshot Genre:dataSnapshot.getChildren() ){
                        genres mGenre = Genre.getValue(genres.class);
                        list.add(mGenre);
                    }
                    mAdapter.notifyDataSetChanged();
                }catch (Exception e){}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                new ToastMsg(getActivity()).toastIconError(getString(R.string.fetch_error));
                coordinatorLayout.setVisibility(View.VISIBLE);
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
