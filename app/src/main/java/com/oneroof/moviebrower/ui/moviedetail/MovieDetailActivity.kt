package com.oneroof.moviebrower.ui.moviedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.oneroof.moviebrower.R
import com.oneroof.moviebrower.data.model.MovieResult
import com.oneroof.moviebrower.data.others.MOVIE_IMAGE_PATH
import com.oneroof.moviebrower.data.others.convertDateFormat
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.custom_movie_items.view.*
import kotlinx.android.synthetic.main.custom_rating_layout.view.*
import kotlinx.android.synthetic.main.custom_toolbar_txt.view.*

class MovieDetailActivity : AppCompatActivity() {
    private var movieDetail: MovieResult?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        includeToolbarDetail.apply {
            this.toolbarTitle.text = "Detail Page"
            this.backBtn.setOnClickListener {
                onBackPressed()
            }
        }
        val bundle = intent.extras
        if(bundle!=null) {
            movieDetail = bundle.getParcelable("details")
            println("movieDetail $movieDetail")
        }
        fillDetails()
    }
    private fun fillDetails() {
        if(movieDetail!=null) {
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide
                .with(this)
                .load("$MOVIE_IMAGE_PATH${movieDetail?.backdropPath?:movieDetail?.posterPath}")
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(coverImage)
            movieTitle.text = "${movieDetail?.originalTitle}"
            val date = movieDetail?.releaseDate?.split("-")?.toTypedArray()
            val year = date?.get(0) ?:""
            yearTxt.text = "$year"

            Glide
                .with(this)
                .load("$MOVIE_IMAGE_PATH${movieDetail?.posterPath}")
                .placeholder(circularProgressDrawable)
                .into(smallImageView)

            if(movieDetail?.overview.isNullOrEmpty())
                overViewTxt.text = "No content"
            else
                overViewTxt.text = "${movieDetail?.overview}"

            includeRating.apply {
                this.ratingTxt.text = "${movieDetail?.voteAverage}/10"
                this.totalRatings.text = "${movieDetail?.voteCount}"
            }
            includeReleaseDate.apply {
                this.starImage.setImageResource(R.drawable.ic_date)
                this.ratingTxt.text = "Release Date"
                this.totalRatings.text = "${convertDateFormat(movieDetail?.releaseDate?:"")}"
            }

        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}