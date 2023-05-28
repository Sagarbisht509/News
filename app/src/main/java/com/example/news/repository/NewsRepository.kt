package com.example.news.repository

import com.example.news.api.RetrofitInstance
import com.example.news.models.NewsResponse
import retrofit2.Response

class NewsRepository {

    suspend fun getNews(): Response<NewsResponse> {
        return RetrofitInstance.api.getNews()
    }
}