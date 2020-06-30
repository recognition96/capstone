package com.example.inhacsecapstone.cameras;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inhacsecapstone.R;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private final static CameraLogger LOG = CameraLogger.create("DemoApp");
    private static final int SELECT_IMAGE = 1;
    private static final int SENDING_IMAGE = 2;
    public static Activity camera_activity;
    private CameraView camera;
    private long mCaptureTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camera_activity = CameraActivity.this;
        setContentView(R.layout.activity_camera);
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);
        camera.addCameraListener(new Listener());

        findViewById(R.id.guideline);
        findViewById(R.id.takeFromGallery).setOnClickListener(this);
        findViewById(R.id.takePicture).setOnClickListener(this);
        findViewById(R.id.cancel_button).setOnClickListener(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Camera result", "BACK TO Camera");
        if (requestCode == SELECT_IMAGE) {
            // 앨범에서 성공적으로 사진을 가져왔을 경우
            Log.d("Camera result", "cause IMAGE Chosen..");
            if (data != null) {
                sendImageToPrev(data.getData());
            }
            else {
                Log.d("@@@", "data.getData() == NULL");
            }
        } else if (requestCode == RESULT_OK) {
            // 찍거나 고른 사진을 성공적으로 PicturePreviewActivity로 보냈을 경우
            setResult(Activity.RESULT_CANCELED);
            Log.d("Camera result", "cause IMAGE Sended..");
        } else if (resultCode == RESULT_CANCELED) {
            // PicturePreviewActivity에서 해당 사진을 선택하지 않았거나 request에 실패했을 경우
            Log.d("Camera result", "cause IMAGE Discarded or Failed to request..");
        }

    }

    private void message(@NonNull String content, boolean important) {
        if (important) {
            LOG.w(content);
            Toast.makeText(this, content, Toast.LENGTH_LONG).show();
        } else {
            LOG.i(content);
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.takeFromGallery:
                // 앨범에서 불러왔을 경우 앨범에서 intent로 받아오고 다시 PicturePreviewActivity로 intent 전송
                // onActivityResult -> sendImageToPrev
                takePhotoFromGallery();
                break;
            case R.id.takePicture:
                // 직접 촬영했을 경우 onPictureTaken에서 결과받고 PicturePreviewActivity로 intent 전송
                capturePictureSnapshot();
                break;
            case R.id.cancel_button:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    public void takePhotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Toast.makeText(this, "처방전사진을 선택하세요.", Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, SELECT_IMAGE);
        Log.d("TAG", "IMAGE SELECTING..");
    }

    public void sendImageToPrev(Uri uri) {
        Intent intent = new Intent(CameraActivity.this, PicturePreviewActivity.class);
        intent.putExtra("imageUri", uri);
        Log.d("TAG", "Success taking a photo.. Transfer this to PreviewActivity");
        startActivityForResult(intent, SENDING_IMAGE);
    }

    public void capturePictureSnapshot() {
        if (camera.isTakingPicture()) return;
        mCaptureTime = System.currentTimeMillis();
        camera.setPlaySounds(false);
        camera.takePicture();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !camera.isOpened()) {
            camera.open();
        }
    }

    // Listener
    private class Listener extends CameraListener {
        @Override
        public void onCameraError(@NonNull CameraException exception) {
            super.onCameraError(exception);
            message("Got CameraException #" + exception.getReason(), true);
        }

        @Override
        public void onPictureTaken(@NonNull PictureResult result) {
            super.onPictureTaken(result);

            // This can happen if picture was taken with a gesture.
            long callbackTime = System.currentTimeMillis();
            if (mCaptureTime == 0) mCaptureTime = callbackTime - 300;
            LOG.w("onPictureTaken called! Launching activity. Delay:", callbackTime - mCaptureTime);
            PicturePreviewActivity.setPictureResult(result);
            Intent intent = new Intent(CameraActivity.this, PicturePreviewActivity.class);
            intent.putExtra("delay", callbackTime - mCaptureTime);
            startActivityForResult(intent, SENDING_IMAGE);
            mCaptureTime = 0;
            LOG.w("onPictureTaken called! Launched activity.");
        }


        @Override
        public void onExposureCorrectionChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers);
            message("Exposure correction:" + newValue, false);
        }

        @Override
        public void onZoomChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onZoomChanged(newValue, bounds, fingers);
            message("Zoom:" + newValue, false);
        }
    }
}
