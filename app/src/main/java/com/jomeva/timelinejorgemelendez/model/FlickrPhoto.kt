package com.jomeva.timelinejorgemelendez.model

import com.jomeva.timelinejorgemelendez.data.local.database.PhotoEntity

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
){
    fun toPhotoEntity(): PhotoEntity {
        return PhotoEntity(
            id,
            owner,
            secret,
            server,
            farm,
            title,
            ispublic,
            isfriend,
            isfamily
        )
    }
}

data class Photos(
   /* val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,*/
    val photo: List<Photo>
)


