package com.mvine.mcomm.presentation.login.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentLoginMenuBinding
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.presentation.LoginActivity
import com.mvine.mcomm.presentation.home.HomeActivity
import com.mvine.mcomm.presentation.login.viewmodel.LoginMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginMenuFragment : Fragment() {

    @Inject
    lateinit var janusManager: JanusManager

    private lateinit var fragmentLoginMenuBinding: FragmentLoginMenuBinding

    private val loginMenuViewModel : LoginMenuViewModel by viewModels()

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
        subscribeObservers()
    }

    private fun initListeners() {
        fragmentLoginMenuBinding.btnChangePassword.setOnClickListener {
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        fragmentLoginMenuBinding.btnLogout.setOnClickListener {
            loginMenuViewModel.logout()
        }
    }

    private fun subscribeObservers(){
        loginMenuViewModel.isLoggedOut.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when(response){
                    is Resource.Success -> {
                        performLogout()
                    }
                    is Resource.Error -> {
                        performLogout()
                    }
                    is Resource.Loading -> {
                        fragmentLoginMenuBinding.logoutProgress.visibility = View.VISIBLE
                    }
                }

            }
        })
    }

    private fun performLogout(){
        loginMenuViewModel.clearData()
        janusManager.endJanusSession()
        fragmentLoginMenuBinding.logoutProgress.visibility = View.GONE
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}