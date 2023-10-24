package com.example.mystoryapp1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp1.data.response.ListStoryItem
import com.example.mystoryapp1.databinding.ListStoryBinding

class UserAdapter(private val onItemClickCallback: OnItemClickCallBack): ListAdapter<ListStoryItem, UserAdapter.MyViewHolder>(DIFF_CALBACK) {
    private var onItemCallback:OnItemClickCallBack?=null

    inner class MyViewHolder(private val binding: ListStoryBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(result: ListStoryItem){
            Glide.with(itemView.context).load(result.photoUrl).skipMemoryCache(true)
                .into(binding.ivProfile)
            binding.tvUsername.text = result.name
            binding.description.text = result.description

            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val story = result
                    onItemClickCallback.onItemClicked(story)
                }
            }
        }
    }

    interface OnItemClickCallBack{
        fun onItemClicked(data: ListStoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
        onItemCallback?.let{callback->

        }
    }
    companion object{
        val DIFF_CALBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return  oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}