package com.oneroof.moviebrower.data.model

import com.google.gson.annotations.SerializedName

data class MovieRequest(
    @SerializedName("api_key")
    val apiKey: String,
    @SerializedName("sort_by")
    val versionNumber: String,
    @SerializedName("page")
    val page: Int
)
