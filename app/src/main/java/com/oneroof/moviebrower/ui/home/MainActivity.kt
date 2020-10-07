package com.oneroof.moviebrower.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.oneroof.moviebrower.R
import com.oneroof.moviebrower.data.model.GridSpacingItemDecoration
import com.oneroof.moviebrower.data.model.MovieResult
import com.oneroof.moviebrower.data.others.*
import com.oneroof.moviebrower.ui.moviedetail.MovieDetailActivity
import com.oneroof.moviebrower.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_bottom_sheet_filter.view.*
import kotlinx.android.synthetic.main.custom_sort_layout.view.*
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
    //for pagination
    var isLoading:Boolean = false
    private var pageNo:Int = 1
    private var sortedBy:String = POPULARITY_SORT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        includeToolbarHome.apply {
            this.backBtn.hide()
            this.searchIcon.show()
            this.filterIcon.hide()
            this.toolbarTitle.text = "Movies"
            this.searchIcon.setOnClickListener {
                val i = Intent(this@MainActivity, SearchActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(i)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        includeSortLayout.setOnClickListener {
            showFilterDialog()
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
            val i = Intent(this@MainActivity, MovieDetailActivity::class.java)
            i.putExtra("details",it)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        val mLayoutManager = GridLayoutManager(this@MainActivity, 2)
        moviesRV.layoutManager = mLayoutManager
        moviesRV.addItemDecoration(GridSpacingItemDecoration(2, globalDpToPx(16), true))
        moviesRV.itemAnimator = DefaultItemAnimator()
        moviesRV.adapter = adapter

        moviesRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (! recyclerView.canScrollVertically(1)){ //1 for down
                    if(!isLoading){
                        println("pagination -- $pageNo")
                        bottomProgressBar.show()
                        isLoading = true
                        viewModel.getAllMovieList(sortedBy,pageNo)
                    }
                }
            }
        })

        getMovies()
        swipeToRefresh.setOnRefreshListener {
            getMovies()
        }
    }

    private fun getMovies() {
        pageNo = 1
        includeSortLayout.hide()
        isLoading = true
        itemList.clear()
        adapter.notifyDataSetChanged()
        swipeToRefresh.isRefreshing = false
        viewModel.getAllMovieList(sortedBy,pageNo)
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
        bottomProgressBar.hide()
    }

    override fun onSuccess(page: Int, totalPages: Int) {
        if(page == 1) {
            includeSortLayout.show()
        }
        onHideLoader()
        pageNo = page+1
        println("$pageNo -- $page -- $totalPages")
        isLoading = pageNo > totalPages
    }
    private fun showFilterDialog(){
        val dialog = BottomSheetDialog(this)
        dialog.setCanceledOnTouchOutside(true)
        val bottomSheet = layoutInflater.inflate(R.layout.custom_bottom_sheet_filter, null)

        if(sortedBy == POPULARITY_SORT)
            bottomSheet.radio_popularity.isChecked = true
        else
            bottomSheet.radio_rated.isChecked = true

        bottomSheet.radio_popularity.setOnClickListener {
            if(sortedBy != POPULARITY_SORT) {
                includeSortLayout.sortedTxt.text = "Sorted by popularity"
                sortedBy = POPULARITY_SORT
                getMovies()
            }
            dialog.dismiss()
        }
        bottomSheet.radio_rated.setOnClickListener {
            if(sortedBy != RATINGS_SORT) {
                includeSortLayout.sortedTxt.text = "Sorted by ratings"
                sortedBy = RATINGS_SORT
                getMovies()
            }
            dialog.dismiss()
        }
        dialog.setContentView(bottomSheet)
        dialog.show()
    }
}