package com.mvine.mcomm.presentation.home.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentCallsBinding
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource.*
import com.mvine.mcomm.util.getSpinnerItems
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallsFragment : Fragment(), CallsAdapter.Interaction {

    private val callsViewModel: CallsViewModel by viewModels()

    private val callsAdapter: CallsAdapter = CallsAdapter(this)

    private lateinit var callHistorySpinnerAdapter: CallHistorySpinnerAdapter

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
    }

    private fun setUpViews() {
        fragmentCallsBinding.rvCalls.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = callsAdapter
        }


    }

    private fun subscribeObservers() {
        callsViewModel.recentCalls.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when (response) {
                    is Success -> {
                        fragmentCallsBinding.progressCalls.visibility = View.GONE
                        response.data?.let { callData ->
                            activity?.let {
                                callHistorySpinnerAdapter = CallHistorySpinnerAdapter(
                                    it,
                                    R.layout.item_spinner_call,
                                    arrayListOf()
                                )
                                callsAdapter.setSpinnerAdapterInstance(callHistorySpinnerAdapter)
                            }
                            callsAdapter.submitList(callData)
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
    }

    override fun onItemSelected(position: Int, item: CallData) {
        Toast.makeText(
            activity,
            "${item.othercaller_company} at position $position clicked",
            Toast.LENGTH_LONG
        ).show()
    }
}