package com.oneroof.moviebrower.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oneroof.moviebrower.data.model.MovieResponse
import com.oneroof.moviebrower.data.model.MovieResult
import kotlinx.coroutines.launch

class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    val movieList: LiveData<List<MovieResult>> = MutableLiveData()
    var moviesListener: MoviesListener? = null
    /*init {
        getAlMovieList()
    }*/

    fun getAllMovieList(sortBy:String,pageNo:Int){
        moviesListener?.onStarted()
        moviesRepository.getOrder(sortBy,pageNo,object :MoviesRepository.OnData{

            override fun onSuccess(response: MovieResponse) {
                moviesListener?.onHideLoader()
                viewModelScope.launch {
                    movieList as MutableLiveData
                    movieList.value = response.results
                }
            }

            override fun onFailure(msg: String) {
                moviesListener?.onFailure(msg)
            }

        })
    }

}