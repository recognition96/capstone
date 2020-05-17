package com.otaliastudios.cameraview.engine;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.otaliastudios.cameraview.BaseTest;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.TestActivity;
import com.otaliastudios.cameraview.controls.Engine;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Hdr;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.controls.WhiteBalance;
import com.otaliastudios.cameraview.engine.orchestrator.CameraState;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.internal.WorkerHandler;
import com.otaliastudios.cameraview.overlay.Overlay;
import com.otaliastudios.cameraview.size.Size;
import com.otaliastudios.cameraview.tools.Op;
import com.otaliastudios.cameraview.tools.Retry;
import com.otaliastudios.cameraview.tools.RetryRule;
import com.otaliastudios.cameraview.tools.SdkExclude;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class CameraIntegrationTest<E extends CameraBaseEngine> extends BaseTest {

    private final static CameraLogger LOG = CameraLogger.create(CameraIntegrationTest.class.getSimpleName());
    private final static long DELAY = 8000;

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);

    @Rule
    public RetryRule retryRule = new RetryRule(3);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    );

    protected CameraView camera;
    protected E controller;
    private CameraListener listener;
    private Op<Throwable> error;

    @NonNull
    protected abstract Engine getEngine();

    @Before
    public void setUp() {
        LOG.w("[TEST STARTED]", "Setting up camera.");
        WorkerHandler.destroyAll();

        uiSync(new Runnable() {
            @Override
            public void run() {
                camera = new CameraView(activityRule.getActivity()) {
                    @NonNull
                    @Override
                    protected CameraEngine instantiateCameraEngine(
                            @NonNull Engine engine,
                            @NonNull CameraEngine.Callback callback) {
                        //noinspection unchecked
                        controller = (E) super.instantiateCameraEngine(getEngine(), callback);
                        return controller;
                    }
                };
                camera.setExperimental(true);
                camera.setEngine(getEngine());
                activityRule.getActivity().inflate(camera);
            }
        });

        listener = mock(CameraListener.class);
        camera.addCameraListener(listener);

        error = new Op<>();
        WorkerHandler crashThread = WorkerHandler.get("CrashThread");
        crashThread.getThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable exception) {
                error.controller().end(exception);
            }
        });
        controller.mCrashHandler = crashThread.getHandler();

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onCameraError(@NonNull CameraException exception) {
                super.onCameraError(exception);
                if (exception.isUnrecoverable()) {
                    LOG.e("[UNRECOVERABLE CAMERAEXCEPTION]",
                            "Got unrecoverable exception, not clear what to do in a test.");
                }
            }
        });

    }

    @After
    public void tearDown() {
        LOG.w("[TEST ENDED]", "Tearing down camera.");
        camera.destroy();
        WorkerHandler.destroyAll();
        LOG.w("[TEST ENDED]", "Torn down camera.");
    }

    protected final CameraOptions openSync(boolean expectSuccess) {
        final Op<CameraOptions> open = new Op<>();
        doEndOp(open, 0).when(listener).onCameraOpened(any(CameraOptions.class));
        camera.open();
        CameraOptions result = open.await(DELAY);
        if (expectSuccess) {
            LOG.i("[OPEN SYNC]", "Expecting success.");
            assertNotNull("Can open", result);
            onOpenSync();
        } else {
            LOG.i("[OPEN SYNC]", "Expecting failure.");
            assertNull("Should not open", result);
        }
        return result;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    protected void onOpenSync() {
        // Extra wait for the bind and preview state, so we run tests in a fully operational
        // state. If we didn't do so, we could have null values, for example, in getPictureSize
        // or in getSnapshotSize.
        while (controller.getState() != CameraState.PREVIEW) {
        }
    }

    protected final void closeSync(boolean expectSuccess) {
        final Op<Boolean> close = new Op<>();
        doEndOp(close, true).when(listener).onCameraClosed();
        camera.close();
        Boolean result = close.await(DELAY);
        if (expectSuccess) {
            LOG.i("[CLOSE SYNC]", "Expecting success.");
            assertNotNull("Can close", result);
        } else {
            LOG.i("[CLOSE SYNC]", "Expecting failure.");
            assertNull("Should not close", result);
        }
    }


    @Nullable
    private PictureResult waitForPictureResult(boolean expectSuccess) {
        final Op<PictureResult> pic = new Op<>();
        doEndOp(pic, 0).when(listener).onPictureTaken(any(PictureResult.class));
        doEndOp(pic, null).when(listener).onCameraError(argThat(new ArgumentMatcher<CameraException>() {
            @Override
            public boolean matches(CameraException argument) {
                return argument.getReason() == CameraException.REASON_PICTURE_FAILED;
            }
        }));
        PictureResult result = pic.await(DELAY);
        if (expectSuccess) {
            LOG.i("[WAIT PICTURE]", "Expecting success.");
            assertNotNull("Can take picture", result);
        } else {
            LOG.i("[WAIT PICTURE]", "Expecting failure.");
            assertNull("Should not take picture", result);
        }
        return result;
    }


    private void waitForError() throws Throwable {
        Throwable throwable = error.await(DELAY);
        if (throwable != null) {
            throw throwable;
        }
    }

    //region test open/close

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testOpenClose() {
        assertEquals(CameraState.OFF, controller.getState());
        openSync(true);
        assertTrue(controller.getState().isAtLeast(CameraState.ENGINE));
        closeSync(true);
        assertEquals(CameraState.OFF, controller.getState());
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testOpenTwice() {
        openSync(true);
        openSync(false);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testCloseTwice() {
        closeSync(false);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    // This works great on the device but crashes often on the emulator.
    // There must be something wrong with the emulated camera...
    // Like stopPreview() and release() are not really sync calls?
    public void testConcurrentCalls() throws Exception {
        final CountDownLatch latch = new CountDownLatch(4);
        doCountDown(latch).when(listener).onCameraOpened(any(CameraOptions.class));
        doCountDown(latch).when(listener).onCameraClosed();

        camera.open();
        camera.close();
        camera.open();
        camera.close();

        boolean did = latch.await(10, TimeUnit.SECONDS);
        assertTrue("Handles concurrent calls to start & stop, " + latch.getCount(), did);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testStartInitializesOptions() {
        assertNull(camera.getCameraOptions());
        openSync(true);
        assertNotNull(camera.getCameraOptions());
    }

    //endregion

    //region test Facing/SessionType
    // Test things that should reset the camera.

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testSetFacing() throws Exception {
        CameraOptions o = openSync(true);
        int size = o.getSupportedFacing().size();
        if (size > 1) {
            // set facing should call stop and start again.
            final CountDownLatch latch = new CountDownLatch(2);
            doCountDown(latch).when(listener).onCameraOpened(any(CameraOptions.class));
            doCountDown(latch).when(listener).onCameraClosed();

            camera.toggleFacing();

            boolean did = latch.await(2, TimeUnit.SECONDS);
            assertTrue("Handles setFacing while active", did);
        }
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testSetMode() throws Exception {
        camera.setMode(Mode.PICTURE);
        openSync(true);

        // set session type should call stop and start again.
        final CountDownLatch latch = new CountDownLatch(2);
        doCountDown(latch).when(listener).onCameraOpened(any(CameraOptions.class));
        doCountDown(latch).when(listener).onCameraClosed();

        boolean did = latch.await(2, TimeUnit.SECONDS);
        assertTrue("Handles setMode while active", did);
    }

    //endregion

    //region test Set Parameters
    // When camera is open, parameters will be set only if supported.

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testSetZoom() {
        CameraOptions options = openSync(true);
        float oldValue = camera.getZoom();
        float newValue = 0.65f;
        camera.setZoom(newValue);
        Op<Void> op = new Op<>(controller.mZoomTask);
        op.await(500);

        if (options.isZoomSupported()) {
            assertEquals(newValue, camera.getZoom(), 0f);
        } else {
            assertEquals(oldValue, camera.getZoom(), 0f);
        }
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testSetExposureCorrection() {
        CameraOptions options = openSync(true);
        float oldValue = camera.getExposureCorrection();
        float newValue = options.getExposureCorrectionMaxValue();
        camera.setExposureCorrection(newValue);
        Op<Void> op = new Op<>(controller.mExposureCorrectionTask);
        op.await(300);

        if (options.isExposureCorrectionSupported()) {
            assertEquals(newValue, camera.getExposureCorrection(), 0f);
        } else {
            assertEquals(oldValue, camera.getExposureCorrection(), 0f);
        }
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testSetFlash() {
        CameraOptions options = openSync(true);
        Flash[] values = Flash.values();
        Flash oldValue = camera.getFlash();
        for (Flash value : values) {

            camera.setFlash(value);
            Op<Void> op = new Op<>(controller.mFlashTask);
            op.await(300);
            if (options.supports(value)) {
                assertEquals(camera.getFlash(), value);
                oldValue = value;
            } else {
                assertEquals(camera.getFlash(), oldValue);
            }
        }
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testSetWhiteBalance() {
        CameraOptions options = openSync(true);
        WhiteBalance[] values = WhiteBalance.values();
        WhiteBalance oldValue = camera.getWhiteBalance();
        for (WhiteBalance value : values) {
            camera.setWhiteBalance(value);
            Op<Void> op = new Op<>(controller.mWhiteBalanceTask);
            op.await(300);
            if (options.supports(value)) {
                assertEquals(camera.getWhiteBalance(), value);
                oldValue = value;
            } else {
                assertEquals(camera.getWhiteBalance(), oldValue);
            }
        }
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testSetHdr() {
        CameraOptions options = openSync(true);
        Hdr[] values = Hdr.values();
        Hdr oldValue = camera.getHdr();
        for (Hdr value : values) {
            camera.setHdr(value);
            Op<Void> op = new Op<>(controller.mHdrTask);
            op.await(300);
            if (options.supports(value)) {
                assertEquals(camera.getHdr(), value);
                oldValue = value;
            } else {
                assertEquals(camera.getHdr(), oldValue);
            }
        }
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testSetLocation() {
        openSync(true);
        camera.setLocation(10d, 2d);
        Op<Void> op = new Op<>(controller.mLocationTask);
        op.await(300);
        assertNotNull(camera.getLocation());
        assertEquals(camera.getLocation().getLatitude(), 10d, 0d);
        assertEquals(camera.getLocation().getLongitude(), 2d, 0d);
        // This also ensures there are no crashes when attaching it to camera parameters.
    }

    //endregion


    //region startAutoFocus

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testStartAutoFocus() {
        CameraOptions o = openSync(true);

        final Op<PointF> focus = new Op<>();
        doEndOp(focus, 0).when(listener).onAutoFocusStart(any(PointF.class));

        camera.startAutoFocus(1, 1);
        PointF point = focus.await(300);
        if (o.isAutoFocusSupported()) {
            assertNotNull(point);
            assertEquals(point, new PointF(1, 1));
        } else {
            assertNull(point);
        }
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testStopAutoFocus() {
        CameraOptions o = openSync(true);

        final Op<PointF> focus = new Op<>();
        doEndOp(focus, 1).when(listener).onAutoFocusEnd(anyBoolean(), any(PointF.class));

        camera.startAutoFocus(1, 1);
        // Stop routine can fail, so engines use a timeout. So wait at least the timeout time.
        PointF point = focus.await(1000 + getMeteringTimeoutMillis());
        if (o.isAutoFocusSupported()) {
            assertNotNull(point);
            assertEquals(point, new PointF(1, 1));
        } else {
            assertNull(point);
        }
    }

    protected abstract long getMeteringTimeoutMillis();

    //endregion

    //region capture

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testCaptureSnapshot_beforeStarted() {
        camera.takePictureSnapshot();
        waitForPictureResult(false);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testCaptureSnapshot_concurrentCalls() throws Exception {
        // Second take should fail.
        openSync(true);

        CountDownLatch latch = new CountDownLatch(2);
        doCountDown(latch).when(listener).onPictureTaken(any(PictureResult.class));

        camera.takePictureSnapshot();
        camera.takePictureSnapshot();
        boolean did = latch.await(6, TimeUnit.SECONDS);
        assertFalse(did);
        assertEquals(1, latch.getCount());
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testCaptureSnapshot_size() {
        openSync(true);
        Size size = camera.getSnapshotSize();
        assertNotNull(size);
        camera.takePictureSnapshot();

        PictureResult result = waitForPictureResult(true);
        assertNotNull(result);
        assertNotNull(result.getData());
        assertNull(result.getLocation());
        assertTrue(result.isSnapshot());
        assertEquals(result.getSize(), size);
        Bitmap bitmap = CameraUtils.decodeBitmap(result.getData(),
                Integer.MAX_VALUE, Integer.MAX_VALUE);
        if (bitmap != null) {
            String message = LOG.i("[PICTURE SIZE]", "Desired:", size, "Bitmap:",
                    new Size(bitmap.getWidth(), bitmap.getHeight()));
            assertNotNull(bitmap);
            assertEquals(message, bitmap.getWidth(), size.getWidth());
            assertEquals(message, bitmap.getHeight(), size.getHeight());
        }
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testCaptureSnapshot_withMetering() {
        openSync(true);
        camera.setPictureSnapshotMetering(true);
        camera.takePictureSnapshot();
        waitForPictureResult(true);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testCaptureSnapshot_withoutMetering() {
        openSync(true);
        camera.setPictureSnapshotMetering(false);
        camera.takePictureSnapshot();
        waitForPictureResult(true);
    }

    //endregion

    //region Frame Processing

    private void assert15Frames(@NonNull FrameProcessor mock) throws Exception {
        // Expect 15 frames. Time is very high because currently Camera2 keeps a very low FPS.
        CountDownLatch latch = new CountDownLatch(15);
        doCountDown(latch).when(mock).process(any(Frame.class));
        boolean did = latch.await(30, TimeUnit.SECONDS);
        assertTrue("Latch count should be 0: " + latch.getCount(), did);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testFrameProcessing_simple() throws Exception {
        FrameProcessor processor = mock(FrameProcessor.class);
        camera.addFrameProcessor(processor);
        openSync(true);

        assert15Frames(processor);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testFrameProcessing_maxSize() {
        final int max = 600;
        camera.setFrameProcessingMaxWidth(max);
        camera.setFrameProcessingMaxHeight(max);
        final Op<Size> sizeOp = new Op<>();
        camera.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                sizeOp.controller().end(frame.getSize());
            }
        });
        openSync(true);
        Size size = sizeOp.await(2000);
        assertNotNull(size);
        assertTrue(size.getWidth() <= max);
        assertTrue(size.getHeight() <= max);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testFrameProcessing_afterSnapshot() throws Exception {
        FrameProcessor processor = mock(FrameProcessor.class);
        camera.addFrameProcessor(processor);
        openSync(true);

        // In Camera1, snapshots will clear the preview callback
        // Ensure we restore correctly
        camera.takePictureSnapshot();
        waitForPictureResult(true);

        assert15Frames(processor);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testFrameProcessing_afterRestart() throws Exception {
        FrameProcessor processor = mock(FrameProcessor.class);
        camera.addFrameProcessor(processor);
        openSync(true);
        closeSync(true);
        openSync(true);

        assert15Frames(processor);
    }


    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testFrameProcessing_freezeRelease() throws Exception {
        // Ensure that freeze/release cycles do not cause OOMs.
        // There was a bug doing this and it might resurface for any improper
        // disposal of the frames.
        FrameProcessor source = new FreezeReleaseFrameProcessor();
        FrameProcessor processor = spy(source);
        camera.addFrameProcessor(processor);
        openSync(true);

        assert15Frames(processor);
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testFrameProcessing_format() {
        // We wouldn't need to open/close for each format, but we do because legacy devices can
        // crash due to their bad internal implementation when we perform restartBind().
        // And setFrameProcessorFormat can trigger such restart.
        CameraOptions o = openSync(true);
        Collection<Integer> formats = o.getSupportedFrameProcessingFormats();
        closeSync(true);
        for (int format : formats) {
            LOG.i("[TEST FRAME FORMAT]", "Testing", format, "...");
            Op<Boolean> op = testFrameProcessorFormat(format);
            assertNotNull(op.await(DELAY));
            closeSync(true);
        }
    }

    @NonNull
    private Op<Boolean> testFrameProcessorFormat(final int format) {
        final Op<Boolean> op = new Op<>();
        camera.setFrameProcessingFormat(format);
        camera.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                if (frame.getFormat() == format) {
                    op.controller().start();
                    op.controller().end(true);
                }
            }
        });
        openSync(true);
        return op;
    }

    @Test
    @Retry(emulatorOnly = true)
    @SdkExclude(maxSdkVersion = 22, emulatorOnly = true)
    public void testOverlay_forPictureSnapshot() {
        Overlay overlay = mock(Overlay.class);
        when(overlay.drawsOn(any(Overlay.Target.class))).thenReturn(true);
        controller.setOverlay(overlay);
        openSync(true);
        camera.takePictureSnapshot();
        waitForPictureResult(true);
        verify(overlay, atLeastOnce()).drawsOn(Overlay.Target.PICTURE_SNAPSHOT);
        verify(overlay, times(1)).drawOn(eq(Overlay.Target.PICTURE_SNAPSHOT), any(Canvas.class));
    }

    //endregion

    //region Overlays

    public class FreezeReleaseFrameProcessor implements FrameProcessor {
        @Override
        public void process(@NonNull Frame frame) {
            frame.freeze().release();
        }
    }


    //endregion
}
