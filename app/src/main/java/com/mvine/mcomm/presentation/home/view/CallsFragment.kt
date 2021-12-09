package com.mvine.mcomm.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentCallsBinding

class CallsFragment : Fragment() {

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
}