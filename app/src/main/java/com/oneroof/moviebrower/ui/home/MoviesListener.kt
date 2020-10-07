package com.oneroof.moviebrower.ui.home

interface MoviesListener {
    fun onStarted()
    fun onFailure(message:String)
    fun onHideLoader()
    fun onSuccess(page:Int,totalPages:Int)
}