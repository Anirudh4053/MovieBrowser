package com.oneroof.moviebrower.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.oneroof.moviebrower.R
import com.oneroof.moviebrower.data.model.GridSpacingItemDecoration
import com.oneroof.moviebrower.data.model.MovieResult
import com.oneroof.moviebrower.data.others.globalDpToPx
import com.oneroof.moviebrower.data.others.hide
import com.oneroof.moviebrower.data.others.show
import com.oneroof.moviebrower.data.others.showToast
import com.oneroof.moviebrower.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar_txt.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MainActivity : AppCompatActivity(),MoviesListener, KodeinAware {
    override val kodein by kodein()
    private val factory by instance<MoviesViewModelFactory>()
    private var itemList = mutableListOf<MovieResult>()
    private lateinit var adapter:MovieAdapter
    private lateinit var viewModel:MoviesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        includeToolbarHome.apply {
            this.backBtn.hide()
            this.searchIcon.show()
            this.filterIcon.show()
            this.toolbarTitle.text = "Movies"
            this.searchIcon.setOnClickListener {
                val i = Intent(this@MainActivity, SearchActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(i)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        viewModel = ViewModelProvider(this,factory).get(MoviesViewModel::class.java)
        viewModel.moviesListener = this
        viewModel.movieList.observe(this, Observer {
            println("All movie List")
            itemList.addAll(it)
            adapter.notifyDataSetChanged()
        })


        moviesRV.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(this, itemList as ArrayList<MovieResult>) {

        }
        val mLayoutManager = GridLayoutManager(this@MainActivity, 2)
        moviesRV.layoutManager = mLayoutManager
        moviesRV.addItemDecoration(GridSpacingItemDecoration(2, globalDpToPx(16), true))
        moviesRV.itemAnimator = DefaultItemAnimator()
        moviesRV.adapter = adapter

        getMovies()
        swipeToRefresh.setOnRefreshListener {
            getMovies()
        }
    }

    private fun getMovies() {
        itemList.clear()
        adapter.notifyDataSetChanged()
        swipeToRefresh.isRefreshing = false
        viewModel.getAllMovieList("popularity.desc",1)
    }
    override fun onStarted() {
        progressBar.show()
    }

    override fun onFailure(message: String) {
        onHideLoader()
        showToast(message)
    }

    override fun onHideLoader() {
        progressBar.hide()
    }
}