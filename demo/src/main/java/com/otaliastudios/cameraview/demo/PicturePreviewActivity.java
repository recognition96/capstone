package com.otaliastudios.cameraview.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.PictureResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class PicturePreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");
    private static final String portNumber = "5000";
    private static final String ipv4Address = "172.30.1.35";
    private static final int SELECT_PICTURE = 1;
    private static PictureResult picture;
    Bitmap mbitmap;

    public static void setPictureResult(@Nullable PictureResult pictureResult) {
        picture = pictureResult;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바를 안보이도록 합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_picture_preview);

        final Uri uri = getIntent().getParcelableExtra("imageUri");
        final PictureResult result = picture;
        final ImageView imageView = findViewById(R.id.image);
        if (result == null && uri == null) {
            finish();
            return;
        } else if (result == null && uri != null) {
            imageView.setImageURI(uri);
            try {
                mbitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (result != null && uri == null) {
            try {
                result.toBitmap(1000, 1000, new BitmapCallback() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        mbitmap = bitmap;
                    }
                });
            } catch (UnsupportedOperationException e) {
                imageView.setImageDrawable(new ColorDrawable(Color.GREEN));
                Toast.makeText(this, "Can't preview this format: " + picture.getFormat(),
                        Toast.LENGTH_LONG).show();
            }

            // Log the real size for debugging reason.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length, options);

            Log.d("@@@", "The picture full size is " + result.getSize().getHeight() + "x" + result.getSize().getWidth());
            if (result.getRotation() % 180 != 0) {
                Log.e("PicturePreview", "The picture full size is " + result.getSize().getHeight() + "x" + result.getSize().getWidth());
            } else {
                Log.e("PicturePreview", "The picture full size is " + result.getSize().getWidth() + "x" + result.getSize().getHeight());
            }
        } else {
            Log.d("plz", "PictureResult == Null && UriFromGallery == Null");
        }

        findViewById(R.id.send_button).setOnClickListener(this);
        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.send_button2).setOnClickListener(this);
        findViewById(R.id.cancel_button2).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            setPictureResult(null);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.send_button) {
            sendToServer();
        } else if (id == R.id.cancel_button) {
            cancel();
        } else if (id == R.id.send_button2) {
            sendToServer();
        } else if (id == R.id.cancel_button2) {
            cancel();
        }
    }

    public void connectServer(Bitmap bitmap) {
        Matcher matcher = IP_ADDRESS.matcher(ipv4Address);
        if (!matcher.matches()) {
            Toast.makeText(this, "Invalid IPv4 Address. Please Check Your Inputs.", Toast.LENGTH_LONG).show();
            return;
        }

        String postUrl = "http://" + ipv4Address + ":" + portNumber + "/image";

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        multipartBodyBuilder.addFormDataPart("image", "Android_Flask_" + ".jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray));

        RequestBody postBodyImage = multipartBodyBuilder.build();
        postRequest(postUrl, postBodyImage);
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                Log.d("FAIL", e.getMessage());

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Failed to Connect to Server. Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(getApplicationContext(), "Server's Response\n" + response.body().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void sendToServer() {
        connectServer(mbitmap);
    }

    public void cancel() {
//        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}

