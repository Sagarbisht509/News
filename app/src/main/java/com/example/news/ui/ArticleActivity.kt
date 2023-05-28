package com.example.news.ui

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.news.databinding.ActivityArticleBinding
import com.example.news.models.Item
import com.google.gson.Gson

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding

    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAppBar()

        getData()

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(item.link)
        }
    }

    private fun setUpAppBar() {
        setSupportActionBar(binding.appBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getData() {
        val bundle = intent.extras
        bundle?.let {
            val jsonItem = it.getString("item")
            if (jsonItem != null) {
                item = Gson().fromJson(jsonItem, Item::class.java)
            }
        }
    }
}