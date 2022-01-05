package com.`fun`.goweather.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.`fun`.goweather.R
import com.`fun`.goweather.model.favorite.FavoriteList
import kotlinx.android.synthetic.main.favorite_items_activity.view.*


class FavoriteAdapter :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val favoriteList: ArrayList<FavoriteList> = ArrayList()
    var onItemClick: ((FavoriteList) -> Unit)? = null

    fun setData(data: List<FavoriteList>?) {
        favoriteList.clear()
        favoriteList.addAll(data!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.favorite_items_activity, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(favoriteList[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(favorite: FavoriteList, onItemClick: ((FavoriteList) -> Unit)?) {
            itemView.house_name.text = favorite.description

            itemView.setOnClickListener {
                onItemClick?.invoke(favorite)
            }

        }
    }

}