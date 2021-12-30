package com.mvine.mcomm.presentation.home.calls

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentCallsBinding
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource.*
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.janus.call
import com.mvine.mcomm.janus.hangUp
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_ACCEPTED
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_DECLINING
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_HANGUP
import com.mvine.mcomm.janus.utils.CommonValues.OUTGOING
import com.mvine.mcomm.janus.utils.toSIPRemoteAddress
import com.mvine.mcomm.presentation.audio.view.AudioDialogFragment
import com.mvine.mcomm.presentation.audio.view.AudioDialogListener
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.MultipleRowTypeAdapter
import com.mvine.mcomm.presentation.common.dialog.CallDialog
import com.mvine.mcomm.presentation.common.dialog.CallDialogData
import com.mvine.mcomm.presentation.common.dialog.CallDialogListener
import com.mvine.mcomm.presentation.home.HomeActivity
import com.mvine.mcomm.presentation.home.HomeViewModel
import com.mvine.mcomm.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallsFragment : Fragment(), ListInteraction<CallData> {

    @Inject
    lateinit var preferenceHandler: PreferenceHandler

    @Inject
    lateinit var janusManager: JanusManager

    private val callsViewModel: CallsViewModel by viewModels()

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val callsAdapter: MultipleRowTypeAdapter = MultipleRowTypeAdapter(arrayListOf())

    private lateinit var fragmentCallsBinding: FragmentCallsBinding

    private lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences = requireContext().getSharedPreferences(
            MCOMM_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    private fun setUpViews() {
        fragmentCallsBinding.rvCalls.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = callsAdapter
        }
        (activity as HomeActivity).showSearchBar()
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
        if((activity as HomeActivity).isRegistered) {
            item.othercaller_company_id?.let { companyId ->
                (activity as HomeActivity).showCallsPopUp(companyId, OUTGOING, item.othercaller_company_id, item.image_src)
            }
            item.othercaller_stx?.let { janusManager.call(it.toSIPRemoteAddress()) }
        }
    }

}