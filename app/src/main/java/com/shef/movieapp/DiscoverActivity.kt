package com.shef.movieapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.shef.movieapp.data.MovieDetail
import com.shef.movieapp.data.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.abs
import kotlin.random.Random

class DiscoverActivity : BaseActivity() {
    private lateinit var gestureDetector: GestureDetector
    private val seenMovies = mutableListOf<Int>()
    private val favouriteMovies = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

        val sharedPreferencesHelper = SharedPreferencesHelper(this)

        val movieImage = findViewById<ImageView>(R.id.movieImage)
        val movieTitle = findViewById<TextView>(R.id.movieTitle)
        val description = findViewById<TextView>(R.id.description)

        val viewModel = ViewModelProvider(this)[MovieViewModel::class.java]

        viewModel.movieDetail.observe(this) { movieDetail: MovieDetail? ->
            movieDetail?.let {
                Glide.with(this).load(it.poster_path).into(movieImage)
                movieTitle.text = it.title
                description.text = it.overview
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
                    // Handle discover action
                    val intent = Intent(this, FavouritesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        fun loadNewMovie() {
            var newMovieId: Int
            do {
                newMovieId = Random.nextInt(1, 1001)
            } while (newMovieId in seenMovies)

            seenMovies.add(newMovieId)

            viewModel.fetchMovieDetail(newMovieId)
        }

        loadNewMovie()

        val swipeGestureListener = SwipeGestureListener()
        swipeGestureListener.onSwipeRight = {
            val lastSeenMovieId = seenMovies.last()
            favouriteMovies.add(lastSeenMovieId)
            sharedPreferencesHelper.addFavouriteMovieId(lastSeenMovieId.toString())
            loadNewMovie()
        }
        swipeGestureListener.onSwipeLeft = {
            loadNewMovie()
        }
        gestureDetector = GestureDetector(this, swipeGestureListener)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val swipeThreshold = 100
        private val swipeVelocityThreshold = 100

        var onSwipeRight: () -> Unit = {}
        var onSwipeLeft: () -> Unit = {}

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - e1.x
            if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                if (diffX > 0) {
                    animateCardSwipeRight()
                    onSwipeRight()
                } else {
                    animateCardSwipeLeft()
                    onSwipeLeft()
                }
            }
            return false
        }
    }

    private fun animateCardSwipeRight() {
        val cardView = findViewById<CardView>(R.id.cardView)
        val animator = ObjectAnimator.ofFloat(cardView, "translationX", cardView.width.toFloat())
        animator.duration = 300
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                cardView.setBackgroundColor(ContextCompat.getColor(this@DiscoverActivity, R.color.glow_green))
                val rotation = cardView.rotation + 15f
                cardView.pivotX = cardView.width.toFloat()
                cardView.pivotY = cardView.height.toFloat()
                cardView.rotation = rotation
            }

            override fun onAnimationEnd(animation: Animator) {
                cardView.translationX = 0f
                cardView.rotation = 0f
                cardView.setBackgroundColor(ContextCompat.getColor(this@DiscoverActivity, R.color.transparent))
            }
        })
        animator.start()
    }

    private fun animateCardSwipeLeft() {
        val cardView = findViewById<CardView>(R.id.cardView)
        val animator = ObjectAnimator.ofFloat(cardView, "translationX", -cardView.width.toFloat())
        animator.duration = 300
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                cardView.setBackgroundColor(ContextCompat.getColor(this@DiscoverActivity, R.color.glow_red))
                val rotation = cardView.rotation - 15f
                cardView.pivotX = 0f
                cardView.pivotY = cardView.height.toFloat()
                cardView.rotation = rotation
            }

            override fun onAnimationEnd(animation: Animator) {
                cardView.translationX = 0f
                cardView.rotation = 0f
                cardView.setBackgroundColor(ContextCompat.getColor(this@DiscoverActivity, R.color.transparent))
            }
        })
        animator.start()
    }

}

