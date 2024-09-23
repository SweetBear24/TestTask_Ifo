package com.example.newsapp

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newsapp.opengl.MyGLRenderer

class MainActivity : ComponentActivity() {
    private var showNews by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Проверка первого запуска
        val sharedPref = getSharedPreferences("news_app", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPref.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            // Сохраняем, что это больше не первый запуск
            sharedPref.edit().putBoolean("isFirstLaunch", false).apply()
        }

        // Устанавливаем начальный экран
        setContent {
            if (showNews) {
                NewsScreen(onDismiss = { showNews = false })
            } else {
                OpenGLView(context = this)
            }
        }
    }
}

@Composable
fun OpenGLView(context: Context) {
    AndroidView(factory = {
        GLSurfaceView(context).apply {
            setEGLContextClientVersion(1) // Используем OpenGL ES 2.0
            setRenderer(MyGLRenderer(context))
            // Включите рендеринг
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
    })
}
