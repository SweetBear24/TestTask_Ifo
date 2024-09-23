// NewsViewModel.kt
package com.example.newsapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
class NewsViewModel : ViewModel() {
    private val _newsList = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsList: StateFlow<List<NewsItem>> = _newsList

    private val newsItems = listOf(
        NewsItem(1, "Breaking: Market hits all-time high", "Market is doing well.", 0),
        NewsItem(2, "Sports: Local team wins championship", "A local team won the championship.", 0),
        NewsItem(3, "Technology: New smartphone released", "A new smartphone has been released.", 0),
        NewsItem(4, "Health: Tips for a balanced diet", "Balanced diet tips.", 0),
        NewsItem(5, "Entertainment: New movie reviews", "Reviews of the latest movies.", 0),
        NewsItem(6, "World: Major geopolitical events", "Geopolitical events around the world.", 0),
        NewsItem(7, "Science: Discoveries in space exploration", "New discoveries in space.", 0),
        NewsItem(8, "Finance: Stock market trends", "Latest trends in the stock market.", 0),
        NewsItem(9, "Travel: Best destinations for 2024", "Top travel destinations for 2024.", 0),
        NewsItem(10, "Education: Innovations in learning", "Innovations in the field of education.", 0)
    )

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    init {
        _newsList.value = generateInitialNewsList()
        startNewsUpdate()
    }

    private fun generateInitialNewsList(): List<NewsItem> {
        // Перемешиваем список новостей и берем первые четыре
        return newsItems.shuffled().take(4)
    }

    private fun startNewsUpdate() {
        coroutineScope.launch {
            while (true) {
                delay(2000)  // Задержка в 5 секунд
                _newsList.update { currentNewsList ->
                    val currentIds = currentNewsList.map { it.id }
                    val availableNewsItems = newsItems.filter { it.id !in currentIds }

                    if (availableNewsItems.isNotEmpty()) {
                        val randomIndex = Random.nextInt(0, currentNewsList.size)
                        val randomNews = availableNewsItems.random()

                        val newNewsList = currentNewsList.toMutableList()
                        newNewsList[randomIndex] = randomNews

                        newNewsList
                    } else {
                        currentNewsList
                    }
                }
            }
        }
    }

    fun likeNews(id: Int) {
        _newsList.update { currentNewsList ->
            currentNewsList.map { news ->
                if (news.id == id) {
                    news.copy(likes = news.likes + 1)
                } else {
                    news
                }
            }
        }
    }
}
