package com.shef.movieapp.data

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("genre_ids") val genre_ids: List<Int>,
    @SerializedName("vote_average") val vote_average: String,
)
