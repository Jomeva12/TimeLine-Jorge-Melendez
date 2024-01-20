package com.jomeva.timelinejorgemelendez

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.jomeva.timelinejorgemelendez.adapter.PhotoAdapter
import com.jomeva.timelinejorgemelendez.data.local.database.AppDatabase
import com.jomeva.timelinejorgemelendez.model.Photo
import com.jomeva.timelinejorgemelendez.api.ApiConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
     lateinit var photoAdapter: PhotoAdapter
    private lateinit var reciclerFotos:RecyclerView
    private lateinit var buscarEditText:EditText
    private lateinit var cargando: LottieAnimationView

    lateinit var apiConstants: ApiConstants
    lateinit var appDatabase:AppDatabase
    private val mainScope = MainScope()
    val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        reciclerFotos=findViewById(R.id.reciclerFotos)
        buscarEditText=findViewById(R.id.buscarEditText)


        photoAdapter = PhotoAdapter(mutableListOf(),this)
        reciclerFotos.adapter = photoAdapter
        reciclerFotos.layoutManager = LinearLayoutManager(this)
        photoAdapter.notifyDataSetChanged()
        cargando = findViewById(R.id.cargando)

        cargando.visibility = View.VISIBLE
        apiConstants=ApiConstants()
        val tag="cat"
        searchPhotos(tag,false)

        reciclerFotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Verificamos si el usuario llega al final del RecyclerView
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    Log.d("escrinsss", totalItemCount.toString())
                    searchPhotos(tag, true)
                }
            }
        })


        buscarEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Realiza la búsqueda cuando el texto cambia

               val text = buscarEditText.text.toString()

                mainScope.launch {
                    delay(1000L) // <-- Delay dentro de la primera corrutina

                    // Ahora lanzamos la segunda corrutina para la búsqueda
                    launch(Dispatchers.IO) {
                        Log.d("escrin", text)
                        searchPhotos(text, true)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                 Log.d("escrin", "text")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("escrin", "text")
            }
        })
    }


   fun searchPhotos(query: String, isSearch: Boolean) {
       MainScope().launch {
           try {
               cargando.visibility = View.GONE

               // Obtengo fotos almacenadas localmente
                appDatabase = AppDatabase.getInstance(context)
               val cachedPhotos = withContext(Dispatchers.IO) {
                   appDatabase.photoDao().getAllPhotos()
               }


               val photos = if (cachedPhotos.isEmpty()) {
                   // No hay fotos en caché, realizo la solicitud a la API
                   val response = apiConstants.searchPhotos(query)
                   val newPhotos = response.photos.photo
                   Log.d("cachedPhotos1", "${newPhotos}")
                   withContext(Dispatchers.IO) {
                       appDatabase.photoDao().insertPhotos(newPhotos.map { it.toPhotoEntity() })
                   }
                   photoAdapter.updatePhotos(newPhotos as MutableList<Photo>)
                   photoAdapter.notifyDataSetChanged()

               } else {
                   if (!isNetworkAvailable(context)) {
                       val photo = if (isSearch) {
                           // Búsqueda local incluso sin conexión a Internet
                           val filteredPhotos = cachedPhotos.map { it.toPhoto() }.filter { photo ->
                               photo.title.contains(query, ignoreCase = true)
                           }
                           Log.d("cachedPhotos3", "${filteredPhotos}")
                           filteredPhotos
                       } else {
                           // Sin conexión a Internet y no es una búsqueda local
                           cachedPhotos.map { it.toPhoto() }
                       }

                       Log.d("cachedPhotos2", "${photo}")
                       photoAdapter.updatePhotos(photo as MutableList<Photo>)
                       photoAdapter.notifyDataSetChanged()

                       Log.d("cachedPhotos4", "...")
                   } else {
                       // Hay conexión a Internet

                       // Eliminar todas las fotos existentes en la caché
                     /*  val cachedPhotosAfterDeletion = withContext(Dispatchers.IO) {
                           appDatabase.photoDao().deleteAllPhotos()
                       }*/

                       // Insertar nuevas fotos de la API en la caché
                       val response = apiConstants.searchPhotos(query)
                       val newPhotos = response.photos.photo
                       withContext(Dispatchers.IO) {
                           appDatabase.photoDao().insertPhotos(newPhotos.map { it.toPhotoEntity() })
                       }

                       photoAdapter.updatePhotos(newPhotos as MutableList<Photo>)
                       photoAdapter.notifyDataSetChanged()
                   }
               }

           } catch (e: Exception) {
               Log.d("sssssa3", e.toString())
               cargando.visibility = View.GONE
               e.printStackTrace()
           }
       }
   }
    //verifico conexion a internet
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

}