package ru.unknowncoder.movieapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.unknowncoder.movieapp.R
import ru.unknowncoder.movieapp.model.response.MovieBody
import ru.unknowncoder.movieapp.presenter.MoviesPresenter
import ru.unknowncoder.movieapp.presenter.interfaces.MainActivityInterface

class MainActivity :
    AppCompatActivity(),
    MainActivityInterface,
    ErrorFragment.OnRefreshClickListener {

    private lateinit var moviesFragment: MoviesFragment
    private val errorFragment by lazy { ErrorFragment() }
    private var emptySearchFragment: EmptySearchFragment? = null

    private lateinit var presenter: MoviesPresenter

    companion object {
        private const val MOVIE_FRAGMENT_TAG = "Movies"
        private const val CONTAINER_ID = R.id.fragmentContainer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupSearchView()

        val foundFragment = supportFragmentManager.findFragmentByTag(MOVIE_FRAGMENT_TAG)
        moviesFragment = foundFragment as MoviesFragment? ?: MoviesFragment()
        setContentFragment()
        presenter = MoviesPresenter(moviesFragment)
    }

    override fun onBackPressed() {
        val containsEmpty = emptySearchFragment != null
                && this.supportFragmentManager.fragments.contains(emptySearchFragment!!)
        if (containsEmpty ||
            this.supportFragmentManager.fragments.contains(moviesFragment) &&
            moviesFragment.hasSearchedContent()
        ) {

            moviesFragment.clearSearchResult()
            resetSearchView()
            if (containsEmpty) {
                setContentFragment()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.loadForSearch(1, query)
                emptySearchFragment = EmptySearchFragment.newInstance(query)
                return true
            }
        })
    }

    fun clearSearch(searchView: SearchView) {
        searchView.setQuery("", false)
        searchView.clearFocus()
    }

    private fun setFragment(fragment: AbstractFragment) {
        supportFragmentManager.beginTransaction()
            .replace(CONTAINER_ID, fragment)
            .commit()
    }

    private fun setContentFragment() {
        supportFragmentManager.beginTransaction()
            .replace(CONTAINER_ID, moviesFragment, MOVIE_FRAGMENT_TAG)
            .commit()
    }

    override fun onRefreshClick(): Unit = setContentFragment()

    override fun showLoadingError(): Unit = setFragment(errorFragment)

    override fun resetSearchView(): Unit = clearSearch(searchView)

    override fun showSearchError(): Unit = setFragment(emptySearchFragment!!)

    override fun showSearchProgress() {
        searchingProgressBar.visibility = View.VISIBLE

        // спрятать клавиатуру
        val view = this.currentFocus
        if (view != null) {
            val ims = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            ims.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun hideSearchProgress() {
        searchingProgressBar.visibility = View.INVISIBLE
    }

    override fun setSearchResult(movies: List<MovieBody>): Unit = setContentFragment()
}

