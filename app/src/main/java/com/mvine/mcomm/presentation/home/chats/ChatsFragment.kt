package com.mvine.mcomm.presentation.home.chats

import androidx.fragment.app.viewModels
import com.mvine.mcomm.BR
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentChatsBinding
import com.mvine.mcomm.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : BaseFragment<FragmentChatsBinding,ChatsViewModel >() {

    private val chatsViewModel: ChatsViewModel by viewModels()

    override val bindingVariable: Int
        get() = BR.chatsViewModel

    override val layoutId: Int
        get() = R.layout.fragment_chats

    override fun getViewModel(): ChatsViewModel {
       return chatsViewModel
    }

}