package ru.unknowncoder.movieapp.model.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.unknowncoder.movieapp.model.response.ResponseBody

interface MoviesAPI {

    companion object {
        private const val DISCOVER_MOVIE = "discover/movie"
        private const val SEARCH_MOVIE = "search/movie"

        private const val SORT_NAME = "sort_by"
        private const val SORT_VALUE = "popularity.desc"

        private const val QUERY_PAGE = "page"
        private const val QUERY = "query"
    }

    @GET(DISCOVER_MOVIE)
    fun discoverMovies(
        @Query(QUERY_PAGE) page: Int,
        @Query(SORT_NAME) sort: String = SORT_VALUE
    ): Call<ResponseBody>


    @GET(SEARCH_MOVIE)
    fun searchMovies(
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY) query: String
    ): Call<ResponseBody>
}