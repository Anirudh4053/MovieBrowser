package com.oneroof.moviebrower.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory (
    private val  repository: SearchRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //will create our view model
        return SearchViewModel(repository) as T
    }
}