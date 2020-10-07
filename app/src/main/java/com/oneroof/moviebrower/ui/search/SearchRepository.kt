package com.oneroof.moviebrower.ui.search

import com.oneroof.moviebrower.data.model.MovieResponse
import com.oneroof.moviebrower.data.network.MyApi
import com.oneroof.moviebrower.data.others.ERROR
import com.oneroof.moviebrower.data.others.makeRequest

class SearchRepository (private val myApi: MyApi){
    fun getSearchList(query:String,pageNo:Int,onData:OnData){
        myApi.queryMovie(query,pageNo).makeRequest(
            onSuccess = { response ->
                onData.onSuccess(response)
            },
            onFailure = { throwable ->
                onData.onFailure(throwable.message ?: ERROR)
            })

    }
    interface OnData {
        fun onSuccess(response: MovieResponse)
        fun onFailure(msg:String)
    }
}