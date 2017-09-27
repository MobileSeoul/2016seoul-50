package com.example.choi.gohome.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.choi.gohome.R;
import com.example.choi.gohome.SettingPreference;
import com.example.choi.gohome.network.AppController;
import com.example.choi.gohome.network.request.ProfileRequest;
import com.example.choi.gohome.network.response.LocationReceiveRes;
import com.example.choi.gohome.network.response.Locations;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by choi on 2016-08-13.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mGoogleMap;
    private ProgressBar progressBar;
    private FloatingActionButton mFab;
    private Button resetBtn;
    private List<Locations> people = new ArrayList<>();
    private List<LatLng> latLng = new ArrayList<>();
    private int targetMember = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        mFab = (FloatingActionButton) findViewById(R.id.FAB);
        resetBtn = (Button) findViewById(R.id.reset_btn);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        progressBar.setVisibility(View.VISIBLE);

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

        getLocations();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomToMyLocation();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetMember = 0;
                progressBar.setVisibility(View.VISIBLE);
                mGoogleMap.clear();
                latLng.clear();
                getLocations();
            }
        });
    }

    public void getMemberMarker() {
        for(int i=0; i<people.size(); i++) {
            String stringDate = people.get(i).getMod_date();
            String recordDate = "";
            int n1 = stringDate.indexOf('T');
            int n2 = stringDate.lastIndexOf('.');
            if(n1 != -1 && n2 != -1) {
                recordDate = stringDate.substring(0, n1)+" ";
                recordDate += stringDate.substring(n1+1, n2);
            } else {
                recordDate = stringDate;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = dateFormat.parse(recordDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR_OF_DAY, 9);
                recordDate = dateFormat.format(calendar.getTime());
            } catch (ParseException ignored) {}

            latLng.add(new LatLng(people.get(i).getNowLat(), people.get(i).getNowLng()));
            mGoogleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .position(latLng.get(i))
                    .title(people.get(i).getName()+", 시간:"+recordDate));
        }
    }

    private void zoomToMyLocation() {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng.get(targetMember%people.size()), 15));
        targetMember++;
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void getLocations() {
        SettingPreference authData = new SettingPreference("auth", MapActivity.this);
        String myPhone = authData.getPref("myPhone");
        String token = authData.getPref("token");
        ProfileRequest profileRequest = new ProfileRequest(myPhone, token);
        Call<LocationReceiveRes> locationReceiveResCall = AppController.getHttpService().receiveLocation(profileRequest);
        locationReceiveResCall.enqueue(new Callback<LocationReceiveRes>() {
            @Override
            public void onResponse(Call<LocationReceiveRes> call, Response<LocationReceiveRes> response) {
                if(response.isSuccessful()) {
                    LocationReceiveRes locationReceiveRes = response.body();

                    for(int i=0; i<locationReceiveRes.getLocations().size(); i++) {
                        if(locationReceiveRes.getLocations().get(i) == null) {
                            break;
                        }
                        people.add(locationReceiveRes.getLocations().get(i));
                    }
                    getMemberMarker();
                    zoomToMyLocation();
                    Toast.makeText(MapActivity.this, "업데이트 성공", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(MapActivity.this, "보호할 피보호자를 등록하세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapActivity.this, GuardiansActivity.class);
                    intent.putExtra("ward", true);
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LocationReceiveRes> call, Throwable t) {
                Toast.makeText(MapActivity.this, "연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

        @Override
        public void onMyLocationChange(Location location) {
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            if(mGoogleMap != null) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14.0f));
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    /*private void renderMarkers(List<SearchResponse> responses) {
        for (SearchResponse r : responses) {
            LatLng latLng = new LatLng(r.getLat(), r.getLng());
            String status = r.getStatus();
            Long timeMillis = r.getCloseAt();
            Marker marker = null;

            if(status == null) {
                continue;
            }


            if (status.equals("OPEN")) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(r.getTitle())
                        .snippet(millisToTime(r.getOpenAt(), r.getCloseAt()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green))
                        .infoWindowAnchor(0.5f, 0.5f));
            } else if (status.equals("BEFORE_ONE_HOUR")){
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(r.getTitle())
                        .snippet(millisToTime(r.getOpenAt(), r.getCloseAt()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow))
                        .infoWindowAnchor(0.5f, 0.5f));
            } else if (status.equals("CLOSE")) {
                marker =   mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(r.getTitle())
                        .snippet(millisToTime(r.getOpenAt(), r.getCloseAt()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red))
                        .infoWindowAnchor(0.5f, 0.5f));
            } else {
                marker =  mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(r.getTitle())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_grey))
                        .snippet("정보없음")
                        .infoWindowAnchor(0.5f, 0.5f));
            }

            placeInfoMap.put(marker.getId(), r);


        }
    }*/

    @Override
    public void onLocationChanged(Location location) {

    }
}
