package ru.unknowncoder.movieapp.presenter

import ru.unknowncoder.movieapp.R
import ru.unknowncoder.movieapp.model.NetworkService
import ru.unknowncoder.movieapp.model.response.MovieBody
import ru.unknowncoder.movieapp.presenter.interfaces.MoviesView


class MoviesPresenter(private val moviesView: MoviesView) {

    private fun loadMovies(loadingState: LoadingState, page: Int = 1) {
        NetworkService.discoverMovies(page, { it ->
            val result = it.results?.requireNoNulls()
            if (!result.isNullOrEmpty()) {
                result.forEach {
                    it.posterPath = NetworkService.BASE_IMAGE_URL + it.posterPath
                    it.parseDate()
                }
                onLoadingSuccess(loadingState, result)
            } else {
                onLoadingFailed(loadingState)
            }
            onLoadingFinish(loadingState)
        }, {
            onLoadingFailed(loadingState)
            onLoadingFinish(loadingState)
        })
        onLoadingFinish(loadingState)

    }

    private fun searchMovies(loadingState: LoadingState, page: Int = 1, query: String) {
        NetworkService.searchMovies(page, query, { it ->
            val result = it.results?.requireNoNulls()
            if (!result.isNullOrEmpty()) {
                result.forEach {
                    it.posterPath = NetworkService.BASE_IMAGE_URL + it.posterPath
                    it.parseDate()
                }
                onLoadingSuccess(loadingState, result)
            }
            else {
                onLoadingFailed(loadingState)
            }
            onLoadingFinish(loadingState)
        }, {
            onLoadingFinish(loadingState)
            onLoadingFailed(loadingState)
        })
    }

    private fun showProgress(loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.LoadingFirst -> moviesView.showLoadingContent()
            LoadingState.Searching -> moviesView.showSearchProgress()
            else -> return
        }
    }

    private fun hideProgress(loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.LoadingFirst -> moviesView.hideLoadingContent()
            LoadingState.Refreshing -> moviesView.hideRefreshing()
            LoadingState.Searching -> moviesView.hideSearchProgress()
        }
    }

    fun loadStart() {
        showProgress(LoadingState.LoadingFirst)
        loadMovies(LoadingState.LoadingFirst)
    }

    fun loadRefresh() {
        showProgress(LoadingState.Refreshing)
        loadMovies(LoadingState.Refreshing)
    }

    fun loadForSearch(pageNumber: Int, query: String) {
        showProgress(LoadingState.Searching)
        searchMovies(LoadingState.Searching, pageNumber, query)
    }

    private fun onLoadingFinish(loadingState: LoadingState) {
        hideProgress(loadingState)
    }

    private fun onLoadingSuccess(loadingState: LoadingState, movies: List<MovieBody>) {
        when (loadingState) {
            LoadingState.LoadingFirst, LoadingState.Refreshing -> moviesView.setMovies(movies)
            LoadingState.Searching -> moviesView.setSearchResult(movies)
        }
    }

    private fun onLoadingFailed(loadingState: LoadingState) {
        val view = moviesView.returnView()
        if (view != null)
            if (moviesView.hasContent() && !NetworkService.checkIsOnline(view.context)
                || loadingState == LoadingState.Refreshing
            ) {
                showConnectionError()
            } else {
                when (loadingState) {
                    LoadingState.LoadingFirst -> moviesView.showLoadingError()
                    LoadingState.Searching -> moviesView.showSearchError()
                    else -> return
                }
            }
    }

    private fun showConnectionError(): Unit =
        moviesView.showConnectionError(R.string.snack_bar_connection_error)
}