package com.caioluis.githubpopular.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.model.UiGitHubRepository

/**
 * Created by Caio Luis (@caio.luis) on 15/11/20
 */

class GitHubRepositoriesAdapter : RecyclerView.Adapter<GitHubRepositoriesAdapter.ViewHolder>() {

    private var gitHubRepositories: MutableList<UiGitHubRepository> = mutableListOf()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(itemInfo: UiGitHubRepository) {

            val title = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryTitle)
            val description = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryDescription)
            val forks = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryForksCounter)
            val stars = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryStarsCounter)
            val userName = itemView.findViewById<AppCompatTextView>(R.id.ghRepositoryUserName)
            val userIcon = itemView.findViewById<AppCompatImageView>(R.id.ghRepositoryUserImage)

            title.text = itemInfo.fullName
            description.text = itemInfo.description
            forks.text = itemInfo.forksCount.toString()
            stars.text = itemInfo.stargazersCount.toString()
            userName.text = itemInfo.owner.login

            Glide.with(itemView.context).load(itemInfo.owner.avatarUrl).into(userIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_github_repository, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(gitHubRepositories[position])
    }

    override fun getItemCount(): Int = gitHubRepositories.count()

    fun updateList(list: List<UiGitHubRepository>) {
        gitHubRepositories.clear()
        gitHubRepositories.addAll(list)
        notifyDataSetChanged()
    }
}