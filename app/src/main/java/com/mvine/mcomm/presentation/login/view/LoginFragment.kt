package com.mvine.mcomm.presentation.login.view

import android.content.Context
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
import com.mvine.mcomm.databinding.FragmentLoginBinding
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.domain.util.Resource.Success
import com.mvine.mcomm.presentation.home.HomeActivity
import com.mvine.mcomm.presentation.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModels()

    private val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

    private lateinit var fragmentLoginBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return fragmentLoginBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        initListeners()

    }

    private fun subscribeObservers() {
        loginViewModel.cookieLiveData.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when (response) {
                    is Success -> {
                        fragmentLoginBinding.loginProgress.visibility = View.GONE
                        sharedPreferences?.edit()?.let { editor ->
                            editor.putString(LOGIN_TOKEN, response.data)
                            editor.apply()
                        }
                        Toast.makeText(activity, "Login Successful!", Toast.LENGTH_LONG).show()
                        val intent = Intent(activity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    is Resource.Error -> {
                        fragmentLoginBinding.loginProgress.visibility = View.GONE
                        Toast.makeText(activity, "Login Failure! Please try again.", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        fragmentLoginBinding.loginProgress.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun initListeners() {
        fragmentLoginBinding.apply {
            btnLogin.setOnClickListener {
                val username = etEmail.text.toString()
                val password = etPassword.text.toString()
                loginViewModel.login(username, password)
            }
        }
    }

    companion object {
        const val LOGIN_TOKEN = "Login Token"
    }

}