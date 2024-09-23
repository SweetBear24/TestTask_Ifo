// NewsData.kt
package com.example.newsapp

data class News(val id: Int, val content: String, var likes: Int)

val NEWS_LIST = listOf(
    News(1, "Breaking: Market hits all-time high", 0),
    News(2, "Sports: Local team wins championship", 0),
    News(3, "Technology: New smartphone released", 0),
    News(4, "Health: Tips for a balanced diet", 0),
    News(5, "Entertainment: New movie reviews", 0),
    News(6, "World: Major geopolitical events", 0),
    News(7, "Science: Discoveries in space exploration", 0),
    News(8, "Finance: Stock market trends", 0),
    News(9, "Travel: Best destinations for 2024", 0),
    News(10, "Education: Innovations in learning", 0)
)
