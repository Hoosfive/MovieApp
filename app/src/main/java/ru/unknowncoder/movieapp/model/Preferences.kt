package ru.unknowncoder.movieapp.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Preferences {
    companion object {
        private const val USER_PREFERENCES = "UserLikes"

        private fun getPref(context: Context): SharedPreferences {
            return context.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)
        }

        fun like(context: Context, id: Int) {
            getPref(context).edit().putBoolean(id.toString(), !isMovieLiked(context, id)).apply()
        }

        fun isMovieLiked(context: Context, id: Int): Boolean {
            return getPref(context).getBoolean(
                id.toString(),
                false
            )
        }
    }
}