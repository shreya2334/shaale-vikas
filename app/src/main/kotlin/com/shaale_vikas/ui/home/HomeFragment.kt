package com.shaale_vikas.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.shaale_vikas.adapter.NeedAdapter
import com.shaale_vikas.databinding.FragmentHomeBinding
import com.shaale_vikas.model.Need
import com.shaale_vikas.ui.auth.AuthActivity
import com.shaale_vikas.ui.need.AddNeedFragment
import com.shaale_vikas.utils.toast
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class HomeFragment : Fragment() {

    private var _binding:
            FragmentHomeBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var adapter:
            NeedAdapter

    private val needRepository =
        com.shaale_vikas.repository.NeedRepository()

    private  var currentNeeds =
        listOf<Need>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentHomeBinding.inflate(
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

        setupRecyclerView()

        observeNeeds()

        val currentUserEmail =

            FirebaseAuth
                .getInstance()
                .currentUser
                ?.email ?: ""

        if (
            currentUserEmail !=
            "john@gmail.com"
        ) {

            binding.btnAddNeed.visibility =
                View.GONE
        }

        binding.btnAddNeed.setOnClickListener {

            parentFragmentManager
                .beginTransaction()
                .replace(
                    (view.parent as View).id,
                    AddNeedFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        binding.btnLogout.setOnClickListener {

            FirebaseAuth
                .getInstance()
                .signOut()

            startActivity(
                Intent(
                    requireContext(),
                    AuthActivity::class.java
                )
            )

            requireActivity().finish()
        }
    }

    private fun setupRecyclerView() {

        adapter = NeedAdapter(

            onPledgeClick = { clickedNeed ->

                val input = EditText(requireContext())

                input.hint =
                    "Enter pledge amount"

                AlertDialog.Builder(requireContext())

                    .setTitle("Pledge Support")

                    .setView(input)

                    .setPositiveButton(
                        "Pledge"
                    ) { _, _ ->

                        val enteredAmount =

                            input.text
                                .toString()
                                .toIntOrNull() ?: 0

                        val updatedNeed =

                            clickedNeed.copy(

                                pledgedAmount =

                                    clickedNeed
                                        .pledgedAmount +
                                            enteredAmount,

                                donors =

                                    clickedNeed.donors +

                                            (
                                                    FirebaseAuth
                                                        .getInstance()
                                                        .currentUser
                                                        ?.email
                                                        ?: "Anonymous"
                                                    )
                            )

                        needRepository
                            .updateNeed(updatedNeed)

                        toast(
                            "Pledged ₹$enteredAmount"
                        )
                    }

                    .setNegativeButton(
                        "Cancel",
                        null
                    )

                    .show()
            },

            onCompleteClick = { clickedNeed ->

                val input = EditText(requireContext())

                input.hint =
                    "Enter after image URL"

                AlertDialog.Builder(requireContext())

                    .setTitle("Add Completion Image")

                    .setView(input)

                    .setPositiveButton(
                        "Complete"
                    ) { _, _ ->

                        val afterImageUrl =

                            input.text.toString()

                        val completedNeed =

                            clickedNeed.copy(

                                status = "Completed",

                                afterImageUrl =
                                    afterImageUrl
                            )

                        needRepository
                            .updateNeed(completedNeed)

                        toast(
                            "${clickedNeed.title} completed"
                        )
                    }

                    .setNegativeButton(
                        "Cancel",
                        null
                    )

                    .show()
            }
        )

        binding.recyclerViewNeeds.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        binding.recyclerViewNeeds.adapter =
            adapter
    }

    private fun observeNeeds() {

        needRepository.observeNeeds {

            currentNeeds = it

            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}