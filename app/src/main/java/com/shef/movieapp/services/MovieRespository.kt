package com.shef.movieapp.services

import com.shef.movieapp.data.MovieDetail
import com.shef.movieapp.data.MovieResponse

class MovieRepository(private val movieService: MovieService) {
    suspend fun getMovies(page: Int): MovieResponse {
        return movieService.getMovies(page)
    }

    suspend fun getMovieDetail(id: Int): MovieDetail {
        return movieService.getMovieDetail(id)
    }
}




