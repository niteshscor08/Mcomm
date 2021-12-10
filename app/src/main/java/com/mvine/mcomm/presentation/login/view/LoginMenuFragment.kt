package com.mvine.mcomm.presentation.login.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentLoginMenuBinding

class LoginMenuFragment : Fragment() {

    private lateinit var fragmentLoginMenuBinding: FragmentLoginMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLoginMenuBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_menu, container, false)
        return fragmentLoginMenuBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        fragmentLoginMenuBinding.btnChangePassword.setOnClickListener {
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }

}