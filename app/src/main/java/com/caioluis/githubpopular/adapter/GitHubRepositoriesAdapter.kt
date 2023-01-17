package com.caioluis.githubpopular.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.model.UiGitHubRepository

/**
 * Created by Caio Luis (caio-luis) on 15/11/20
 */

const val VISIBLE_THRESHOLD = 5

class GitHubRepositoriesAdapter : RecyclerView.Adapter<GitHubReposViewHolder>() {

    private var gitHubRepositories: MutableList<UiGitHubRepository> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubReposViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_github_repository, parent, false)

        return GitHubReposViewHolder(view)
    }

    override fun onBindViewHolder(holder: GitHubReposViewHolder, position: Int) {
        holder.bindView(gitHubRepositories[position])
    }

    override fun getItemCount(): Int = gitHubRepositories.count()

    fun populateList(list: List<UiGitHubRepository>) = refreshList(list)

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshList(list: List<UiGitHubRepository>) {
        gitHubRepositories.clear()
        gitHubRepositories.addAll(list)
        notifyDataSetChanged()
    }

    fun insertMoreItems(list: List<UiGitHubRepository>) {
        gitHubRepositories.addAll(list)
        notifyItemRangeInserted(gitHubRepositories.lastIndex, list.size)
    }

    fun addPagination(action: () -> Unit = {}): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0) return

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                val visibleItemCount = recyclerView.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD)
                    action()
            }
        }
    }
}
