package com.winsontan520.mobiledatausage.feature.home.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.winsontan520.mobiledatausage.R
import com.winsontan520.mobiledatausage.data.model.MobileDataUsage
import com.winsontan520.mobiledatausage.feature.home.HomeViewModel

class HomeAdapter(private val viewModel: HomeViewModel): RecyclerView.Adapter<HomeViewHolder>() {

    private val list: MutableList<MobileDataUsage> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false))

    override fun getItemCount(): Int
            = list.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int)
            = holder.bindTo(list[position], viewModel)

    fun updateData(items: List<MobileDataUsage>) {
        val diffCallback = HomeItemDiffCallback(list, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        list.clear()
        list.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}