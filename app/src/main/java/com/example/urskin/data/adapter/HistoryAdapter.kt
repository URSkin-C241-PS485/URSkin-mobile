package com.example.urskin.data.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.urskin.data.room.HistoryEntity
import com.example.urskin.databinding.ItemHistoryBinding
import com.example.urskin.view.result.ResultActivity

class HistoryAdapter: ListAdapter<HistoryEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(history:HistoryEntity){
            binding.imgHistory.setImageURI(Uri.parse(history.uri))
            binding.tvResult.text = history.result
            binding.tvDate.text = history.date

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, ResultActivity::class.java)
                intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, history.uri)
                intent.putExtra(ResultActivity.EXTRA_RESULT, history.result)
                binding.root.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}