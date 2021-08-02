package ru.unknowncoder.movieapp.model.response

import com.google.gson.annotations.Expose

data class ResponseBody(
	@Expose val page: Int? = null,
	@Expose val results: List<MovieBody?>? = null,
	val totalPages: Int? = null,
	val totalResults: Int? = null
)