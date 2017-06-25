package com.smartcampus.view.fragment.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.nostra13.universalimageloader.utils.L;
import com.smartcampus.R;
import com.smartcampus.util.MyLocationListener;
import com.smartcampus.view.fragment.BaseFragment;

/**
 * @author: vision
 * @function:
 * @date: 16/7/14
 */
public class BaiduMapFragment extends BaseFragment {

    private MapView bMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;

    public BaiduMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_layout,container,false);
        bMapView = (MapView) view.findViewById(R.id.bmapView);
        initView();
        return view;
    }
    private void initView() {
        mBaiduMap = bMapView.getMap();
        //声明LocationClient类
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(new MyLocationListener(mBaiduMap));
        initLocation();
        //开启定位
        mLocationClient.start();
        L.e("开始定位");
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        ////可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        int span = 0;
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在fragment执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        bMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在fragment执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        bMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在fragment执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        bMapView.onPause();
    }
}
