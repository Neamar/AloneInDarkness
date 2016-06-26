package fr.neamar.aloneindarkness.entity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;

import fr.neamar.aloneindarkness.layoutdata.ZombieLayoutData;


public class ZombieLoader {
    public FloatBuffer cubeVertices;
    public FloatBuffer cubeColors;
    public FloatBuffer cubeFoundColors;
    public FloatBuffer cubeNormals;

    public ZombieLoader() {

    }

    public void onSurfaceCreated(EGLConfig config) {
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
    }
}