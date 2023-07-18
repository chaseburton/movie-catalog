package com.shef.movieapp

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlin.math.abs
import androidx.lifecycle.ViewModelProvider
import com.shef.movieapp.data.MovieViewModel

class MovieDetailsActivity : BaseActivity() {
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val movieImage = findViewById<ImageView>(R.id.movieImage)
        val movieTitle = findViewById<TextView>(R.id.movieTitle)
        val releaseDate = findViewById<TextView>(R.id.releaseDate)
        val description = findViewById<TextView>(R.id.description)
        val rating = findViewById<TextView>(R.id.rating)

        val viewModel = ViewModelProvider(this)[MovieViewModel::class.java]

        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId != -1) {
            viewModel.movieList.observe(this) { movies ->
                val movieDetail = movies.find { it.id == movieId }
                if (movieDetail != null) {
                    Glide.with(this).load(movieDetail.poster_path).into(movieImage)
                    movieTitle.text = movieDetail.title
                    releaseDate.text = "Release Date: ${movieDetail.release_date}"
                    description.text = movieDetail.overview
                    rating.text = movieDetail.vote_average.toString()
                }
            }
        }

        val extras = intent.extras
        if (extras != null) {
            val imageUrl = extras.getString("image_url")
            val title = extras.getString("title")
            val date = extras.getString("release_date")
            val desc = extras.getString("description")
            val rate = extras.getFloat("rating")

            Glide.with(this).load(imageUrl).into(movieImage)
            movieTitle.text = title
            releaseDate.text = "Release Date: $date"
            description.text = desc
            rating.text = rate.toString()
        }

        val swipeGestureListener = SwipeGestureListener()
        swipeGestureListener.onSwipeRight = {
            finish()
        }
        gestureDetector = GestureDetector(this, swipeGestureListener)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }
}

class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100

    var onSwipeRight: () -> Unit = {}

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val diffX = e2.x - e1.x
        val diffY = e2.y - e1.y
        if (abs(diffX) > abs(diffY)) {
            if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                if (diffX > 0) {
                    onSwipeRight()
                }
            }
        }
        return false
    }
}
