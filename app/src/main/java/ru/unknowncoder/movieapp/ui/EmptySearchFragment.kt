package ru.unknowncoder.movieapp.ui

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.empty_search_view.*
import ru.unknowncoder.movieapp.R

class EmptySearchFragment : AbstractFragment() {

    private var query: String? = null

    companion object {
        private const val QUERY_BUNDLE_KEY = "QUERY"

        fun newInstance(query: String): EmptySearchFragment {
            val emptySearchFragment = EmptySearchFragment()
            val args = Bundle()
            args.putString(QUERY_BUNDLE_KEY, query)
            emptySearchFragment.arguments = args
            return emptySearchFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = arguments?.getString(QUERY_BUNDLE_KEY, "")
    }

    override fun getLayoutRes(): Int = R.layout.empty_search_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvEmpty.text = view.context.getString(R.string.empty_search, query)
    }
}