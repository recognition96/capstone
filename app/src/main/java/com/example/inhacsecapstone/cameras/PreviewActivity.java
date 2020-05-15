package com.example.inhacsecapstone.cameras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.Drugs;
import com.example.inhacsecapstone.drugs.Recog.RecogResultActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class   PreviewActivity extends AppCompatActivity {
    private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");
    private static final String portNumber = "5000";
    private static final String ipv4Address = "172.30.1.35";
    private static final int SELECT_PICTURE = 1;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "Preview_Called");
        setContentView(R.layout.activity_preview);
        context = getApplicationContext();
        // intent로 촬영한 img의 uri를 받아옴
        final Uri uri = getIntent().getParcelableExtra("imageUri");

        ImageView img_out = findViewById(R.id.prev_img);
        Button bt_yes = findViewById(R.id.btn_yes);
        Button bt_no = findViewById(R.id.btn_no);

        img_out.setImageURI(uri);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 결과코드를 'RESULT_OK'로 세팅.
                // 이후 이전 액티비티로 돌아가 onActivityResult()가 호출됨.
                setResult(Activity.RESULT_OK);
                connectServer(uri);
                Log.d("TAG", "Choose this photo.. transfer to server");
                finish();
            }
        });

        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 재촬영을 위해 받아온 img 파일 및 uri 삭제
                getContentResolver().delete(uri, null, null);
                Log.d("TAG", "Discard this photo.. back to camera to retry");
                // 결과코드를 'RESULT_CANCELED'로 세팅.
                // 이후 이전 액티비티로 돌아가 onActivityResult()가 호출됨.
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    public void connectServerSendText(String texts){
        Matcher matcher = IP_ADDRESS.matcher(ipv4Address);
        // 전송 텍스트
        String sendText = texts;

        if (!matcher.matches()) {
            Toast.makeText(this, "Invalid IPv4 Address. Please Check Your Inputs.", Toast.LENGTH_LONG).show();
            return;
        }
        String postUrl = "http://" + ipv4Address + ":" + portNumber + "/string";
        RequestBody formBody = new FormBody.Builder()
                .add("message", sendText)
                .build();
        postRequest(postUrl,formBody);

    }

    public void connectServer(Uri uri) {
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

        try {
            // Read BitMap by file path.
//            Bitmap bitmap = BitmapFactory.decodeFile(getPath(this,uri), options);
            Bitmap bitmap = BitmapFactory.decodeFile(getPath(uri), options); // 에러 지점
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }catch(Exception e){
            Toast.makeText(this, "Please Make Sure the Selected File is an Image.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        byte[] byteArray = stream.toByteArray();
        multipartBodyBuilder.addFormDataPart("image" , "Android_Flask_" + ".jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray));

        RequestBody postBodyImage = multipartBodyBuilder.build();
        postRequest(postUrl, postBodyImage);
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                Log.d("FAIL", e.getMessage());

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Failed to Connect to Server. Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!response.isSuccessful() || response.body() == null) {
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }
                        String OCR_Result = "";
                        try {
                            OCR_Result = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Gson gson = new GsonBuilder().create();
                        JsonParser parser = new JsonParser();
                        JsonElement rootObject = parser.parse(OCR_Result)
                                .getAsJsonObject().get("drugs");
                        Drugs[] drugs = gson.fromJson(rootObject, Drugs[].class);
                        for(int i=0; i<drugs.length; i++) {
                            System.out.println(drugs[i].printres());
                        }
                        Intent intent = new Intent(context, RecogResultActivity.class);
                        intent.putExtra("drugs", drugs);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    public String getPath(Uri uri) {
        // uri가 null일경우 null반환
        if( uri == null ) {
            return null;
        }
        // 미디어스토어에서 유저가 선택한 사진의 URI를 받아온다.
        String[] projection = { MediaStore.Images.Media.DATA };
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        }
        // URI경로를 반환한다.
        return uri.getPath();
    }
}
