package com.example.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.models.NewsResponse
import com.example.news.repository.NewsRepository
import com.example.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val liveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    init {
        getNews()
    }

    fun getNews() = viewModelScope.launch {
        liveData.postValue(Resource.Loading())
        val response = newsRepository.getNews()
        liveData.postValue(handleNewsResponse(response))
    }

    private fun handleNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }
}