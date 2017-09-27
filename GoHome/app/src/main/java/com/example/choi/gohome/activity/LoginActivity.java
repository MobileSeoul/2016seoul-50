package com.example.choi.gohome.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.choi.gohome.R;
import com.example.choi.gohome.SettingPreference;
import com.example.choi.gohome.gcmService.RegistrationIntentService;
import com.example.choi.gohome.network.AppController;
import com.example.choi.gohome.network.request.LoginRequest;
import com.example.choi.gohome.network.response.LoginResponse;
import com.example.choi.gohome.network.response.ResultResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tsengvn.typekit.TypekitContextWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by choi on 2016-08-16.
 */
public class LoginActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private EditText loginPhone;
    private EditText loginPW;
    private AppCompatCheckBox loginCookie;
    private Button loginBtn;
    private Button linkRegisterBtn, pwReceiveBtn;
    private String phone, pw;
    private LoginRequest loginRequest;
    private ProgressBar progressBar;
    private SettingPreference authData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startOverlayWindowService();
            checkPermission();
        }*/

        loginPhone = (EditText)findViewById(R.id.login_phone_num);
        loginPW = (EditText)findViewById(R.id.login_pw);
        loginCookie = (AppCompatCheckBox)findViewById(R.id.login_cookie);
        loginBtn = (Button)findViewById(R.id.login_btn);
        linkRegisterBtn = (Button)findViewById(R.id.link_register_btn);
        pwReceiveBtn = (Button)findViewById(R.id.pw_receive_btn);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        authData = new SettingPreference("auth", LoginActivity.this);
        if(authData.getFlagPref("checked") && authData.getPref("myPhone") != null && authData.getPref("pw") != null) {
            loginPhone.setText(authData.getPref("myPhone"));
            loginPW.setText(authData.getPref("pw"));
            loginCookie.setChecked(true);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + LoginActivity.this.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    LoginActivity.this.startActivityForResult(intent, 200);
                    Toast.makeText(LoginActivity.this, "필수기능(위치, 저장공간, 전화)에 대한 권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    phone = loginPhone.getText().toString();
                    pw = loginPW.getText().toString();

                    if (phone.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else if (pw.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        loginRequest = new LoginRequest(phone, pw);
                        if (loginCookie.isChecked()) {
                            authData.setFlagPref("checked", true);
                        } else if (!loginCookie.isChecked()) {
                            authData.setFlagPref("checked", false);
                        }
                        in();
                    }
                }
            }
        });

        linkRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + LoginActivity.this.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    LoginActivity.this.startActivityForResult(intent, 200);
                    Toast.makeText(LoginActivity.this, "\'전화\'에 대한 권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            }
        });

        pwReceiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + LoginActivity.this.getPackageName()));
                    LoginActivity.this.startActivityForResult(intent, 200);
                    Toast.makeText(LoginActivity.this, "\'전화\'에 대한 권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    final String phone = telephonyManager.getLine1Number();
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("비밀번호 재발급").setMessage(phone+"에 등록된 이메일로 비밀번호를 재발급 받으시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    sendEmail(phone);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!loginPhone.getText().toString().isEmpty() && !loginPW.getText().toString().isEmpty()) {
            loginRequest = new LoginRequest(phone, pw);
//            loginBtn.performClick();
        }
    }

    @Override
    protected void onDestroy() {
        loginRequest = null;
        authData = null;
        super.onDestroy();
    }

    private void in() {
        progressBar.setVisibility(View.VISIBLE);
        Call<LoginResponse> loginResponseCall = AppController.getHttpService().login(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if(loginResponse.isResult()) {
                        authData.setPref("myPhone", phone);
                        authData.setPref("pw", pw);
                        authData.setPref("token", loginResponse.getToken());

                        getInstanceIdToken();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "phone또는 pw가 옳바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "연결 실패", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService 실행
     */
    public void getInstanceIdToken() {
        if(checkPlayServices()) {
            Log.e("MainActivity", "checkPlayServices()");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            intent.putExtra("phone", phone);
            startService(intent);
        }
    }

    /**
     * Google Play Service를 사용할 수 있는 환경인지 체크
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e("LoginActivity", "이 기기는 지원되지 않습니다.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("checkPermission()", "수락");
                    checkPermission();
                } else {
                    Log.e("checkPermission()", "거절");
                    checkPermission();
                }
            case 1:
                if(grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("checkPermission()", "수락");
                    checkPermission();
                } else {
                    Log.e("checkPermission()", "거절");
                    checkPermission();
                }
            case 2:
                if(grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("checkPermission()", "수락");
                    checkPermission();
                } else {
                    Log.e("checkPermission()", "거절");
                    checkPermission();
                }
            case 3:
                if(grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("checkPermission()", "수락");
                    checkPermission();
                } else {
                    Log.e("checkPermission()", "거절");
                    checkPermission();
                }
            case 4:
                if(grantResults.length > 0 && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("checkPermission()", "수락");
                    checkPermission();
                } else {
                    Log.e("checkPermission()", "거절");
                    checkPermission();
                }
        }
    }

    public void startOverlayWindowService() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent, 200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == 200) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    //
                } else {
                    Toast.makeText(LoginActivity.this, "오버레이 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("checkPermission()", "스토리지");
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //최초 접속이 아니고, 사용자가 다시 보지 않기에 체크를 하지 않고, 거절만 누른경우
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                //최초 접속시, 사용자가 다시 보지 않기에 체크를 했을 경우
            }
            //만약 사용자가 다시 보지 않기에 체크를 했을 경우엔 권한 설정 다이얼로그가 뜨지 않는다.
            //사용자에게 접근권한 설정을 요구하는 다이얼로그를 띄운다.
        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("checkPermission()", "gps");
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
            }
        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("checkPermission()", "phone");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
            } else {
            }
        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.e("checkPermission()", "sms");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, 3);
            } else {
            }
        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.e("checkPermission()", "audio");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 4);
            } else {
            }
        }
    }

    public void sendEmail(String phone) {
        Call<ResultResponse> sendMailCall = AppController.getHttpService().sendMail(phone);
        sendMailCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if(response.isSuccessful()) {
                    ResultResponse resultResponse = response.body();
                    if(resultResponse.isResult()) {
                        Toast.makeText(LoginActivity.this, "임시 비밀번호를 이메일로 전송하였습니다.\n메일 전송 시간이 다소(1~5분 가량) 소요될수 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "이메일 전송에 실패하였습니다. 비회원이거나 email이 틀렸을수 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "이메일 전송에 실패하였습니다. 비회원이거나 email이 틀렸을수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "이메일 전송에 실패하였습니다. 비회원이거나 email이 틀렸을수 있습니다.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
