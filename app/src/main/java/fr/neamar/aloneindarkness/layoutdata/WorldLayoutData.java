/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.neamar.aloneindarkness.layoutdata;

/**
 * Contains vertex, normal and color data.
 */
public final class WorldLayoutData {

    // The grid lines on the floor are rendered procedurally and large polygons cause floating point
    // precision problems on some architectures. So we split the floor into 4 quadrants.
    public static final float[] FLOOR_COORDS = new float[]{
            // +X, +Z quadrant
            200, 0, 0,
            0, 0, 0,
            0, 0, 200,
            200, 0, 0,
            0, 0, 200,
            200, 0, 200,

            // -X, +Z quadrant
            0, 0, 0,
            -200, 0, 0,
            -200, 0, 200,
            0, 0, 0,
            -200, 0, 200,
            0, 0, 200,

            // +X, -Z quadrant
            200, 0, -200,
            0, 0, -200,
            0, 0, 0,
            200, 0, -200,
            0, 0, 0,
            200, 0, 0,

            // -X, -Z quadrant
            0, 0, -200,
            -200, 0, -200,
            -200, 0, 0,
            0, 0, -200,
            -200, 0, 0,
            0, 0, 0,
    };

    public static final float[] FLOOR_NORMALS = new float[]{
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
    };

    public static final float[] FLOOR_COLORS = new float[]{
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
            0.0f, 0f, 0f, 1.0f,
    };
}
