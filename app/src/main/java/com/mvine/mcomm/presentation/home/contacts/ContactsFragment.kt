package com.mvine.mcomm.presentation.home.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentContactsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private val contactsViewModel: ContactsViewModel by viewModels()

    private lateinit var fragmentContactsBinding: FragmentContactsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentContactsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)
        return fragmentContactsBinding.root
    }
}