package fr.neamar.aloneindarkness.entity;

import com.google.vr.sdk.audio.GvrAudioEngine;

public class WaterDrops {
    private final GvrAudioEngine gvrAudioEngine;
    private final float maxModelDistance;

    private static final int MAX_SECONDS_TO_NEXT_WATER_DROP = 7;
    private static final String[] WATER_DROP_SOUND_FILES = new String[]{
            "water/water_drops_1.wav",
            "water/water_drops_2.wav",
            "water/water_drops_3.wav",
            "water/water_drops_4.wav"
    };

    private int countToNextWaterDrop;
    private int countSinceLastWaterDrop;

    public WaterDrops(GvrAudioEngine gvrAudioEngine, float maxModelDistance) {
        this.gvrAudioEngine = gvrAudioEngine;
        this.maxModelDistance = maxModelDistance;
        resetWaterDropCounters();
    }

    public void advanceWaterDropCounters() {
        if (countSinceLastWaterDrop >= countToNextWaterDrop) {
            resetWaterDropCounters();
            playRandomWaterDropSound();
        }
        ++countSinceLastWaterDrop;
    }

    private void resetWaterDropCounters() {
        countToNextWaterDrop = getRandomCountToNextWaterDrop();
        countSinceLastWaterDrop = 0;
    }

    private int getRandomCountToNextWaterDrop() {
        return (int) (Math.random() * MAX_SECONDS_TO_NEXT_WATER_DROP * 60);
    }

    private void playRandomWaterDropSound() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String waterDropFile = getRandomWaterDropFile();
                        gvrAudioEngine.preloadSoundFile(waterDropFile);
                        int waterDropSound = gvrAudioEngine.createSoundObject(waterDropFile);

                        float xPosition = getRandomPositionWithinModel();
                        float zPosition = getRandomPositionWithinModel();
                        gvrAudioEngine.setSoundObjectPosition(waterDropSound, xPosition, 0, zPosition);

                        gvrAudioEngine.playSound(waterDropSound, false);
                    }
                })
                .start();
    }

    private String getRandomWaterDropFile() {
        int waterDropFileIndex = (int) Math.floor(Math.random() * WATER_DROP_SOUND_FILES.length);
        return WATER_DROP_SOUND_FILES[waterDropFileIndex];
    }

    private float getRandomPositionWithinModel() {
        return (float) (Math.random() * maxModelDistance);
    }
}
