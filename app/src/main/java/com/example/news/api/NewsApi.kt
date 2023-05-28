package com.example.news.api

import com.example.news.models.NewsResponse
import com.example.news.util.RSS_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v1/api.json")
    suspend fun getNews(
        @Query("rss_url")
        rss_url: String = RSS_URL
    ): Response<NewsResponse>
}