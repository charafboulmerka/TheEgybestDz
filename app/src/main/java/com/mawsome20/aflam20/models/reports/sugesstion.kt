package com.mawsome20.aflam20.models.reports

class sugesstion {

    var suggestion_description:String?=null
    var phone_type:String?=null
    var android_version:String?=null
    var time:String?=null
    var isVisible:String?=null
    constructor(suggestion_description:String,phone_type:String,android_version:String,time:String,isVisible:String){
        this.suggestion_description=suggestion_description
        this.phone_type=phone_type
        this.android_version=android_version
        this.time=time
        this.isVisible=isVisible
    }
}