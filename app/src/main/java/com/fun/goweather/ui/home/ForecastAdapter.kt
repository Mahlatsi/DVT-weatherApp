package com.`fun`.goweather.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.`fun`.goweather.R
import com.`fun`.goweather.helpers.utils.utils.StringUtil
import com.`fun`.goweather.model.forecast.Daily
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.forcast_items_activity.view.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ForecastAdapter :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {


    private val forecast: ArrayList<Daily> = ArrayList()
    private var onItemClick: ((Daily) -> Unit)? = null

    fun setData(data: List<Daily>?) {
        forecast.clear()
        forecast.addAll(data!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.forcast_items_activity, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(forecast[position], onItemClick)

    }

    override fun getItemCount(): Int {
        if (forecast.size > 5)
            return 5
        return forecast.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val context: Context = view.context

        @SuppressLint("SimpleDateFormat")
        fun bindItems(favorite: Daily, onItemClick: ((Daily) -> Unit)?) {
            val URL = "https://openweathermap.org/img/wn/"
            val time = Date(favorite.dt as Long * 1000)
            val sdf = SimpleDateFormat("EEEE")
            val icon: String = favorite.weather?.get(0)?.icon.toString() + ".png"

            Glide.with(context)
                .load("$URL$icon")
                .into(itemView.description_icon)
            itemView.forecast_day.text = sdf.format(time)
            itemView.forecast_temp.text =
                context.getString(R.string.celsius, StringUtil.validateTemp(favorite.temp?.day))

            itemView.setOnClickListener {
                onItemClick?.invoke(favorite)
            }

        }
    }
}