package com.oneroof.moviebrower

import android.app.Application
import androidx.annotation.Keep
import com.oneroof.moviebrower.data.network.MyApi
import com.oneroof.moviebrower.ui.home.MoviesRepository
import com.oneroof.moviebrower.ui.home.MoviesViewModel
import com.oneroof.moviebrower.ui.home.MoviesViewModelFactory
import com.oneroof.moviebrower.ui.search.SearchRepository
import com.oneroof.moviebrower.ui.search.SearchViewModel
import com.oneroof.moviebrower.ui.search.SearchViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class MVVMApplication : Application(), KodeinAware {
    override val kodein =  Kodein.lazy{
        import(androidXModule(this@MVVMApplication))
        bind() from singleton { MyApi() }
        bind() from singleton { MoviesRepository(instance()) }
        bind() from singleton { MoviesViewModel(instance()) }
        bind() from provider { MoviesViewModelFactory(instance()) }

        bind() from singleton { SearchRepository(instance()) }
        bind() from singleton { SearchViewModel(instance()) }
        bind() from provider { SearchViewModelFactory(instance()) }
    }
}