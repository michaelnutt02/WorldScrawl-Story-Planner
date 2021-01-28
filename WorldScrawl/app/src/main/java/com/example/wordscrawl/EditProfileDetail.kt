package com.example.wordscrawl

data class EditProfileDetail(var title: String = "", var body: String = "", var numChar:Int = 0, var profileId:String = "", var detailId:String = "") {

    fun setDetailText(newBody:String){
        body = newBody
        numChar = body.count()
    }
    fun setDetailTitle(newTitle:String){
        title = newTitle
    }

}