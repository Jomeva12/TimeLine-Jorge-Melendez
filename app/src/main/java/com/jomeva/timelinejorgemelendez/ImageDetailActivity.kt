package com.jomeva.timelinejorgemelendez

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import com.jomeva.timelinejorgemelendez.model.Photo
import com.squareup.picasso.Picasso

class ImageDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val photoView: PhotoView = findViewById(R.id.detailImageView)
        val titleTextView: TextView = findViewById(R.id.titulo)
        val back:ImageView= findViewById(R.id.back)
        back.setOnClickListener {
            finish()
        }
        // Recupera los datos de la foto seleccionada desde el intent
        val photo = intent.getStringExtra("photo")
        val titulo = intent.getStringExtra("titulo")
        // Actualiza la interfaz de usuario con los detalles de la foto
        if (photo != null) {
                Picasso.get().load(photo).into(photoView)

            titleTextView.text = titulo
        }
    }
}