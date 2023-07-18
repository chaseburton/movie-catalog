package com.shef.movieapp.data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.shef.movieapp.services.MovieRepository
import com.shef.movieapp.services.MovieService
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieViewModel : ViewModel() {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .header("Content-Type", "application/json")

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://app-vpigadas.herokuapp.com/api/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val movieService = retrofit.create(MovieService::class.java)
    private val movieRepository = MovieRepository(movieService)

    val movieList = MutableLiveData<List<MovieDetail>>()
    val movieDetail = MutableLiveData<MovieDetail>()

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            try {
                // Fetching the movies and storing them
                val allMovies = mutableListOf<MovieDetail>()
                val movieResponse = withContext(Dispatchers.IO) { movieRepository.getMovies(1) }
                allMovies.addAll(movieResponse.movies)

                // Post the fetched movies
                movieList.postValue(allMovies)
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error fetching movies", e)
            } finally {
                if (movieList.value != null) {
                    movieList.postValue(movieList.value) // Post the current value to trigger an update
                }
            }
        }
    }

    fun fetchMovieDetail(id: Int) {
        viewModelScope.launch {
            try {
                val movieDetailResponse = withContext(Dispatchers.IO) { movieRepository.getMovieDetail(id) }
                movieDetail.postValue(movieDetailResponse)
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error fetching movie detail", e)
            }
        }
    }

    fun refreshData() {
        fetchMovies()
    }
}
