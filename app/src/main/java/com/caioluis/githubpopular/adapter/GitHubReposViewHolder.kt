package com.caioluis.githubpopular.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caioluis.githubpopular.databinding.ListItemGithubRepositoryBinding
import com.caioluis.githubpopular.extensions.openBrowserIntent
import com.caioluis.githubpopular.model.UiGitHubRepository

class GitHubReposViewHolder(private val binding: ListItemGithubRepositoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindView(itemInfo: UiGitHubRepository) {

        binding.root.setOnClickListener {
            binding.root.context.openBrowserIntent(itemInfo.htmlUrl)
        }

        binding.ghRepositoryTitle.text = itemInfo.name
        binding.ghRepositoryDescription.text = itemInfo.description
        binding.ghRepositoryForksCounter.text = itemInfo.forksCount.toString()
        binding.ghRepositoryStarsCounter.text = itemInfo.stargazersCount.toString()
        binding.ghRepositoryUserName.text = itemInfo.owner.login

        Glide
            .with(itemView.context)
            .load(itemInfo.owner.avatarUrl)
            .centerCrop()
            .into(binding.ghRepositoryUserImage)
    }
}
