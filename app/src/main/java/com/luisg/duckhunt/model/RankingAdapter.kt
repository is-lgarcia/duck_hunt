package com.luisg.duckhunt.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisg.duckhunt.R

class RankingAdapter (var mValues: List<Users>): RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(mValues.size == 0)
            return 0;
        return mValues.size;
    }

    fun putUsers(list: List<Users>) {
        mValues = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        val pos = position + 1

        holder.txtRanking.text = "${pos}º"
        holder.txtNickName.text = item.nick
        holder.txtDuckCounter.text = item.ducks.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtRanking = itemView.findViewById<TextView>(R.id.txt_ramking)
        var txtNickName = itemView.findViewById<TextView>(R.id.txt_nick_username_ranking)
        var txtDuckCounter = itemView.findViewById<TextView>(R.id.txt_counter_duck)
    }

}