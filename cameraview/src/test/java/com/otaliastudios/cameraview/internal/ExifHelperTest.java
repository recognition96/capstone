package com.otaliastudios.cameraview.internal;


import androidx.exifinterface.media.ExifInterface;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExifHelperTest {

    @Test
    public void testValues() {
        assertEquals(0, ExifHelper.getOrientation(ExifInterface.ORIENTATION_NORMAL));
        assertEquals(0, ExifHelper.getOrientation(ExifInterface.ORIENTATION_FLIP_HORIZONTAL));
        assertEquals(180, ExifHelper.getOrientation(ExifInterface.ORIENTATION_ROTATE_180));
        assertEquals(180, ExifHelper.getOrientation(ExifInterface.ORIENTATION_FLIP_VERTICAL));
        assertEquals(90, ExifHelper.getOrientation(ExifInterface.ORIENTATION_ROTATE_90));
        assertEquals(90, ExifHelper.getOrientation(ExifInterface.ORIENTATION_TRANSPOSE));
        assertEquals(270, ExifHelper.getOrientation(ExifInterface.ORIENTATION_ROTATE_270));
        assertEquals(270, ExifHelper.getOrientation(ExifInterface.ORIENTATION_TRANSVERSE));
    }

    @Test
    public void testUnknownValues() {
        assertEquals(0, ExifHelper.getOrientation(-15));
        assertEquals(0, ExifHelper.getOrientation(-1));
        assertEquals(0, ExifHelper.getOrientation(195));
        assertEquals(0, ExifHelper.getOrientation(Integer.MAX_VALUE));
    }

}
