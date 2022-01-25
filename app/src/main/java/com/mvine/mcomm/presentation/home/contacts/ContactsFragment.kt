package com.mvine.mcomm.presentation.home.contacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvine.mcomm.BR
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentContactsBinding
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.MultipleRowTypeAdapter
import com.mvine.mcomm.presentation.common.base.BaseFragment
import com.mvine.mcomm.presentation.home.HomeActivity
import com.mvine.mcomm.presentation.home.HomeViewModel
import com.mvine.mcomm.util.EMPTY_STRING
import com.mvine.mcomm.util.prepareRowTypesFromContactsData
import com.mvine.mcomm.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding, ContactsViewModel>(), ListInteraction<ContactsData> {

    private val contactsViewModel: ContactsViewModel by viewModels()

    @Inject
    lateinit var janusManager: JanusManager

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val contactsAdapter: MultipleRowTypeAdapter = MultipleRowTypeAdapter(arrayListOf())

    override val bindingVariable: Int
        get() = BR.contactsViewModel

    override val layoutId: Int
        get() = R.layout.fragment_contacts

    override fun getViewModel(): ContactsViewModel {
        return contactsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        setUpViews()
    }

    private fun setUpViews() {
        binding.rvContacts.apply {
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
                        binding.progressContacts.visibility = View.GONE
                        response.data?.let {
                            contactsAdapter.updateData(prepareRowTypesFromContactsData(contactsViewModel.sortAlphabetically(it), this, true))
                        }
                    }
                    is Resource.Error -> {
                        binding.progressContacts.visibility = View.GONE
                        response.message?.let {
                            showSnackBar(binding.root, response.message, null, false)
                        }
                    }
                    is Resource.Loading -> {
                        binding.progressContacts.visibility = View.VISIBLE
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
        item.STX?.let { companyId ->
            (activity as HomeActivity).startOutgoingCall(
                sTX = companyId,
                userName = item.username ?: EMPTY_STRING,
                uri = item.image_src ?: EMPTY_STRING
            )
        }
    }

    override fun onItemSelectedForExpansion(
        position: Int,
        item: ContactsData,
        isExpanded: Boolean
    ) {
        val contactDataList: ArrayList<ContactsData>? = contactsViewModel.contacts.value?.data
        contactDataList?.let {
            it.forEachIndexed { index, contactsData ->
                contactsData.isExpanded = if (index == position) isExpanded else false
            }
            contactsAdapter.updateData(prepareRowTypesFromContactsData(contactsViewModel.sortAlphabetically(it), this))
        }
    }
}
