package com.example.inhacsecapstone;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PreviewActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "Preview_Called");
        setContentView(R.layout.activity_preview);

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
}
