package com.example.inhacsecapstone.chatbot;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.alarm.AlarmReceiver;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.serverconnect.HttpConnection;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
import static android.speech.tts.TextToSpeech.ERROR;

public class MessengerActivity extends Activity {

    private Intent SttIntent;
    private SpeechRecognizer mRecognizer;
    private TextToSpeech tts;
    private ChatView mChatView;
    private ArrayList<User> mUsers;
    private AppDatabase db;
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
    boolean notendofspeech=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        initUsers();
        db = AppDatabase.getDataBase(getApplicationContext());

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

        mChatView.setOnBubbleClickListener(new Message.OnBubbleClickListener() {
            @Override
            public void onClick(Message message) {
                final Bitmap bitmap = message.getPicture();

                LayoutInflater factory = LayoutInflater.from(MessengerActivity.this);
                final View view = factory.inflate(R.layout.myphoto_layout, null);

                Dialog dialog = new Dialog(MessengerActivity.this);
                ImageView iv = view.findViewById(R.id.iv);

                Glide.with(getApplicationContext()).load(bitmap).into(iv);
                dialog.setContentView(view);
                dialog.show();
            }
        });

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

        // 화면 생성 시 Welcome Message 출력 이후 getIntExtra에서 코드값에 따라 Welcome 메시지 다르게 전송
        Intent intent = getIntent();
        int code = intent.getIntExtra("Code", 0);

        switch(code) {
            case 0:
                sendTextToServer("안녕");
                break;
            case 1:
                int errorcode = intent.getIntExtra("errorcode",0);
                if(errorcode!=0) {
                    String texts = "비정상적인 종료가 발생된 약품입니다.";
                    receiveMessage(texts);
                    tts.speak(texts, TextToSpeech.QUEUE_FLUSH, null, null);
                }
                sendTextToServer("약 먹으러 왔어");
                break;
        }
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
            // onResults가 2번씩 호출되서 count로 제한
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

    private void receiveImage(String uri) {
        Glide.with(getApplicationContext()).asBitmap().load(uri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        final Bitmap bitmap = resource;
                        Message message = new Message.Builder()
                                .setRight(false)
                                .setText(Message.Type.PICTURE.name())
                                .setUser(mUsers.get(1))
                                .setPicture(bitmap)
                                .setType(Message.Type.PICTURE)
                                .setStatusIconFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                                .setStatusStyle(Message.Companion.getSTATUS_ICON())
                                .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                                .build();
                        mChatView.receive(message);
                    }
                });
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

    public void sendTextToServer(String texts){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        String postUrl = httpConn.getUrl("webhook");
        Log.d("https",postUrl);
        RequestBody formBody = new FormBody.Builder()
                .add("message", texts).add("username",sharedPreferences.getString("Name", "noName"))
                .build();
        postRequest(postUrl, formBody);
    }

    public void doTextBasedAction(String texts) {
        String regex = "\\d{2}[가-힣]{1,}\\s*\\d{2}[가-힣]{1,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texts);
        if(texts.contains("잘하셨어요")) {
            while (matcher.find()) {
                String temp = matcher.group();
                temp = temp.replace("분에", "시"); temp = temp.replace("분으로", "시");
                String hr[] = temp.split("시");
                hr[0] = hr[0].trim();
                hr[1] = hr[1].trim();
                ArrayList<Medicine> medi = (ArrayList<Medicine>) getIntent().getSerializableExtra("medicine");
                if (!medi.isEmpty()) {
                    for (int i = 0; i < medi.size(); i++) {
                        Integer code = medi.get(i).getCode();
                        Calendar calendar = Calendar.getInstance();
                        String days = Integer.toString(calendar.get(Calendar.YEAR)) + "." + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendar.get(Calendar.DATE));
                        String times = Integer.parseInt(hr[0]) + ":" + Integer.parseInt(hr[1]);
                        Takes takes = new Takes(code, days, times);
                        db.insert(takes);
                        Log.d("DB저장완료", medi.get(i).getName() + " - " + hr[0] + ":" + hr[1] );
                    }
                    notendofspeech = false;
                    Log.d("DB저장완료", "시간저장완료");
                    return;
                }
            }
        } else if(texts.contains("알려드릴게요")){
            while (matcher.find()) {
                String temp = matcher.group();
                temp = temp.replace("분에", "시");
                String hr[] = temp.split("시");
                hr[0] = hr[0].trim();
                hr[1] = hr[1].trim();
                ArrayList<Medicine> medi = (ArrayList<Medicine>) getIntent().getSerializableExtra("medicine");

                Calendar calendar = Calendar.getInstance();
                if(Integer.parseInt(hr[0]) < calendar.get(Calendar.HOUR))
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE+1), Integer.parseInt(hr[0]), Integer.parseInt(hr[1]), 0);
                else
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Integer.parseInt(hr[0]), Integer.parseInt(hr[1]), 0);

                AlarmManager am = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("medicine", medi);
                PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
            }
            notendofspeech = false;
            return;
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull android.os.Message msg) {
            if(msg.what==0) {
                Toast.makeText(getApplicationContext(), "연결에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                return false;
            } else if(msg.what == 1){
                String res = (String)msg.obj;
                receiveMessage(res);
                tts.speak(res, TextToSpeech.QUEUE_FLUSH, null, null);
                doTextBasedAction(res);
                return true;
            } else if(msg.what==2) {
                String res = (String)msg.obj;
                receiveMessage(res);
                tts.speak(res, TextToSpeech.QUEUE_FLUSH, null, null);
                ArrayList<Medicine> medi = (ArrayList<Medicine>) getIntent().getSerializableExtra("medicine");
                if(medi==null)
                    return false;
                for(int i=0; i<medi.size(); i++) {
                    if(medi.get(i).getImage()!=null)
                        receiveImage(medi.get(i).getImage());
                }
                return true;
            }
            return false;
        }
    });

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
                android.os.Message message = android.os.Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Thread(() -> {
                    String res = "";
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        android.os.Message message = android.os.Message.obtain();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                    if (!res.equals("")) {
                        android.os.Message message = android.os.Message.obtain();
                        message.obj = res;
                        if(res.contains("약 드실 시간이에요.")) {
                            message.what = 2;
                            notendofspeech = true;
                        } else
                            message.what = 1;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        initUsers();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mRecognizer.destroy();
    }

    @Override
    protected void onDestroy()
    {
        if(notendofspeech) {
            Log.d("@@@", "비정상적 종료");
            ArrayList<Medicine> medi = (ArrayList<Medicine>) getIntent().getSerializableExtra("medicine");
            if(!medi.isEmpty()) {
                AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("medicine", medi).putExtra("errorcode",1);
                PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000 * 60, pIntent);
            }

            Log.d("@@@", "세팅완료");
        }
        if(tts!=null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
