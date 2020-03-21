package com.arivas.moviesappkotlin.ui.movies.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.arivas.moviesappkotlin.R
import com.arivas.moviesappkotlin.common.dto.ResultsItem
import com.arivas.moviesappkotlin.ui.movies.adapter.PopularMoviesRecyclerView
import com.arivas.moviesappkotlin.ui.movies.repository.MoviesRepository
import com.arivas.moviesappkotlin.ui.movies.viewmodel.MoviesViewModel
import com.arivas.moviesappkotlin.ui.movies.viewmodel.MoviesViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.koin.android.ext.android.inject


class MoviesActivity : AppCompatActivity() {
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var container: RelativeLayout
    private lateinit var lottieAnimation: LottieAnimationView
    private lateinit var searchView: SearchView
    private lateinit var adapter: PopularMoviesRecyclerView

    lateinit var moviesViewModel: MoviesViewModel
    private val moviesRepository: MoviesRepository by inject()
    private lateinit var moviesList: List<ResultsItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
        getMovies()
    }

    private fun setup() {
        setViews()
        setupSearchView()
        handlerCollapsing()
        moviesViewModel = getViewModelProvider()
    }

    private fun getMovies() {
        moviesViewModel
            .getPopularMovies()
            .observe(this, Observer {
                it.data?.let { result -> successPopularMovies(result) }
        })
        showLoading()
    }

    private fun setViews() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_movies)
        lottieAnimation = findViewById(R.id.animation_view)
        recyclerView = findViewById(R.id.recycler_view)
        container = findViewById(R.id.container_info)
        searchView = findViewById(R.id.search_movies)
        appBarLayout = findViewById(R.id.app_bar)
    }

    private fun getViewModelProvider(): MoviesViewModel {
        return ViewModelProviders
            .of(this, MoviesViewModelFactory(moviesRepository))
            .get(MoviesViewModel::class.java)
    }

    private fun successPopularMovies(movies: List<ResultsItem>) {
       moviesList = movies
       hideLoading()
       setupAdapter()
    }

    private fun setupAdapter() {
        layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        adapter = PopularMoviesRecyclerView(moviesList, this)
        recyclerView.adapter = adapter
    }

    private fun showLoading() {
        recyclerView.visibility = View.GONE
        lottieAnimation.apply {
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    private fun hideLoading() {
        Handler().postDelayed({
            recyclerView.visibility = View.VISIBLE
            lottieAnimation.apply {
                visibility = View.GONE
                cancelAnimation()
            }
        }, 3000)
    }

    private fun handlerCollapsing() {
        var isShow = false
        var scrollRange: Int = -1

        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                collapsingToolbarLayout.title = resources.getString(R.string.title_movies)
                isShow = true
            } else if (isShow){
                collapsingToolbarLayout.title = " "
                isShow = false
            }
        })
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }
}
