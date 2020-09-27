package com.mawsome20.aflam20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inmobi.ads.InMobiBanner;
import com.mawsome20.aflam20.adapters.SearchAdapter;
import com.mawsome20.aflam20.network.model.AdsConfig;
import com.mawsome20.aflam20.new_conception.models.mix_movies_series;
import com.mawsome20.aflam20.utils.Constants;
import com.mawsome20.aflam20.utils.Tools;
import com.mawsome20.aflam20.utils.ads.BannerAds;
import com.mawsome20.aflam20.utils.ads.PopUpAds;
import com.mawsome20.aflam20.widget.RangeSeekBar;
import com.noqoush.adfalcon.android.sdk.ADFView;
import com.startapp.android.publish.ads.banner.Banner;
/*
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;


 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.view.View.VISIBLE;

public class SearchActivity extends AppCompatActivity {
    private boolean isDark;
    private RangeSeekBar range_seek_bar;
    private TextView year_min, year_max, range_tv;
    private Button search_btn, clear_btn, btn_flex_1, btn_flex_2, btn_flex_3,btn_done;
    private EditText search_edit_text;
    private LinearLayout rangeLayout;
    private RelativeLayout tvCategoryLayout, genreLayout, countryLayout;
    private EditText genreSpinner, countrySpinner, tvCategorySpinner;
    private boolean [] selectedType = new boolean[3];
    private int selectedGenreId = 0;
    private int selectedTvCategoryId = 0;
    private int selectedCountryId = 0;

    private List<mix_movies_series> movieList =new ArrayList<>();
    private List<mix_movies_series> SeriesList =new ArrayList<>();
    private RecyclerView movieRv, tvRv, tvSeriesRv;
    private SearchAdapter movieAdapter, tvSeriesAdapter;
    private LinearLayout movieLayout, tvSeriesLayout;
    private TextView tvTitle, movieTitle, tvSeriesTv, searchQueryTv;
    private Banner startAppBanner;
    private RelativeLayout adView;
    InMobiBanner bannerAd;
    List<Boolean> chance10ToShowAd =  Arrays.asList(true,false,false,false,false);
    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        isDark = sharedPreferences.getBoolean("dark", false);
        mRef = FirebaseDatabase.getInstance().getReference();
        if (isDark) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);

        initComponent();
         //bannerAd = (InMobiBanner)findViewById(R.id.banner);

        tvTitle=findViewById(R.id.fast_result_label);
        adView = findViewById(R.id.adView);
        startAppBanner=findViewById(R.id.startAppBanner);
        movieLayout=findViewById(R.id.fast_movie_layout);
        tvSeriesLayout=findViewById(R.id.fast_tv_series_layout);
        movieTitle= findViewById(R.id.fast_movie_title);
        tvSeriesTv = findViewById(R.id.tv_series_title);
        movieRv = findViewById(R.id.fast_movie_rv);
        tvSeriesRv = findViewById(R.id.fast_tv_series_rv);

        if (!isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            search_btn.setBackgroundResource(R.drawable.btn_rect_primary);
            search_btn.setTextColor(getResources().getColor(R.color.white));
            genreSpinner.setBackground(ContextCompat.getDrawable(this,R.drawable.edit_text_round_bg_overlay_light));
            tvCategorySpinner.setBackground(ContextCompat.getDrawable(this,R.drawable.edit_text_round_bg_overlay_light));
            countrySpinner.setBackground(ContextCompat.getDrawable(this,R.drawable.edit_text_round_bg_overlay_light));
            //flex btn
            btn_flex_1.setBackgroundResource(R.drawable.btn_rounded_primary_outline_flex);
            btn_flex_2.setBackgroundResource(R.drawable.btn_rounded_primary_outline_flex);
            btn_flex_3.setBackgroundResource(R.drawable.btn_rounded_primary_outline_flex);
        }else {
            //search_btn.setBackgroundResource(R.drawable.btn_rect_grey_outline);
            search_btn.setTextColor(getResources().getColor(R.color.white));
            clear_btn.setTextColor(getResources().getColor(R.color.white));
            range_seek_bar.setBarHighlightColor(getResources().getColor(R.color.grey_60));
            range_seek_bar.setRightThumbColor(getResources().getColor(R.color.grey_60));
            range_seek_bar.setRightThumbHighlightColor(getResources().getColor(R.color.grey_90));
            range_seek_bar.setLeftThumbColor(getResources().getColor(R.color.grey_60));
            range_seek_bar.setLeftThumbHighlightColor(getResources().getColor(R.color.grey_90));
            //spinner
            genreSpinner.setBackground(ContextCompat.getDrawable(this,R.drawable.edit_text_round_bg_overlay_dark));
            tvCategorySpinner.setBackground(ContextCompat.getDrawable(this,R.drawable.edit_text_round_bg_overlay_dark));
            countrySpinner.setBackground(ContextCompat.getDrawable(this,R.drawable.edit_text_round_bg_overlay_dark));
            //flex btn
            btn_flex_1.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_rounded_grey_outline_flex));
            btn_flex_2.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_rounded_grey_outline_flex));
            btn_flex_3.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_rounded_grey_outline_flex));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("البـحـث");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---analytics-----------
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "profile_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);



        movieRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //movieRv.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(this, 4), true));
        movieRv.setHasFixedSize(true);
        movieAdapter = new SearchAdapter(movieList, this);
        movieRv.setAdapter(movieAdapter);




        tvSeriesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //tvSeriesRv.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(this, 4), true));
        tvSeriesRv.setHasFixedSize(true);
        tvSeriesAdapter = new SearchAdapter(SeriesList, this);
        tvSeriesRv.setAdapter(tvSeriesAdapter);

       // loadAd();
    }

    private void loadAd() {
        List<Boolean> chanceToShowAd =  Arrays.asList(true,false);
        Collections.shuffle(chanceToShowAd);
        Boolean chosenChance = chanceToShowAd.get(0);
        AdsConfig adsConfig = new AdsConfig();
        if (adsConfig.getAdsEnable().equals("1")) {
            if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADMOB)) {
                BannerAds.ShowAdmobBannerAds(this, adView);
                if (chosenChance){
                    PopUpAds.ShowAdmobInterstitialAds(this);
                }

            } else if (adsConfig.getMobileAdsNetwork().equals(Constants.START_APP)) {
                LinearLayout startappBannerLayout = findViewById(R.id.startapp_linearlayout);
                startappBannerLayout.setVisibility(VISIBLE);
                BannerAds.showStartAppBanner(this,null,"new_method",startAppBanner);
                if (chosenChance){
                    PopUpAds.showStartappInterstitialAds(this);
                }
            } else if(adsConfig.getMobileAdsNetwork().equals(Constants.NETWORK_AUDIENCE)) {
                BannerAds.showFANBanner(this, adView);
                if (chosenChance){
                    PopUpAds.showFANInterstitialAds(this);
                }
            }
            else if (adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.INMOBI)){
                BannerAds.showInmobiBanner(bannerAd);
                if (chosenChance){
                    PopUpAds.showInmobiInterstitialAds(this);
                }
            }
            else if(adsConfig.getMobileAdsNetwork().equalsIgnoreCase(Constants.ADFALCON)){
                ADFView AD_FALCON = findViewById(R.id.adFalconView);
                BannerAds.showAdFalconBanner(this,AD_FALCON);
                if (chosenChance){
                    PopUpAds.showAdFalconInterstitialAds(this);
                }
            }

        }
    }

    private void getSearchSeries(){
        mRef.child("Series").orderByChild("title").startAt(search_edit_text.getText().toString())
                .endAt(search_edit_text.getText().toString()+"\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    SeriesList.clear();
                    if (dataSnapshot.exists()) {
                        tvTitle.setVisibility(View.VISIBLE);
                        tvSeriesLayout.setVisibility(View.VISIBLE);
                        tvSeriesAdapter.notifyDataSetChanged();
                    } else {
                        tvSeriesLayout.setVisibility(View.GONE);
                    }

                    tvTitle.setText("نتائج البحث");

                    for (DataSnapshot serie :  dataSnapshot.getChildren()){
                        mix_movies_series mSerie = serie.getValue(mix_movies_series.class);
                        if (Integer.parseInt(mSerie.getYear()) >= Integer.parseInt(year_min.getText().toString()) &&
                                Integer.parseInt(mSerie.getYear()) <= Integer.parseInt(year_max.getText().toString())){
                            SeriesList.add(mSerie);
                            Log.e("CHARAF1013",mSerie.getId().toString());
                        }

                    }
                    tvSeriesAdapter.notifyDataSetChanged();
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this,"حصل خطأ الرجاء تفقد إتصالك بالأنترنت و إعادة المحاولة",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSearchMovies(){
        mRef.child("Movies").orderByChild("title").startAt(search_edit_text.getText().toString())
                .endAt(search_edit_text.getText().toString()+"\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    movieList.clear();
                    if (dataSnapshot.exists()) {
                        tvTitle.setVisibility(View.VISIBLE);
                        movieLayout.setVisibility(View.VISIBLE);
                        movieAdapter.notifyDataSetChanged();
                    } else {
                        movieLayout.setVisibility(View.GONE);
                    }

                    tvTitle.setText("نتائج البحث");

                    for (DataSnapshot movie :  dataSnapshot.getChildren()){
                        mix_movies_series mMovie = movie.getValue(mix_movies_series.class);
                        if (Integer.valueOf(mMovie.getYear()) >= Integer.valueOf(year_min.getText().toString()) &&
                                Integer.valueOf(mMovie.getYear()) <= Integer.valueOf(year_max.getText().toString())){
                            movieList.add(mMovie);
                            Log.e("CHARAF1013",mMovie.getId().toString());
                        }

                    }
                    movieAdapter.notifyDataSetChanged();
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this,"حصل خطأ الرجاء تفقد إتصالك بالأنترنت و إعادة المحاولة",Toast.LENGTH_LONG).show();
            }
        });
    }



    private void initComponent() {
        search_btn = findViewById(R.id.search_btn);
        clear_btn = findViewById(R.id.clear_btn);
        genreLayout = findViewById(R.id.genre_layout);
        genreSpinner = findViewById(R.id.genre_spinner);
        genreSpinner.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(this,R.drawable.ic_arrow_drop_down_white_24dp),null);

        tvCategoryLayout = findViewById(R.id.tv_category_layout);
        tvCategorySpinner = findViewById(R.id.tv_category_spinner);
        countryLayout = findViewById(R.id.country_layout);
        countrySpinner = findViewById(R.id.country_spinner);
        countrySpinner.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(this,R.drawable.ic_arrow_drop_down_white_24dp),null);

        btn_flex_1 = findViewById(R.id.btn_flex_1);
        btn_flex_2 = findViewById(R.id.btn_flex_2);
        btn_flex_3 = findViewById(R.id.btn_flex_3);
        btn_flex_1.setSelected(true);
        btn_flex_2.setSelected(true);
        btn_flex_3.setSelected(true);
        selectedType[0] = true;
        selectedType[1] = true;
        selectedType[2] = true;
        //populate genre list
        List<String> genreList = new ArrayList<>();
        List<String> genreNames = new ArrayList<>();
        if (Constants.genreList != null){
            genreList.add(0, "كل الأصناف");
            for (int i = 0; i < Constants.genreList.size(); i++) {
                //to avoid dublicatin
                String itemName = Constants.genreList.get(i).getName();
                if (!genreNames.contains(itemName.trim().toLowerCase()) && !Tools.DeletedGeners().contains(itemName.trim().toLowerCase())){
                    genreList.add(Tools.getArName(itemName) );
                    genreNames.add(itemName.trim().toLowerCase());
                }
            }
        }
        final String[] genreArray = new String[genreList.size()];
        for (int i = 0; i < genreList.size(); i++) {
            genreArray[i] = genreList.get(i);
        }

        genreSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("أختر النوع");
                builder.setSingleChoiceItems(genreArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((TextView) v).setText(genreArray[i]);
                        if (i != 0){
                            selectedGenreId = Integer.parseInt(Constants.genreList.get(i - 1).getGenreId());
                        }
                        else{
                            selectedGenreId = 0;
                        }
                        dialogInterface.dismiss();
                        if (!TextUtils.isEmpty(search_edit_text.getText().toString())) {
                            //getSearchData();
                        }
                    }
                });
                builder.show();
            }
        });



        range_tv = findViewById(R.id.rangeTV);
        rangeLayout = findViewById(R.id.range_picker_layout);
        range_seek_bar = findViewById(R.id.range_seek_bar);
        year_min = findViewById(R.id.year_min);
        year_max = findViewById(R.id.year_max);
        btn_done=findViewById(R.id.btn_search_done);
        search_edit_text = findViewById(R.id.search_text);
        //search_edit_text.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,R.drawable.ic_search_grey), null);
        /*
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search_edit_text.getText().toString().length()!=0) {
                    Collections.shuffle(chance10ToShowAd);
                    if (chance10ToShowAd.get(0)){
                       // loadAd();
                    }
                    getSearchMovies();
                    getSearchSeries();
                    tvTitle.setVisibility(View.VISIBLE);
                    movieRv.setVisibility(View.VISIBLE);
                    tvSeriesRv.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Log.e("NULLTEST","NO");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_done.setVisibility(View.GONE);
                        }
                    },300);
                }
                else{
                    search_edit_text.setError("الرجاء إدخال كلمة مفتاحية لبدأ البحث");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


        search_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_done.setVisibility(View.VISIBLE);
                    }
                },500);
            }
        });

        search_edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (search_edit_text.getText().toString().length()!=0) {
                        //getSearchData();
                        getSearchMovies();
                        getSearchSeries();
                        tvTitle.setVisibility(View.VISIBLE);
                        movieRv.setVisibility(View.VISIBLE);
                        tvSeriesRv.setVisibility(View.VISIBLE);
                        Log.e("NULLTEST","NO");

                    }
                    else{
                        search_edit_text.setError("الرجاء إدخال كلمة مفتاحية لبدأ البحث");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
*/

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (search_edit_text.getText().toString().equals(" ")){
                    search_edit_text.setText("");
                }
                else{
                    if (search_edit_text.getText().toString().length()!=0) {
                        getSearchMovies();
                        getSearchSeries();
                        tvTitle.setVisibility(View.VISIBLE);
                        movieRv.setVisibility(View.VISIBLE);
                        tvSeriesRv.setVisibility(View.VISIBLE);
                        Log.e("NULLTEST","NO");

                    }else{
                        movieList.clear();
                        SeriesList.clear();
                        tvTitle.setVisibility(View.GONE);
                        movieRv.setVisibility(View.GONE);
                        tvSeriesRv.setVisibility(View.GONE);
                        Log.e("NULLTEST","YES");
                        
                    }
                }

            }
        });


        //set min year and max year
        range_seek_bar.setMaxValue(Float.parseFloat(getString(R.string.year_range_end)));
        range_seek_bar.setMinValue(Float.parseFloat(getString(R.string.year_range_start)));
        // set listener
        range_seek_bar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                year_min.setText(String.valueOf(minValue));
                year_max.setText(String.valueOf(maxValue));
                if (!TextUtils.isEmpty(search_edit_text.getText().toString())) {
                    getSearchMovies();
                    getSearchSeries();
                }
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // search();
            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }





    public void btToggleClick(View view) {
        if (view instanceof Button) {
            Button b = (Button) view;
            if (b.isSelected()) {
                b.setTextColor(getResources().getColor(R.color.grey_40));
                if (b.getText().equals(getResources().getString(R.string.movie))) {
                    selectedType[0] = false;
                    if (!selectedType[1]){
                        rangeLayout.setVisibility(View.GONE);
                        range_tv.setVisibility(View.GONE);
                        genreLayout.setVisibility(View.GONE);
                        genreSpinner.setVisibility(View.GONE);
                        countryLayout.setVisibility(View.GONE);
                        countrySpinner.setVisibility(View.GONE);
                    }
                } else if (b.getText().equals(getResources().getString(R.string.tv_series))) {
                    selectedType[1] = false;
                    if (!selectedType[0] ){
                        rangeLayout.setVisibility(View.GONE);
                        range_tv.setVisibility(View.GONE);
                        genreLayout.setVisibility(View.GONE);
                        genreSpinner.setVisibility(View.GONE);
                        countryLayout.setVisibility(View.GONE);
                        countrySpinner.setVisibility(View.GONE);
                    }
                } else if (b.getText().equals(getResources().getString(R.string.live_tv))) {
                    selectedType[2] = false;
                    tvCategoryLayout.setVisibility(View.GONE);
                    tvCategorySpinner.setVisibility(View.GONE);
                }

            } else {
                b.setTextColor(getResources().getColor(R.color.white));
                if (b.getText().equals(getResources().getString(R.string.movie))) {
                    selectedType[0] = true;
                    rangeLayout.setVisibility(View.VISIBLE);
                    range_tv.setVisibility(View.VISIBLE);
                    genreLayout.setVisibility(View.VISIBLE);
                    genreSpinner.setVisibility(View.VISIBLE);
                    countryLayout.setVisibility(View.VISIBLE);
                    countrySpinner.setVisibility(View.VISIBLE);
                } else if (b.getText().equals(getResources().getString(R.string.tv_series))) {
                    selectedType[1] = true;
                    rangeLayout.setVisibility(View.VISIBLE);
                    range_tv.setVisibility(View.VISIBLE);
                    genreLayout.setVisibility(View.VISIBLE);
                    genreSpinner.setVisibility(View.VISIBLE);
                    countryLayout.setVisibility(View.VISIBLE);
                    countrySpinner.setVisibility(View.VISIBLE);
                } else if (b.getText().equals(getResources().getString(R.string.live_tv))) {
                    selectedType[2] = true;
                    tvCategoryLayout.setVisibility(View.VISIBLE);
                    tvCategorySpinner.setVisibility(View.VISIBLE);
                }
            }
            b.setSelected(!b.isSelected());
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
