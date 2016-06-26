package fr.neamar.aloneindarkness.entity;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;

import fr.neamar.aloneindarkness.DarknessActivity;
import fr.neamar.aloneindarkness.layoutdata.ZombieLayoutData;


public class ZombieLoader {
    public FloatBuffer cubeVertices;
    public FloatBuffer cubeColors;
    public FloatBuffer cubeFoundColors;
    public FloatBuffer cubeNormals;

    public int cubeProgram;
    public int cubePositionParam;
    public int cubeNormalParam;
    public int cubeColorParam;
    public int cubeModelParam;
    public int cubeModelViewParam;
    public int cubeModelViewProjectionParam;
    public int cubeLightPosParam;

    public ZombieLoader() {

    }

    public void onSurfaceCreated(EGLConfig config, int vertexShader, int passthroughShader) {
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(ZombieLayoutData.CUBE_COORDS.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        cubeVertices = bbVertices.asFloatBuffer();
        cubeVertices.put(ZombieLayoutData.CUBE_COORDS);
        cubeVertices.position(0);

        ByteBuffer bbColors = ByteBuffer.allocateDirect(ZombieLayoutData.CUBE_COLORS.length * 4);
        bbColors.order(ByteOrder.nativeOrder());
        cubeColors = bbColors.asFloatBuffer();
        cubeColors.put(ZombieLayoutData.CUBE_COLORS);
        cubeColors.position(0);

        ByteBuffer bbFoundColors =
                ByteBuffer.allocateDirect(ZombieLayoutData.CUBE_FOUND_COLORS.length * 4);
        bbFoundColors.order(ByteOrder.nativeOrder());
        cubeFoundColors = bbFoundColors.asFloatBuffer();
        cubeFoundColors.put(ZombieLayoutData.CUBE_FOUND_COLORS);
        cubeFoundColors.position(0);

        ByteBuffer bbNormals = ByteBuffer.allocateDirect(ZombieLayoutData.CUBE_NORMALS.length * 4);
        bbNormals.order(ByteOrder.nativeOrder());
        cubeNormals = bbNormals.asFloatBuffer();
        cubeNormals.put(ZombieLayoutData.CUBE_NORMALS);
        cubeNormals.position(0);


        cubeProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(cubeProgram, vertexShader);
        GLES20.glAttachShader(cubeProgram, passthroughShader);
        GLES20.glLinkProgram(cubeProgram);
        GLES20.glUseProgram(cubeProgram);

        DarknessActivity.checkGLError("Cube program");

        cubePositionParam = GLES20.glGetAttribLocation(cubeProgram, "a_Position");
        cubeNormalParam = GLES20.glGetAttribLocation(cubeProgram, "a_Normal");
        cubeColorParam = GLES20.glGetAttribLocation(cubeProgram, "a_Color");

        cubeModelParam = GLES20.glGetUniformLocation(cubeProgram, "u_Model");
        cubeModelViewParam = GLES20.glGetUniformLocation(cubeProgram, "u_MVMatrix");
        cubeModelViewProjectionParam = GLES20.glGetUniformLocation(cubeProgram, "u_MVP");
        cubeLightPosParam = GLES20.glGetUniformLocation(cubeProgram, "u_LightPos");

        DarknessActivity.checkGLError("Cube program params");
    }
}