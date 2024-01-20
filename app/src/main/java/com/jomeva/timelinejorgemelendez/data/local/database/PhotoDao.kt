package com.jomeva.timelinejorgemelendez.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jomeva.timelinejorgemelendez.model.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos")
     fun getAllPhotos(): MutableList<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
      fun insertPhotos(photos: List<PhotoEntity>)
    @Query("DELETE FROM photos")
    fun deleteAllPhotos()
}
