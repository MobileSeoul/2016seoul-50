package com.example.choi.gohome.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.choi.gohome.LatLngSize;
import com.example.choi.gohome.R;
import com.example.choi.gohome.SQLiteHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choi on 2016-09-07.
 * google maps에서 터치한 좌표로 안전 경로를 설정하는 액티비티
 */
public class RouteSettingMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mGoogleMap;
    private Location location;
    private ProgressBar progressBar;
    private Button saveBtn;
    private FloatingActionButton mFab;
    private PolylineOptions polylineOptions;
    private List<LatLng> arrayPoints = new ArrayList<>();
    private List<Integer> safetyZone = new ArrayList<>();
    private List<LatLngSize> latLngSize = new ArrayList<>();
    private int errCnt = 0, errCnt2 = 0, order = 0, newOrder = 0;
    private SQLiteHandler sqLiteHandler;
    private boolean searchFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        searchFlag = true;
        saveBtn = (Button) findViewById(R.id.save_btn);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        mFab = (FloatingActionButton) findViewById(R.id.FAB);
        MapsInitializer.initialize(getApplicationContext());

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

        sqLiteHandler = new SQLiteHandler(RouteSettingMapActivity.this);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                errCnt = 0;
                zoomToMyLocation();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                polylineOptions = new PolylineOptions();
//                polylineOptions.addAll(arrayPoints);
//                mGoogleMap.addPolyline(polylineOptions);
                if(newOrder > 0) {
                    for (int i = 0; i < arrayPoints.size(); i++) {
                        double lat = arrayPoints.get(i).latitude;
                        double lng = arrayPoints.get(i).longitude;
                        int size = safetyZone.get(i);
                        sqLiteHandler.insert("insert into map_route(lat, lng, size) values(" + lat + ", " + lng + ", " + size + ");");
                    }
                    Toast.makeText(RouteSettingMapActivity.this, "경로가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    arrayPoints.clear();
                    safetyZone.clear();
                    newOrder = 0;
                } else {
                    Toast.makeText(RouteSettingMapActivity.this, "먼저 경로를 지정하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(location == null) {
                    zoomToMyLocation();
                }
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(latLngSize.isEmpty()) {
                    getRouteDB();
                }
            }
        }, 1000);
    }



    private void zoomToMyLocation() {
        try {
            if(errCnt >= 5) {
                Toast.makeText(RouteSettingMapActivity.this, "GPS환경이 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                LatLng seoulStation = new LatLng(37.555172, 126.970777);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seoulStation, 15)); //서울역
                progressBar.setVisibility(View.INVISIBLE);
                searchFlag = false;
            } else {
                location = mGoogleMap.getMyLocation();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
                progressBar.setVisibility(View.INVISIBLE);
                searchFlag = false;
            }
        } catch (Exception e) {
            errCnt++;
            onResume();
        }
    }

    private GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            if (mGoogleMap != null) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17.0f));
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        enableMyLocation();
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mGoogleMap != null) {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLatitude();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17));
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setTitle("안전구역(m단위)").setMessage("50미터 이상으로 설정 가능합니다.\n(GPS오차범위 최대 25m)")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value;
                        int size;
                        try {
                            value = input.getText().toString().trim();
                            if(!value.equals("")) {
                                size = Integer.parseInt(value);
                            } else {
                                size = 50;
                            }
                        } catch (NumberFormatException nfe) {
                            size = 50;
                        } catch (NullPointerException e) {
                            size = 50;
                        }
                        if(size >= 50) {
                            arrayPoints.add(latLng);
                            safetyZone.add(size);
                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                    .position(latLng));
                            marker.setSnippet(String.valueOf("_"+newOrder));
                            newOrder++;
                            mGoogleMap.addCircle(new CircleOptions()
                                    .center(latLng)
                                    .radius(size))
                                    .setStrokeColor(Color.parseColor("#5587cefa"));
                        } else {
                            Toast.makeText(RouteSettingMapActivity.this, "최소 50미터 이상 설정 가능합니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alert.show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("전체 경로를 삭제 하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGoogleMap.clear();
                        arrayPoints.clear();
                        if (!latLngSize.isEmpty()) {
                            sqLiteHandler.delete("delete from map_route;");
                        }
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("선택한 경로를 삭제 하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGoogleMap.clear();
                        order = 0;
                        newOrder = 0;
                        String strNum = marker.getSnippet().substring(0, 1);

                        if(strNum.equals("_")) {
                            //최종 저장하기 이전 리스트
                            if (!arrayPoints.isEmpty() && !safetyZone.isEmpty()) {
                                String _num = marker.getSnippet().substring(1);
                                int num = Integer.parseInt(_num);
                                arrayPoints.remove(num);
                                safetyZone.remove(num);
                                for (int i = 0; i < arrayPoints.size(); i++) {
                                    LatLng latLng = new LatLng(arrayPoints.get(i).latitude, arrayPoints.get(i).longitude);
                                    Marker newMarker = mGoogleMap.addMarker(new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                            .position(latLng));
                                    newMarker.setSnippet(String.valueOf(order));
                                    order++;
                                    mGoogleMap.addCircle(new CircleOptions()
                                            .center(latLng)
                                            .radius(safetyZone.get(i)))
                                            .setStrokeColor(Color.parseColor("#5587cefa"));
                                }
                                getRouteDB();
                            }
                        } else if(!strNum.equals("_")) {
                            if (!arrayPoints.isEmpty() && !safetyZone.isEmpty()) {
                                for (int i = 0; i < arrayPoints.size(); i++) {
                                    LatLng latLng = new LatLng(arrayPoints.get(i).latitude, arrayPoints.get(i).longitude);
                                    Marker newMarker = mGoogleMap.addMarker(new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                            .position(latLng));
                                    newMarker.setSnippet(String.valueOf(order));
                                    order++;
                                    mGoogleMap.addCircle(new CircleOptions()
                                            .center(latLng)
                                            .radius(safetyZone.get(i)))
                                            .setStrokeColor(Color.parseColor("#5587cefa"));
                                }
                            }
                            //디비에서 불러온 저장된 리스트
                            if (!latLngSize.isEmpty()) {
                                int num = Integer.parseInt(marker.getSnippet());
                                double lat = latLngSize.get(num).getLat();
                                double lng = latLngSize.get(num).getLng();
                                sqLiteHandler.delete("delete from map_route where lat=" + lat + " AND lng=" + lng + ";");
                                getRouteDB();
                            }
                        }
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
        return false;
    }

    public void getRouteDB() {
        try {
            latLngSize = new ArrayList<>();
            if(errCnt2 <= 10) {
                for (int i = 0; i < sqLiteHandler.selectAll().size(); i++) {
                    latLngSize.add(sqLiteHandler.selectAll().get(i));
                }
                for (int i = 0; i < latLngSize.size(); i++) {
                    LatLng latLng = new LatLng(latLngSize.get(i).getLat(), latLngSize.get(i).getLng());
                    Marker newMarker = mGoogleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                            .position(latLng));
                    newMarker.setSnippet(String.valueOf(order));
                    order++;
                    mGoogleMap.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(latLngSize.get(i).getSize()))
                            .setStrokeColor(Color.parseColor("#5587cefa"));
                }
            } else {
                Toast.makeText(RouteSettingMapActivity.this, "저장된 경로가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            errCnt2++;
            onResume();
        }
    }

    @Override
    public void onBackPressed() {
        if(searchFlag) {
            //처음 Map을 키자마자 바로 종료할 수 없음
        } else {
            super.onBackPressed();
        }
    }
}
