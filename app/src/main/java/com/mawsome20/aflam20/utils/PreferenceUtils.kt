package com.mawsome20.aflam20.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.mawsome20.aflam20.BuildConfig
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PreferenceUtils {
    var mCtx:Context?=null

    constructor(mCtx:Context){
        this.mCtx=mCtx
    }

    fun setConfig(cnf: com.mawsome20.aflam20.new_conception.models.Config) {
        val mShared = mCtx!!.getSharedPreferences("CNF",Context.MODE_PRIVATE).edit()
        mShared.putString("enable_ads",cnf.enable_ads)
        mShared.putString("ads_company",cnf.ads_company)
        mShared.putString("auto_slider",cnf.auto_slider)
        mShared.putString("enable_update",cnf.enable_update)
        mShared.putString("update_version",cnf.update_version)
        mShared.putString("new_update_text",cnf.new_update_text)
        mShared.putString("apk_url",cnf.apk_url)
        mShared.putString("version_name",cnf.version_name)
        mShared.putString("is_skipable",cnf.is_skipable)
        mShared.apply()
        mShared.commit()
    }

    fun getConfig():com.mawsome20.aflam20.new_conception.models.Config{
        val mShared = mCtx!!.getSharedPreferences("CNF",Context.MODE_PRIVATE)
        return com.mawsome20.aflam20.new_conception.models.Config(mShared.getString("enable_ads","true"),mShared.getString("ads_company","startapp"),mShared.getString("auto_slider","true")
        ,mShared.getString("enable_update","false"),mShared.getString("update_version","1"),mShared.getString("new_update_text","NEW UPDATE"),
                mShared.getString("apk_url","http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID),mShared.getString("version_name","v1.0"),mShared.getString("is_skipable","true"))

    }

    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun getUserId(): String? {
        return if (FirebaseAuth.getInstance().currentUser != null) FirebaseAuth.getInstance().currentUser!!.uid else null
    }

    fun setProfile(user:com.mawsome20.aflam20.new_conception.models.User){
        val mShared = mCtx!!.getSharedPreferences("USER",Context.MODE_PRIVATE).edit()
        mShared.putString("UID",user.uid)
        mShared.putString("NAME",user.name)
        mShared.putString("EMAIL",user.email)
        mShared.apply()
        mShared.commit()
    }

    fun getProfile():com.mawsome20.aflam20.new_conception.models.User{
        val mShared = mCtx!!.getSharedPreferences("USER",Context.MODE_PRIVATE)
        return com.mawsome20.aflam20.new_conception.models.User(mShared.getString("UID","NULL"),mShared.getString("NAME","NULL"),mShared.getString("EMAIL","NULL"))
    }


    val currentTime: Long
        get() {
            val sdf = SimpleDateFormat("yyyy:MM:dd:HH:mm")
            val currentDateandTime = sdf.format(Date())
            var date: Date? = null
            try {
                date = sdf.parse(currentDateandTime)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar.timeInMillis
        }
}