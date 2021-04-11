package com.example.dicodinggithubusers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.databinding.ItemRvUserBinding
import com.example.dicodinggithubusers.loadImage
import com.example.dicodinggithubusers.model.Users
import java.util.*
import kotlin.collections.ArrayList

class RvUsersAdapter :
        RecyclerView.Adapter<RvUsersAdapter.ListViewHolder>(), Filterable {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var mData = ArrayList<Users>()


    fun setData(items: ArrayList<Users>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRvUserBinding.bind(itemView)
        fun bind(users: Users) {
            binding.imgItemUserAvatar.loadImage(users.avatar)
            binding.tvItemUsername.text = users.username
            binding.tvItemLocation.text = users.location
        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ListViewHolder {
        val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_rv_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(mData[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Users)
    }

    //Filter Data dari Search
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                mData = if (charSearch.isEmpty()) {
                    mData
                } else {
                    val resultList = ArrayList<Users>()
                    for (row in mData) {
                        if (row.name?.toLowerCase(Locale.ROOT)
                                        ?.contains(charSearch.toLowerCase(Locale.ROOT)) == true
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = mData
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mData = results?.values as ArrayList<Users>
                notifyDataSetChanged()
            }

        }
    }
}