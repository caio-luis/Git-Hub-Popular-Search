package com.caioluis.githubpopular.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.caioluis.githubpopular.Constants.REPOSITORIES_VIEW_TYPE
import com.caioluis.githubpopular.Constants.RETRY_BUTTON_VIEW_TYPE
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.model.RetryButtonModel
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiModel

class GitHubRepositoriesAdapter(private val clickOutput: () -> Unit) :
    RecyclerView.Adapter<ViewHolder>() {

    private val itemList: MutableList<UiModel> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is UiGitHubRepository -> REPOSITORIES_VIEW_TYPE
            is RetryButtonModel -> RETRY_BUTTON_VIEW_TYPE
            else -> -1
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {

            RETRY_BUTTON_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_retry_button, parent, false)

                RetryButtonViewHolder(view, clickOutput)
            }

            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_github_repository,
                    parent,
                    false
                )
                GitHubReposViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            REPOSITORIES_VIEW_TYPE -> {
                holder as GitHubReposViewHolder
                holder.bindView(itemList[position] as UiGitHubRepository)
            }

            RETRY_BUTTON_VIEW_TYPE -> {
                holder as RetryButtonViewHolder
                holder.bindView(itemList[position] as RetryButtonModel)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<UiModel>?) {
        itemList.clear()
        if (list != null) {
            itemList.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun insertMoreItems(list: List<UiModel>) {
        itemList.addAll(list)
        notifyItemRangeInserted(itemList.lastIndex, list.size)
    }

    fun addRetryButton(exception: Throwable?) {
        if (!itemList.any { it is RetryButtonModel }) {
            itemList.add(RetryButtonModel(exception = exception))
            notifyItemInserted(itemList.lastIndex)
        }
    }

    fun removeAllRetryButtons() {
        itemList.forEachIndexed { index, uiModel ->
            uiModel
                .takeIf { it is RetryButtonModel }
                ?.let {
                    itemList.removeAt(index)
                    notifyItemRemoved(index)
                }
        }
    }
}
