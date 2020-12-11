package com.mawsome20.aflam.models.reports

class issue {
    var issue_description:String?=null
    var phone_type:String?=null
    var android_version:String?=null
    var time:String?=null
    var isVisible:String?=null
    constructor(issue_description:String,phone_type:String,android_version:String,time:String,isVisible:String){
        this.issue_description=issue_description
        this.phone_type=phone_type
        this.android_version=android_version
        this.time=time
        this.isVisible=isVisible
    }
}