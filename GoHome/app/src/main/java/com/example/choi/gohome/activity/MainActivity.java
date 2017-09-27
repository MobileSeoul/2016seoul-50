package com.example.choi.gohome.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choi.gohome.Authentication;
import com.example.choi.gohome.GpsInfo;
import com.example.choi.gohome.LatLngSize;
import com.example.choi.gohome.MyLatLng;
import com.example.choi.gohome.NaverRecognizer;
import com.example.choi.gohome.R;
import com.example.choi.gohome.SMSService;
import com.example.choi.gohome.SQLiteHandler;
import com.example.choi.gohome.SettingPreference;
import com.example.choi.gohome.gcmService.GcmClass;
import com.example.choi.gohome.network.AppController;
import com.example.choi.gohome.network.request.ProfileRequest;
import com.example.choi.gohome.network.request.WithdrawRequest;
import com.example.choi.gohome.network.response.ProfileResponse;
import com.example.choi.gohome.network.response.ResultResponse;
import com.example.choi.gohome.utils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechConfig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private GpsInfo gpsInfo;
    private GcmClass gcmClass;
    private SMSService smsService;
    private SettingPreference authData, pref;
    private SQLiteHandler sqLiteHandler;
    private Handler handler;
    private TimerTask timerTask;
    private Timer timer;

    private Button serviceStart, serviceStop, emergencyCall, mapSearch, routeSetting, etcSetting;
    private TextView myName, myPhoneNum;
    private ProgressBar progressBar;

    private String name, phone, myPhone, token;
    private String nowAddress, msg;
    private String guardian_phone1, guardian_phone2, guardian_phone3;
    private int age, gender;
    private double lat, lng;
    private int addressListSize, errCnt = 0, errCnt2 = 0;
    private int timePref;
    private boolean smsPref, sms112Pref, voicePref;
    private boolean routerFlag = false, emergency = false, isConnected = false, guardiansFlag = false, safetyServiceRunning = false, alarmFlag = false, firstExecute = false;
    private List<String> addressList = new ArrayList<>();
    private List<String> guardian_phone = new ArrayList<>();
    private List<LatLngSize> latLngSize;

    //Naver Speech API
    private static final String CLIENT_ID = Authentication.CLIENT_ID; // "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    private static final SpeechConfig SPEECH_CONFIG = SpeechConfig.OPENAPI_KR; // or SpeechConfig.OPENAPI_EN
    private RecognitionHandler recogHandler;
    private NaverRecognizer naverRecognizer;
    private AudioWriterPCM writer;
    //음성인식
    private String mResult;
    private boolean serviceFlag = false, voiceFlag = false, isRunning;
    int firstFlag = 0;  //firstFlag는 음성인식 긴급 호출 시 partialResult가 여러번 호출되어 한번만 emergencyCall을 작동하게하려함
    private List<String> arrayResult = new ArrayList<>();
    private List<String> helpVoiceList;

    //Count
    private Context mContext;
    private Counter counter;
    private AlertDialog adDialog;
    private AlertDialog.Builder cDialog;
    private TextView tv, alarm_text, alarm_text1, alarm_text2;
    private Button send, close;
    private int count;
    private boolean countFlag = false;

    //음성인식 데이터 핸들메시지
    private void handleMessage(Message msg) {
        switch (msg.what) {
            // Handle speech recognition Messages.
            case R.id.clientReady:
                // Now an user can speak.
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                try {
                    writer.write((short[]) msg.obj);
                } catch (NullPointerException ignored) {
                } catch (Exception ignored) {}
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                Log.e("partialResult", mResult);
                if(helpVoiceList.contains(mResult) && mResult != null) {
                    firstFlag++;
                    if(firstFlag == 1) {
                        emergencyCall.performClick();
                    }
                }
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                String[] results = (String[]) msg.obj;
                mResult = results[0];
                arrayResult.add(mResult);
                Log.e("arrayResult", String.valueOf(arrayResult));
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                Log.e("recognitionError", mResult);
                isRunning = false;
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }
                Log.e("clientInactive", String.valueOf(serviceFlag));
                isRunning = false;
                if (serviceFlag) {
                    onStart();
                    speechServiceStart();
                }
                break;
        }
    }
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteHandler = new SQLiteHandler(MainActivity.this);

        serviceStart = (Button) findViewById(R.id.service_start);
        serviceStop = (Button) findViewById(R.id.service_stop);
        emergencyCall = (Button) findViewById(R.id.emergency_call);
        mapSearch = (Button) findViewById(R.id.map_search);
        routeSetting = (Button) findViewById(R.id.route_setting);
        etcSetting = (Button) findViewById(R.id.etc_setting);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
