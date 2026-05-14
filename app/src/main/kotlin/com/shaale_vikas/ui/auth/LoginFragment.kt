package com.shaale_vikas.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.shaale_vikas.databinding.FragmentLoginBinding
import com.shaale_vikas.utils.Resource
import com.shaale_vikas.utils.toast
import com.shaale_vikas.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding:
            FragmentLoginBinding? = null

    private val binding
        get() = _binding!!

    private val viewModel:
            AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentLoginBinding.inflate(
                inflater,
                container,
                false
            )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        binding.btnLogin.setOnClickListener {

            viewModel.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        binding.btnGoRegister.setOnClickListener {

            (activity as AuthActivity)
                .goToRegister()
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.state.collect { result ->

                binding.progressBar.visibility =

                    if (result is Resource.Loading)
                        View.VISIBLE
                    else
                        View.GONE

                when (result) {

                    is Resource.Success -> {

                        toast("Login successful")

                        viewModel.reset()

                        (activity as AuthActivity)
                            .goToMain()
                    }

                    is Resource.Error -> {

                        toast(result.message)

                        viewModel.reset()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}