package com.otaliastudios.cameraview.controls;


import androidx.annotation.NonNull;

import com.otaliastudios.cameraview.CameraView;

/**
 * The preview engine to be used.
 *
 * @see CameraView#setPreview(Preview)
 */
public enum Preview implements Control {

    /**
     * Preview engine based on {@link android.opengl.GLSurfaceView}.
     * This is the best engine available. Supports video snapshots,
     * supports picture snapshots while taking videos, supports
     * watermarks and overlays, supports real-time filters.
     */
    GL_SURFACE(0);

    final static Preview DEFAULT = GL_SURFACE;

    private int value;

    Preview(int value) {
        this.value = value;
    }

    int value() {
        return value;
    }

    @NonNull
    static Preview fromValue(int value) {
        Preview[] list = Preview.values();
        for (Preview action : list) {
            if (action.value() == value) {
                return action;
            }
        }
        return DEFAULT;
    }
}
