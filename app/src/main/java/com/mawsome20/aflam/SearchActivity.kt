package com.mawsome20.aflam

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.inmobi.ads.InMobiBanner
import com.mawsome20.aflam.adapters.All_MoviesSeriesSearchAdapter
import com.mawsome20.aflam.adapters.SearchAdapter
import com.mawsome20.aflam.api_inerfaces.MovieApiService
import com.mawsome20.aflam.api_inerfaces.SerieApiService
import com.mawsome20.aflam.network.model.AdsConfig
import com.mawsome20.aflam.new_conception.models.mix_movies_series
import com.mawsome20.aflam.utils.Constants
import com.mawsome20.aflam.utils.SpacingItemDecoration
import com.mawsome20.aflam.utils.Tools
import com.mawsome20.aflam.utils.ads.BannerAds
import com.mawsome20.aflam.utils.ads.PopUpAds
import com.noqoush.adfalcon.android.sdk.ADFView
import com.startapp.android.publish.ads.banner.Banner
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/*
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;


 */   class SearchActivity : AppCompatActivity() {
    private var isDark = false

    private val movieList: MutableList<mix_movies_series> = ArrayList()
    private val SeriesList: MutableList<mix_movies_series> = ArrayList()
    private var movieRv: RecyclerView? = null
    private var tvSeriesRv: RecyclerView? = null
    private var tvMovieAdapter: SearchAdapter? = null
    private var tvSeriesAdapter: SearchAdapter? = null
    private var movieLayout: LinearLayout? = null
    private var tvSeriesLayout: LinearLayout? = null

    private var mixedRv: RecyclerView? = null
    private var mixedLayout: LinearLayout? = null
    private val mixedList: MutableList<mix_movies_series> = ArrayList()
    private var tvMixedAdapter:All_MoviesSeriesSearchAdapter?=null

    private var tvTitle: TextView? = null
    private var movieTitle: TextView? = null
    private var tvSeriesTv: TextView? = null
    private var startAppBanner: Banner? = null
    private var adView: RelativeLayout? = null
    var bannerAd: InMobiBanner? = null
    var chance10ToShowAd = Arrays.asList(true, false, false, false, false)
    var mRef: DatabaseReference? = null


    private val HIDE_THRESHOLD = 20
    private var scrolledDistance = 0
    private var controlsVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val sharedPreferences = getSharedPreferences("push", Context.MODE_PRIVATE)
        isDark = sharedPreferences.getBoolean("dark", false)
        mRef = FirebaseDatabase.getInstance().reference
        if (isDark) {
            setTheme(R.style.AppThemeDark)
        } else {
            setTheme(R.style.AppThemeLight)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //searchMovieApi()

        //bannerAd = (InMobiBanner)findViewById(R.id.banner);
        tvTitle = findViewById(R.id.fast_result_label)
        adView = findViewById(R.id.adView)
        startAppBanner = findViewById(R.id.startAppBanner)
        movieLayout = findViewById(R.id.fast_movie_layout)
        tvSeriesLayout = findViewById(R.id.fast_tv_series_layout)
        movieTitle = findViewById(R.id.fast_movie_title)
        tvSeriesTv = findViewById(R.id.tv_series_title)
        movieRv = findViewById(R.id.fast_movie_rv)
        tvSeriesRv = findViewById(R.id.fast_tv_series_rv)
        mixedRv = findViewById(R.id.mRec_movies_series)
        if (!isDark) {
            toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))

        } else {
            //search_btn.setBackgroundResource(R.drawable.btn_rect_grey_outline);


        }
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("البـحـث")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //---analytics-----------
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "profile_activity")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity")
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        movieRv!!.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
        //movieRv.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(this, 4), true));
        movieRv!!.setHasFixedSize(true)
        tvMovieAdapter = SearchAdapter(movieList, this)
        movieRv!!.setAdapter(tvMovieAdapter)
        tvSeriesRv!!.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
        //tvSeriesRv.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(this, 4), true));
        tvSeriesRv!!.setHasFixedSize(true)
        tvSeriesAdapter = SearchAdapter(SeriesList, this)
        tvSeriesRv!!.setAdapter(tvSeriesAdapter)

        mixedRv!!.setLayoutManager(GridLayoutManager(this, 3))
        mixedRv!!.addItemDecoration(SpacingItemDecoration(3, Tools.dpToPx(this, 0), true))

        mixedRv!!.setHasFixedSize(true)
        mixedRv!!.setNestedScrollingEnabled(false)
        tvMixedAdapter = All_MoviesSeriesSearchAdapter(this,mixedList,"")
        mixedRv!!.setAdapter(tvMixedAdapter)

        mixedRv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //if (!recyclerView.canScrollVertically(1)) {
                    //Toast.makeText(applicationContext,"scroll",Toast.LENGTH_SHORT).show()
                    //coordinatorLayout.setVisibility(View.GONE)
                    //pageCount = pageCount + 1
                    //isLoading = true
                   // progressBar.setVisibility(View.VISIBLE)
                    //getDataOnScroll()
                    if((tvMixedAdapter!!.num)*10 < mixedList.size)
                        tvMixedAdapter!!.num = tvMixedAdapter!!.num +1;
                    //tvMixedAdapter!!.notifyDataSetChanged()
               // }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //Toast.makeText(applicationContext,"scrolled",Toast.LENGTH_SHORT).show()

                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                   // animateSearchBar(true)
                    controlsVisible = false
                    scrolledDistance = 0
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                   // animateSearchBar(false)
                    controlsVisible = true
                    scrolledDistance = 0
                }
                if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
                    scrolledDistance += dy
                }
            }
        })

        btn_request_search.setOnClickListener {
            if (edit_search_text.text.isEmpty()){
                edit_search_text.setError("الرجاء إدخال كلمة مفتاحية")
            }
            else {
                //Movie
                viewsState(false)
                mixedList.clear()
                tvMixedAdapter!!.notifyDataSetChanged()
                if (mRadioGroup.checkedRadioButtonId.equals(radioMovie.id)){
                    searchMovieApi()
                }else if (mRadioGroup.checkedRadioButtonId.equals(radioSerie.id)){
                    searchSerieApi()
                }

            }
        }
        // loadAd();
    }

    fun searchMovieApi() {
       val retrofit = Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val movieApiService = retrofit!!.create(MovieApiService::class.java)
        //movie title
        val call = movieApiService.getMovie(edit_search_text!!.text.toString())

        call!!.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(call: Call<JsonArray?>, response: Response<JsonArray?>) {
                if (response.code() == 200) {
                    viewsState(true)
                    mixedList.clear()
                    val mJsonArry = response.body()

                    if (mJsonArry!!.size()>0) {
                        tvTitle!!.visibility = View.VISIBLE
                        //movieLayout!!.visibility = View.VISIBLE
                        tvMixedAdapter!!.notifyDataSetChanged()
                    }
                    else {
                        Toast.makeText(this@SearchActivity,"لا توجد نتيجة",Toast.LENGTH_SHORT).show()
                        //movieLayout!!.visibility = View.GONE
                    }

                    println("HEY_THERE")
                    for (arrayItem in mJsonArry){
                        val gson = Gson()
                        val movie:HashMap<*,*> =gson.fromJson((arrayItem).toString(), HashMap::class.java)
                        val list = ArrayList(movie!!.values)
                        var jsonTree = list[0]
                        val jsonObject: JsonObject = gson.toJsonTree(jsonTree).asJsonObject
                        val MovieObj: mix_movies_series = gson.fromJson(jsonObject.toString(), mix_movies_series::class.java)
                        MovieObj.name_class = "movie"
                        mixedList.add(MovieObj)
                    }
                    tvMixedAdapter!!.notifyDataSetChanged()


                } else {
                    viewsState(true)
                    Toast.makeText(this@SearchActivity, "حصل خطأ في الإتصال الرجاء تفقد إتصالك بالأنترنت و إعادة المحاولة", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                viewsState(true)
                Log.e("THE_ERROR", t.message.toString())
            }
        })
    }

    fun searchSerieApi() {
        val httpClient = OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

        val mretrofit = Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val serieApiService = mretrofit!!.create(SerieApiService::class.java)
        //serie title
        val call = serieApiService.getSerie(edit_search_text!!.text.toString())

        call!!.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(call: Call<JsonArray?>, response: Response<JsonArray?>) {
                if (response.code() == 200) {
                    viewsState(true)
                    mixedList.clear()
                    val mJsonArry = response.body()

                    if (mJsonArry!!.size()>0) {
                        tvTitle!!.visibility = View.VISIBLE
                        //movieLayout!!.visibility = View.VISIBLE
                        tvMixedAdapter!!.notifyDataSetChanged()
                    } else {
                        //movieLayout!!.visibility = View.GONE
                    }

                    println("HEY_THERE")
                    println(mJsonArry.toString())
                    for (arrayItem in mJsonArry!!){
                        val gson: Gson = GsonBuilder()
                                .setLenient()
                                .create()

                        val movie:HashMap<*,*> =gson.fromJson((arrayItem).toString(), HashMap::class.java)
                        val list = ArrayList(movie!!.values)
                        var jsonTree = list[0]
                        val jsonObject: JsonObject = gson.toJsonTree(jsonTree).asJsonObject
                        val SerieObj: mix_movies_series = gson.fromJson(jsonObject.toString(), mix_movies_series::class.java)
                        SerieObj.name_class = "tvseries"
                        Log.v("HA2020",SerieObj.title)
                        mixedList.add(SerieObj)
                    }
                    tvMixedAdapter!!.notifyDataSetChanged()


                } else {
                    viewsState(true)
                    Toast.makeText(this@SearchActivity, "حصل خطأ في الإتصال الرجاء تفقد إتصالك بالأنترنت و إعادة المحاولة", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                viewsState(true)
                Log.e("THE_ERROR", t.message.toString())
            }
        })
    }


    fun viewsState(state:Boolean){
        btn_request_search.isEnabled = state
        if (!state){
            mProgressBar_Search.visibility = View.VISIBLE
            btn_request_search.alpha = 0.5f
        }else{
            mProgressBar_Search.visibility = View.GONE
            btn_request_search.alpha = 1f
        }
        edit_search_text.isEnabled = state
        mRadioGroup.isEnabled = state
        radioMovie.isEnabled = state
        radioSerie.isEnabled = state
    }

    private fun loadAd() {
        val chanceToShowAd = Arrays.asList(true, false)
        Collections.shuffle(chanceToShowAd)
        val chosenChance = chanceToShowAd[0]
        val adsConfig = AdsConfig()
        if (adsConfig.adsEnable == "1") {
            if (adsConfig.mobileAdsNetwork.equals(Constants.ADMOB, ignoreCase = true)) {
                BannerAds.ShowAdmobBannerAds(this, adView)
                if (chosenChance!!) {
                    PopUpAds.ShowAdmobInterstitialAds(this)
                }
            } else if (adsConfig.mobileAdsNetwork == Constants.START_APP) {
                val startappBannerLayout = findViewById<LinearLayout>(R.id.startapp_linearlayout)
                startappBannerLayout.visibility = View.VISIBLE
                BannerAds.showStartAppBanner(this, null, "new_method", startAppBanner)
                if (chosenChance!!) {
                    PopUpAds.showStartappInterstitialAds(this)
                }
            } else if (adsConfig.mobileAdsNetwork == Constants.NETWORK_AUDIENCE) {
                BannerAds.showFANBanner(this, adView)
                if (chosenChance!!) {
                    PopUpAds.showFANInterstitialAds(this)
                }
            } else if (adsConfig.mobileAdsNetwork.equals(Constants.INMOBI, ignoreCase = true)) {
                BannerAds.showInmobiBanner(bannerAd)
                if (chosenChance!!) {
                    PopUpAds.showInmobiInterstitialAds(this)
                }
            } else if (adsConfig.mobileAdsNetwork.equals(Constants.ADFALCON, ignoreCase = true)) {
                val AD_FALCON = findViewById<ADFView>(R.id.adFalconView)
                BannerAds.showAdFalconBanner(this, AD_FALCON)
                if (chosenChance!!) {
                    PopUpAds.showAdFalconInterstitialAds(this)
                }
            }
        }
    }
