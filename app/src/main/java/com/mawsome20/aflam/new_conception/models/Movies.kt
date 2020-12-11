package com.mawsome20.aflam.new_conception.models

class Movies {
    var id:String?=null
    var title:String?=null
    var description:String?=null
    var pic_cover:String?=null
    var pic_profile:String?=null
    var geners:String?=null
    var stream_links:ArrayList<strm_link>?=null
    var download_links:ArrayList<String>?=null
    var type:String?=null
    var year:String?=null
    var director:String?=null
    constructor(id:String,title:String,description:String,pic_cover:String,pic_profile:String,geners:String,stream_links:ArrayList<strm_link>,
                download_links:ArrayList<String>,type:String,year:String,director:String){
        this.id=id
        this.title=title
        this.description=description
        this.pic_cover=pic_cover
        this.pic_profile=pic_profile
        this.geners=geners
        this.stream_links=stream_links
        this.download_links=download_links
        this.type=type
        this.year=year
        this.director=director
    }
    constructor()
}