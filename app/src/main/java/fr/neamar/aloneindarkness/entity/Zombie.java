package fr.neamar.aloneindarkness.entity;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.google.vr.sdk.audio.GvrAudioEngine;

import fr.neamar.aloneindarkness.DarknessActivity;


public class Zombie {
    public static String TAG = "Zombie";
    public static final String ZOMBIE_SOUND_FILE = "zombie_walk.wav";
    public static final String ZOMBIE_DEATH_SOUND_FILE = "zombie_death.wav";
    public static final float ROTATION_SPEED = 0.3f;


    public float[] modelCube;
    public float[] modelPosition;
    public float speed;

    private int zombieSoundId = GvrAudioEngine.INVALID_ID;

    public Zombie(final float[] modelPosition, final GvrAudioEngine gvrAudioEngine, float speed) {
        modelCube = new float[16];
        this.modelPosition = modelPosition;

        this.speed = speed;

        // Avoid any delays during start-up due to decoding of sound files.
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        // Start spatial audio playback of SOUND_FILE at the model postion. The returned
                        //zombieSoundId handle is stored and allows for repositioning the sound object whenever
                        // the position changes.
                        gvrAudioEngine.preloadSoundFile(ZOMBIE_SOUND_FILE);
                        zombieSoundId = gvrAudioEngine.createSoundObject(ZOMBIE_SOUND_FILE);

                        gvrAudioEngine.setSoundObjectPosition(
                                zombieSoundId, modelPosition[0], modelPosition[1], modelPosition[2]);
                        gvrAudioEngine.playSound(zombieSoundId, true /* looped playback */);
                    }
                })
                .start();

        updateModelPosition(gvrAudioEngine);
    }

    public boolean onNewFrame(final GvrAudioEngine gvrAudioEngine) {
        Matrix.rotateM(modelCube, 0, ROTATION_SPEED, 0.5f, 0.5f, 1.0f);

        double angleXZ = Math.atan2(modelPosition[2], modelPosition[0]);
        double distance = Math.sqrt(Math.pow(modelPosition[0], 2) + Math.pow(modelPosition[2], 2));

        double newDistance = distance - speed;

        newDistance = Math.max(2, newDistance);
        if(newDistance < 2) {
            gvrAudioEngine.stopSound(zombieSoundId);
            return true;
        }
        else {
            modelPosition[0] = (float) (Math.cos(angleXZ) * newDistance);
            modelPosition[2] = (float) (Math.sin(angleXZ) * newDistance);

            updateModelPosition(gvrAudioEngine);

            return false;
        }
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

    /**
     * Updates the cube model position.
     */
    public void updateModelPosition(GvrAudioEngine gvrAudioEngine) {
        Matrix.setIdentityM(modelCube, 0);
        Matrix.translateM(modelCube, 0, modelPosition[0], modelPosition[1], modelPosition[2]);

        // Update the sound location to match it with the new cube position.
        if (zombieSoundId != GvrAudioEngine.INVALID_ID) {
            gvrAudioEngine.setSoundObjectPosition(
                    zombieSoundId, modelPosition[0], modelPosition[1], modelPosition[2]);
        }

        DarknessActivity.checkGLError("updateCubePosition");
    }

    public void kill(final GvrAudioEngine gvrAudioEngine) {
        gvrAudioEngine.stopSound(zombieSoundId);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        // Start spatial audio playback of SOUND_FILE at the model postion. The returned
                        //zombieSoundId handle is stored and allows for repositioning the sound object whenever
                        // the cube position changes.
                        gvrAudioEngine.preloadSoundFile(ZOMBIE_DEATH_SOUND_FILE);
                        int deathSound = gvrAudioEngine.createSoundObject(ZOMBIE_DEATH_SOUND_FILE);

                        gvrAudioEngine.setSoundObjectPosition(
                                deathSound, modelPosition[0], modelPosition[1], modelPosition[2]);
                        gvrAudioEngine.playSound(deathSound, false);
                    }
                })
                .start();
    }
}
