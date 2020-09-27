package com.mawsome20.aflam20.new_conception.models

class Seasons {
    var season_title:String?=null
    var season_pic:String?=null
    var episodes:ArrayList<episodes>?=null
    constructor(season_title:String,season_pic:String,episodes:ArrayList<episodes>){
        this.season_title=season_title
        this.season_pic=season_pic
        this.episodes=episodes
    }
    constructor()
}