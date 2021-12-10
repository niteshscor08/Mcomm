package com.mvine.mcomm.presentation.home.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentChatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : Fragment() {

    private val chatsViewModel: ChatsViewModel by viewModels()

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