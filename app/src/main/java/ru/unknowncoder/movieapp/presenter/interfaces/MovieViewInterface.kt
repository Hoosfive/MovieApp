package ru.unknowncoder.movieapp.presenter.interfaces

import ru.unknowncoder.movieapp.presenter.MoviesPresenter

interface MoviesView: ContentView, SearchingView {

    fun setPresenter(presenter: MoviesPresenter)

    fun hasContent(): Boolean
}