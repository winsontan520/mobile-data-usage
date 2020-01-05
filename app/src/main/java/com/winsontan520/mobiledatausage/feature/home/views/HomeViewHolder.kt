package com.winsontan520.mobiledatausage.feature.home.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.winsontan520.mobiledatausage.data.model.MobileDataUsage
import com.winsontan520.mobiledatausage.databinding.ItemHomeBinding
import com.winsontan520.mobiledatausage.feature.home.HomeViewModel

class HomeViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

    private val binding = ItemHomeBinding.bind(parent)

    fun bindTo(mobileDataUsage: MobileDataUsage, viewModel: HomeViewModel) {
        binding.mobileDataUsage = mobileDataUsage
        binding.viewmodel = viewModel
    }
}