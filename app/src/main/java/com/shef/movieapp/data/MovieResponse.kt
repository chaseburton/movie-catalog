package com.shef.movieapp.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,
    @SerializedName("results") val movies: List<MovieDetail>
)