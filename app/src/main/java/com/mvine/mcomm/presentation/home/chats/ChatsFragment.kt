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
import com.mvine.mcomm.janus.JanusManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment : Fragment() {

    private val chatsViewModel: ChatsViewModel by viewModels()

    private lateinit var fragmentChatsBinding: FragmentChatsBinding

    @Inject
    lateinit var janusManager: JanusManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentChatsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false)
        return fragmentChatsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentChatsBinding.send.setOnClickListener {

            janusManager.connect()
        }
    }
}