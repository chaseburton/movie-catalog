package com.shef.movieapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shef.movieapp.RecyclerView.CategoryAdapter
import com.shef.movieapp.data.MovieDetail
import com.shef.movieapp.data.MovieViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class MainActivity : BaseActivity() {

    private val movieViewModel: MovieViewModel by viewModels()

    // Map of genre IDs to genre names
    private val genreMap = mapOf(
        35 to "Comedy",
        27 to "Horror",
        28 to "Action",
//        35 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
//        16 to "Animation",
        36 to "History",
        10749 to "Romance",
        53 to "Thriller",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val verticalRecyclerView = findViewById<RecyclerView>(R.id.verticalRecyclerView)
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        verticalRecyclerView.layoutManager = LinearLayoutManager(this)

        val categoryAdapter = CategoryAdapter(mutableListOf())
        verticalRecyclerView.adapter = categoryAdapter

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
                    // Handle discover action
                    val intent = Intent(this, FavouritesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        movieViewModel.movieList.observe(this, Observer { movieList: List<MovieDetail>? ->
            // movieList is a List<MovieDetail>

            // Create a mutable map to group movies by genre
            val genreMovieMap = mutableMapOf<String, MutableList<MovieDetail>>()

            // Loop through each movie
            movieList?.forEach { movie ->
                // For each genre ID of the movie, add the movie to the corresponding genre category
                movie.genre_ids.forEach { genreId ->
                    val genreName = genreMap[genreId]
                    if (genreName != null) {
                        if (!genreMovieMap.containsKey(genreName)) {
                            genreMovieMap[genreName] = mutableListOf()
                        }
                        genreMovieMap[genreName]?.add(movie)
                    }
                }
            }

            // Randomize the order of movies in each category
            genreMovieMap.forEach { (genreName, movieList) ->
                genreMovieMap[genreName] = movieList.shuffled().toMutableList()
            }

            // Convert the map to a List<CategoryData> for the CategoryAdapter
            val categoryList = genreMovieMap.map { (title, movies) ->
                CategoryData(title, movies)
            }

            // Update the adapter data
            categoryAdapter.setData(categoryList)

            // Stop refreshing animation
            swipeRefreshLayout.isRefreshing = false
        })


        swipeRefreshLayout.setOnRefreshListener {
            // call API to refresh data
            movieViewModel.refreshData()
        }
    }
}

