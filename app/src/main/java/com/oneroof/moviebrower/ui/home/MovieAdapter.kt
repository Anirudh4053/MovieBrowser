package com.oneroof.moviebrower.ui.home

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.oneroof.moviebrower.R
import com.oneroof.moviebrower.data.model.MovieResult
import com.oneroof.moviebrower.data.others.MOVIE_IMAGE_PATH
import kotlinx.android.synthetic.main.custom_movie_items.view.*

class MovieAdapter(private var context: Context, private var sectionList: ArrayList<MovieResult>,
                          private var onItemClick:(item: MovieResult) -> Unit = {}):
    RecyclerView.Adapter<MovieAdapter.OrderHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.custom_movie_items, parent, false)
        return OrderHolder(view)
    }

    override fun getItemCount(): Int = sectionList.size

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val item = sectionList[position]
        holder.itemView.movieTitle.text = item.title
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularProgressDrawable.setTint(ContextCompat.getColor(context, R.color.whiteColor))
        }*/
        circularProgressDrawable.start()
        Glide
            .with(context)
            .load("$MOVIE_IMAGE_PATH${item.posterPath}")
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(holder.itemView.movieImage)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}