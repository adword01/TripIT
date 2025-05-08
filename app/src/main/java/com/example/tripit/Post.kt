package com.example.tripit

data class Post(
    var content : String,
    var imageUrl : String,
    var location : String,
    var post_number : Int,
    var ProfileImage : String,
    var Post_Date: String,
    var username: String,
    var like: Int,
    var comment: String,
    var save:Boolean,
    val likes: Map<String, Boolean>? = null,
    val uid:String
){
    fun likeCount(): Int = likes?.size ?: 0
    fun isLikedBy(userId: String): Boolean = likes?.containsKey(userId) == true
constructor():this("","","",0,"","","",0,"",false,null,"")}