package com.mvine.mcomm.presentation.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentLoginMenuBinding
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.presentation.LoginActivity
import com.mvine.mcomm.presentation.home.HomeActivity
import com.mvine.mcomm.presentation.login.viewmodel.LoginMenuViewModel
import com.mvine.mcomm.util.SPACE
import com.mvine.mcomm.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginMenuFragment : Fragment() {

    @Inject
    lateinit var janusManager: JanusManager

    private lateinit var fragmentLoginMenuBinding: FragmentLoginMenuBinding

    private val loginMenuViewModel: LoginMenuViewModel by viewModels()

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
        loadUiData()
        initListeners()
        subscribeObservers()
        (activity as HomeActivity).hideBottomTabBar()
    }

    private fun loadUiData() {
        fragmentLoginMenuBinding.version.text = resources.getString(R.string.version).plus(SPACE).plus(BuildConfig.VERSION_NAME).plus("(").plus(BuildConfig.VERSION_CODE).plus(")")
        fragmentLoginMenuBinding.userName.text = loginMenuViewModel.getUserData().view.contact?.name
    }

    private fun initListeners() {
        fragmentLoginMenuBinding.btnChangePassword.setOnClickListener {
            startChangePasswordActivity()
        }
        fragmentLoginMenuBinding.btnLogout.setOnClickListener {
            loginMenuViewModel.logout()
        }
    }

    private fun subscribeObservers() {
        loginMenuViewModel.isLoggedOut.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when (response) {
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

    private fun performLogout() {
        loginMenuViewModel.clearData()
        janusManager.endJanusSession()
        fragmentLoginMenuBinding.logoutProgress.visibility = View.GONE
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            showSnackBar(
                fragmentLoginMenuBinding.root,
                resources.getString(R.string.pass_changed),
                resources.getString(R.string.password_change)
            )
        }
    }

    private fun startChangePasswordActivity() {
        val intent = Intent(activity, ChangePasswordActivity::class.java)
        resultLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as HomeActivity).resetAppbarMenuItem()
    }
}
