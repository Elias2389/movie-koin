package com.arivas.moviesappkotlin.ui.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arivas.moviesappkotlin.common.network.services.MoviesServices

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(private val moviesServices: MoviesServices): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(moviesServices) as T
    }
}