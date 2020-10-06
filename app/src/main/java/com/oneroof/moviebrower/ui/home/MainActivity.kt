package com.oneroof.moviebrower.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.oneroof.moviebrower.R
import com.oneroof.moviebrower.data.model.MovieResult
import com.oneroof.moviebrower.data.others.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MainActivity : AppCompatActivity(),MoviesListener, KodeinAware {
    override val kodein by kodein()
    private val factory by instance<MoviesViewModelFactory>()
    val itemList = arrayListOf<MovieResult>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this,factory).get(MoviesViewModel::class.java)
        viewModel.moviesListener = this
        viewModel.movieList.observe(this, Observer {
            println("All movie List $it")
            itemList.addAll(it)
            //adapter.notifyDataSetChanged()
        })
        viewModel.getAllMovieList("popularity.desc",1)
    }

    override fun onStarted() {

    }

    override fun onFailure(message: String) {
        showToast(message)
    }

    override fun onHideLoader() {

    }
}