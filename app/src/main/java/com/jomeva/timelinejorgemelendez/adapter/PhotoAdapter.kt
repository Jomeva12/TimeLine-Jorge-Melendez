package com.jomeva.timelinejorgemelendez.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jomeva.timelinejorgemelendez.ImageDetailActivity
import com.jomeva.timelinejorgemelendez.R
import com.jomeva.timelinejorgemelendez.data.local.database.AppDatabase
import com.jomeva.timelinejorgemelendez.model.Photo
import com.squareup.picasso.Picasso
import java.io.File

class PhotoAdapter(private var photos: MutableList<Photo>, private val context: Context) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)

        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        val currentPhoto = photos[position]
        val currentTitle = currentPhoto.title

        // Verifico si la imagen está en caché
        val cachedImage = getCachedImage(currentPhoto)
        Log.d("PhotoAdapter1", "$cachedImage")
        if (cachedImage != null) {
            holder.photoImageView.setImageBitmap(cachedImage)
        } else {
            val photoUrl = getPhotoUrl(currentPhoto)
            Picasso.get().load(photoUrl).into(holder.photoImageView)
        }


        holder.titleTextView.text = currentTitle
        //envío laimagen y el titulo para ver el detalle
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ImageDetailActivity::class.java)
            intent.putExtra("photo", getPhotoUrl(currentPhoto))
            intent.putExtra("titulo", currentTitle)
            context.startActivity(intent)
        }
    }
    private fun getCachedImage(photo: Photo): Bitmap? {
        val imagePath = getImagePath(photo)
        Log.d("PhotoAdapter2", "Image Path: $imagePath")

        try {
            if (File(imagePath).exists()) {
                return BitmapFactory.decodeFile(imagePath)
            } else {
                Log.e("PhotoAdapter2", "File does not exist: $imagePath")
                return null
            }
        } catch (e: Exception) {
            Log.e("PhotoAdapter3", "Error decoding image", e)
            return null
        }

    }

    private fun getImagePath(photo: Photo): String {
        return "${context.filesDir}/images/${photo.id}.jpg"
    }

    override fun getItemCount() = photos.size

    private fun getPhotoUrl(photo: Photo): String {
        return "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg"
    }

    fun updatePhotos(newPhotos: MutableList<Photo>) {
        photos = newPhotos
        notifyDataSetChanged()
        Log.d("PhotoAdapter8", "Dataset updated. New size: ${photos.size}")
    }


}
public class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    init {
        Log.d("ViewHolder", "Inicializando ViewHolder")

    }
}