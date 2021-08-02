package ru.unknowncoder.movieapp.presenter.interfaces

import ru.unknowncoder.movieapp.model.response.MovieBody

interface OnMovieClickListener {

    fun onMovieClick(movie: MovieBody)

    fun like(id: Int, position: Int)
}