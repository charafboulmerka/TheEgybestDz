package com.mawsome20.aflam20.new_conception.models

class User {
    var uid:String?=null
    var name:String?=null
    var email:String?=null
    constructor(uid:String,name:String,email:String){
        this.uid=uid
        this.name=name
        this.email=email
    }

    constructor()
}