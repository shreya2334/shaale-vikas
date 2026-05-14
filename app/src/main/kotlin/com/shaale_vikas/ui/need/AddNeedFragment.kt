package com.shaale_vikas.ui.need

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.shaale_vikas.databinding.FragmentAddNeedBinding
import com.shaale_vikas.utils.Resource
import com.shaale_vikas.utils.toast
import com.shaale_vikas.viewmodel.AddNeedViewModel
import kotlinx.coroutines.launch
import android.net.Uri
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class AddNeedFragment : Fragment() {

    private var _binding:
            FragmentAddNeedBinding? = null

    private val binding
        get() = _binding!!

    private val viewModel:
            AddNeedViewModel by viewModels()

    private var selectedImageUri:
            Uri? = null

    private val imagePicker =

        registerForActivityResult(
            ActivityResultContracts
                .StartActivityForResult()
        ) { result ->

            if (
                result.resultCode ==
                android.app.Activity.RESULT_OK
            ) {

                selectedImageUri =
                    result.data?.data

                toast("Image Selected")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentAddNeedBinding.inflate(
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

        binding.btnPickImage.setOnClickListener {

            val intent = Intent(

                Intent.ACTION_PICK
            )

            intent.type = "image/*"

            imagePicker.launch(intent)
        }

        binding.btnAddNeed.setOnClickListener {

            val uid =

                FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.uid ?: ""

            binding.progressBar.visibility =
                View.VISIBLE

            viewModel.addNeed(

                title =
                    binding.etTitle.text.toString(),

                description =
                    binding.etDescription.text.toString(),

                amount =
                    binding.etAmount.text.toString(),

                imageUrl =

                    "https://images.unsplash.com/photo-1580582932707-520aed937b7b",

                createdBy = uid,

                onSuccess = {

                    binding.progressBar.visibility =
                        View.GONE

                    toast("Need Added Successfully")

                    clearFields()

                    parentFragmentManager
                        .popBackStack()
                },

                onError = { error ->

                    binding.progressBar.visibility =
                        View.GONE

                    toast(error)
                }
            )
        }
    }

    private fun clearFields() {

        binding.etTitle.text?.clear()

        binding.etDescription.text?.clear()

        binding.etAmount.text?.clear()
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}