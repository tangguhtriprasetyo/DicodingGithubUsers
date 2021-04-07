package com.example.dicodinggithubusers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.databinding.ItemRvUserBinding
import com.example.dicodinggithubusers.model.Users
import java.util.*
import kotlin.collections.ArrayList

class RvUsersAdapter(private val listUsers: ArrayList<Users>) : RecyclerView.Adapter<RvUsersAdapter.ListViewHolder>(), Filterable {
    private lateinit var onItemClickCallback: OnItemClickCallback

    //List untuk Search
    var filterList = ArrayList<Users>()

    init {
        filterList = listUsers
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRvUserBinding.bind(itemView)
        var userAvatar = binding.imgItemUserAvatar
        var tvUserName = binding.tvItemUsername
        var tvUserLocation = binding.tvItemLocation
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int): ListViewHolder {
        val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_rv_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val users = filterList[position]

        Glide.with(holder.itemView.context)
                .load(users.avatar)
                .apply(RequestOptions().override(64, 64))
                .into(holder.userAvatar)

        holder.tvUserName.text = users.username
        holder.tvUserLocation.text = users.location

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(filterList[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Users)
    }

    //Filter Data dari Search
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = listUsers
                } else {
                    val resultList = ArrayList<Users>()
                    for (row in listUsers) {
                        if (row.name?.toLowerCase(Locale.ROOT)
                                ?.contains(charSearch.toLowerCase(Locale.ROOT)) == true
                        ) {
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<Users>
                notifyDataSetChanged()
            }

        }
    }
}