package com.example.inhacsecapstone.chatbot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.serverconnect.HttpConnection;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.speech.tts.TextToSpeech.ERROR;

public class MessengerActivity extends Activity {

    private Intent SttIntent;
    private SpeechRecognizer mRecognizer;
    private TextToSpeech tts;
    private ChatView mChatView;
    private ArrayList<User> mUsers;
    private HttpConnection httpConn = HttpConnection.getInstance();
    public void setColors() {
        int RIGHT_BUBBLE_COLOR = R.color.colorPrimaryDark;
        int SEND_ICON = R.drawable.ic_action_send;
        int RIGHT_MESSAGE_TEXT_COLOR = Color.WHITE;
        int LEFT_MESSAGE_TEXT_COLOR = Color.BLACK;
        String INPUT_TEXT_HINT = "메시지를 입력하세요.";
        int MESSAGE_MARGIN = 5;
        mChatView.setRightBubbleColor(ContextCompat.getColor(getApplicationContext(), RIGHT_BUBBLE_COLOR));
        mChatView.setLeftBubbleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        mChatView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mChatView.setSendButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        mChatView.setSendIcon(SEND_ICON);
        mChatView.setOptionIcon(R.drawable.ic_mic_black_24dp);
        mChatView.setOptionButtonColor(R.color.colorAccent);
        mChatView.setRightMessageTextColor(RIGHT_MESSAGE_TEXT_COLOR);
        mChatView.setLeftMessageTextColor(LEFT_MESSAGE_TEXT_COLOR);
        mChatView.setUsernameTextColor(R.color.colorPrimary);
        mChatView.setSendTimeTextColor(R.color.colorPrimary);
        mChatView.setDateSeparatorColor(R.color.colorPrimary);
        mChatView.setMessageStatusTextColor(R.color.colorPrimary);
        mChatView.setInputTextHint(INPUT_TEXT_HINT);
        mChatView.setMessageMarginTop(MESSAGE_MARGIN);
        mChatView.setMessageMarginBottom(MESSAGE_MARGIN);
        mChatView.setMaxInputLine(5);
        mChatView.setUsernameFontSize(getResources().getDimension(R.dimen.font_small));
        mChatView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        mChatView.setInputTextColor(ContextCompat.getColor(this, android.R.color.black));
        mChatView.setInputTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        initUsers();
        // 화면 생성 시 Welcome Message 출력
        sendTextToServer("안녕");
        //
        mChatView = findViewById(R.id.chat_view);
        setColors();

        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mChatView.getInputText().equals("")) {
                    Message message = new Message.Builder()
                            .setUser(mUsers.get(0))
                            .setRight(true)
                            .setText(mChatView.getInputText())
                            .hideIcon(true)
                            .setStatusIconFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                            .setStatusTextFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                            .setStatusStyle(Message.Companion.getSTATUS_ICON())
                            .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                            .build();

                    sendTextToServer(mChatView.getInputText());
                    //Set to chat view
                    mChatView.send(message);
                    //Reset edit text
                    mChatView.setInputText("");
                }
            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        tts.setPitch(1.0f);
        tts.setSpeechRate(1.0f);


        SttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");//한국어 사용
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);

        //Click option button
        mChatView.setOnClickOptionButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MessengerActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
                } else {
                    try {
                        mRecognizer.startListening(SttIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private int count=0;

    private RecognitionListener listener=new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float v) {
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int i) {
        }

        @Override
        public void onResults(Bundle results) {
            String key= SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult =results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            // onResults가 이유를 모르지만 2번씩 호출되서 count로 제한...
            if(count==1) {
                Message message = new Message.Builder()
                        .setUser(mUsers.get(0))
                        .setRight(true)
                        .setText(rs[0])
                        .hideIcon(true)
                        .setStatusIconFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                        .setStatusTextFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                        .setStatusStyle(Message.Companion.getSTATUS_ICON())
                        .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                        .build();
                sendTextToServer(rs[0]);
                mChatView.send(message);
                count=0;
            }else
                count=1;
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    private void receiveMessage(String getText) {
        final Message receivedMessage = new Message.Builder()
                .setUser(mUsers.get(1))
                .setRight(false)
                .setText(getText)
                .setStatusIconFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                .setStatusTextFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                .setStatusStyle(Message.Companion.getSTATUS_ICON())
                .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                .build();
        mChatView.receive(receivedMessage);
    }

    private void initUsers() {
        mUsers = new ArrayList<>();
        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        SharedPreferences sharedPreferences = getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        String myName = sharedPreferences.getString("Name", "NoName");

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.chatbotimg);
        String yourName = "복약 알리미";

        final User me = new User(myId, myName, myIcon);
        final User you = new User(yourId, yourName, yourIcon);

        mUsers.add(me);
        mUsers.add(you);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUsers();
    }

    public void sendTextToServer(String texts){
        String postUrl = httpConn.getUrl("webhook");
        RequestBody formBody = new FormBody.Builder()
                .add("message", texts)
                .build();
        postRequest(postUrl, formBody);
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
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Failed to Connect to Server. Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String res = "";
                        try {
                            res = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "연결 오류가 발생되어 응답을 받지 못했습니다.", Toast.LENGTH_LONG).show();
                        }
                        if (!res.equals("")) {
                            receiveMessage(res);
                            tts.speak(res, TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    }
                });

            }
        });
    }

}