//            navigationView.setItemIconTintList();
        }

        View header = navigationView.getHeaderView(0);
        myName = (TextView) header.findViewById(R.id.myName);
        myPhoneNum = (TextView) header.findViewById(R.id.myPhoneNum);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        MyLatLng myLatLng = (MyLatLng)msg.obj;
                        lat = myLatLng.getLat();
                        lng = myLatLng.getLng();
                        isConnected = myLatLng.getConnected();
                        break;
                }
            }
        };

        serviceStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!safetyServiceRunning) {
                    safetyServiceRunning = true;
                    if (!guardiansFlag) {
                        Toast.makeText(MainActivity.this, "'내 프로필-보호자 설정'에서 보호자를 등록하세요.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, GuardiansActivity.class));
                    }
                    if (voicePref) {
                        String[] helpVoice = getResources().getStringArray(R.array.help_voice_arrays);
                        helpVoiceList = new ArrayList<>(Arrays.asList(helpVoice));
                        try {
                            speechServiceStart();
                        } catch (IllegalMonitorStateException e) {
                            speechServiceStart();
                            e.printStackTrace();
                        }
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                progressBar.setVisibility(View.VISIBLE);
                                if (errCnt < 5) {
                                    gpsSetting();
                                    if ((gpsInfo == null) || (nowAddress.isEmpty()) || (!isConnected) || (lat == 0) || (lng == 0)) {
                                        errCnt++;
                                        safetyServiceRunning = false;
                                        throw new NullPointerException();
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                    serviceStart.setBackground(getResources().getDrawable(R.drawable.menu_1_ing));
                                    start();
                                } else {
                                    stop();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, "GPS상태가 좋지않습니다.\n서비스를 종료합니다.", Toast.LENGTH_SHORT).show();
                                    errCnt = 0;
                                    safetyServiceRunning = false;
                                }
                            } catch (NullPointerException e) {
                                serviceStart.performClick();
                            }
                        }
                    }, 1000);
                } else {
                    Toast.makeText(MainActivity.this, "안심귀가 서비스가 실행 중 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        serviceStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "안심귀가 서비스를 종료합니다.", Toast.LENGTH_SHORT).show();
                stop();
                if(voicePref) {
//                    naverRecognizer.getSpeechRecognizer().stopImmediately();
//                    naverRecognizer.getSpeechRecognizer().release();
                    naverRecognizer = null;
                    serviceFlag = false;
                    voiceFlag = false;
                    isRunning = false;
                }
            }
        });
        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "긴급 호출 클릭", Toast.LENGTH_SHORT).show();
                if(nowAddress != null) {
                    msg = name + "님의 긴급호출! 위치:"+nowAddress;
                    /*sendGcm(msg);
                    if(smsPref) {
                        for(int i=0; i<guardian_phone.size(); i++) {
                            smsService.sendSMS(guardian_phone.get(i), name+"님이 긴급호출을 하였습니다. 위치: "+nowAddress);
                        }
                    }*/
                    countDialog();
                    if(!safetyServiceRunning) {
                        serviceStart.performClick();
                    }
                } else {
                    emergency = true;
                    Toast.makeText(MainActivity.this, "서비스를 시작합니다.", Toast.LENGTH_SHORT).show();
                    serviceStart.performClick();
                }
            }
        });
        mapSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });
        routeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RouteSettingActivity.class));
            }
        });
        etcSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SafetyScoutActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recogHandler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, recogHandler, CLIENT_ID, SPEECH_CONFIG);
        authData = new SettingPreference("auth", MainActivity.this);
        smsService = new SMSService(MainActivity.this);
        addressList.clear();
        addressListSize = authData.getListPref("addressList");
        for(int i=0; i<addressListSize; i++) {
            addressList.add(authData.getPref("addressList_"+i));
        }
        gcmClass = new GcmClass();
        myPhone = authData.getPref("myPhone");
        token = authData.getPref("token");
        if(!authData.getFlagPref("first")) {
            firstExecute = authData.getFlagPref("first");
            if(!firstExecute) {
                new AlertDialog.Builder(this)
                        .setTitle("앱 설명서").setMessage("처음이세요? 앱 사용 설명서 화면으로 넘어가기")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                startActivity(new Intent(MainActivity.this, GuideActivity.class));
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
                authData.setFlagPref("first", true);
                pref = new SettingPreference("setting", MainActivity.this);
                pref.setFlagPref("uploadPref", true);
            }
        }

        pref = new SettingPreference("setting", MainActivity.this);
        if(pref.getPref("timePref") == null) {
            pref.setPref("timePref", "1");
        }
        timePref = Integer.parseInt(pref.getPref("timePref"));
        smsPref = pref.getFlagPref("smsPref");
        sms112Pref = pref.getFlagPref("sms112Pref");
        voicePref = pref.getFlagPref("voicePref");
        getProfile(myPhone, token);
        gcmClass.getGcmTokenList(myPhone, MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!voiceFlag) {
            try {
                naverRecognizer.getSpeechRecognizer().initialize();
                voiceFlag = !voiceFlag;
            } catch (NullPointerException ignored) {}
        }
        latLngSize = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    getRouteDB();
                } catch (NullPointerException ignored) {}
            }
        }, 500);
        errCnt2 = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!safetyServiceRunning) {
            gpsInfo = null;
            gcmClass = null;
            smsService = null;
            authData = null;
            pref = null;
            sqLiteHandler = null;
            timerTask = null;
            timer = null;
            addressList = null;
            guardian_phone = null;
            latLngSize = null;
            recogHandler = null;
            naverRecognizer = null;
            writer = null;
            arrayResult = null;
            helpVoiceList = null;
        }
    }

    public void gpsSetting() {
        if (gpsInfo == null) {
            gpsInfo = new GpsInfo(MainActivity.this, handler);
        }
        if (gpsInfo != null) {
            gpsInfo.connectStart();
            nowAddress = gpsInfo.getAddress(MainActivity.this, lat, lng);
        }
    }

    public void start() {
        safetyServiceRunning = true;
        Toast.makeText(MainActivity.this, "안심귀가 서비스를 시작합니다.", Toast.LENGTH_SHORT).show();
        if(addressListSize > 0 || (!latLngSize.isEmpty())) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if((!isConnected) || (lat == 0) || (lng == 0)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "GPS상태가 좋지않습니다.", Toast.LENGTH_SHORT).show();
                                String msg = name+"님의 GPS상태가 좋지않습니다.";
                                sendGcm(msg);
                                if(smsPref) {
                                    for(int i=0; i<guardian_phone.size(); i++) {
                                        if(nowAddress != null) {
                                            smsService.sendSMS(guardian_phone.get(i), name+"님의 GPS상태가 좋지않습니다. 마지막 위치: "+nowAddress);
                                        } else if(nowAddress == null) {
                                            smsService.sendSMS(guardian_phone.get(i), msg);
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        String summaryNowAddress = "";
                        if(lat != 0 && lng != 0) {
                            nowAddress = gpsInfo.getAddress(MainActivity.this, lat, lng);//37.508069, 127.020408
                            if(nowAddress.charAt(nowAddress.length()-1) == '동') {
                                summaryNowAddress = nowAddress.substring(0, nowAddress.lastIndexOf("동"));
                            } else if(nowAddress.charAt(nowAddress.length()-1) == '로') {
                                summaryNowAddress = nowAddress.substring(0, nowAddress.lastIndexOf("로"));
                            } else {
                                summaryNowAddress = nowAddress;
                            }
                            char ch = summaryNowAddress.charAt(summaryNowAddress.length()-1);
                            if(ch>='1' && ch<='9') {    //논현1동, 논현2동으로 주소가 구성되어있는 경우 논현으로 통합
                                summaryNowAddress = summaryNowAddress.substring(0, summaryNowAddress.length()-1);
                            }
                        }
                        for (int i = 0; i < addressListSize; i++) {
                            String summaryAddress = addressList.get(i).substring(0, addressList.get(i).lastIndexOf("동"));
                            if(summaryNowAddress.contains(summaryAddress)) {
                                routerFlag = true;
                            }
                            /*if (nowAddress.contains(addressList.get(i))) {
                                routerFlag = true;
                            }*/
                        }
                        for(int i=0; i<latLngSize.size(); i++) {
                            float[] results = new float[3];
                            Location.distanceBetween(lat, lng, latLngSize.get(i).getLat(), latLngSize.get(i).getLng(), results);
                            if (results[0] <= latLngSize.get(i).getSize()) {
                                routerFlag = true;
                            }
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (routerFlag) {
                                    Toast.makeText(MainActivity.this, "경로 유지 "+nowAddress, Toast.LENGTH_SHORT).show();
                                    routerFlag = false;
                                } else {
                                    Toast.makeText(MainActivity.this, "경로 이탈 "+nowAddress, Toast.LENGTH_SHORT).show();
                                    if (emergency) {
                                        msg = name + "님의 긴급호출! 위치:" + nowAddress;
                                    } else {
                                        msg = name + "님 경로 이탈! 위치:" + nowAddress;
                                    }
                                    /*sendGcm(msg);
                                    if(smsPref) {
                                        for(int i=0; i<guardian_phone.size(); i++) {
                                            String g_phone = guardian_phone.get(i);
                                            smsService.sendSMS(g_phone, msg);
                                        }
                                    }*/
                                    countDialog();
                                }
                            }
                        });
                    }
                }
            };
            timer.schedule(timerTask, 0, 1000*60*timePref);  //기본timePref값 1분 마다 반복
        } else if (addressListSize == 0 && latLngSize.isEmpty()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "먼저 경로를 설정하세요.\n안심귀가 서비스를 종료합니다.", Toast.LENGTH_SHORT).show();
                    routeSetting.performClick();
                }
            });
            stop();
        }
    }

    public void stop() {
        serviceStart.setBackground(getResources().getDrawable(R.drawable.menu_1));
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
        if(gpsInfo != null) {
            gpsInfo.onDestroy();
            gpsInfo = null;
        }
        safetyServiceRunning = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.etc_btn:

        }
        return super.onOptionsItemSelected(item);
    }

    public void getProfile(String myPhone, String token) {
        ProfileRequest profileRequest = new ProfileRequest(myPhone, token);
        Call<ProfileResponse> profileResponseCall = AppController.getHttpService().getProfile(profileRequest);
        profileResponseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.isSuccessful()) {
                    ProfileResponse profile = response.body();

                    name = profile.getMyProfile().getName();
                    phone = profile.getMyProfile().getMyPhone();
                    age = profile.getMyProfile().getAge();
                    gender = profile.getMyProfile().getGender();

                    myName.setText(name);
                    myPhoneNum.setText(phone);

                    guardian_phone1 = profile.getMyProfile().getGuardian_phone1();
                    guardian_phone2 = profile.getMyProfile().getGuardian_phone2();
                    guardian_phone3 = profile.getMyProfile().getGuardian_phone3();
                    if(guardian_phone1 != null || guardian_phone2 != null || guardian_phone3 != null){
                        if(guardian_phone1.equals("") && guardian_phone2.equals("") && guardian_phone3.equals("")) {
                            guardiansFlag = false;
                        } else {
                            guardiansFlag = true;
                        }
                    }
                    guardian_phone.clear();

                    if(guardian_phone1 != null) {
                        guardian_phone.add(guardian_phone1);
                    }
                    if(guardian_phone2 != null) {
                        guardian_phone.add(guardian_phone2);
                    }
                    if(guardian_phone3 != null) {
                        guardian_phone.add(guardian_phone3);
                    }

                } else {
                    name = "프로필 정보를 받지 못했습니다.";
                    phone = "프로필 정보를 받지 못했습니다.";
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                name = "프로필 정보를 받지 못했습니다.";
                phone = "프로필 정보를 받지 못했습니다.";
            }
        });
    }

    public void sendGcm(String msg) {
        String to;
        for (int i = 0; i < 3; i++) {
            try {
                to = authData.getPref("gcm_token" + i);
                if (to != null) {
                    gcmClass.sendGcm(to, msg);
                }
            } catch (NullPointerException ignored) {}
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if(safetyServiceRunning) {
                    AlertDialog.Builder end = new AlertDialog.Builder(this);
                    end.setTitle("서비스를 종료하시겠습니까?")
                            .setMessage("서비스가 실행 중입니다. 홈버튼을 누르세요.")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    serviceStop.performClick();
                                    finish();
                                }
                            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    end.show();
                } else {
                    super.onBackPressed();
                }
            }
        }
        if(countFlag) {
            close.performClick();
        }
        if(firstFlag >= 1) {
            firstFlag = 0;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else if(id == R.id.nav_app_setting) {
            startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
        } else if(id == R.id.nav_guide) {
            startActivity(new Intent(MainActivity.this, GuideActivity.class));
        } else if(id == R.id.nav_question) {
            startActivity(new Intent(MainActivity.this, SendQuestionMail.class));
        } else if(id == R.id.nav_logout) {
            logout();
        } else if(id == R.id.nav_withdraw) {
            if(safetyServiceRunning) {
                Toast.makeText(MainActivity.this, "안심귀가 서비스가 실행 중 입니다. 서비스를 종료한 후 가능합니다.", Toast.LENGTH_SHORT).show();
            } else {
                withdrawAlarm();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void getRouteDB() {
        try {
            if(errCnt2 <= 5) {
                latLngSize.addAll(sqLiteHandler.selectAll());
            }
        } catch (Exception e) {
            errCnt2++;
            onResume();
        }
    }

    static class RecognitionHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        RecognitionHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            } else if(activity == null) {
                //아무 반응 없음
            }
        }
    }

    //위치 이탈이나 긴급호출로 인한 알람시 20초 이내 취소할 수 있는 다이얼로그
    public void countDialog() {
        counter = new Counter();
        count = 0;
        countFlag=true;
        mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.popupid));
        cDialog = new AlertDialog.Builder(MainActivity.this);
        cDialog.setView(layout);
        tv = (TextView)layout.findViewById(R.id.TimeText);
        alarm_text = (TextView)layout.findViewById(R.id.alarm_text1);
        alarm_text1 = (TextView)layout.findViewById(R.id.alarm_text2);
        alarm_text2 = (TextView)layout.findViewById(R.id.alarm_text3);
        send = (Button)layout.findViewById(R.id.send);
        close = (Button)layout.findViewById(R.id.close);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 20;
                firstFlag = 0;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.interrupt();
                adDialog.dismiss();
                count = 0;
                countFlag = false;
                firstFlag = 0;
            }
        });
        adDialog = cDialog.create();
        adDialog.show();
        counter.start();
    }

    class Counter extends Thread {

        @Override
        public void run() {
            while(countFlag) {
                mHandler.sendEmptyMessage(count);
                count++;
                SystemClock.sleep(1000L);
            }
        }
    }
    final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if(message.what!=21) {
                tv.setText(20 - message.what + "");
            } else {
                alarm_text.setText("알림 및 신고가 완료되었습니다.");
                alarm_text1.setText("알림창이 곧 종료됩니다.");
                alarm_text2.setText("");
                tv.setVisibility(TextView.GONE);
                sendGcm(msg);
                if(smsPref) {
                    for(int i=0; i<guardian_phone.size(); i++) {
                        String g_phone = guardian_phone.get(i);
                        smsService.sendSMS(g_phone, msg);
                    }
                }
                if(sms112Pref) {
                    String strAge = "미등록", strGender = "미등록";
                    String strLat, strLng;
                    if(age == 1) {
                        strAge = "1~10세";
                    } else if(age == 2) {
                        strAge = "11~20세";
                    } else if(age == 3) {
                        strAge = "21~30세";
                    } else if(age == 4) {
                        strAge = "31~40세";
                    } else if(age == 5) {
                        strAge = "41~50세";
                    } else if(age == 6) {
                        strAge = "51~60세";
                    } else if(age == 7) {
                        strAge = "61세 이상";
                    }

                    if(gender == 1) {
                        strGender = "남성";
                    } else if(gender == 2) {
                        strGender = "여성";
                    }
                    strLat = String.format("%.4f", lat);
                    strLng = String.format("%.4f", lng);
                    smsService.sendSMS("112", "안심귀가앱 신고, "+msg+", 위도경도:"+strLat+", "+strLng+", "+strAge+" "+strGender);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        close.performClick();
                    }
                }, 3000);
                firstFlag = 0;
            }

        }



    };

    public void speechServiceStart() {
        serviceFlag = true;
        if (!isRunning) {
            // Start button is pushed when SpeechRecognizer's state is inactive.
            // Run SpeechRecongizer by calling recognize().
            mResult = "";
            isRunning = true;
            try {
                naverRecognizer.recognize();
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // This flow is occurred by pushing start button again
            // when SpeechRecognizer is running.
            // Because it means that a user wants to cancel speech
            // recognition commonly, so call stop().
            naverRecognizer.getSpeechRecognizer().stop();
        }
    }

    public void logout() {
        if(safetyServiceRunning) {
            new AlertDialog.Builder(this)
                    .setTitle("로그아웃").setMessage("서비스가 실행 중입니다. 로그아웃 하시겠습니까?")
                    .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            serviceStop.performClick();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();
        }
    }

    private void withdrawAlarm() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);

        alert.setTitle("회원탈퇴").setMessage("회원탈퇴를 하시겠습니까?\n비밀번호를 입력하세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value;
                        try {
                            value = input.getText().toString();
                            if(!value.equals("") || !value.isEmpty()) {
                                WithdrawRequest withdrawRequest = new WithdrawRequest(myPhone, value, token);
                                withdraw(withdrawRequest);
                            } else {
                                Toast.makeText(MainActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(MainActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void withdraw(WithdrawRequest withdrawRequest) {
        Call<ResultResponse> resultResponseCall = AppController.getHttpService().withdrawFrom(withdrawRequest);
        resultResponseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if(response.isSuccessful()) {
                    ResultResponse resultResponse = response.body();
                    if(resultResponse.isResult()) {
                        Toast.makeText(MainActivity.this, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
