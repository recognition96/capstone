package com.otaliastudios.cameraview.gesture;


import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.otaliastudios.cameraview.tools.SdkExclude;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * On API 26 these tests fail during Espresso's inRoot() - the window never gains focus.
 * This might be due to a system popup or something similar.
 */
@SdkExclude(minSdkVersion = 26, maxSdkVersion = 26)
@RunWith(AndroidJUnit4.class)
@SmallTest
public class PinchGestureFinderTest extends GestureFinderTest<PinchGestureFinder> {

    @Override
    protected PinchGestureFinder createFinder(@NonNull GestureFinder.Controller controller) {
        return new PinchGestureFinder(controller);
    }

    @Test
    public void testDefaults() {
        assertEquals(finder.getGesture(), Gesture.PINCH);
        assertEquals(finder.getPoints().length, 2);
        assertEquals(finder.getPoints()[0].x, 0, 0);
        assertEquals(finder.getPoints()[0].y, 0, 0);
        assertEquals(finder.getPoints()[1].x, 0, 0);
        assertEquals(finder.getPoints()[1].y, 0, 0);
    }

}
