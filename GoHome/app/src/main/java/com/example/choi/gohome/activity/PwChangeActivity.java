package com.example.choi.gohome.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.choi.gohome.R;
import com.example.choi.gohome.SettingPreference;
import com.example.choi.gohome.network.AppController;
import com.example.choi.gohome.network.request.UpdatePW;
import com.example.choi.gohome.network.response.ResultResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by choi on 2016-10-11.
 */
public class PwChangeActivity extends AppCompatActivity {

    private EditText nowPW, new1PW, new2PW;
    private String strNowPW, strNew1PW, strNew2PW, myPhone, token;
    private boolean pwFlag = false;
    private Button pwChangeBtn;
    private SettingPreference auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_change);

        nowPW = (EditText) findViewById(R.id.now_pw);
        new1PW = (EditText) findViewById(R.id.new1_pw);
        new2PW = (EditText) findViewById(R.id.new2_pw);
        pwChangeBtn = (Button) findViewById(R.id.pw_change_btn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new2PW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strNew1PW = new1PW.getText().toString();
                strNew2PW = new2PW.getText().toString();
                if(!s.toString().equals(strNew1PW)) {
                    if(strNew2PW != null) {
                        new1PW.setTextColor(0xFFFF9999);
                        new2PW.setTextColor(0xFFFF9999);
                        pwFlag = false;
                    }
                } else if(s.toString().equals(strNew1PW)) {
                    new1PW.setTextColor(0xFF66CC99);
                    new2PW.setTextColor(0xFF66CC99);
                    pwFlag = true;
                }
                new1PW.invalidate();
                new2PW.invalidate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pwChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strNowPW = nowPW.getText().toString();
                if(pwFlag) {
                    changePW();
                } else {
                    Toast.makeText(PwChangeActivity.this, "새로운 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = new SettingPreference("auth", PwChangeActivity.this);
        myPhone = auth.getPref("myPhone");
        token = auth.getPref("token");
    }

    public void changePW() {
        UpdatePW updatePW = new UpdatePW(myPhone, strNowPW, strNew1PW, token);
        Call<ResultResponse> resultResponseCall = AppController.getHttpService().updatePassword(updatePW);
        resultResponseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if(response.isSuccessful()) {
                    ResultResponse resultResponse = response.body();
                    if(resultResponse.isResult()) {
                        Toast.makeText(PwChangeActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PwChangeActivity.this, "현재 비밀번호를 다시 작성하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                Toast.makeText(PwChangeActivity.this, "네트워크 에러, 다시 시도하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
