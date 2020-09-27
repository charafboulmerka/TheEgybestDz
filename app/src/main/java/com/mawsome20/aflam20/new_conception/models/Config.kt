package com.mawsome20.aflam20.new_conception.models

class Config {
    var enable_ads:String?=null
    var ads_company:String?=null
    var auto_slider:String?=null
    var enable_update:String?=null
    var update_version:String?=null
    var new_update_text:String?=null
    var apk_url:String?=null
    var version_name:String?=null
    var is_skipable:String?=null
    constructor(enable_ads:String,ads_company:String,auto_slider:String,enable_update:String,update_version:String,new_update_text:String,apk_url:String,version_name:String,is_skipable:String){
        this.enable_ads=enable_ads
        this.ads_company=ads_company
        this.auto_slider=auto_slider
        this.enable_update=enable_update
        this.update_version=update_version
        this.new_update_text=new_update_text
        this.apk_url=apk_url
        this.version_name=version_name
        this.is_skipable=is_skipable
    }
    constructor()
}