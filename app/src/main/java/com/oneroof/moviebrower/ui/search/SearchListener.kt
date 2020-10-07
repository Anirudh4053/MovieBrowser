package com.oneroof.moviebrower.ui.search

import com.oneroof.moviebrower.data.model.MovieResult

interface SearchListener {
    fun onStarted()
    fun onFailure(message:String)
    fun onHideLoader()
    fun onSuccess(searchList:List<MovieResult>,page:Int,totalPages:Int)
}