package com.example.choi.gohome;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.choi.gohome.network.AppController;
import com.example.choi.gohome.network.request.LocationUpdateReq;
import com.example.choi.gohome.network.response.LocationUpdateRes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by choi on 2016-07-28.
 */
public class GpsInfo extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static volatile GoogleApiClient mGoogleApiClient = null;
    private static volatile LocationRequest mLocationRequest = null;
    private Location mLocation = null;
    private Handler mHandler;
    private Context mContext;
    private double lat = 0.0, lng = 0.0;
    private boolean isConnected = false;
    private List<String> addressList = new ArrayList<>();
    private SettingPreference authData;
    private String currentLocationAddress = "";
    private SettingPreference pref;
    private boolean uploadPref;

    public GpsInfo() {
    }

    public GpsInfo(Activity context, Handler handler) {
        this.mHandler = handler;
        this.mContext = context;
        if (mGoogleApiClient == null) {
            synchronized (GpsInfo.class) {
                if(mGoogleApiClient == null) {
                    mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();
                }
            }
        }

        if (mLocationRequest == null) {
            synchronized (GpsInfo.class) {
                if(mLocationRequest == null) {
                    mLocationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(1000*60)           //10초
                            .setFastestInterval(1000);      //1초
                }
            }
        }
        pref = new SettingPreference("setting", (Activity) mContext);
        uploadPref = pref.getFlagPref("uploadPref");
//        mGoogleApiClient.connect();
    }

    public void connectStart() {
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        mGoogleApiClient = null;
        mLocationRequest = null;
        mLocation = null;
        mHandler = null;
        mContext = null;
    }

    public void gpsSend() {
        new Thread() {
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = new MyLatLng(getLat(), getLng(), isConnected());
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    public void addressSend() {
        new Thread() {
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = currentLocationAddress;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult((Activity) mContext, 10000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("connection", "Location connection service failed");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void searchNewLocation(Location location) {
        mLocation = location;
        lat = mLocation.getLatitude();
        lng = mLocation.getLongitude();
        this.isConnected = true;

        gpsSend();
        if(uploadPref) {    //업로드 설정을 on한 경우만 위치를 upload
            updateLocation(lat, lng);
        }
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public double getLat() {
        if(mLocation != null) {
            lat = mLocation.getLatitude();
        }
        return lat;
    }
    public double getLng() {
        if(mLocation != null) {
            lng = mLocation.getLongitude();
        }
        return lng;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        searchNewLocation(mLocation);
    }

    public void updateLocation(double lat, double lng) {
        authData = new SettingPreference("auth", (Activity) mContext);
        String myPhone = authData.getPref("myPhone");
        String token = authData.getPref("token");
        LocationUpdateReq locationUpdateReq = new LocationUpdateReq(myPhone, lat, lng, token);

        Call<LocationUpdateRes> locationResCall = AppController.getHttpService().setLocation(locationUpdateReq);
        locationResCall.enqueue(new Callback<LocationUpdateRes>() {
            @Override
            public void onResponse(Call<LocationUpdateRes> call, Response<LocationUpdateRes> response) {
                if(response.isSuccessful()) {
                    LocationUpdateRes locationUpdateRes = response.body();
                    if (locationUpdateRes.isResult()) {
                        Log.e("업데이트 결과", locationUpdateRes.getMessage());
                    } else if (!locationUpdateRes.isResult()) {
                        Toast.makeText(mContext, "위치정보 업데이트 실패", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "위치정보 업데이트 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocationUpdateRes> call, Throwable t) {
                Toast.makeText(mContext, "연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getAddress(Activity context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        /*List<Address> address;
        try {
            if(geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if(address != null && address.size() > 0) {
                    currentLocationAddress = address.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            searchNewLocation(mLocation);
        }*/
        try {
            Iterator addresses = geocoder.getFromLocation(lat, lng, 1).iterator();
            while(addresses.hasNext()) {
                Address addressName = (Address) addresses.next();
                String country = addressName.getCountryName();
                String placeName = addressName.getLocality();   //특별시에서는 '구', 도에서는 '시'구역
                String goo = addressName.getSubLocality();      //도에서는 '구'
                String road = addressName.getThoroughfare();    //동
                String featureName = addressName.getFeatureName();
                currentLocationAddress = placeName+" "+road;
            }
        } catch (IOException ignored) {
        }
        addressSend();
        return currentLocationAddress;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
