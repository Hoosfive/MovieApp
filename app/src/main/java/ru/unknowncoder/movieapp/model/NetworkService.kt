package ru.unknowncoder.movieapp.model

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.unknowncoder.movieapp.model.api.MoviesAPI

class NetworkService {


    companion object {
        private var retrofit: Retrofit

        private const val API_KEY_NAME = "api_key"
        private const val API_KEY_VALUE = "6ccd72a2a8fc239b13f209408fc31c33"

        private const val LANGUAGE_NAME = "language"
        private const val LANGUAGE_VALUE = "ru-RU"

        private const val IMAGE_SIZE = "w342/"
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/" + IMAGE_SIZE

        init {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .build()

            val gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

            retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build()
        }

        class ApiKeyInterceptor : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()

                val url = originalRequest.url.newBuilder()
                    .addQueryParameter(API_KEY_NAME, API_KEY_VALUE)
                    .addQueryParameter(LANGUAGE_NAME, LANGUAGE_VALUE)
                    .build()

                val request = originalRequest.newBuilder()
                    .url(url)
                    .build()
                return chain.proceed(request)
            }
        }

        private val MovieAPI = retrofit.create(MoviesAPI::class.java)

        fun discoverMovies(
            page: Int,
            onSuccess: (ru.unknowncoder.movieapp.model.response.ResponseBody) -> Unit,
            onError: (Throwable) -> Unit = { }
        ) {
            MovieAPI
                .discoverMovies(page)
                .enqueue(
                    RetrofitCallback(
                        { data -> onSuccess(data) },
                        { error -> onError(error) }
                    )
                )
        }

        fun searchMovies(
            page: Int,
            query: String,
            onSuccess: (ru.unknowncoder.movieapp.model.response.ResponseBody) -> Unit,
            onError: (Throwable) -> Unit = { }
        ) {
            MovieAPI
                .searchMovies(page, query)
                .enqueue(
                    RetrofitCallback(
                        { data -> onSuccess(data) },
                        { error -> onError(error) }
                    )
                )
        }

        fun checkIsOnline(context: Context): Boolean {
            val manager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manager.activeNetworkInfo
            return info != null && info.isConnected
        }
    }
}