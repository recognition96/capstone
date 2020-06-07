package com.example.inhacsecapstone.cameras;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.Drugs;
import com.example.inhacsecapstone.drugs.Recog.RecogResultActivity;
import com.example.inhacsecapstone.serverconnect.HttpConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.PictureResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PicturePreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static PictureResult picture;
    Bitmap mbitmap;
    private HttpConnection httpConn = HttpConnection.getInstance();

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mbitmap.recycle();
        mbitmap = null;
        if (!isChangingConfigurations()) {
            setPictureResult(null);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.send_button) {
            // 결과코드를 'RESULT_OK'로 세팅.
            // 이후 이전 액티비티로 돌아가 onActivityResult()가 호출됨.
            Log.d("TAG", "Choose this photo.. transfer to server");
            CameraActivity camera = (CameraActivity) CameraActivity.camera_activity;
            camera.finish();
            sendImageToServer(mbitmap);
            finish();
        } else if (id == R.id.cancel_button) {
            // 결과코드를 'RESULT_CANCELED'로 세팅.
            // 이후 이전 액티비티로 돌아가 onActivityResult()가 호출됨.
            Log.d("TAG", "Discard this photo.. back to camera to retry");
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    public void sendImageToServer(Bitmap bitmap) {
        String postUrl = httpConn.getUrl("image");
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

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull android.os.Message msg) throws JsonIOException, JsonSyntaxException {
            String notiTitle = null;
            String notiText = null;
            Intent intent = new Intent(PicturePreviewActivity.this, RecogResultActivity.class);

            if (msg.what == 1) {
                String OCR_Result = (String) msg.obj;
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                try {
                    JsonElement rootObject = parser.parse(OCR_Result)
                            .getAsJsonObject().get("drugs");
                    Drugs[] drugs = gson.fromJson(rootObject, Drugs[].class);
                    for (int i = 0; i < drugs.length; i++) {
                        System.out.println(drugs[i].printres());
                    }
                    intent.putExtra("drugs", drugs);
                    notiTitle = "처방전 결과가 도착했습니다!";
                    notiText = "알림을 눌러 결과를 확인하실 수 있습니다.";
                }
                catch(Exception e) {
                    e.printStackTrace();
                    notiTitle = "처방전 인식을 실패했습니다.";
                    notiText = "다시 시도해주세요.";
                }
            }
            else {
                notiTitle = "서버 연결에 실패했습니다.";
                notiText = "다시 시도해주세요.";
            }

            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationManager nm = getApplicationContext().getSystemService(NotificationManager.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "채널";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("result", channelName, importance);
                nm.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "result");
            builder.setSmallIcon(R.drawable.ic_alarm_add_black_48dp)
                    .setContentTitle(notiTitle)
                    .setContentText(notiText)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);
            if (intent.getSerializableExtra("drugs") == null) Log.d("@@@", "already null");

            PowerManager.WakeLock sCpuWakeLock = null;
            if (sCpuWakeLock != null) { return false; }
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            sCpuWakeLock = pm.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.ON_AFTER_RELEASE, "Tag: for PowerManager");
            sCpuWakeLock.acquire(60 * 1000L /*1 minutes*/);
            if (sCpuWakeLock != null) {
                sCpuWakeLock.release();
                sCpuWakeLock = null;
            }

            if (msg.what == 1) {
                nm.notify(-1, builder.build());
                return true;
            } else {
                nm.notify(-1, builder.build());
                return false;
            }
        }
    });

    void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.MINUTES)
                .build();
        okhttp3.Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("@@@", "FAIL");
                call.cancel();
                android.os.Message message = android.os.Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Thread(() -> {
                    Log.d("@@@", "in Thread");
                    if (!response.isSuccessful() || response.body() == null) {
                        setResult(Activity.RESULT_CANCELED);
                    }
                    String OCR_Result = "";

                    try {
                        OCR_Result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        android.os.Message message = android.os.Message.obtain();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                    android.os.Message message = android.os.Message.obtain();
                    message.obj = OCR_Result;
                    message.what = 1;
                    handler.sendMessage(message);
                }).start();
            }
        });
    }
}

