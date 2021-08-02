package ru.unknowncoder.movieapp.presenter.interfaces

import ru.unknowncoder.movieapp.model.response.MovieBody

interface SearchingView {

    fun showSearchProgress()

    fun showSearchError()

    fun hideSearchProgress()

    fun setSearchResult(movies: List<MovieBody>)
}