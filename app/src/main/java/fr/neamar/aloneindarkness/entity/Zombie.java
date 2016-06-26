package fr.neamar.aloneindarkness.entity;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

import fr.neamar.aloneindarkness.DarknessActivity;

/**
 * Created by neamar on 26/06/16.
 */
public class Zombie {
    public static String TAG = "Zombie";

    public Zombie() {

    }

    public void drawZombie(int cubeProgram, int cubeLightPosParam, float[] lightPosInEyeSpace, int cubeModelParam, float[] modelCube, int cubeModelViewParam, float[] modelView, int cubePositionParam, FloatBuffer cubeVertices, int cubeModelViewProjectionParam, float[] modelViewProjection, int cubeNormalParam, FloatBuffer cubeNormals, int cubeColorParam, FloatBuffer cubeColors) {
        GLES20.glUseProgram(cubeProgram);

        GLES20.glUniform3fv(cubeLightPosParam, 1, lightPosInEyeSpace, 0);

        // Set the Model in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(cubeModelParam, 1, false, modelCube, 0);

        // Set the ModelView in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(cubeModelViewParam, 1, false, modelView, 0);

        // Set the position of the cube
        GLES20.glVertexAttribPointer(
                cubePositionParam, DarknessActivity.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, cubeVertices);

        // Set the ModelViewProjection matrix in the shader.
        GLES20.glUniformMatrix4fv(cubeModelViewProjectionParam, 1, false, modelViewProjection, 0);

        // Set the normal positions of the cube, again for shading
        GLES20.glVertexAttribPointer(cubeNormalParam, 3, GLES20.GL_FLOAT, false, 0, cubeNormals);
        GLES20.glVertexAttribPointer(cubeColorParam, 4, GLES20.GL_FLOAT, false, 0,
                cubeColors);

        // Enable vertex arrays
        GLES20.glEnableVertexAttribArray(cubePositionParam);
        GLES20.glEnableVertexAttribArray(cubeNormalParam);
        GLES20.glEnableVertexAttribArray(cubeColorParam);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        DarknessActivity.checkGLError("Drawing cube");
    }
}
