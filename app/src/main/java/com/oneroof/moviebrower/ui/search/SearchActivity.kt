package com.oneroof.moviebrower.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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
import com.oneroof.moviebrower.ui.home.MovieAdapter
import com.oneroof.moviebrower.ui.home.MoviesViewModelFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.progressBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity(),SearchListener, KodeinAware {
    private var disposables = CompositeDisposable()
    override val kodein by kodein()
    private val factory by instance<SearchViewModelFactory>()
    private var itemList = mutableListOf<MovieResult>()
    private lateinit var viewModel:SearchViewModel
    private lateinit var adapter: MovieAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backBtn.setOnClickListener {
            onBackPressed()
        }
        val cancelIcon = movie_search.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setImageResource(R.drawable.ic_close)
        //cancelIcon.setColorFilter(R.color.whiteColor)
        val textView = movie_search.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.whiteColor))
        cancelIcon.setOnClickListener {
            movie_search.setQuery("", false)
            movie_search.clearFocus()
            clearList()
        }
        movie_search.isFocusable = true
        movie_search.isIconified = false
        movie_search.requestFocusFromTouch()


        val observableQueryText = Observable
            .create(object: ObservableOnSubscribe<String> {
                override fun subscribe(emitter: ObservableEmitter<String>?) {
                    movie_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            //searchIcon.hide()
                            movie_search.clearFocus()
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            println("search onQueryTextChange $newText")
                            onStarted()
                            if (!emitter?.isDisposed!!) {
                                emitter.onNext(newText)
                            }
                            return false
                        }

                    })
                }
            })
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())


        observableQueryText.subscribe(object: Observer<String> {
            override fun onComplete() {
                //progressBar.hide()
                println("search onComplete")
            }

            override fun onSubscribe(d: Disposable?) {
                println("search onSubscribe")
                disposables.add(d)
            }


            override fun onNext(s: String?) {
                println("search onNext $s")
                //onHideLoader()
                sendRequestToServer(s);
            }

            override fun onError(e: Throwable?) {
                //progressBar.hide()
                println("search onError")
            }
        })

        viewModel = ViewModelProvider(this,factory).get(SearchViewModel::class.java)
        viewModel.searchListener = this
        /*viewModel.movieList.observe(this, Observer {
            println("All movie List")
            itemList.addAll(it)
            adapter.notifyDataSetChanged()
        })*/

        searchRV.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(this, itemList as ArrayList<MovieResult>) {

        }
        val mLayoutManager = GridLayoutManager(this@SearchActivity, 2)
        searchRV.layoutManager = mLayoutManager
        searchRV.addItemDecoration(GridSpacingItemDecoration(2, globalDpToPx(16), true))
        searchRV.itemAnimator = DefaultItemAnimator()
        searchRV.adapter = adapter
    }
    private fun sendRequestToServer(query:String?){
        println("Search Query $query")
        viewModel.getSearchMovieList(query?:"")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onStarted() {
        //clearList()
        progressBar.show()
    }

    override fun onFailure(message: String) {
        clearList()
        onHideLoader()
        showToast(message)
    }
    override fun onHideLoader() {
        progressBar.hide()
    }
    override fun onSuccess(searchList: List<MovieResult>) {
        if(searchList.isNullOrEmpty()) {
            showToast("No record found")
        } else {
            itemList.clear()
            onHideLoader()
            itemList.addAll(searchList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun clearList() {
        itemList.clear()
        adapter.notifyDataSetChanged()
    }
}