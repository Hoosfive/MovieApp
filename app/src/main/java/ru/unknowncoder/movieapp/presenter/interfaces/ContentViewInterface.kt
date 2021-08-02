package ru.unknowncoder.movieapp.presenter.interfaces

import android.view.View
import androidx.annotation.StringRes
import ru.unknowncoder.movieapp.model.response.MovieBody

interface ContentView {

    fun showLoadingContent()

    fun hideLoadingContent()

    fun showLoadingError()

    fun setMovies(movies: List<MovieBody>)

    fun hideRefreshing()

    fun showConnectionError(@StringRes messageRes: Int)

    fun returnView() : View?
}