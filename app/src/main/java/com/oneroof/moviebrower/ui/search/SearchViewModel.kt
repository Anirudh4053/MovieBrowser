package com.oneroof.moviebrower.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oneroof.moviebrower.data.model.MovieResponse
import com.oneroof.moviebrower.data.model.MovieResult
import kotlinx.coroutines.launch

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    //val movieList: LiveData<List<MovieResult>> = MutableLiveData()
    var searchListener: SearchListener? = null
    fun getSearchMovieList(query:String,pageNo:Int){
        if(pageNo == 1) {
            searchListener?.onStarted()
        }
        searchRepository.getSearchList(query,pageNo,object : SearchRepository.OnData{
            override fun onSuccess(response: MovieResponse) {
                searchListener?.onHideLoader()
                viewModelScope.launch {
                    searchListener?.onSuccess(response.results,response.page,response.totalPages)
                }
            }

            override fun onFailure(msg: String) {
                searchListener?.onFailure(msg)
            }

        })
    }

}