package com.shef.movieapp

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun addFavouriteMovieId(movieId: String) {
        val favouriteMovieIds = getFavouriteMovieIds().toMutableSet()
        favouriteMovieIds.add(movieId)
        editor.putStringSet(FAVOURITE_MOVIE_IDS, favouriteMovieIds).apply()
    }

    fun removeFavouriteMovieId(movieId: String) {
        val favouriteMovieIds = getFavouriteMovieIds().toMutableSet()
        favouriteMovieIds.remove(movieId)
        editor.putStringSet(FAVOURITE_MOVIE_IDS, favouriteMovieIds).apply()
    }

    fun getFavouriteMovieIds(): Set<String> {
        return sharedPreferences.getStringSet(FAVOURITE_MOVIE_IDS, emptySet()) ?: emptySet()
    }

    companion object {
        private const val PREFS_NAME = "com.shef.movieapp"
        private const val FAVOURITE_MOVIE_IDS = "favourite_movie_ids"
    }
}
