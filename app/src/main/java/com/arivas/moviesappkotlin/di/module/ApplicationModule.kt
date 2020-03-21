package com.arivas.moviesappkotlin.di.module

import com.arivas.moviesappkotlin.BuildConfig
import com.arivas.moviesappkotlin.common.API_KEY
import com.arivas.moviesappkotlin.common.db.AppDatabase
import com.arivas.moviesappkotlin.common.network.services.MoviesServices
import com.arivas.moviesappkotlin.ui.movies.viewmodel.MoviesViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val applicationModule = module {

    single { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }

    single {
        OkHttpClient.Builder()
        .addInterceptor(get<HttpLoggingInterceptor>())
        .addInterceptor { chain ->
            val urlRequest: HttpUrl = chain.request().url()
            val url: HttpUrl = urlRequest.newBuilder()
                .addQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build()
            val request: Request = chain.request().newBuilder()
                .url(url)
                .build()

            return@addInterceptor chain.proceed(request)
        }
        .build() }

    single {
        Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(get<OkHttpClient>())
        .addConverterFactory(GsonConverterFactory.create())
        .build() }

    single<MoviesServices> { get<Retrofit>().create(MoviesServices::class.java) }

    single { AppDatabase.getInstance(androidContext()).moviesDao() }


}