package com.example.newsapp.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.example.newsapp.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Square background;
    private Sphere sun;
    private Sphere earth;
    private Sphere moon;
    private Sphere venus;
    private Sphere mars;
    private Sphere jupiter;
    private Sphere saturn;
    private Sphere mercury; // Меркурий
    private Sphere uranus; // Уран
    private Sphere neptune; // Нептун

    // Angles for rotation
    private float angleSun = 0.0f;
    private float angleEarth = 0.0f;
    private float angleMoon = 0.0f;
    private float angleEarthOrbit = 0.0f;
    private float angleVenusOrbit = 0.0f;
    private float angleMarsOrbit = 0.0f;
    private float angleJupiterOrbit = 0.0f;
    private float angleSaturnOrbit = 0.0f;
    private float angleMercuryOrbit = 0.0f; // Угол для Меркурия
    private float angleUranusOrbit = 0.0f; // Угол для Урана
    private float angleNeptuneOrbit = 0.0f; // Угол для Нептуна

    private Context context;

    public MyGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // Create background square
        background = new Square();
        background.loadTexture(gl, context, R.drawable.galaxy_texture);

        // Create spheres
        sun = new Sphere(0.5f); // Солнце в 2 раза меньше
        earth = new Sphere(0.25f); // Земля в 2 раза меньше
        moon = new Sphere(0.05f); // Луна в 2 раза меньше
        venus = new Sphere(0.25f); // Венера
        mars = new Sphere(0.2f); // Марс
        jupiter = new Sphere(0.5f); // Юпитер
        saturn = new Sphere(0.6f); // Сатурн
        mercury = new Sphere(0.15f); // Меркурий
        uranus = new Sphere(0.3f); // Уран
        neptune = new Sphere(0.3f); // Нептун

        // Load textures for spheres
        sun.loadTexture(gl, context, R.drawable.sun);
        earth.loadTexture(gl, context, R.drawable.earth);
        moon.loadTexture(gl, context, R.drawable.moon);
        venus.loadTexture(gl, context, R.drawable.venus);
        mars.loadTexture(gl, context, R.drawable.mars);
        jupiter.loadTexture(gl, context, R.drawable.jupiter);
        saturn.loadTexture(gl, context, R.drawable.saturn);
        mercury.loadTexture(gl, context, R.drawable.mercury); // Текстура для Меркурия
        uranus.loadTexture(gl, context, R.drawable.uranus); // Текстура для Урана
        neptune.loadTexture(gl, context, R.drawable.neptune); // Текстура для Нептуна
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Настройка камеры для солнечной системы
        gl.glLoadIdentity();

        // Отрисовка фона
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -40.0f); // Позиция квадрата дальше от камеры
        background.draw(gl);
        gl.glPopMatrix();


        gl.glTranslatef(0.0f, 0.0f, -15.0f); // Позиция квадрата дальше от камеры

        // Отрисовка Солнца
        gl.glPushMatrix();
        gl.glRotatef(angleSun, 0.0f, 1.0f, 0.0f); // Вращение вокруг своей оси
        sun.draw(gl);
        gl.glPopMatrix();

        // Отрисовка Меркурия
        gl.glPushMatrix();
        gl.glRotatef(angleMercuryOrbit, 0.0f, 1.0f, 0.0f); // Вращение вокруг Солнца
        gl.glTranslatef(4.0f, 0.0f, 0.0f); // Позиция Меркурия
        mercury.draw(gl);
        gl.glPopMatrix();

        // Отрисовка Венеры
        gl.glPushMatrix();
        gl.glRotatef(angleVenusOrbit, 0.0f, 1.0f, 0.0f); // Вращение вокруг Солнца
        gl.glTranslatef(5.0f, 0.0f, 0.0f); // Позиция Венеры
        venus.draw(gl);
        gl.glPopMatrix();

        // Отрисовка Земли
        gl.glPushMatrix();
        gl.glRotatef(angleEarthOrbit, 0.0f, 1.0f, 0.0f); // Вращение вокруг Солнца
        gl.glTranslatef(6.0f, 0.0f, 0.0f); // Позиция Земли
        earth.draw(gl);

        // Отрисовка Луны
        gl.glPushMatrix();
        gl.glRotatef(angleMoon, 0.0f, 1.0f, 0.0f); // Вращение Луны вокруг своей оси
        gl.glTranslatef(0.5f, 0.0f, 0.0f); // Позиция Луны относительно Земли (уменьшено)
        moon.draw(gl);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Отрисовка Марса
        gl.glPushMatrix();
        gl.glRotatef(angleMarsOrbit, 0.0f, 1.0f, 0.0f); // Вращение вокруг Солнца
        gl.glTranslatef(7.5f, 0.0f, 0.0f); // Позиция Марса
        mars.draw(gl);
        gl.glPopMatrix();

        // Отрисовка Юпитера
        gl.glPushMatrix();
        gl.glRotatef(angleJupiterOrbit, 0.0f, 1.0f, 0.0f); // Вращение вокруг Солнца
        gl.glTranslatef(10.0f, 0.0f, 0.0f); // Позиция Юпитера
        jupiter.draw(gl);
        gl.glPopMatrix();

        // Отрисовка Сатурна
        gl.glPushMatrix();
        gl.glRotatef(angleSaturnOrbit, 0.0f, 1.0f, 0.0f); // Вращение вокруг Солнца
        gl.glTranslatef(12.0f, 0.0f, 0.0f); // Позиция Сатурна
        saturn.draw(gl);
        gl.glPopMatrix();

        // Отрисовка Урана
        gl.glPushMatrix();
        gl.glRotatef(angleUranusOrbit, 0.0f, 1.0f, 0.0f); // Вращение вокруг Солнца
        gl.glTranslatef(14.0f, 0.0f, 0.0f); // Позиция Урана
        uranus.draw(gl);
        gl.glPopMatrix();

        // Отрисовка Нептуна
        gl.glPushMatrix();
        gl.glRotatef(angleNeptuneOrbit, 0.0f, 1.0f, 0.0f); // Вращение вокруг Солнца
        gl.glTranslatef(16.0f, 0.0f, 0.0f); // Позиция Нептуна
        neptune.draw(gl);
        gl.glPopMatrix();

        // Обновление углов
        angleSun += 0.1f; // Вращение Солнца
        angleEarth += 2.0f; // Вращение Земли
        angleMoon += 7.0f; // Вращение Луны
        angleEarthOrbit += 0.2f; // Вращение Земли вокруг Солнца
        angleVenusOrbit += 0.3f; // Вращение Венеры
        angleMarsOrbit += 0.2f; // Вращение Марса
        angleJupiterOrbit += 0.1f; // Вращение Юпитера
        angleSaturnOrbit += 0.05f; // Вращение Сатурна
        angleMercuryOrbit += 0.4f; // Вращение Меркурия
        angleUranusOrbit += 0.05f; // Вращение Урана
        angleNeptuneOrbit += 0.03f; // Вращение Нептуна
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        float aspectRatio = (float) width / height;
        GLU.gluPerspective(gl, 45.0f, aspectRatio, 1.0f, 100.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
