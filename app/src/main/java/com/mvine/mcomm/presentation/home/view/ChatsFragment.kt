package com.mvine.mcomm.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentChatsBinding

class ChatsFragment : Fragment() {

    private lateinit var fragmentChatsBinding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentChatsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false)
        return fragmentChatsBinding.root
    }
}