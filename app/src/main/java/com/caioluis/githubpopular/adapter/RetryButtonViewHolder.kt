package com.caioluis.githubpopular.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.caioluis.githubpopular.data.bridge.remote.NoMoreItemsException
import com.caioluis.githubpopular.databinding.ItemRetryButtonBinding
import com.caioluis.githubpopular.model.RetryButtonModel

class RetryButtonViewHolder(
    private val binding: ItemRetryButtonBinding,
    private val clickOutput: () -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(retryButtonModel: RetryButtonModel) {

        binding.errorText.text = retryButtonModel.exception?.message

        when (retryButtonModel.exception) {
            is NoMoreItemsException -> binding.retryButton.visibility = View.GONE
            else -> {
                binding.retryButton.visibility = View.VISIBLE
                binding.retryButton.setOnClickListener { clickOutput() }
            }
        }
    }
}