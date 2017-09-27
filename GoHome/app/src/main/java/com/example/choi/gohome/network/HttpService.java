package com.example.choi.gohome.network;

import android.support.annotation.NonNull;

import com.example.choi.gohome.network.request.CheckUserCode;
import com.example.choi.gohome.network.request.DeleteWard;
import com.example.choi.gohome.network.request.GcmTokenUpdate;
import com.example.choi.gohome.network.request.LocationUpdateReq;
import com.example.choi.gohome.network.request.LoginRequest;
import com.example.choi.gohome.network.request.Phone;
import com.example.choi.gohome.network.request.ProfileRequest;
import com.example.choi.gohome.network.request.UpdateProfile;
import com.example.choi.gohome.network.request.SendGcm;
import com.example.choi.gohome.network.request.UpdateGuard;
import com.example.choi.gohome.network.request.UpdatePW;
import com.example.choi.gohome.network.request.UpdateWard;
import com.example.choi.gohome.network.request.User;
import com.example.choi.gohome.network.request.WithdrawRequest;
import com.example.choi.gohome.network.response.ResultResponse;
import com.example.choi.gohome.network.response.GcmTokenReceive;
import com.example.choi.gohome.network.response.GcmTokenUpdateResult;
import com.example.choi.gohome.network.response.LocationReceiveRes;
import com.example.choi.gohome.network.response.LocationUpdateRes;
import com.example.choi.gohome.network.response.LoginResponse;
import com.example.choi.gohome.network.response.ProfileResponse;
import com.example.choi.gohome.network.response.RegisterResponse;
import com.example.choi.gohome.network.response.WardListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by choi on 2016-08-16.
 */
public interface HttpService {

    /**
     * 로그인
     * @param loginRequest(phone, pw)
     * @return result, message
     */
    @POST("users/login")
    Call<LoginResponse> login(@NonNull @Body LoginRequest loginRequest);

    /**
     * 회원가입
     * @param user
     * @return result, message
     */
    @POST("users/register")
    Call<RegisterResponse> register(@NonNull @Body User user);

    /**
     * 연락처 중복확인
     * @param myPhone
     * @return result
     */
    @GET("users/duplication")
    Call<ResultResponse> duplication(@NonNull @Query("myPhone") String myPhone);

    /**
     * 비밀번호 잃어버렸을 경우 - 이메일로 변경된 비밀번호 전송
     * @param myPhone
     * @return result
     */
    @GET("users/send-mail")
    Call<ResultResponse> sendMail(@NonNull @Query("myPhone") String myPhone);

    /**
     * 사용자 정보 요청
     * @param profileRequest{myPhone, token}
     * @return result, message, myProfile{myPhone, email, name, age, gender, phone1, phone2, phone3, guardians_phone1, guardians_phone2, guardians_phone3, user_code}
     */
    @POST("users/profile")
    Call<ProfileResponse> getProfile(@NonNull @Body ProfileRequest profileRequest);

    /**
     * user_code를 확인
     * @param checkUserCode{myPhone, user_code, token}
     * @return result
     */
    @POST("users/check-user-code")
    Call<ResultResponse> checkUserCode(@NonNull @Body CheckUserCode checkUserCode);

    /**
     * 피보호자 한명 삭제
     * @param deleteWard
     * @return result
     */
    @POST("users/ward-delete")
    Call<ResultResponse> deleteWard(@NonNull @Body DeleteWard deleteWard);

    /**
     * 피보호자 리스트 요청
     * @param profileRequest{myPhone, token}
     * @return result, users{phone}
     */
    @POST("users/ward-list")
    Call<WardListResponse> getWards(@NonNull @Body ProfileRequest profileRequest);

    /**
     * 보호자, 피보호자를 제외한 개인정보 수정
     * @param updateProfile{email, name, age, gender, myPhone, token}
     * @return result
     */
    @POST("users/profile-update")
    Call<ResultResponse> updateMyProfile(@NonNull @Body UpdateProfile updateProfile);

    /**
     * 보호자 리스트 수정
     * @param updateGuard{guardian_phone1, guardian_phone2, guardian_phone3, myPhone, token}
     * @return result
     */
    @POST("users/profile-update")
    Call<ResultResponse> updateGuardians(@NonNull @Body UpdateGuard updateGuard);

    /**
     * 피보호자 리스트 수정
     * @param updateWard{myPhone, phone, user_code, token} - 한명씩 추가
     * @return result
     */
    @POST("users/profile-update")
    Call<ResultResponse> updateWards(@NonNull @Body UpdateWard updateWard);

    /**
     * 비밀번호 수정
     * @param updatePW{myPhone, now_pw, new_pw, token}
     * @return result
     */
    @POST("users/profile-update")
    Call<ResultResponse> updatePassword(@NonNull @Body UpdatePW updatePW);

    /**
     * 사용자 위치 정보 업데이트
     * @param locationUpdateReq
     * @return locationUpdateRes
     */
    @POST("users/location")
    Call<LocationUpdateRes> setLocation(@NonNull @Body LocationUpdateReq locationUpdateReq);

    /**
     * 다른 사용자 위치 가져오기(보호자가 정보를 받아오는 api)
     * @param profileRequest
     * @return LocationReceiveRes
     */
    @POST("users/receive")
    Call<LocationReceiveRes> receiveLocation(@NonNull @Body ProfileRequest profileRequest);

    /**
     * 사용자 gcm token 서버 디비에 저장
     * @param gcmTokenUpdate
     * @return
     */
    @POST("users/gcm-token")
    Call<GcmTokenUpdateResult> setGcmToken(@NonNull @Body GcmTokenUpdate gcmTokenUpdate);

    /**
     * 가족들 gcm token 가져오기
     * @param myPhone
     * @return
     */
    @POST("users/gcm-receive")
    Call<GcmTokenReceive> getGcmTokenList(@NonNull @Body Phone myPhone);

    /**
     * GCM
     * @param sendGcm
     */
    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AIzaSyD8J4ZYrZuGtOjllg9JrDK2CsWXpnUL2iM"
    })
    @POST("https://gcm-http.googleapis.com/gcm/send")
    Call<Void> gcmAuth(@NonNull @Body SendGcm sendGcm);

    /**
     * 탈퇴
     * @param withdrawReq{myPhone, pw, token}
     * @return result
     */
    @POST("users/withdraw")
    Call<ResultResponse> withdrawFrom(@NonNull @Body WithdrawRequest withdrawReq);
}
