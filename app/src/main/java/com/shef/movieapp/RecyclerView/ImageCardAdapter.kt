package com.shef.movieapp.RecyclerView

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shef.movieapp.MovieDetailsActivity
import com.shef.movieapp.R
import com.shef.movieapp.data.MovieDetail

class ImageCardAdapter(private val imageList: List<MovieDetail>) :
    RecyclerView.Adapter<ImageCardAdapter.ImageCardViewHolder>() {

    inner class ImageCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_card, parent, false)
        return ImageCardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageCardViewHolder, position: Int) {
        val currentItem = imageList[position]
        Glide.with(holder.itemView.context).load(currentItem.poster_path).into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MovieDetailsActivity::class.java)
            Log.d("ImageCardAdapter", "Rating: ${currentItem.vote_average.toString()}")
            intent.putExtra("movie_id", currentItem.id)
            intent.putExtra("image_url", currentItem.poster_path)
            intent.putExtra("title", currentItem.title)
            intent.putExtra("release_date", currentItem.release_date)
            intent.putExtra("description", currentItem.overview)
            intent.putExtra("rating", currentItem.vote_average.toString())
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = imageList.size
}
