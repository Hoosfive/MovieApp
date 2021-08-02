package ru.unknowncoder.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_movie_card.view.*
import ru.unknowncoder.movieapp.R
import ru.unknowncoder.movieapp.model.Preferences
import ru.unknowncoder.movieapp.model.response.MovieBody
import ru.unknowncoder.movieapp.presenter.interfaces.OnMovieClickListener


class MovieAdapter(private val movieClickListener: OnMovieClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = ArrayList<MovieBody>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_movie_card, parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int): Unit =
        holder.bind(movies[position], position, movieClickListener)

    override fun getItemCount() = movies.size


    fun hasContent() = itemCount > 0


    fun clear() = movies.clear()

    fun setMovies(movies: List<MovieBody>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            movie: MovieBody,
            position: Int,
            onMovieClickListener: OnMovieClickListener
        ): Unit = with(itemView) {
            movieNameTV.text = movie.title
            movieDescriptionTV.text = movie.overview
            dateTV.text = movie.releaseDate
            Glide.with(itemView).load(movie.posterPath).into(posterIV)
            setOnClickListener { onMovieClickListener.onMovieClick(movie) }
            val imageIcon =
                if (!Preferences.isMovieLiked(context, movie.id!!))
                    R.drawable.ic_heart
                else
                    R.drawable.ic_heart_activated
            likeBtn.setOnClickListener { onMovieClickListener.like(movie.id, position) }
            likeBtn.setImageResource(imageIcon)
        }
    }


}