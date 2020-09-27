package com.mawsome20.aflam20

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.mawsome20.aflam20.new_conception.models.Config
import com.mawsome20.aflam20.utils.PreferenceUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashScreenActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 100
    private val SPLASH_TIME = 1600
    private var timer: Thread? = null
    private var alertDialog: AlertDialog? = null
    private var mRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splashscreen)
        //db = DatabaseHelper(this@SplashScreenActivity)
        overridePendingTransition(0, 0)
        mRef = FirebaseDatabase.getInstance().reference
        //print keyHash for facebook login
        createKeyHash(this@SplashScreenActivity, BuildConfig.APPLICATION_ID)


      //  var listGenres = mutableListOf<String>("action","adventure","comedy","crime","drama","fantasy","horror","mystery","romance","thriller","animation","documentary","family","musical","history","war","short","sport","action & adventure","science fiction","news","reality","kids","historical fiction","philosophical","political","saga","social","speculative","urban","film-noir","magical realism","satire","western")
/*
        for (i in 0..listGenres.size-1){
            var itemName = listGenres[i]
         mRef!!.child("Genres").child(i.toString()).setValue(genres(itemName,"",0))
        }

        var strm = ArrayList<strm_link>()
        strm.add(strm_link("url1","mp4"))
        strm.add(strm_link("url2","mkv"))
        var down = ArrayList<String>()
        down.add("url1")
        down.add("url2")
        var ep = ArrayList<episodes>()
        ep.add(episodes("ep1",strm,down))
        ep.add(episodes("ep2",strm,down))
        ep.add(episodes("ep3",strm,down))
        var mSeasons = ArrayList<Seasons>()
        mSeasons.add(Seasons("season 1","pic1",ep))
        mSeasons.add(Seasons("season 2","pic2",ep))
        for (i in 0..20) {
            val Key = FirebaseDatabase.getInstance().reference.child("Series").push()

            Key.setValue(Series(Key.key.toString(),"title $i","description $i","pic_cover $i","pic_profile $i","geners $i",mSeasons,"type $i","2020"))
        }

         */

        if (isNetworkAvailable()){
            getConfig()
        }
        else{

            showErrorDialog("خطأ","حصل خطأ ما الرجاء تفقد إتصالك بالأنترنت")
        }


        val animation = AnimationUtils.loadAnimation(this@SplashScreenActivity, R.anim.rotate_move_down)
        findViewById<View>(R.id.mSplash).startAnimation(animation)
        timer = object : Thread() {
            override fun run() {
                try {
                    sleep(SPLASH_TIME.toLong())
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                }
            }
        }


    }

    private fun getConfig() {
        mRef!!.child("Config").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            showErrorDialog("خطأ","حصل خطأ ما الرجاء تفقد إتصالك بالأنترنت")
            }

            override fun onDataChange(p0: DataSnapshot) {
                try{
                    val config = p0.getValue(Config::class.java)
                    PreferenceUtils(this@SplashScreenActivity).setConfig(config!!)
                    if (isNeedUpdate(config.update_version!!)) {
                        showAppUpdateDialog(config);
                        return;
                    }
                    else{
                        timer!!.start()
                    }
                }catch (e:Exception){

                }

            }

        })
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
                    / *
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
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showAppUpdateDialog(config: Config) {
        var finalLink = ""
        var googlePlay = "http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        if (config.apk_url.equals("")){
            finalLink=googlePlay
        }else{
            finalLink=config.apk_url!!
        }

        val alert = androidx.appcompat.app.AlertDialog.Builder(this)
        alert.setTitle("إصدار جديد متوفر : " + config.version_name)
                .setMessage("""
    ${config.new_update_text}

    اختر الطريقة التي تفضلها لتحميل التحديث ؟
    """.trimIndent())
                .setPositiveButton("جوجل بلاي") { dialog, which ->
                    try {
                        dialog.dismiss()
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(finalLink))
                        startActivity(browserIntent)
                        finish()
                    } catch (e: Exception) {
                    }
                    //update clicked
                }
                .setNegativeButton("لاحقا") { dialog, which -> //exit clicked
                    if (config.is_skipable.equals("true")) {
                        timer!!.start()
                    } else {
                        System.exit(0)
                    }
                    dialog.dismiss()
                }
        alert.setCancelable(false)
        alert.create().show()
        //.show();
    }



    private fun showErrorDialog(title: String, message: String) {
        try {
            androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("إعادة المحاولة") { dialog, which -> recreate() }
                    .setCancelable(false)
                    .show()
        } catch (e: Exception) {
            Log.e("SPLASH_ERROR", e.message)
        }
    }

    private fun isNeedUpdate(versionCode: String): Boolean {
        return versionCode.toInt() > BuildConfig.VERSION_CODE
    }

    // ------------------ checking storage permission ------------
    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
            //getConfigurationData();
            //configurationData2
        }
    }

    companion object {
        private const val TAG = "SplashScreen"
        fun createKeyHash(activity: Activity, yourPackage: String?) {
            try {
                val info = activity.packageManager.getPackageInfo(yourPackage, PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
    }
}