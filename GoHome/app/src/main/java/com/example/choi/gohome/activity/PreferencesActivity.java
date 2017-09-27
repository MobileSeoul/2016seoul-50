package com.example.choi.gohome.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.choi.gohome.R;
import com.example.choi.gohome.SettingPreference;

/**
 * Created by choi on 2016-09-20.
 * 환경설정 액티비티
 */
public class PreferencesActivity extends AppCompatActivity {

    private EditText timePrefEt;
    private SwitchCompat smsPrefSwitch, sms112PrefSwitch, voicePrefSwitch, uploadPrefSwitch;
    private Button prefSaveBtn;
    private int timePref;
    private boolean smsPref, sms112Pref, voicePref, uploadPref;
    private SettingPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        timePrefEt = (EditText) findViewById(R.id.time_pref_et);
        smsPrefSwitch = (SwitchCompat) findViewById(R.id.sms_pref_switch);
        sms112PrefSwitch = (SwitchCompat) findViewById(R.id.sms112_pref_switch);
        voicePrefSwitch = (SwitchCompat) findViewById(R.id.voice_pref_switch);
        uploadPrefSwitch = (SwitchCompat) findViewById(R.id.upload_pref_switch);
        prefSaveBtn = (Button) findViewById(R.id.pref_save_btn);
        pref = new SettingPreference("setting", PreferencesActivity.this);

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

        timePrefEt.setText(pref.getPref("timePref"));

        if(pref.getFlagPref("smsPref")) {
            smsPrefSwitch.setChecked(true);
            smsPref = true;
        } else {
            smsPrefSwitch.setChecked(false);
            smsPref = false;
        }

        if(pref.getFlagPref("sms112Pref")) {
            sms112PrefSwitch.setChecked(true);
            sms112Pref = true;
        } else {
            sms112PrefSwitch.setChecked(false);
            sms112Pref = false;
        }

        if(pref.getFlagPref("voicePref")) {
            voicePrefSwitch.setChecked(true);
            voicePref = true;
        } else {
            voicePrefSwitch.setChecked(false);
            voicePref = false;
        }

        if(pref.getFlagPref("uploadPref")) {
            uploadPrefSwitch.setChecked(true);
            uploadPref = true;
        } else {
            uploadPrefSwitch.setChecked(false);
            uploadPref = false;
        }

        smsPrefSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(ContextCompat.checkSelfPermission(PreferencesActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(PreferencesActivity.this, "SMS 권한 설정을 추가하세요. 권한-SMS", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + PreferencesActivity.this.getPackageName()));
                        startActivityForResult(intent, 200);
                    }
                    smsPref = true;
                } else {
                    smsPref = false;
                }
            }
        });
        sms112PrefSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(ContextCompat.checkSelfPermission(PreferencesActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(PreferencesActivity.this, "SMS 권한 설정을 추가하세요. 권한-SMS", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + PreferencesActivity.this.getPackageName()));
                        startActivityForResult(intent, 200);
                    }
                    sms112Pref = true;
                } else {
                    sms112Pref = false;
                }
            }
        });
        voicePrefSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(ContextCompat.checkSelfPermission(PreferencesActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(PreferencesActivity.this, "마이크 권한 설정을 추가하세요. 권한-마이크", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + PreferencesActivity.this.getPackageName()));
                        startActivityForResult(intent, 200);
                    }
                    voicePref = true;
                } else {
                    voicePref = false;
                }
            }
        });
        uploadPrefSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    uploadPref = true;
                } else {
                    uploadPref = false;
                }
            }
        });

        prefSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTime = timePrefEt.getText().toString();
                if(strTime != null && !strTime.equals("")) {
                    timePref = Integer.parseInt(strTime);
                    pref.setFlagPref("smsPref", smsPref);
                    pref.setFlagPref("sms112Pref", sms112Pref);
                    pref.setFlagPref("voicePref", voicePref);
                    pref.setFlagPref("uploadPref", uploadPref);
                    if (timePref > 10) {
                        Toast.makeText(PreferencesActivity.this, "위치확인주기는 최대10분입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        pref.setPref("timePref", String.valueOf(timePref));
                        Toast.makeText(PreferencesActivity.this, "변경사항이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PreferencesActivity.this, "위치확인주기를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
