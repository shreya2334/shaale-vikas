package com.shaale_vikas.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.shaale_vikas.databinding.FragmentRegisterBinding
import com.shaale_vikas.model.User
import com.shaale_vikas.utils.Resource
import com.shaale_vikas.utils.toast
import com.shaale_vikas.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding:
            FragmentRegisterBinding? = null

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
            FragmentRegisterBinding.inflate(
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

        binding.btnRegister.setOnClickListener {

            val role = User.ROLE_ALUMNI

            viewModel.register(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etConfirmPassword.text.toString(),
                role,
                binding.etSchool.text.toString(),
                binding.etYear.text.toString()
            )
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

                        toast("Registration successful")

                        (activity as AuthActivity)
                            .goToMain()
                    }

                    is Resource.Error -> {

                        toast(result.message)
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