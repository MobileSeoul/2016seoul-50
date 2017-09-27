package com.example.choi.gohome;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.choi.gohome.utils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechConfig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by choi on 2016-09-27.
 */
public class SpeechService extends Service {

    private Context mContext;
    private static final String CLIENT_ID = Authentication.CLIENT_ID; // "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    private static final SpeechConfig SPEECH_CONFIG = SpeechConfig.OPENAPI_KR; // or SpeechConfig.OPENAPI_EN

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private List<String> arrayResult = new ArrayList<>();
    private String mResult;

    private AudioWriterPCM writer;

    private boolean isRunning;
    private boolean serviceFlag = false;

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                String[] results = (String[]) msg.obj;
                mResult = results[0];
                arrayResult.add(mResult);
                Log.d("arrayResult", String.valueOf(arrayResult));
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                isRunning = false;
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                isRunning = false;
                if(serviceFlag) {
                    start();
//                    btnStart.performClick();
                }
                break;
        }
    }

    public SpeechService(Activity context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer((Activity)mContext, handler, CLIENT_ID, SPEECH_CONFIG);

        /*btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    txtResult.setText("Connecting...");
                    btnStart.setText(R.string.str_listening);
                    isRunning = true;

                    naverRecognizer.recognize();
                } else {
                    // This flow is occurred by pushing start button again
                    // when SpeechRecognizer is running.
                    // Because it means that a user wants to cancel speech
                    // recognition commonly, so call stop().
                    btnStart.setEnabled(false);

                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });*/
        start();
    }

    public void start() {
        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer((Activity) mContext, handler, CLIENT_ID, SPEECH_CONFIG);
        naverRecognizer.getSpeechRecognizer().initialize();
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        naverRecognizer.getSpeechRecognizer().stopImmediately();
        naverRecognizer.getSpeechRecognizer().release();
        isRunning = false;
    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<SpeechService> mActivity;

        RecognitionHandler(SpeechService activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SpeechService activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}
