package com.oneroof.moviebrower.data.network

import com.oneroof.moviebrower.data.model.MovieResponse
import com.oneroof.moviebrower.data.others.API
import com.oneroof.moviebrower.data.others.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MyApi {
    @GET("/3/discover/movie?api_key=$API")
    fun getMovies(@Query("sort_by") sortBy: String, @Query("page")page:Int): Call<MovieResponse>

    @GET("/3/search/movie?api_key=$API")
    fun queryMovie(@Query("query") query: String): Call<MovieResponse>

    companion object {
        operator fun invoke() : MyApi{

            //var token:String = ""

            val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            httpClient.addInterceptor {
                val original: Request = it.request()
                var request: Request? = null
                //if(data!=null){
                //token = data?.token?:""
                request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    //.addHeader("Authorization", "Bearer $token")
                    .build()


                it.proceed(request)
            }
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            //httpClient.addInterceptor(networkConnectionInterceptor)
            httpClient.addInterceptor(interceptor)
            val client = httpClient.build()
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build()
                .create(MyApi::class.java)
        }
    }
}