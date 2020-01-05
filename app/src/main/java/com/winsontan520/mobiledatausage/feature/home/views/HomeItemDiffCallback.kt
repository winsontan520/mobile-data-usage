package com.winsontan520.mobiledatausage.feature.home.views

import androidx.recyclerview.widget.DiffUtil
import com.winsontan520.mobiledatausage.data.model.MobileDataUsage

class HomeItemDiffCallback(
    private val oldList: List<MobileDataUsage>,
    private val newList: List<MobileDataUsage>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].year == newList[newItemPosition].year
    }
}