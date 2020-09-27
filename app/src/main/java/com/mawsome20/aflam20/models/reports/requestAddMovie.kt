package com.mawsome20.aflam20.models.reports

class requestAddMovie {
    var request_name:String?=null
    var phone_type:String?=null
    var android_version:String?=null
    var time:String?=null
    var isVisible:String?=null
    constructor(request_name:String,phone_type:String,android_version:String,time:String,isVisible:String){
        this.request_name=request_name
        this.phone_type=phone_type
        this.android_version=android_version
        this.time=time
        this.isVisible=isVisible
    }
}