package com.mvine.mcomm.presentation.login.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentLoginBinding
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.domain.util.Resource.Success
import com.mvine.mcomm.presentation.home.HomeActivity
import com.mvine.mcomm.presentation.login.viewmodel.LoginViewModel
import com.mvine.mcomm.util.*
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var preferenceHandler: PreferenceHandler

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences = requireContext().getSharedPreferences(
            MCOMM_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
        setUpData()
    }

    private fun subscribeObservers() {
        loginViewModel.cookieLiveData.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when (response) {
                    is Success -> {
                        sharedPreferences.edit()?.let { editor ->
                            editor.putString(LOGIN_TOKEN, response.data)
                            editor.apply()
                        }
                        sharedPreferences.getString(LOGIN_TOKEN, null)?.let {
                            loginViewModel.getUserInfo(it)
                        }
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

        loginViewModel.userInfo.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when(response){
                    is Success -> {
                        preferenceHandler.save(USER_INFO, Gson().toJson(response.data))
                        fragmentLoginBinding.loginProgress.visibility = View.GONE
                        Toast.makeText(activity, "Login Successful!", Toast.LENGTH_LONG).show()
                        val intent = Intent(activity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    is Resource.Error -> {
                        fragmentLoginBinding.loginProgress.visibility = View.GONE
                        Toast.makeText(activity, "Login Failure! Please try again.", Toast.LENGTH_LONG).show()
                    }
                }

            }
        })
    }

    private fun setUpData() {
        if (BuildConfig.DEBUG) {
            fragmentLoginBinding.etEmail.setText("asen@mvine.com", TextView.BufferType.EDITABLE)
            fragmentLoginBinding.etPassword.setText("1qaz2wsx", TextView.BufferType.EDITABLE)
        }
    }

    private fun initListeners() {

        fragmentLoginBinding.etEmail.apply {
            setOnClickListener {
                showKeyboard()
            }
        }

        fragmentLoginBinding.etPassword.apply {
            setOnClickListener {
                showKeyboard()
            }
        }

        fragmentLoginBinding.apply {
            btnLogin.setOnClickListener {
                fragmentLoginBinding.etPassword.hideKeyboard()
                fragmentLoginBinding.etEmail.hideKeyboard()
                val username = etEmail.text.toString()
                val password = etPassword.text.toString()
                loginViewModel.login(username, password)
            }
        }
    }

}