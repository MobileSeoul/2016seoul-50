package com.example.choi.gohome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choi.gohome.R;
import com.example.choi.gohome.SettingPreference;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

/**
 * Created by choi on 2016-08-02.
 */
public class RouteSettingActivity extends AppCompatActivity {

    private FloatingActionsMenu routeAddMenu;
    private FloatingActionButton routeAddAddress, routeAddMap;
    private boolean isFabOpen = false;
    private ScrollView routeScroll;
    private ListView routeListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> routeArrayList = new ArrayList<>();
    private SettingPreference authData;
    private AlertDialog.Builder alerBuilder, alerBuilder_dong;
    private CharSequence[] goo;
    //dong_1:강남, dong_2:강동, dong_3:강북, dong_4:강서, dong_5:관악, dong_6:광진, dong_7:구로구, dong_8:금천, dong_9:노원, dong_10:도봉, dong_11:동대문, dong_12:동작,
    //dong_13:마포, dong_14:서대문, dong_15:서초, dong_16:성동, dong_17:성북, dong_18:송파, dong_19:양천, dong_20:영등포, dong_21:용산, dong_22:은평, dong_23:종로,
    //dong_24:중(구), dong_25:중랑
    private CharSequence[] dong_1, dong_2, dong_3, dong_4, dong_5, dong_6, dong_7, dong_8, dong_9, dong_10, dong_11, dong_12, dong_13, dong_14, dong_15, dong_16,
            dong_17, dong_18, dong_19, dong_20, dong_21, dong_22, dong_23, dong_24, dong_25;
    CharSequence[] dong;
    private String address;
    private RelativeLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        routeScroll = (ScrollView) findViewById(R.id.route_scroll);
        routeListView = (ListView) findViewById(R.id.route_list);
        routeAddAddress = (FloatingActionButton) findViewById(R.id.route_add_address);
        routeAddMap = (FloatingActionButton) findViewById(R.id.route_add_map);
        routeAddMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        final CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.route_layout);

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

        authData = new SettingPreference("auth", RouteSettingActivity.this);

        for(int i=0; i<authData.getListPref("addressList"); i++) {
            routeArrayList.add(i, authData.getPref("addressList_"+i));
        }
        mAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item, R.id.list_text, routeArrayList);
        routeListView.setAdapter(mAdapter);

        goo = new CharSequence[]{"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};
        dong_1 = new CharSequence[]{"개포동", "논현동", "대치동", "도곡동", "삼성동", "세곡동", "수서동", "신사동", "압구정동",
                "역삼동", "율현동", "일원동", "자곡동", "청담동"};
        dong_2 = new CharSequence[]{"강일동", "고덕동", "길동", "둔촌동", "명일동", "상일동", "성내동", "암사동", "천호동"};
        dong_3 = new CharSequence[]{"미아동", "번동", "수유동", "유이동"};
        dong_4 = new CharSequence[]{"가양동", "개화동", "공항동", "과해동", "내발산동", "등촌동", "마곡동", "방화동", "염창동", "오곡동", "오쇠동", "외발산동", "화곡동"};
        dong_5 = new CharSequence[]{"남현동", "봉천동", "신림동"};
        dong_6 = new CharSequence[]{"광장동", "구의동", "군자동", "능동", "자양동", "중곡동", "화양동"};
        dong_7 = new CharSequence[]{"가리봉동", "개봉동", "고척동", "구로동", "궁동", "신도림동", "오류동", "온수동", "천왕동", "항동"};
        dong_8 = new CharSequence[]{"가산동", "독산동", "시흥동"};
        dong_9 = new CharSequence[]{"공릉동", "상계동", "월계동", "중계동", "하계동"};
        dong_10 = new CharSequence[]{"도봉동", "방학동", "쌍문동", "창동"};
        dong_11 = new CharSequence[]{"답십리동", "신설동", "용두동", "이문동", "장안동", "전농동", "제기동", "청량리동", "회기동", "휘경동"};
        dong_12 = new CharSequence[]{"노량진동" ,"대방동", "동작동", "본동", "사당동", "상도동", "신대방동", "흑석동"};
        dong_13 = new CharSequence[]{"공덕동", "구수동", "노고산동", "당인동", "대흥동", "도화동", "동교동", "마포동", "망원동", "상수동", "상암동", "서교동", "성산동", "신공덕동", "신수동", "신정동", "아현동", "연남동", "염리동", "용강동", "중동", "창전동", "토정동", "하중동", "합정동", "현석동"};
        dong_14 = new CharSequence[]{"남가좌동", "냉천동", "대신동", "대현동", "미근동", "봉원동", "북가좌동", "북아현동", "신촌동", "연희동", "영천동", "옥천동", "창천동", "천연동", "합동", "현저동", "홍은동", "홍제동", "충정로"};
        dong_15 = new CharSequence[]{"내곡동", "반포동", "방배동", "서초동", "신원동", "양재동", "염곡동", "우면동", "원지동", "잠원동"};
        dong_16 = new CharSequence[]{"금호동", "도선동", "마장동", "사근동", "상왕십리동", "성수동", "송정동", "옥수동", "용답동", "응봉동", "하왕십리동", "행당동", "홍익동"};
        dong_17 = new CharSequence[]{"길음동", "돈암동", "동선동", "동소문동", "보문동", "삼선동", "상월곡동", "석관동", "성북동", "안암동", "장위동", "정릉동", "종암동", "하월곡동"};
        dong_18 = new CharSequence[]{"가락동", "거여동", "마천동", "문정동", "방이동", "삼전동", "석촌동", "송파동", "신천동", "오금동", "잠실동", "장지동", "풍납동"};
        dong_19 = new CharSequence[]{"목동", "신월동", "신정동"};
        dong_20 = new CharSequence[]{"당산동", "대림동", "도림동", "문래동", "신길동", "양평동", "양화동", "여의도동", "영등포동"};
        dong_21 = new CharSequence[]{"갈월동", "남영동", "도원동", "동빙고동", "동자동", "문배동", "보광동", "산천동", "서계동", "서빙고동", "신계동", "신창동", "용문동", "용산동", "원효로", "이촌동", "이태원동", "주성동", "청암동", "청파동", "한강로", "한남동", "효창동", "후암동"};
        dong_22 = new CharSequence[]{"갈현동", "구산동", "녹번동", "대조동", "불광동", "수색동", "신사동", "역촌동", "응암동", "증산동", "진관동"};
        dong_23 = new CharSequence[]{"가회동", "견지동", "경운동", "계동", "공평동", "관수동", "관철동", "관훈동", "교남동", "교북동", "구기동", "궁정동", "권농동", "낙원동", "내수동", "내자동", "누상동", "누하동", "당주동", "도렴동", "돈의동", "동숭동", "명륜동", "묘동", "무악동", "봉익동", "부암동", "사간동", "사직동", "삼청동", "서린동", "세종로", "소격동", "송월동", "송현동", "수송동", "숭인동", "신교동", "신문로", "신영동", "안국동", "연건동", "연지동", "예지동", "옥인동", "와룡동", "운니동", "원남동", "원서동", "이화동", "익선동", "인사동", "인의동", "장사동", "재동", "적선동", "종로", "중학동", "창성동", "창신동", "청운동", "청진동", "체부동", "충신동", "통의동", "통인동", "팔판동", "평동", "평창동", "필운동", "행촌동", "혜화동", "홍지동", "홍파동", "화동", "효자동", "효제동", "훈정동"};
        dong_24 = new CharSequence[]{"광희동", "남대문로", "남산동", "남창동", "남학동", "다동", "만리동", "명동", "무교동", "무학동", "묵정동", "방산동", "봉래동", "북창동", "산림동", "삼각동", "서소문동", "소공동", "수표동", "수하동", "순화동", "신당동", "쌍림동", "예관동", "예장동", "오장동", "을지로", "의주로", "인현동", "입정동", "장교동", "장충동", "저동", "정동", "주교동", "주자동", "중림동", "초동", "충무로", "태평로", "필동", "황학동", "회현동", "흥인동", "충정로1가"};
        dong_25 = new CharSequence[]{"망우동", "면목동", "묵동", "상봉동", "신내동", "중화동"};

        alerBuilder = new AlertDialog.Builder(this);
        alerBuilder.setTitle("구를 선택하세요.")
                .setItems(goo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        address = (String) goo[which];
                        switch (which) {
                            case 0:
                                dong = dong_1;
                                break;
                            case 1:
                                dong = dong_2;
                                break;
                            case 2:
                                dong = dong_3;
                                break;
                            case 3:
                                dong = dong_4;
                                break;
                            case 4:
                                dong = dong_5;
                                break;
                            case 5:
                                dong = dong_6;
                                break;
                            case 6:
                                dong = dong_7;
                                break;
                            case 7:
                                dong = dong_8;
                                break;
                            case 8:
                                dong = dong_9;
                                break;
                            case 9:
                                dong = dong_10;
                                break;
                            case 10:
                                dong = dong_11;
                                break;
                            case 11:
                                dong = dong_12;
                                break;
                            case 12:
                                dong = dong_13;
                                break;
                            case 13:
                                dong = dong_14;
                                break;
                            case 14:
                                dong = dong_15;
                                break;
                            case 15:
                                dong = dong_16;
                                break;
                            case 16:
                                dong = dong_17;
                                break;
                            case 17:
                                dong = dong_18;
                                break;
                            case 18:
                                dong = dong_19;
                                break;
                            case 19:
                                dong = dong_20;
                                break;
                            case 20:
                                dong = dong_21;
                                break;
                            case 21:
                                dong = dong_22;
                                break;
                            case 22:
                                dong = dong_23;
                                break;
                            case 23:
                                dong = dong_24;
                                break;
                            case 24:
                                dong = dong_25;
                                break;
                        }
                        alerBuilder_dong = new AlertDialog.Builder(RouteSettingActivity.this);
                        alerBuilder_dong.setTitle("동을 선택하세요.")
                                .setItems(dong, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        address += " "+dong[which];
                                        setAddress();
                                    }
                                }).create().show();
                    }
                });
        final AlertDialog alertDialog = alerBuilder.create();

        routeListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                routeScroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        routeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(RouteSettingActivity.this);
                confirm.setMessage(routeArrayList.get(position)+"을(를) 삭제 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String deleteRegion = routeArrayList.get(position);
                                ArrayList<String> addressList = new ArrayList<>();
                                if(deleteRegion.equals(authData.getPref("addressList_"+position))) {
                                    authData.deletePref("addressList_"+position);
                                }
                                for(int i=0; i<authData.getListPref("addressList"); i++) {
                                    if(authData.getPref("addressList_"+i) != null) {
                                        addressList.add(authData.getPref("addressList_" + i));
                                    }
                                }
                                routeArrayList.clear();
                                mAdapter.clear();

                                listShow(addressList);
                                Toast.makeText(RouteSettingActivity.this, deleteRegion+"을(를) 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                routeScroll.invalidate();
                                routeScroll.requestLayout();
                                onStart();
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog confirmDialog = confirm.create();
                confirmDialog.show();
            }
        });

        routeAddMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                isFabOpen = true;
                layout.setBackgroundColor(Color.GRAY);
                if(layout != null) {
                    layout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            routeAddMenu.collapse();
                            return true;
                        }
                    });
                }
                if(routeListView != null) {
                    routeListView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            routeAddMenu.collapse();
                            return true;
                        }
                    });
                }
            }

            @Override
            public void onMenuCollapsed() {
                isFabOpen = false;
                layout.setBackgroundColor(Color.TRANSPARENT);
                layout.setOnTouchListener(null);
            }
        });

        routeAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeAddMenu.collapse();
                alertDialog.show();
            }
        });

        routeAddMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RouteSettingActivity.this, RouteSettingMapActivity.class));
                routeAddMenu.collapse();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView alert = new TextView(this);
        container = (RelativeLayout) findViewById(R.id.list_main);
        if(authData.getListPref("addressList") == 0) {
            alert.setText("등록된 경로가 없습니다.");
            alert.setTextSize(20);
            RelativeLayout.LayoutParams alertLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            alertLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
            alert.setLayoutParams(alertLayout);
            container.addView(alert);
        } else if(authData.getListPref("addressList") != 0) {
            container.removeView(alert);
        }
    }

    public void setAddress() {
        ArrayList<String> addressList = new ArrayList<>();
        for(int i=0; i<authData.getListPref("addressList"); i++) {
            addressList.add(authData.getPref("addressList_"+i));
        }
        if(addressList.contains(address)) {
            Toast.makeText(RouteSettingActivity.this, "이미 등록된 경로입니다.", Toast.LENGTH_SHORT).show();
        } else if(!addressList.contains(address)) {
            routeArrayList.clear();
            mAdapter.clear();
            container.removeAllViews();

            addressList.add(address);
            listShow(addressList);
            Toast.makeText(RouteSettingActivity.this, address+"을(를) 추가했습니다.", Toast.LENGTH_SHORT).show();
            routeScroll.invalidate();
            routeScroll.requestLayout();
            onStart();
        }
    }

    public void listShow(ArrayList<String> addressList) {
        authData.setListPref("addressList", addressList);
        for(int i=0; i<addressList.size(); i++) {
            routeArrayList.add(authData.getPref("addressList_"+i));
        }
        mAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item, R.id.list_text, routeArrayList);
        routeListView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        if(isFabOpen) {
            routeAddMenu.collapse();
        } else {
            super.onBackPressed();
        }
    }
}
