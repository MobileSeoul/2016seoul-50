package com.example.choi.gohome;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.choi.gohome.network.AppController;
import com.example.choi.gohome.network.request.CheckUserCode;
import com.example.choi.gohome.network.request.DeleteWard;
import com.example.choi.gohome.network.request.ProfileRequest;
import com.example.choi.gohome.network.request.UpdateGuard;
import com.example.choi.gohome.network.request.UpdateWard;
import com.example.choi.gohome.network.response.ProfileResponse;
import com.example.choi.gohome.network.response.ResultResponse;
import com.example.choi.gohome.network.response.WardListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by choi on 2016-10-10.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments = new Fragment[2];

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new FragmentGuardActivity();
        fragments[1] = new FragmentWardActivity();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @SuppressLint("ValidFragment")
    public static class FragmentGuardActivity extends Fragment {

        private SettingPreference authData;
        private String myPhone, token, protec1, protec2, protec3;
        private EditText protector1, protector2, protector3;
        private Button protectorSaveBtn;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_profile_guard, container, false);

            protector1 = (EditText) rootView.findViewById(R.id.protector1);
            protector2 = (EditText) rootView.findViewById(R.id.protector2);
            protector3 = (EditText) rootView.findViewById(R.id.protector3);
            protectorSaveBtn = (Button) rootView.findViewById(R.id.protector_save_btn);

            protectorSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    protec1 = protector1.getText().toString();
                    protec2 = protector2.getText().toString();
                    protec3 = protector3.getText().toString();
                    setProfile();
                }
            });
            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            authData = new SettingPreference("auth", getActivity());
            myPhone = authData.getPref("myPhone");
            token = authData.getPref("token");
            getProfile();
        }

        public void getProfile() {
            ProfileRequest profileRequest = new ProfileRequest(myPhone, token);
            Call<ProfileResponse> profileResponseCall = AppController.getHttpService().getProfile(profileRequest);
            profileResponseCall.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if(response.isSuccessful()) {
                        ProfileResponse profile = response.body();
                        if(profile.isResult()) {
                            protec1 = profile.getMyProfile().getGuardian_phone1();
                            protec2 = profile.getMyProfile().getGuardian_phone2();
                            protec3 = profile.getMyProfile().getGuardian_phone3();
                            if(protec1 != null) {
                                protector1.setText(protec1);
                            }
                            if(protec2 != null) {
                                protector2.setText(protec2);
                            }
                            if(protec3 != null) {
                                protector3.setText(protec3);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {

                }
            });
        }

        public void setProfile() {
            UpdateGuard updateGuard = new UpdateGuard(protec1, protec2, protec3, myPhone, token);
            Call<ResultResponse> resultResponseCall = AppController.getHttpService().updateGuardians(updateGuard);
            resultResponseCall.enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                    if(response.isSuccessful()) {
                        ResultResponse resultResponse = response.body();
                        if(resultResponse.isResult()) {
                            Toast.makeText(getActivity(), "프로필 설정 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "네트워크 에러, 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @SuppressLint("ValidFragment")
    public static class FragmentWardActivity extends Fragment {

        private SettingPreference authData;
        private String myPhone, token, newWard_code;
        private EditText ward1, ward2, ward3, ward1_code, ward2_code, ward3_code;
        private boolean checkCodeFlag1 = false, checkCodeFlag2 = false, checkCodeFlag3 = false;
        private Button deleteBtn1, deleteBtn2, deleteBtn3, wardSaveBtn;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_profile_ward, container, false);

            ward1 = (EditText) rootView.findViewById(R.id.ward1);
            ward2 = (EditText) rootView.findViewById(R.id.ward2);
            ward3 = (EditText) rootView.findViewById(R.id.ward3);
            ward1_code = (EditText) rootView.findViewById(R.id.ward1_code);
            ward2_code = (EditText) rootView.findViewById(R.id.ward2_code);
            ward3_code = (EditText) rootView.findViewById(R.id.ward3_code);
            deleteBtn1 = (Button) rootView.findViewById(R.id.delete_btn1);
            deleteBtn2 = (Button) rootView.findViewById(R.id.delete_btn2);
            deleteBtn3 = (Button) rootView.findViewById(R.id.delete_btn3);
            wardSaveBtn = (Button) rootView.findViewById(R.id.ward_save_btn);

            ward1_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    newWard_code = ward1_code.getText().toString();
                    checkCode(1);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            ward2_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    newWard_code = ward2_code.getText().toString();
                    checkCode(2);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            ward3_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    newWard_code = ward3_code.getText().toString();
                    checkCode(3);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            deleteBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteWard(ward1.getText().toString().trim());
                }
            });

            deleteBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteWard(ward2.getText().toString().trim());
                }
            });

            deleteBtn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteWard(ward3.getText().toString().trim());
                }
            });

            wardSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ward1_code.getText().toString().isEmpty()) {
                        if(checkCodeFlag1) {
                            setProfile(0);
                        }
                    }
                    if (!ward2_code.getText().toString().isEmpty()) {
                        if(checkCodeFlag2) {
                            setProfile(1);
                        }
                    }
                    if (!ward3_code.getText().toString().isEmpty()) {
                        if(checkCodeFlag3) {
                            setProfile(2);
                        }
                    }
                }
            });
            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            authData = new SettingPreference("auth", getActivity());
            myPhone = authData.getPref("myPhone");
            token = authData.getPref("token");
            getProfile();
        }

        public void getProfile() {
            ProfileRequest profileRequest = new ProfileRequest(myPhone, token);
            Call<WardListResponse> wardListResponseCall = AppController.getHttpService().getWards(profileRequest);
            wardListResponseCall.enqueue(new Callback<WardListResponse>() {
                @Override
                public void onResponse(Call<WardListResponse> call, Response<WardListResponse> response) {
                    if(response.isSuccessful()) {
                        WardListResponse wardListResponse = response.body();
                        if(wardListResponse.isResult()) {
                            for(int i=0; i<3; i++) {
                                if(wardListResponse.getUsers().get(0) != null) {
                                    ward1.setText(wardListResponse.getUsers().get(0).getPhone());
                                }
                                if(wardListResponse.getUsers().get(1) != null) {
                                    ward2.setText(wardListResponse.getUsers().get(1).getPhone());
                                }
                                if(wardListResponse.getUsers().get(2) != null) {
                                    ward3.setText(wardListResponse.getUsers().get(2).getPhone());
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<WardListResponse> call, Throwable t) {

                }
            });
        }

        public void checkCode(final int location) {
            String phone = "";
            if(location == 1) {
                phone = ward1.getText().toString().trim();
            } else if(location == 2) {
                phone = ward2.getText().toString().trim();
            } else if(location == 3) {
                phone = ward3.getText().toString().trim();
            }
            CheckUserCode checkUserCode = new CheckUserCode(phone, newWard_code, token);
            Call<ResultResponse> resultResponseCall = AppController.getHttpService().checkUserCode(checkUserCode);
            resultResponseCall.enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                    if(response.isSuccessful()) {
                        ResultResponse resultResponse = response.body();
                        if(resultResponse.isResult()) {
                            if(location == 1) {
                                ward1_code.setTextColor(0xFF66CC99);
                                ward1_code.invalidate();
                                checkCodeFlag1 = true;
                            } else if(location == 2) {
                                ward2_code.setTextColor(0xFF66CC99);
                                ward2_code.invalidate();
                                checkCodeFlag2 = true;
                            } else if(location == 3) {
                                ward3_code.setTextColor(0xFF66CC99);
                                ward3_code.invalidate();
                                checkCodeFlag3 = true;
                            }
                        } else {
                            if(location == 1) {
                                ward1_code.setTextColor(0xFFFF0000);
                                ward1_code.invalidate();
                                checkCodeFlag1 = false;
                            } else if(location == 2) {
                                ward2_code.setTextColor(0xFFFF0000);
                                ward2_code.invalidate();
                                checkCodeFlag2 = false;
                            } else if(location == 3) {
                                ward3_code.setTextColor(0xFFFF0000);
                                ward3_code.invalidate();
                                checkCodeFlag3 = false;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {

                }
            });
        }

        public void setProfile(int i) {
            String phone = "";
            String code = "";
            if(i == 0) {
                phone = ward1.getText().toString().trim();
                code = ward1_code.getText().toString().trim();
            } else if(i == 1) {
                phone = ward2.getText().toString().trim();
                code = ward2_code.getText().toString().trim();
            } else if(i == 2) {
                phone = ward3.getText().toString().trim();
                code = ward3_code.getText().toString().trim();
            }
            UpdateWard updateWard = new UpdateWard(myPhone, phone, code, token);
            Call<ResultResponse> resultResponseCall = AppController.getHttpService().updateWards(updateWard);
            resultResponseCall.enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                    if(response.isSuccessful()) {
                        ResultResponse resultResponse = response.body();
                        if(resultResponse.isResult()) {
                            Toast.makeText(getActivity(), "프로필 설정 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "이미 등록되어있는 번호입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "네트워크 에러, 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void deleteWard(String phone) {
            DeleteWard deleteWard = new DeleteWard(myPhone, phone, token);
            Call<ResultResponse> resultResponseCall = AppController.getHttpService().deleteWard(deleteWard);
            resultResponseCall.enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                    if(response.isSuccessful()) {
                        ResultResponse resultResponse = response.body();
                        if(resultResponse.isResult()) {
                            Toast.makeText(getActivity(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "네트워크 에러, 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
