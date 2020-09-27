package com.mawsome20.aflam20.new_conception.models

class Series {
    var id:String?=null
    var title:String?=null
    var description:String?=null
    var pic_cover:String?=null
    var pic_profile:String?=null
    var geners:String?=null
    var seasons:ArrayList<Seasons>?=null
    var type:String?=null
    var year:String?=null
    var director:String?=null
    constructor(id:String,title:String,description:String,pic_cover:String,pic_profile:String,geners:String,seasons:ArrayList<Seasons>,
                type:String,year:String,director:String){
        this.id=id
        this.title=title
        this.description=description
        this.pic_cover=pic_cover
        this.pic_profile=pic_profile
        this.geners=geners
        this.seasons=seasons
        this.type=type
        this.year=year
        this.director=director
    }
    constructor()
}