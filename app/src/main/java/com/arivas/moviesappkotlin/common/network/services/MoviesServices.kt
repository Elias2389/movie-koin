package com.arivas.moviesappkotlin.common.network.services

import com.arivas.moviesappkotlin.common.dto.MoviesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface MoviesServices {
    @GET("popular")
    suspend fun getPopularMoviesAsync(): Response<MoviesResponse>
}