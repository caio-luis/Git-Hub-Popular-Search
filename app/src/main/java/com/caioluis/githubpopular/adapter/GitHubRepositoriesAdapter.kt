package com.caioluis.githubpopular.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.model.UiGitHubRepository

/**
 * Created by Caio Luis (caio-luis) on 15/11/20
 */


class GitHubRepositoriesAdapter : ListAdapter<UiGitHubRepository, GitHubReposViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubReposViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_github_repository, parent, false)

        return GitHubReposViewHolder(view)
    }

    override fun onBindViewHolder(holder: GitHubReposViewHolder, position: Int) {
        holder.bindView(currentList[position])
    }

    fun refreshList(list: List<UiGitHubRepository>) {
        submitList(null)
        submitList(list)
    }

    fun insertMoreItems(list: List<UiGitHubRepository>) {
        val newList = currentList.toMutableList()
        newList.addAll(list)
        submitList(newList)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UiGitHubRepository>() {
            override fun areItemsTheSame(
                oldItem: UiGitHubRepository,
                newItem: UiGitHubRepository,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UiGitHubRepository,
                newItem: UiGitHubRepository,
            ): Boolean = oldItem.id == newItem.id &&
                    oldItem.description == newItem.description &&
                    oldItem.name == newItem.name &&
                    oldItem.owner == newItem.owner &&
                    oldItem.fullName == newItem.fullName &&
                    oldItem.forksCount == newItem.forksCount &&
                    oldItem.pullsUrl == newItem.pullsUrl &&
                    oldItem.stargazersCount == newItem.stargazersCount
        }
    }
}
