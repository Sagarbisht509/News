package com.example.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.ItemBinding
import com.example.news.models.Item
import java.time.LocalDate

class NewsAdapter(
    private val onArticleClicked: (Item) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.textViewTitle.text = item.title
            binding.textViewTime.text = getTime(item)

            Glide.with(binding.root.context).load(item.thumbnail)
                .placeholder(R.drawable.ic_placeholder).into(binding.thumbnail)
        }


        private fun getTime(item: Item) : String {
            val todayDate = LocalDate.now()
            val newsDate = item.pubDate.substring(0, 10)
            val newsTime = item.pubDate.substring(if (item.pubDate[11] != '0') 11 else 12, 16)

            return when (newsDate) {
                todayDate.toString() -> {
                    "Today at $newsTime"
                }

                todayDate.minusDays(1).toString() -> {
                    "Yesterday at $newsTime"
                }

                else -> {
                    val date = item.pubDate.substring(if (item.pubDate[11] != '0') 8 else 9, 10)
                    "$date at $newsTime"
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Item>() {
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return false
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = differ.currentList[position]
        item?.let {
            holder.bind(it)
        }

        holder.itemView.setOnClickListener {
            onArticleClicked(item)
        }
    }
}