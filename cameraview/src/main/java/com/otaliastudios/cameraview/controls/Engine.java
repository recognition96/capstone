package com.otaliastudios.cameraview.controls;


import androidx.annotation.NonNull;

import com.otaliastudios.cameraview.CameraView;

/**
 * The engine to be used.
 *
 * @see CameraView#setEngine(Engine)
 */
public enum Engine implements Control {
    /**
     * Camera2 based engine. For API versions older than 21,
     * the system falls back to {@link #CAMERA1}.
     */
    CAMERA2(0);

    final static Engine DEFAULT = CAMERA2;

    private int value;

    Engine(int value) {
        this.value = value;
    }

    @NonNull
    static Engine fromValue(int value) {
        Engine[] list = Engine.values();
        for (Engine action : list) {
            if (action.value() == value) {
                return action;
            }
        }
        return DEFAULT;
    }

    int value() {
        return value;
    }
}
