package fr.neamar.aloneindarkness.entity;

import android.opengl.GLES20;

import fr.neamar.aloneindarkness.DarknessActivity;


public class Zombie {
    public static String TAG = "Zombie";

    public float[] modelCube;
    public float[] modelPosition;

    public Zombie(float[] modelPosition) {
        modelCube = new float[16];
        // Model first appears directly in front of user.
        this.modelPosition = modelPosition;
    }

    public void drawZombie(ZombieLoader zombieLoader, float[] lightPosInEyeSpace, float[] modelView, float[] modelViewProjection, boolean isLookingAtObject) {
        GLES20.glUseProgram(zombieLoader.cubeProgram);

        GLES20.glUniform3fv(zombieLoader.cubeLightPosParam, 1, lightPosInEyeSpace, 0);

        // Set the Model in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(zombieLoader.cubeModelParam, 1, false, modelCube, 0);

        // Set the ModelView in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(zombieLoader.cubeModelViewParam, 1, false, modelView, 0);

        // Set the position of the cube
        GLES20.glVertexAttribPointer(
                zombieLoader.cubePositionParam, DarknessActivity.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, zombieLoader.cubeVertices);

        // Set the ModelViewProjection matrix in the shader.
        GLES20.glUniformMatrix4fv(zombieLoader.cubeModelViewProjectionParam, 1, false, modelViewProjection, 0);

        // Set the normal positions of the cube, again for shading
        GLES20.glVertexAttribPointer(zombieLoader.cubeNormalParam, 3, GLES20.GL_FLOAT, false, 0, zombieLoader.cubeNormals);
        GLES20.glVertexAttribPointer(zombieLoader.cubeColorParam, 4, GLES20.GL_FLOAT, false, 0,
                isLookingAtObject ? zombieLoader.cubeFoundColors : zombieLoader.cubeColors);

        // Enable vertex arrays
        GLES20.glEnableVertexAttribArray(zombieLoader.cubePositionParam);
        GLES20.glEnableVertexAttribArray(zombieLoader.cubeNormalParam);
        GLES20.glEnableVertexAttribArray(zombieLoader.cubeColorParam);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        DarknessActivity.checkGLError("Drawing cube");
    }
}
