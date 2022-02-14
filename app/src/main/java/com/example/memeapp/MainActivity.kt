package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var currentImageUrl: String? = null
    private var memeImageView = findViewById<ImageView>(R.id.memeImageView)
    private var progressBar = findViewById<ProgressBar>(R.id.progressBar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private fun loadMeme() {
        progressBar.visibility = View.VISIBLE
        // Instantiate the RequestQueue.
        //volley inserts all the requests sent by the client in the priority queue
        //so we create a priority queue in singleton class

        //pass the url of the API from where we gonna get the response
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        //sends a get request to the url if we get the response then we return the response
        //else it goes to error listener
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentImageUrl = response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(memeImageView)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
        //instead of creating multiple instances of volley we write it in a singleton class
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        //create an intent to for sharing it externally to device
        //create a chooser which says to which platform we want to share the meme
        val intent = Intent(Intent.ACTION_SEND)
        //writing the type of the thing we are sharing ..here text
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme i got from reddit through my own app $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using")
        startActivity(chooser)

    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}