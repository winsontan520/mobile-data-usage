package com.winsontan520.mobiledatausage.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.winsontan520.mobiledatausage.common.base.BaseFragment
import com.winsontan520.mobiledatausage.common.base.BaseViewModel
import com.winsontan520.mobiledatausage.databinding.FragmentHomeBinding
import com.winsontan520.mobiledatausage.feature.home.views.HomeAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureRecyclerView()

        viewModel.toastMessage.observe(this, Observer { res ->
            if (res != null) {
                val message = getString(res)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getViewModel(): BaseViewModel = viewModel

    private fun configureRecyclerView() {
        binding.fragmentHomeRv.adapter = HomeAdapter(viewModel)
    }
}