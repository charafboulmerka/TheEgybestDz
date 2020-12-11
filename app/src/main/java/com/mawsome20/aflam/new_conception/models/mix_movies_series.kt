package com.mawsome20.aflam.new_conception.models

class mix_movies_series {
    var id:String?=null
    var title:String?=null
    var pic_cover:String?=null
    var pic_profile:String?=null
    var geners:String?=null
    var year:String?=null
    var type:String?=null
    var name_class:String?=null

    constructor(id:String,title:String,pic_cover:String,pic_profile:String,geners:String,
                year:String,type:String,name_class:String){
        this.id=id
        this.title=title
        this.pic_cover=pic_cover
        this.pic_profile=pic_profile
        this.geners=geners
        this.year=year
        this.type=type
        this.name_class=name_class
    }
    constructor()
}