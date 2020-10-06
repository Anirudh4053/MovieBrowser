package com.oneroof.moviebrower.ui.home

import com.oneroof.moviebrower.data.model.MovieResponse
import com.oneroof.moviebrower.data.network.MyApi
import com.oneroof.moviebrower.data.others.makeRequest

class MoviesRepository (private val myApi: MyApi){
    fun getOrder(sortBy:String,pageNo:Int,onData:OnData){
        myApi.getMovies(sortBy,1).makeRequest(
            onSuccess = { response ->
                try {
                    onData.onSuccess(response)
                } catch (e:Exception) {
                    onData.onFailure("Something Went Wrong")
                }
            },
            onFailure = { throwable ->
                onData.onFailure(throwable.message ?: "Some error occurred")
            })

    }
    interface OnData {
        fun onSuccess(response: MovieResponse)
        fun onFailure(msg:String)
    }
}