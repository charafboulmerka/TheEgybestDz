package com.mawsome20.aflam20.new_conception.models

class episodes {
    var ep_title:String?=null
    var streaming_links:ArrayList<strm_link>?=null
    var download_links:ArrayList<String>?=null
    constructor(ep_title:String,streaming_links:ArrayList<strm_link>,download_links:ArrayList<String>){
        this.ep_title=ep_title
        this.streaming_links=streaming_links
        this.download_links=download_links
    }

    constructor()
}