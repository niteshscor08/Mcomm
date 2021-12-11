package com.mvine.mcomm.presentation.home.contacts

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
import com.mvine.mcomm.databinding.FragmentContactsBinding
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment(), ContactsAdapter.Interaction {

    private val contactsViewModel: ContactsViewModel by viewModels()

    private lateinit var fragmentContactsBinding: FragmentContactsBinding

    private val contactsAdapter: ContactsAdapter = ContactsAdapter(this)

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
                        response.data?.let { contactsAdapter.submitList(it) }
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
    }

    override fun onItemSelected(position: Int, item: ContactsData) {

    }
}