package com.example.newsapp.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Sphere {

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private ShortBuffer indexBuffer;
    private int textureId;
    private float radius;

    public Sphere(float radius) {
        this.radius = radius;
        initializeBuffers();
    }

    private void initializeBuffers() {
        int numSlices = 36;
        int numStacks = 18;

        int numVertices = (numSlices + 1) * (numStacks + 1);
        int numIndices = numSlices * numStacks * 6;

        float[] vertices = new float[numVertices * 3];
        float[] textureCoords = new float[numVertices * 2];
        short[] indices = new short[numIndices];

        float stackStep = (float) Math.PI / numStacks;
        float sliceStep = (float) (2 * Math.PI) / numSlices;

        int vertexIndex = 0;
        int texCoordIndex = 0;
        for (int i = 0; i <= numStacks; i++) {
            float stackAngle = (float) (Math.PI / 2 - i * stackStep);
            float xy = (float) Math.cos(stackAngle);
            float z = (float) Math.sin(stackAngle);

            for (int j = 0; j <= numSlices; j++) {
                float sliceAngle = j * sliceStep;
                float x = xy * (float) Math.cos(sliceAngle);
                float y = xy * (float) Math.sin(sliceAngle);

                vertices[vertexIndex++] = x * radius;
                vertices[vertexIndex++] = y * radius;
                vertices[vertexIndex++] = z * radius;

                textureCoords[texCoordIndex++] = (float) j / numSlices;
                textureCoords[texCoordIndex++] = 1 - (float) i / numStacks;
            }
        }

        int index = 0;
        for (int i = 0; i < numStacks; i++) {
            for (int j = 0; j < numSlices; j++) {
                short v1 = (short) (i * (numSlices + 1) + j);
                short v2 = (short) (i * (numSlices + 1) + (j + 1));
                short v3 = (short) ((i + 1) * (numSlices + 1) + j);
                short v4 = (short) ((i + 1) * (numSlices + 1) + (j + 1));

                indices[index++] = v1;
                indices[index++] = v2;
                indices[index++] = v3;
                indices[index++] = v2;
                indices[index++] = v4;
                indices[index++] = v3;
            }
        }

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(textureCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(indices.length * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        indexBuffer = byteBuffer.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    public void loadTexture(GL10 gl, Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);

        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        textureId = textures[0];

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        bitmap.recycle();
    }

    public void draw(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        gl.glDrawElements(GL10.GL_TRIANGLES, indexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, indexBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
