package com.jomeva.timelinejorgemelendez.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jomeva.timelinejorgemelendez.model.Photo

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
){
    fun toPhoto(): Photo {

        return Photo(
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