/*
    private val searchSeries: Unit
        private get() {
            mRef!!.child("Series").orderByChild("title").startAt(search_edit_text!!.text.toString())
                    .endAt(search_edit_text!!.text.toString() + "\uf8ff").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            try {
                                SeriesList.clear()
                                if (dataSnapshot.exists()) {
                                    tvTitle!!.visibility = View.VISIBLE
                                    tvSeriesLayout!!.visibility = View.VISIBLE
                                    tvSeriesAdapter!!.notifyDataSetChanged()
                                } else {
                                    tvSeriesLayout!!.visibility = View.GONE
                                }
                                tvTitle!!.text = "نتائج البحث"
                                for (serie in dataSnapshot.children) {
                                    val mSerie = serie.getValue(mix_movies_series::class.java)!!
                                    if (mSerie.year!!.toInt() >= year_min!!.text.toString().toInt() &&
                                            mSerie.year!!.toInt() <= year_max!!.text.toString().toInt()) {
                                        SeriesList.add(mSerie)
                                        Log.e("CHARAF1013", mSerie.id.toString())
                                    }
                                }
                                tvSeriesAdapter!!.notifyDataSetChanged()
                            } catch (e: Exception) {
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(this@SearchActivity, "حصل خطأ الرجاء تفقد إتصالك بالأنترنت و إعادة المحاولة", Toast.LENGTH_LONG).show()
                        }
                    })
        }

    private val searchMovies: Unit
        private get() {
            Log.e("SEARCH1013", "NOPE NOPE")
            mRef!!.child("Movies").orderByChild("title").startAt(search_edit_text!!.text.toString())
                    .endAt(search_edit_text!!.text.toString() + "\uf8ff").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            movieList.clear()
                            if (dataSnapshot.exists()) {
                                tvTitle!!.visibility = View.VISIBLE
                                movieLayout!!.visibility = View.VISIBLE
                                movieAdapter!!.notifyDataSetChanged()
                            } else {
                                movieLayout!!.visibility = View.GONE
                            }
                            tvTitle!!.text = "نتائج البحث"
                            for (movie in dataSnapshot.children) {
                                val mMovie = movie.getValue(mix_movies_series::class.java)!!
                                if (mMovie.year!!.toInt() >= year_min!!.text.toString().toInt() &&
                                        mMovie.year!!.toInt() <= year_max!!.text.toString().toInt()) {
                                    movieList.add(mMovie)
                                    Log.e("SEARCH1013", mMovie.id.toString())
                                } else {
                                    Log.e("SEARCH1013", "NOPE NOPE")
                                }
                            }
                            movieAdapter!!.notifyDataSetChanged()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(this@SearchActivity, "حصل خطأ الرجاء تفقد إتصالك بالأنترنت و إعادة المحاولة", Toast.LENGTH_LONG).show()
                        }
                    })
        }


 */




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