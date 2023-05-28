package com.example.news.models

data class NewsResponse(
    val feed: Feed,
    val items: List<Item>,
    val status: String
)