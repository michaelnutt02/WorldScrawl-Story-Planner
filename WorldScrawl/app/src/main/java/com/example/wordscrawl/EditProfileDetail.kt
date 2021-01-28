package com.example.wordscrawl

data class EditProfileDetail(var title: String = "", var body: String = "", var numChar:Int = 0, var profileId:String = "") {

    fun setDetailText(newBody:String){
        body = newBody
        numChar = body.count()
    }
    fun setDetailTitle(newTitle:String){
        title = newTitle
    }

}