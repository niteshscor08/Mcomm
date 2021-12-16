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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentContactsBinding
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.MultipleRowTypeAdapter
import com.mvine.mcomm.presentation.home.HomeViewModel
import com.mvine.mcomm.util.prepareRowTypesFromContactsData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment(), ListInteraction<ContactsData> {

    private val contactsViewModel: ContactsViewModel by viewModels()

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
        Toast.makeText(
            activity,
            "Voice call selected for ${item.username}",
            Toast.LENGTH_LONG
        ).show()
    }
}