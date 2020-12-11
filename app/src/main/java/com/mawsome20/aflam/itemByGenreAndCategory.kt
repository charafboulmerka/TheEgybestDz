package com.mawsome20.aflam

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import com.inmobi.ads.InMobiBanner
import com.mawsome20.aflam.adapters.All_MoviesSeriesAdapter
import com.mawsome20.aflam.api_inerfaces.CategoryApiService
import com.mawsome20.aflam.api_inerfaces.GenreApiService
import com.mawsome20.aflam.network.model.AdsConfig
import com.mawsome20.aflam.new_conception.models.mix_movies_series
import com.mawsome20.aflam.utils.Constants
import com.mawsome20.aflam.utils.NetworkInst
import com.mawsome20.aflam.utils.SpacingItemDecoration
import com.mawsome20.aflam.utils.Tools
import com.mawsome20.aflam.utils.ads.BannerAds
import com.noqoush.adfalcon.android.sdk.ADFView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class itemByGenreAndCategory : AppCompatActivity() {
    private var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var recyclerView: RecyclerView? = null
    private var mAdapter: All_MoviesSeriesAdapter? = null
    private val list: MutableList<mix_movies_series> = ArrayList()
    private var isLoading = false
    private var progressBar: ProgressBar? = null
    private val pageCount = 1
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var id = ""
    private var type = ""
    private var isMovieOrSerie = ""
    private var coordinatorLayout: CoordinatorLayout? = null
    private var tvNoItem: TextView? = null
    private var adView: RelativeLayout? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mRef: DatabaseReference? = null
    var bannerAd: InMobiBanner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val sharedPreferences = getSharedPreferences("push", Context.MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean("dark", false)
        mRef = FirebaseDatabase.getInstance().reference
        if (isDark) {
            setTheme(R.style.AppThemeDark)
        } else {
            setTheme(R.style.AppThemeLight)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_show)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        if (!isDark) {
            toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        }
        setSupportActionBar(toolbar)

        //---analytics-----------
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "movie_activity")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        supportActionBar!!.setTitle(intent.getStringExtra("title"))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        adView = findViewById(R.id.adView)
        coordinatorLayout = findViewById(R.id.coordinator_lyt)
        progressBar = findViewById(R.id.item_progress_bar)
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container)
        shimmerFrameLayout!!.startShimmer()
        //bannerAd = (InMobiBanner)findViewById(R.id.banner);
        swipeRefreshLayout = findViewById(R.id.swipe_layout)
        swipeRefreshLayout!!.setOnRefreshListener(OnRefreshListener {
            if (swipeRefreshLayout!!.isRefreshing()) {
                swipeRefreshLayout!!.setRefreshing(false)
                Toast.makeText(applicationContext, "الرجاء السحب للأسفل", Toast.LENGTH_LONG).show()
            }
        })
        tvNoItem = findViewById(R.id.tv_noitem)


        //----movie's recycler view-----------------
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.setLayoutManager(GridLayoutManager(this, 3))
        recyclerView!!.addItemDecoration(SpacingItemDecoration(3, Tools.dpToPx(this, 8), true))
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setNestedScrollingEnabled(false)
        mAdapter = All_MoviesSeriesAdapter(this, list, "")
        recyclerView!!.setAdapter(mAdapter)
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
        id = intent.getStringExtra("id")
        type = intent.getStringExtra("type")
        isMovieOrSerie = intent.getStringExtra("isMovieSerie")
        if (NetworkInst(this).isNetworkAvailable) {
            initData()
        } else {
            tvNoItem!!.setText(getString(R.string.no_internet))
            shimmerFrameLayout!!.stopShimmer()
            shimmerFrameLayout!!.setVisibility(View.GONE)
            coordinatorLayout!!.setVisibility(View.VISIBLE)
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
    var selectedChild = ""
    private fun initData() {
        if (isMovieOrSerie.equals("movie")){
            selectedChild="Movies"
        }else{
            selectedChild="Series"
        }

        if (type.equals("genre")){
            getDataByGenreApi()
        }else{
            getDataByCategoryApi()
        }
    }

    fun getDataByGenreApi(){
        val httpClient = OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

        var mretrofit = Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val genreApiService = mretrofit!!.create(GenreApiService::class.java)
        //serie title
        val call = genreApiService.getDataByGenre(id.toLowerCase(),selectedChild)
        call!!.enqueue(object : retrofit2.Callback<List<mix_movies_series>?> {
            override fun onResponse(call: Call<List<mix_movies_series>?>, response: Response<List<mix_movies_series>?>) {
                if (response.code() == 200) {
                    list.clear()
                    isLoading = false
                    progressBar!!.visibility = View.GONE
                    shimmerFrameLayout!!.stopShimmer()
                    shimmerFrameLayout!!.visibility = View.GONE
                    swipeRefreshLayout!!.isRefreshing = false
                    val mJsonArry = response.body()

                    if (mJsonArry!!.size>0) {
                        coordinatorLayout!!.visibility = View.GONE
                    } else {
                        coordinatorLayout!!.visibility = View.VISIBLE
                    }

                    println("HEY_THERE")
                   for (i in mJsonArry){
                       i.name_class = isMovieOrSerie
                       list.add(i)
                   }
                    mAdapter!!.notifyDataSetChanged()


                } else {
                    Toast.makeText(this@itemByGenreAndCategory, "حصل خطأ في الإتصال الرجاء تفقد إتصالك بالأنترنت و إعادة المحاولة"+response.code(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<mix_movies_series>?>, t: Throwable) {
                Log.e("THE_ERROR", t.message.toString())
            }
        })
    }

    fun getDataByCategoryApi(){
        val httpClient = OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

        var mretrofit = Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val categoryApiService = mretrofit!!.create(CategoryApiService::class.java)
        //category
        val call = categoryApiService.getDataByCategory(id.toLowerCase(),selectedChild)
        call!!.enqueue(object : retrofit2.Callback<List<mix_movies_series>?> {
            override fun onResponse(call: Call<List<mix_movies_series>?>, response: Response<List<mix_movies_series>?>) {
                if (response.code() == 200) {
                    list.clear()
                    isLoading = false
                    progressBar!!.visibility = View.GONE
                    shimmerFrameLayout!!.stopShimmer()
                    shimmerFrameLayout!!.visibility = View.GONE
                    swipeRefreshLayout!!.isRefreshing = false
                    val mJsonArry = response.body()

                    if (mJsonArry!!.size>0) {
                        coordinatorLayout!!.visibility = View.GONE
                    } else {
                        coordinatorLayout!!.visibility = View.VISIBLE
                    }

                    println("HEY_THERE")
                    for (i in mJsonArry){
                        i.name_class = isMovieOrSerie
                        list.add(i)
                    }
                    mAdapter!!.notifyDataSetChanged()


                } else {
                    Toast.makeText(this@itemByGenreAndCategory, "حصل خطأ في الإتصال الرجاء تفقد إتصالك بالأنترنت و إعادة المحاولة", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<mix_movies_series>?>, t: Throwable) {
                Log.e("THE_ERROR", t.message.toString())
            }
        })
    }


    private fun loadAd() {
        val adsConfig = AdsConfig()
        if (adsConfig.adsEnable == "1") {
            if (adsConfig.mobileAdsNetwork.equals(Constants.ADMOB, ignoreCase = true)) {
                BannerAds.ShowAdmobBannerAds(this@itemByGenreAndCategory, adView)
            } else if (adsConfig.mobileAdsNetwork == Constants.START_APP) {
                BannerAds.showStartAppBanner(this@itemByGenreAndCategory, adView, "old_method", null)
            } else if (adsConfig.mobileAdsNetwork == Constants.NETWORK_AUDIENCE) {
                BannerAds.showFANBanner(this@itemByGenreAndCategory, adView)
            } else if (adsConfig.mobileAdsNetwork == Constants.INMOBI) {
                BannerAds.showInmobiBanner(bannerAd)
            } else if (adsConfig.mobileAdsNetwork.equals(Constants.ADFALCON, ignoreCase = true)) {
                val AD_FALCON = findViewById<ADFView>(R.id.adFalconView)
                BannerAds.showAdFalconBanner(this, AD_FALCON)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}