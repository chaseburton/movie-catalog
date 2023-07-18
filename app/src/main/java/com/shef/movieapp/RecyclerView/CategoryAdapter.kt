package com.shef.movieapp.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shef.movieapp.CategoryData
import com.shef.movieapp.R

class CategoryAdapter(private var categoryList: MutableList<CategoryData>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.categoryTitle)
        val horizontalRecyclerView: RecyclerView = view.findViewById(R.id.horizontalRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryData = categoryList[position]

        holder.title.text = categoryData.title

        val imageCardAdapter = ImageCardAdapter(categoryData.movies)
        holder.horizontalRecyclerView.adapter = imageCardAdapter
    }


    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setData(newData: List<CategoryData>) {
        categoryList = newData.toMutableList()
        notifyDataSetChanged()
    }

}
