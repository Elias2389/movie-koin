package com.arivas.moviesappkotlin.ui.movies.viewmodel

import androidx.lifecycle.*
import com.arivas.moviesappkotlin.common.dto.MoviesResponse
import com.arivas.moviesappkotlin.common.network.services.MoviesServices
import retrofit2.Response

class MoviesViewModel(private val moviesServices: MoviesServices): ViewModel() {

    var movies = MutableLiveData<Response<MoviesResponse>>()

    fun getPopularMovies(): LiveData<Response<MoviesResponse>> {
        return liveData {
            emit(moviesServices.getPopularMoviesAsync())
        }
    }

}