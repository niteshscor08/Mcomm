package com.mvine.mcomm.presentation.home.calls

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mvine.mcomm.BR
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentCallsBinding
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource.*
import com.mvine.mcomm.janus.commonvalues.CommonValues.Companion.OUTGOING
import com.mvine.mcomm.janus.extension.toSIPRemoteAddress
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
        subscribeObservers()
        setUpViews()
        initListeners()
        sharedPreferences = requireContext().getSharedPreferences(
            MCOMM_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
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
                        callsViewModel.getAllCalls()
                        response.data?.let { callData ->
                            callsAdapter.updateData(prepareRowTypesFromCallData(callData, this))
                        }
                    }
                    is Error -> {
                        binding.progressCalls.visibility = View.GONE
                        Toast.makeText(activity, response.message, Toast.LENGTH_LONG).show()
                    }
                    is Loading -> {
                        binding.progressCalls.visibility = View.VISIBLE
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
                            allCallsAdapter.updateData(prepareRowTypesFromCallData(callData, this))
                        }
                    }
                    is Error -> {
                        binding.progressCalls.visibility = View.GONE
                        Toast.makeText(activity, res.message, Toast.LENGTH_LONG).show()
                    }
                    is Loading -> {
                        binding.progressCalls.visibility = View.VISIBLE
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
            item.othercaller_company_id?.let { companyId ->
                (activity as HomeActivity).startOutgoingCall( sTX = companyId,
                    userName = item.othercaller_company_id,
                    uri = item.image_src?: EMPTY_STRING )
            }
    }

}