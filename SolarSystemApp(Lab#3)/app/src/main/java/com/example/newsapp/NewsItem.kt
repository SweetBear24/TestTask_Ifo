package com.example.newsapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class NewsItem(
    val id: Int,
    val title: String,
    val content: String,
    var likes: Int = 0 // Значение по умолчанию для лайков
)
@Composable
fun NewsItemView(news: NewsItem, onLike: (Int) -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxSize() // Ensures the card fills the space allocated to it
            .padding(4.dp) // Adds padding around the card
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize() // Ensures content fills the size of the card
        ) {
            // Column to arrange the text and button
            Column(
                modifier = Modifier
                    .weight(1f) // This makes the Column expand to fill available space
                    .fillMaxSize()
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = news.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            // Button at the bottom
            Button(
                onClick = { onLike(news.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier
                    .fillMaxWidth() // Ensures button fills the width of the card
                    .padding(top = 8.dp) // Adds space between the text and the button
            ) {
                Text(text = "❤ ${news.likes}", color = Color.White)
            }
        }
    }
}