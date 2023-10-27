package com.example.tripit

data class Post(
    var content : String,
    var imageUrl : String,
    var location : String,
    var post_number : Int,
    var ProfileImage : String,
    var Post_Date: String,
    var username: String
){
constructor():this("","","",0,"","","")}