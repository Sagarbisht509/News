package com.example.news.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.NewsViewModel
import com.example.news.NewsViewModelProviderFactory
import com.example.news.adapter.NewsAdapter
import com.example.news.databinding.ActivityMainBinding
import com.example.news.models.Item
import com.example.news.repository.NewsRepository
import com.example.news.util.Resource
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter

    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAppBar()

        val newsRepository = NewsRepository()
        val newsViewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, newsViewModelProviderFactory)[NewsViewModel::class.java]

        viewModel.liveData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let {
                        newsAdapter.differ.submitList(it.items)
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    response.message?.let {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

        setUpRecyclerView()
        setUpSwipeRefresh()
    }

    private fun setUpAppBar() {
        setSupportActionBar(binding.appBar)
        supportActionBar?.title = "News"
    }

    private fun setUpSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.getNews()
        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter(::onArticleClicked)
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun onArticleClicked(item: Item) {
        val bundle = Bundle()
        bundle.putString("item", Gson().toJson(item))

        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}