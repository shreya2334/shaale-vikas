package com.shaale_vikas.adapter

import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shaale_vikas.databinding.ItemNeedBinding
import com.shaale_vikas.model.Need
import com.google.firebase.auth.FirebaseAuth

class NeedAdapter(

    private val onPledgeClick:
        (Need) -> Unit,

    private val onCompleteClick:
        (Need) -> Unit
) :
    RecyclerView.Adapter<NeedAdapter.NeedViewHolder>() {

    private val needsList =
        mutableListOf<Need>()

    inner class NeedViewHolder(
        private val binding:
        ItemNeedBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(need: Need) {

            Glide
                .with(binding.root.context)
                .load(need.imageUrl)
                .into(binding.ivNeedImage)

            binding.tvStatus.text =
                need.status

            binding.tvTitle.text =
                need.title

            binding.tvDescription.text =
                need.description

            binding.tvAmount.text =
                "₹${need.amountNeeded}"

            val totalAmount =

                need.amountNeeded
                    .toIntOrNull() ?: 0

            val pledged =
                need.pledgedAmount

            binding.tvProgress.text =

                "₹$pledged raised out of ₹$totalAmount"

            val progress =

                if (totalAmount > 0)
                    (pledged * 100) / totalAmount
                else
                    0

            binding.progressBarNeed.progress =
                progress

            binding.tvDonors.text =

                if (need.donors.isEmpty()) {

                    "No donors yet"

                } else {

                    "Donors: ${
                        need.donors.joinToString()
                    }"
                }

            if (
                need.afterImageUrl.isNotEmpty()
            ) {

                binding.ivAfterImage.visibility =
                    android.view.View.VISIBLE

                Glide
                    .with(binding.root.context)
                    .load(need.afterImageUrl)
                    .into(binding.ivAfterImage)

            } else {

                binding.ivAfterImage.visibility =
                    android.view.View.GONE
            }

            if (need.status == "Completed") {

                binding.btnPledge.visibility =
                    android.view.View.GONE

            } else {

                binding.btnPledge.visibility =
                    android.view.View.VISIBLE

                binding.btnPledge.setOnClickListener {

                    onPledgeClick(need)
                }
            }

            val currentUserEmail =

                FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.email ?: ""

            if (
                currentUserEmail !=
                "john@gmail.com"
            ) {

                binding.btnComplete.visibility =
                    android.view.View.GONE

            } else {

                binding.btnComplete.visibility =
                    android.view.View.VISIBLE

                if (need.status == "Completed") {

                    binding.btnComplete.text =
                        "Completed"

                    binding.btnComplete.isEnabled =
                        false

                } else {

                    binding.btnComplete.text =
                        "Mark Completed"

                    binding.btnComplete.isEnabled =
                        true

                    binding.btnComplete.setOnClickListener {

                        onCompleteClick(need)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NeedViewHolder {

        val binding =
            ItemNeedBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )

        return NeedViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NeedViewHolder,
        position: Int
    ) {

        holder.bind(
            needsList[position]
        )
    }

    override fun getItemCount():
            Int = needsList.size

    fun submitList(
        list: List<Need>
    ) {

        needsList.clear()

        needsList.addAll(list)

        notifyDataSetChanged()
    }
}