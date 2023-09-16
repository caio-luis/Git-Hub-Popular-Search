package com.caioluis.githubpopular.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.data.bridge.remote.NoMoreItemsException
import com.caioluis.githubpopular.model.RetryButtonModel

class RetryButtonViewHolder(view: View, private val clickOutput: () -> Unit) :
    RecyclerView.ViewHolder(view) {

    fun bindView(retryButtonModel: RetryButtonModel) {
        val errorText = itemView.findViewById<AppCompatTextView>(R.id.error_text)
        val retryButton = itemView.findViewById<AppCompatButton>(R.id.retry_button)

        errorText.text = retryButtonModel.exception?.message

        when (retryButtonModel.exception) {
            is NoMoreItemsException -> retryButton.visibility = View.GONE
            else -> {
                retryButton.visibility = View.VISIBLE
                retryButton.setOnClickListener { clickOutput() }
            }
        }
    }
}