package com.coding.albums.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Album {
   /* @SerializedName("albumId")
    @Expose
    var albumId = 0

    @SerializedName("id")
    @Expose
    var id = 0*/

    @SerializedName("title")
    @Expose
    var title: String? = null


  /*  constructor(albumId: Int, id: Int, title: String?) {
        this.albumId = albumId
        this.id = id
        this.title = title
    }*/
}