package com.mvine.mcomm.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

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