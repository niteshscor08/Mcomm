package com.mvine.mcomm.presentation.home.calls

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentCallsBinding
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource.*
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.MultipleRowTypeAdapter
import com.mvine.mcomm.presentation.home.HomeViewModel
import com.mvine.mcomm.util.prepareRowTypesFromCallData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallsFragment : Fragment(), ListInteraction<CallData> {

    private val callsViewModel: CallsViewModel by viewModels()

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val callsAdapter: MultipleRowTypeAdapter = MultipleRowTypeAdapter(arrayListOf())

    private lateinit var fragmentCallsBinding: FragmentCallsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCallsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_calls, container, false)
        return fragmentCallsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        setUpViews()
        initListeners()
    }

    private fun setUpViews() {
        fragmentCallsBinding.rvCalls.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = callsAdapter
        }


    }

    private fun initListeners() {
        fragmentCallsBinding.callsAll.apply {
            setOnClickListener {
                background = ContextCompat.getDrawable(context, R.drawable.ic_white_filled_rounded_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.mcomm_blue))
                setTypeface(typeface, Typeface.BOLD)
                fragmentCallsBinding.callsRecents.background = ContextCompat.getDrawable(context, R.drawable.ic_mcomm_blue_light_rounded_rectangle)
                fragmentCallsBinding.callsRecents.setTextColor(ContextCompat.getColor(context, R.color.white))
                fragmentCallsBinding.callsRecents.setTypeface(typeface, Typeface.NORMAL)
            }
        }

        fragmentCallsBinding.callsRecents.apply {
            setOnClickListener {
                background = ContextCompat.getDrawable(context, R.drawable.ic_white_filled_rounded_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.mcomm_blue))
                setTypeface(typeface, Typeface.BOLD)
                fragmentCallsBinding.callsAll.background = ContextCompat.getDrawable(context, R.drawable.ic_mcomm_blue_light_rounded_rectangle)
                fragmentCallsBinding.callsAll.setTextColor(ContextCompat.getColor(context, R.color.white))
                fragmentCallsBinding.callsAll.setTypeface(typeface, Typeface.NORMAL)
            }
        }
    }

    private fun subscribeObservers() {
        callsViewModel.recentCalls.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when (response) {
                    is Success -> {
                        fragmentCallsBinding.progressCalls.visibility = View.GONE
                        response.data?.let { callData ->
                            callsAdapter.updateData(prepareRowTypesFromCallData(callData, this))
                        }
                    }
                    is Error -> {
                        fragmentCallsBinding.progressCalls.visibility = View.GONE
                        Toast.makeText(activity, response.message, Toast.LENGTH_LONG).show()
                    }
                    is Loading -> {
                        fragmentCallsBinding.progressCalls.visibility = View.VISIBLE
                    }
                }
            }
        })
        homeViewModel.searchLiveData.observe(viewLifecycleOwner, {
            it?.let { callsViewModel.filterData(it) }
        })
        callsViewModel.searchCalls.observe(viewLifecycleOwner, {
            it?.let { callData ->
                callsAdapter.updateData(prepareRowTypesFromCallData(callData, this))
            }
        })
    }

    override fun onItemSelectedForExpansion(position: Int, item: CallData, isExpanded: Boolean) {
        if (isExpanded) {
            callsAdapter.expandCallHistory(position, item)
        } else {
            callsAdapter.dissolveCallHistory(position, item)
        }
    }

    override fun onVoiceCallSelected(item: CallData) {
        Toast.makeText(
            activity,
            "Voice call selected for ${item.othercaller_company_id}",
            Toast.LENGTH_LONG
        ).show()
    }
}