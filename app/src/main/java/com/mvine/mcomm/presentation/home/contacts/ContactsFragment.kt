package com.mvine.mcomm.presentation.home.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentContactsBinding
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.janus.extension.call
import com.mvine.mcomm.janus.commonvalues.CommonValues.Companion.OUTGOING
import com.mvine.mcomm.janus.extension.toSIPRemoteAddress
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.MultipleRowTypeAdapter
import com.mvine.mcomm.presentation.home.HomeActivity
import com.mvine.mcomm.presentation.home.HomeViewModel
import com.mvine.mcomm.util.prepareRowTypesFromContactsData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactsFragment : Fragment(), ListInteraction<ContactsData> {

    private val contactsViewModel: ContactsViewModel by viewModels()

    @Inject
    lateinit var janusManager: JanusManager

    private lateinit var fragmentContactsBinding: FragmentContactsBinding

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val contactsAdapter: MultipleRowTypeAdapter = MultipleRowTypeAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentContactsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)
        return fragmentContactsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        setUpViews()
    }

    private fun setUpViews() {
        fragmentContactsBinding.rvContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactsAdapter
        }
        (activity as HomeActivity).showSearchBar()
    }

    private fun subscribeObservers() {
        contactsViewModel.contacts.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        fragmentContactsBinding.progressContacts.visibility = View.GONE
                        response.data?.let {
                            contactsAdapter.updateData(prepareRowTypesFromContactsData(it, this))
                        }
                    }
                    is Resource.Error -> {
                        fragmentContactsBinding.progressContacts.visibility = View.GONE
                        Toast.makeText(activity, response.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        fragmentContactsBinding.progressContacts.visibility = View.VISIBLE
                    }
                }
            }
        })

        homeViewModel.searchLiveData.observe(viewLifecycleOwner, {
            it?.let { contactsViewModel.filterData(it) }
        })

        contactsViewModel.searchCalls.observe(viewLifecycleOwner, {
            contactsAdapter.updateData(prepareRowTypesFromContactsData(it, this))
        })
    }

    override fun onVoiceCallSelected(item: ContactsData) {
        if((activity as HomeActivity).isRegistered) {
            item.STX?.let { companyId ->
                (activity as HomeActivity).showCallsPopUp(companyId, OUTGOING, item.username, item.image_src)
            }
            item.STX?.let { janusManager.call(it.toSIPRemoteAddress()) }
        }
    }
}