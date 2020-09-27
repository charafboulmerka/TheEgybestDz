package com.mawsome20.aflam20.models.reports

class reportMovie{
    var movie_name:String?=null
    var report_description:String?=null
    var phone_type:String?=null
    var android_version:String?=null
    var time:String?=null
    var isVisible:String?=null
    constructor(movie_name:String,report_description:String,phone_type:String,android_version:String,time:String,isVisible:String){
        this.movie_name=movie_name
        this.report_description=report_description
        this.phone_type=phone_type
        this.android_version=android_version
        this.time=time
        this.isVisible=isVisible
    }
}