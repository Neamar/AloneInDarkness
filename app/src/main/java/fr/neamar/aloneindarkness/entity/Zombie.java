package fr.neamar.aloneindarkness.entity;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.google.vr.sdk.audio.GvrAudioEngine;

import java.util.Random;

import fr.neamar.aloneindarkness.DarknessActivity;


public class Zombie {
    public static String TAG = "Zombie";
    public static final String[] ZOMBIE_BREATHING_SOUND_FILES = new String[] {
            "zombies/breathing_1.wav",
            "zombies/breathing_2.wav",
            "zombies/breathing_3.wav",
            "zombies/breathing_4.wav"
    };
    public static final String[] ZOMBIE_WALKING_SOUND_FILES = new String[] {
            "zombies/walking_1.wav"
    };
    public static final String[] ZOMBIE_DEATH_SOUND_FILES = new String[] {
            "zombies/death_1.wav",
            "zombies/death_2.wav",
            "zombies/death_3.wav",
            "zombies/death_4.wav",
            "zombies/death_5.wav",
            "zombies/death_6.wav",
            "zombies/death_7.wav",
            "zombies/death_8.wav",
            "zombies/death_9.wav",
            "zombies/death_10.wav"
    };
    public static final float ROTATION_SPEED = 0.3f;


    public float[] modelCube;
    public float[] modelPosition;
    public float speed;

    private float minZombieBreathingVolume = 0.3f;
    private float maxZombieBreathingVolume = 0.9f;

    private int breathingSoundId = GvrAudioEngine.INVALID_ID;
    private int footstepSoundId = GvrAudioEngine.INVALID_ID;

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
                        preloadFiles(gvrAudioEngine, ZOMBIE_BREATHING_SOUND_FILES);
                        preloadFiles(gvrAudioEngine, ZOMBIE_WALKING_SOUND_FILES);
                        preloadFiles(gvrAudioEngine, ZOMBIE_DEATH_SOUND_FILES);

                        int totalFilesLoaded = ZOMBIE_BREATHING_SOUND_FILES.length + 1
                                + ZOMBIE_BREATHING_SOUND_FILES.length + 1
                                +  ZOMBIE_DEATH_SOUND_FILES.length + 1;

                        Log.i("BreathingSounds", String.format("All sounds initialized, %s sounds loaded", totalFilesLoaded));
                    }
                })
                .start();

        updateModelPosition(gvrAudioEngine);
    }

    private void preloadFiles(final GvrAudioEngine gvrAudioEngine, String[] fileList) {
        for (String soundFile: fileList) {
            Log.i("BreathingSounds", String.format("Loading sound file, %s", soundFile));
            gvrAudioEngine.preloadSoundFile(soundFile);
        }
        Log.i("ZombieSounds", String.format("Preloading sounds, %s sounds loaded", fileList.length+1));

    }

    // Update the sound position from the model.
    private void updateSoundPosition(final GvrAudioEngine gvrAudioEngine, int[] soundIds, float[] modelPosition) {
        for(int soundId: soundIds){
            gvrAudioEngine.setSoundObjectPosition(
                    soundId, modelPosition[0], modelPosition[1], modelPosition[2]);
        }
    }

    public void updateZombieSounds(final GvrAudioEngine gvrAudioEngine) {
        Random soundGenerator = new Random();

        // Set the initial position
        updateSoundPosition(gvrAudioEngine, new int[] {breathingSoundId, footstepSoundId}, modelPosition);

        if (!gvrAudioEngine.isSoundPlaying(footstepSoundId)) {

            int soundIndex = soundGenerator.nextInt(ZOMBIE_WALKING_SOUND_FILES.length);
            String newSoundFile = ZOMBIE_WALKING_SOUND_FILES[soundIndex];
            int newSoundId = gvrAudioEngine.createSoundObject(newSoundFile);

            if (newSoundId != GvrAudioEngine.INVALID_ID) {
                // update the current sound to ensure we can follow the zombie model
                footstepSoundId = newSoundId;
                gvrAudioEngine.playSound(footstepSoundId, true);

            }
        }

        // Update zombie breathing sound
        if (!gvrAudioEngine.isSoundPlaying(breathingSoundId)) {

            int soundIndex = soundGenerator.nextInt(ZOMBIE_BREATHING_SOUND_FILES.length);
            String newSoundFile = ZOMBIE_BREATHING_SOUND_FILES[soundIndex];

            int newZombieSoundId = gvrAudioEngine.createSoundObject(newSoundFile);

            if (newZombieSoundId != GvrAudioEngine.INVALID_ID) {
                // update the current sound to ensure we can follow the zombie model
                breathingSoundId = newZombieSoundId;
                gvrAudioEngine.playSound(breathingSoundId, false);
            }
        }

    }

    public boolean onNewFrame(final GvrAudioEngine gvrAudioEngine) {
        Matrix.rotateM(modelCube, 0, ROTATION_SPEED, 0.5f, 0.5f, 1.0f);

        double angleXZ = Math.atan2(modelPosition[2], modelPosition[0]);
        double distance = Math.sqrt(Math.pow(modelPosition[0], 2) + Math.pow(modelPosition[2], 2));

        double newDistance = distance - speed;

//        newDistance = Math.max(2, newDistance);
        if(newDistance < 2) {
            gvrAudioEngine.stopSound(breathingSoundId);
            gvrAudioEngine.stopSound(footstepSoundId);
            return true;
        }
        else {
            modelPosition[0] = (float) (Math.cos(angleXZ) * newDistance);
            modelPosition[2] = (float) (Math.sin(angleXZ) * newDistance);


            updateModelPosition(gvrAudioEngine);
            updateZombieSounds(gvrAudioEngine); // Zombie noises

            // Player still not dead, return false
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
        if (breathingSoundId != GvrAudioEngine.INVALID_ID) {
            updateSoundPosition(gvrAudioEngine, new int[] {breathingSoundId, footstepSoundId}, modelPosition);
        }

        DarknessActivity.checkGLError("updateCubePosition");
    }

    public void kill(final GvrAudioEngine gvrAudioEngine) {
        gvrAudioEngine.stopSound(breathingSoundId);
        gvrAudioEngine.stopSound(footstepSoundId);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        // Start spatial audio playback of SOUND_FILE at the model postion. The returned
                        //zombieSoundId handle is stored and allows for repositioning the sound object whenever
                        // the cube position changes.
                        Random randGenerator = new Random();
                        int idx = randGenerator.nextInt(ZOMBIE_DEATH_SOUND_FILES.length);
                        int deathSound = gvrAudioEngine.createSoundObject(ZOMBIE_DEATH_SOUND_FILES[idx]);

                        gvrAudioEngine.setSoundObjectPosition(
                                deathSound, modelPosition[0], modelPosition[1], modelPosition[2]);
                        gvrAudioEngine.playSound(deathSound, false);
                    }
                })
                .start();
    }
}
