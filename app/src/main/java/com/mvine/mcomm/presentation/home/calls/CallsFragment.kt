package com.mvine.mcomm.presentation.home.calls

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mvine.mcomm.BR
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentCallsBinding
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource.*
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.MultipleRowTypeAdapter
import com.mvine.mcomm.presentation.common.base.BaseFragment
import com.mvine.mcomm.presentation.home.HomeActivity
import com.mvine.mcomm.presentation.home.HomeViewModel
import com.mvine.mcomm.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.Error

@AndroidEntryPoint
class CallsFragment : BaseFragment<FragmentCallsBinding,CallsViewModel>(), ListInteraction<CallData> {

    @Inject
    lateinit var preferenceHandler: PreferenceHandler

    private val callsViewModel: CallsViewModel by viewModels()

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val callsAdapter: MultipleRowTypeAdapter = MultipleRowTypeAdapter(arrayListOf()) // allCallsAdapter

    private val allCallsAdapter: MultipleRowTypeAdapter = MultipleRowTypeAdapter(arrayListOf()) // allCallsAdapter

    private lateinit var sharedPreferences: SharedPreferences

    override val bindingVariable: Int
        get() = BR.callsViewModel

    override val layoutId: Int
        get() = R.layout.fragment_calls

    override fun getViewModel(): CallsViewModel {
        return callsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callsViewModel.getRecentCalls()
        subscribeObservers()
        setUpViews()
        initListeners()
        sharedPreferences = requireContext().getSharedPreferences(
            MCOMM_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
        refreshRecentCallsData()
    }

    private fun setUpViews() {
        binding.rvCalls.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = callsAdapter
        }
        binding.rvAllCalls.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allCallsAdapter
        }
        (activity as HomeActivity).showSearchBar()
    }

    private fun initListeners() {
        binding.callsAll.apply {
            setOnClickListener {
                binding.rvAllCalls.visibility = View.VISIBLE
                binding.rvCalls.visibility = View.GONE
                background = ContextCompat.getDrawable(context, R.drawable.ic_white_filled_rounded_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.mcomm_blue))
                setTypeface(typeface, Typeface.BOLD)
                binding.apply {
                    callsRecents.background = ContextCompat.getDrawable(context, R.drawable.ic_mcomm_blue_light_rounded_rectangle)
                    callsRecents.setTextColor(ContextCompat.getColor(context, R.color.white))
                    callsRecents.setTypeface(typeface, Typeface.NORMAL)
                }
            }
        }

        binding.callsRecents.apply {
            setOnClickListener {
                binding.rvAllCalls.visibility = View.GONE
                binding.rvCalls.visibility = View.VISIBLE
                background = ContextCompat.getDrawable(context, R.drawable.ic_white_filled_rounded_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.mcomm_blue))
                setTypeface(typeface, Typeface.BOLD)
                binding.apply {
                   callsAll.background = ContextCompat.getDrawable(context, R.drawable.ic_mcomm_blue_light_rounded_rectangle)
                   callsAll.setTextColor(ContextCompat.getColor(context, R.color.white))
                   callsAll.setTypeface(typeface, Typeface.NORMAL)
                }
            }
        }
    }

    private fun subscribeObservers() {
        callsViewModel.userInfo.observe(viewLifecycleOwner, {result ->
            result?.let { res ->
                when(res){
                    is Success -> {
                        preferenceHandler.save(USER_INFO, Gson().toJson(res.data))
                    }
                }
            }
        })
        callsViewModel.recentCalls.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when (response) {
                    is Success -> {
                        if(callsViewModel.allCalls.value == null)
                            callsViewModel.getAllCalls()
                        else{
                            binding.rvCallsRefresh.isRefreshing = false
                            binding.progressCalls.visibility = View.GONE
                        }
                        response.data?.let { callData ->
                            callsAdapter.updateData(prepareRowTypesFromCallData(callData, this))
                            callsAdapter.notifyDataSetChanged()
                        }
                    }
                    is Error -> {
                        binding.progressCalls.visibility = View.GONE
                        response.message?.let {
                            showSnackBar(binding.root, it, null, false)
                        }
                    }
                    is Loading -> {
                        binding.progressCalls.isVisible = !binding.rvCallsRefresh.isRefreshing
                    }
                }
            }
        })
        callsViewModel.allCalls.observe(viewLifecycleOwner, { result ->
            result?.let { res ->
                when(res){
                    is Success -> {
                        binding.progressCalls.visibility = View.GONE
                        res.data?.let { callData ->
                            allCallsAdapter.updateData(prepareRowTypesFromAllCallData(callsViewModel.sortAlphabetically(callData), this))
                        }
                    }
                    is Error -> {
                        binding.progressCalls.visibility = View.GONE
                        res.message?.let {
                            showSnackBar(binding.root, it, null, false)
                        }
                    }
                    is Loading -> {
                        binding.progressCalls.visibility = View.VISIBLE
                    }
                }

            }

        })
        homeViewModel.searchLiveData.observe(viewLifecycleOwner, {
            it?.let {
                callsViewModel.filterData(it)
            }
        })
        homeViewModel.shouldRefreshData.observe(viewLifecycleOwner, {
            it?.let {
                if(it){
                    homeViewModel.shouldRefreshData.postValue(false)
                    callsViewModel.getRecentCalls()
                }
            }
        })
        callsViewModel.searchCalls.observe(viewLifecycleOwner, {
            it?.let { callData ->
                callsAdapter.updateData(prepareRowTypesFromCallData(callData, this))
            }
        })

        callsViewModel.searchAllCalls.observe(viewLifecycleOwner, {
            it?.let {
                allCallsAdapter.updateData(prepareRowTypesFromAllCallData(it, this))
            }
        })
    }

    override fun onItemSelectedForExpansion(position: Int, item: CallData, isExpanded: Boolean) {
        if(binding.rvCalls.isVisible){
            if (isExpanded) {
                callsAdapter.expandCallHistory(position, item)
            } else {
                callsAdapter.dissolveCallHistory(position, item)
            }
        }else{
            if (isExpanded) {
                allCallsAdapter.expandCallHistory(position, item)
            } else {
                allCallsAdapter.dissolveCallHistory(position, item)
            }
        }

    }

    override fun onVoiceCallSelected(item: CallData) {
        item.othercaller_stx?.let {
            (activity as HomeActivity).startOutgoingCall(
                sTX = it,
                userName = item.othercaller_department ?: it,
                uri = item.image_src?: EMPTY_STRING )
        }

    }

    private fun refreshRecentCallsData(){
        binding.rvCallsRefresh.setOnRefreshListener {
            binding.rvCallsRefresh.isRefreshing = true
            callsViewModel.getRecentCalls()
        }
    }

}