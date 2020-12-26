package com.caioluis.githubpopular.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.model.UiGitHubRepository

class GitHubReposViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(itemInfo: UiGitHubRepository) {
        val title = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryTitle)
        val description = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryDescription)
        val forks = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryForksCounter)
        val stars = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryStarsCounter)
        val userName = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryUserName)
        val userIcon = itemView.findViewById<AppCompatImageView>(R.id.ghRepositoryUserImage)

        title.text = itemInfo.name
        description.text = itemInfo.description
        forks.text = itemInfo.forksCount.toString()
        stars.text = itemInfo.stargazersCount.toString()
        userName.text = itemInfo.owner.login

        Glide.with(itemView.context).load(itemInfo.owner.avatarUrl).into(userIcon)
    }
}