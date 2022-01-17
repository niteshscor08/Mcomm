package com.mvine.mcomm.presentation.home.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mvine.mcomm.BR
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentChatsBinding
import com.mvine.mcomm.presentation.common.base.BaseFragment
import com.mvine.mcomm.presentation.home.HomeActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showSearchBar()
    }

}