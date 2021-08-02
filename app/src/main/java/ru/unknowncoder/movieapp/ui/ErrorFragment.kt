package ru.unknowncoder.movieapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import ru.unknowncoder.movieapp.R
import kotlinx.android.synthetic.main.error_view.*

class ErrorFragment : AbstractFragment() {

    private lateinit var onRefreshClickListener: OnRefreshClickListener

    override fun getLayoutRes(): Int = R.layout.error_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshBtn.setOnClickListener { onRefreshClickListener.onRefreshClick() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onRefreshClickListener = activity as OnRefreshClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString())
        }
    }

    interface OnRefreshClickListener {

        fun onRefreshClick()
    }
}