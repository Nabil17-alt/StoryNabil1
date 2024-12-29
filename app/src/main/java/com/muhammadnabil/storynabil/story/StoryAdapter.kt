package com.muhammadnabil.storynabil.story

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muhammadnabil.storynabil.databinding.ItemStoryBinding
import com.muhammadnabil.storynabil.model.Story

class StoryAdapter(
    private val onItemClick: (Story) -> Unit
) : PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(StoryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val storyItem = getItem(position)
        if (storyItem != null) {
            Log.d("StoryAdapter", "Binding story: ${storyItem.name}, ${storyItem.photoUrl}")
            holder.bind(storyItem)
            holder.itemView.setOnClickListener {
                onItemClick(storyItem)
            }
        } else {
            Log.d("StoryAdapter", "Story item is null at position: $position")
        }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.tvItemName.text = story.name
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
        }
    }

    object StoryComparator : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
    }
}