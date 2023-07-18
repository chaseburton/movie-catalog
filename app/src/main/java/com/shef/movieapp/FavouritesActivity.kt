package com.shef.movieapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shef.movieapp.RecyclerView.ImageCardAdapter
import com.shef.movieapp.data.MovieDetail
import com.shef.movieapp.data.MovieViewModel

class FavouritesActivity : BaseActivity() {
    private lateinit var movieViewModel: MovieViewModel
    private val favouriteMovies = mutableListOf<MovieDetail>()
    private lateinit var imageCardAdapter: ImageCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        val favMoviesRecyclerView = findViewById<RecyclerView>(R.id.favMoviesRecyclerView)
        favMoviesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val favouriteMovieIds = sharedPreferencesHelper.getFavouriteMovieIds()

        if (favouriteMovieIds.isEmpty()) {
            Toast.makeText(this, "No favourites yet", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        imageCardAdapter = ImageCardAdapter(favouriteMovies)
        favMoviesRecyclerView.adapter = imageCardAdapter

        favouriteMovieIds.forEach { movieId ->
            movieViewModel.fetchMovieDetail(movieId.toInt())
        }

        movieViewModel.movieDetail.observe(this) { movieDetail ->
            movieDetail?.let {
                favouriteMovies.add(it)
                imageCardAdapter.notifyDataSetChanged()
            }
        }

        val removeButton = findViewById<Button>(R.id.removeButton)
        removeButton.setOnClickListener {
            // Handle remove button click
            val position = 0 // Replace with the position of the item to be removed
            if (position >= 0 && position < favouriteMovies.size) {
                val movieId = favouriteMovies[position].id.toString()
                sharedPreferencesHelper.removeFavouriteMovieId(movieId)
                favouriteMovies.removeAt(position)
                imageCardAdapter.notifyItemRemoved(position)
                Toast.makeText(this, "Movie removed from favourites", Toast.LENGTH_SHORT).show()
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle home action
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_discover -> {
                    // Handle discover action
                    val intent = Intent(this, DiscoverActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_favourites -> {
                    // Handle favourites action
                    val intent = Intent(this, FavouritesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
