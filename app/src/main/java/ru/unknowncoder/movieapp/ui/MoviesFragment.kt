package ru.unknowncoder.movieapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movies_list.*
import kotlinx.android.synthetic.main.view_movie_card.*
import ru.unknowncoder.movieapp.R
import ru.unknowncoder.movieapp.model.response.MovieBody
import ru.unknowncoder.movieapp.presenter.MoviesPresenter
import ru.unknowncoder.movieapp.presenter.interfaces.MainActivityInterface
import ru.unknowncoder.movieapp.presenter.interfaces.MoviesView
import ru.unknowncoder.movieapp.presenter.interfaces.OnMovieClickListener
import ru.unknowncoder.movieapp.ui.adapter.MovieAdapter

class MoviesFragment : AbstractFragment(), MoviesView {

    private var presenter: MoviesPresenter = MoviesPresenter(this)

    private lateinit var activityInterface: MainActivityInterface

    private val adapter: MovieAdapter by lazy { MovieAdapter(movieClickListener) }

    private val searchAdapter: MovieAdapter by lazy { MovieAdapter(movieClickListener) }

    private lateinit var progressBarLoading: ProgressBar
    private lateinit var moviesListRV: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout

    private val movieClickListener = object : OnMovieClickListener {
        override fun onMovieClick(movie: MovieBody) {
            Snackbar.make(swipeRefreshLayout, movie.title!!, Snackbar.LENGTH_SHORT).show()
        }

        override fun like(id: Int, position: Int) {
            ru.unknowncoder.movieapp.model.Preferences.like(context!!, id)
            likeBtn.isSelected = !likeBtn.isSelected
            getActiveAdapter().notifyItemChanged(position)
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_movies_list


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarLoading = loadingProgressBar

        movies_rv.layoutManager = LinearLayoutManager(context)
        movies_rv.adapter = if (searchAdapter.hasContent()) searchAdapter else adapter
        moviesListRV = movies_rv

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener {
            presenter.loadRefresh()
            activityInterface.resetSearchView()
        }
        swipeLayout = swipeRefreshLayout

        startLoading()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityInterface = activity as MainActivityInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString())
        }
    }

    override fun setPresenter(presenter: MoviesPresenter) {
        this.presenter = presenter
    }

    private fun startLoading() {
        if (!hasContent()) {
            presenter.loadStart()
        }
    }

    fun hasSearchedContent(): Boolean = searchAdapter.hasContent()


    private fun getActiveAdapter(): MovieAdapter = moviesListRV.adapter as MovieAdapter

    override fun hasContent(): Boolean = getActiveAdapter().itemCount > 0

    override fun showLoadingContent() {
        progressBarLoading.visibility = View.VISIBLE
    }

    override fun hideLoadingContent() {
        progressBarLoading.visibility = View.GONE
    }

    override fun showLoadingError(): Unit = activityInterface.showLoadingError()

    override fun setMovies(movies: List<MovieBody>) {
        adapter.setMovies(movies)
        clearSearchResult()
    }

    override fun hideRefreshing() {
        swipeLayout.isRefreshing = false
    }

    override fun showConnectionError(messageRes: Int): Unit =
        Snackbar.make(swipeLayout, messageRes, Snackbar.LENGTH_LONG).show()

    override fun showSearchProgress(): Unit = activityInterface.showSearchProgress()

    override fun hideSearchProgress(): Unit = activityInterface.hideSearchProgress()

    override fun showSearchError(): Unit = activityInterface.showSearchError()

    override fun setSearchResult(movies: List<MovieBody>) {
        searchAdapter.setMovies(movies)
        moviesListRV.adapter = searchAdapter
        activityInterface.setSearchResult(movies)
    }

    fun clearSearchResult() {
        searchAdapter.clear()
        moviesListRV.adapter = adapter
        startLoading()
    }

    override fun returnView(): View? {
        return view
    }
}