package com.shef.movieapp.services

import com.shef.movieapp.data.MovieDetail
import com.shef.movieapp.data.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movies/")
    suspend fun getMovies(@Query("page") page: Int): MovieResponse

    @GET("movies/{id}")
    suspend fun getMovieDetail(@Path("id") id: Int): MovieDetail

}