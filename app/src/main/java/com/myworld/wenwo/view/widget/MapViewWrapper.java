package com.myworld.wenwo.view.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jianglei on 16/7/20.
 */

public class MapViewWrapper extends FrameLayout implements BDLocationListener, TextWatcher, OnGetSuggestionResultListener, BaseQuickAdapter.OnRecyclerViewItemChildClickListener, BaseQuickAdapter.OnRecyclerViewItemClickListener, BaiduMap.OnMarkerDragListener, BaiduMap.OnMapClickListener, OnGetGeoCoderResultListener {
    private MapView mapView;
    private EditText editText, detailText;
    private LocationClient locationClient;
    private GeoCoder geoCoder;
    private SuggestionSearch search;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private String address;
    private String simpleAddress;
    private String detailAddress;
    private double geoX, geoY;
    private ImageView locationImage;
    private BDLocation location;
    private BaiduMap map;

    public MapViewWrapper(Context context) {
        this(context, null);
    }

    public MapViewWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapViewWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
        Log.d(TAG, "MapViewWrapper: init");
    }

    private static final String TAG = "MapViewWrapper";

    private void initUI() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        FrameLayout root = (FrameLayout) inflater.inflate(R.layout.map_view_layout, this, true);
        mapView = (MapView) root.findViewById(R.id.map_view);
        editText = (EditText) root.findViewById(R.id.edit_text);
        detailText = (EditText) root.findViewById(R.id.detail_edit);
        locationImage = (ImageView) root.findViewById(R.id.location_image);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerAdapter(getContext(), new ArrayList<SuggestionResult.SuggestionInfo>());
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(GONE);
        adapter.setOnRecyclerViewItemChildClickListener(this);
        adapter.setOnRecyclerViewItemClickListener(this);
        locationClient = new LocationClient(getContext());
        locationClient.registerLocationListener(this);
        initLocation();
        locationClient.start();
        editText.addTextChangedListener(this);
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        search = SuggestionSearch.newInstance();
        map = mapView.getMap();
        search.setOnGetSuggestionResultListener(this);
        mapView.getMap().setOnMarkerDragListener(this);
        mapView.getMap().setOnMapClickListener(this);
        mapView.getMap().setMaxAndMinZoomLevel(21, 13);
        locationImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location == null)
                    return;
                LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng latLng2 = new LatLng(location.getLatitude(), location.getLongitude());
                map.setMapStatusLimits(new LatLngBounds.Builder().include(latLng1).include(latLng2).build());
            }
        });
        detailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(address))
                    return;
                try {
                    JSONObject addressObject = new JSONObject(address);
                    addressObject.put("adetail", s.toString());
                    address = addressObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                simpleAddress += s.toString();
                if (listener != null)
                    listener.onChanged();
            }
        });
    }


    public double getGeoX() {
        return Config.mark_geo_x;
    }

    public void setGeoX(double geoX) {
        this.geoX = geoX;
        Config.mark_geo_x = geoX;
    }

    public double getGeoY() {
        return Config.mark_geo_y;
    }

    public void setGeoY(double geoY) {
        this.geoY = geoY;
        Config.mark_geo_y = geoY;
    }

    public String getAddress() {
        return address;
    }

    public String getSimpleAddress() {
        return simpleAddress;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private AddressChangedListener listener;

    public AddressChangedListener getListener() {
        return listener;
    }

    public void setListener(AddressChangedListener listener) {
        this.listener = listener;
    }

    public String getDetailAddress() {
        detailAddress = detailText.getText().toString();
        return detailAddress;
    }

    public void setSimpleAddress(String simpleAddress) {
        this.simpleAddress = simpleAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
        detailText.setText(detailAddress);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        BaiduMap map = mapView.getMap();
        map.setMyLocationEnabled(true);
        location = bdLocation;
        MyLocationData locationData = new MyLocationData.Builder().accuracy(bdLocation.getRadius()).direction(0)
                .latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).build();
        map.setMyLocationData(locationData);
        map.getUiSettings().setCompassEnabled(true);
        LatLng latLng1 = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        LatLng latLng2 = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        map.setMapStatusLimits(new LatLngBounds.Builder().include(latLng1).include(latLng2).build());
        if (TextUtils.isEmpty(address)) {
            initAddressFromLocation(bdLocation);
        }
    }

    public void initAddressFromLocation(BDLocation bdLocation) {
        if (bdLocation == null)
            return;
        LatLng point = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        Config.mark_geo_x = point.latitude;
        Config.mark_geo_y = point.longitude;
        Log.d("init geoX", "" + Config.mark_geo_x);
        Log.d("init geoY", "" + Config.mark_geo_y);
        if (marker == null) {
            //定义Maker坐标点

            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_position);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap).zIndex(0).draggable(true);
            //在地图上添加Marker，并显示
            marker = (Marker) map.addOverlay(option);
            Log.d("scale", map.getMapStatus().zoom + "");
        } else {
            marker.setPosition(point);
        }
        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
        option.location(point);
        geoCoder.reverseGeoCode(option);
    }

    public void onActivityDestory(Activity activity) {
        locationClient.stop();
    }

    public void initWithPosition(double geoX, double geoY) {
        BDLocation bdLocation = new BDLocation();
        bdLocation.setLatitude(geoX);
        bdLocation.setLongitude(geoY);
        initAddressFromLocation(bdLocation);
    }


    private Marker marker;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() > 0) {
            search.requestSuggestion(new SuggestionSearchOption().city("成都").keyword(s.toString()));
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null)
            return;
        List<SuggestionResult.SuggestionInfo> suggestionInfos = suggestionResult.getAllSuggestions();
        Iterator<SuggestionResult.SuggestionInfo> iterator = suggestionInfos.iterator();
        while (iterator.hasNext()) {
            SuggestionResult.SuggestionInfo info = iterator.next();
            if (info.pt == null)
                iterator.remove();
        }
        recyclerView.setVisibility(VISIBLE);
        adapter.setNewData(suggestionInfos);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

    }

    @Override
    public void onItemClick(View view, int i) {
        recyclerView.setVisibility(GONE);
        SuggestionResult.SuggestionInfo info = (SuggestionResult.SuggestionInfo) adapter.getData().get(i);
        BDLocation bdLocation = new BDLocation();
        bdLocation.setLatitude(info.pt.latitude);
        bdLocation.setLongitude(info.pt.longitude);
        Address address = new Address.Builder().city(info.city).district(info.district).build();
        bdLocation.setAddr(address);
        initAddressFromLocation(bdLocation);
        editText.setText("");
        editText.setHint(info.key);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng point) {
        Log.d(TAG, "onMapClick: ");

        Config.mark_geo_x = point.latitude;
        Config.mark_geo_y = point.longitude;
        Log.d("onMapClick geoX", "" + Config.mark_geo_x);
        Log.d("onMapClick geoY", "" + Config.mark_geo_y);
        if (marker == null) {
            //定义Maker坐标点

            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_position);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap).zIndex(0).draggable(true);
            //在地图上添加Marker，并显示
            marker = (Marker) map.addOverlay(option);
        } else {
            marker.setPosition(point);
        }
        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
        option.location(point);
        geoCoder.reverseGeoCode(option);
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    protected void onAttachedToWindow() {
        if (locationClient != null)
            locationClient.start();
        mapView.onResume();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (marker != null)
            marker.remove();
        if (locationClient.isStarted())
            locationClient.stop();
        marker = null;
        mapView.onPause();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        Log.d("pos", reverseGeoCodeResult.getAddress());
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR)
            return;

        simpleAddress = reverseGeoCodeResult.getAddress() + getDetailAddress();
        editText.setText("");
        editText.setHint(reverseGeoCodeResult.getAddress());
        Config.mark_geo_x = reverseGeoCodeResult.getLocation().latitude;
        Config.mark_geo_y = reverseGeoCodeResult.getLocation().longitude;
        Log.d("received geoX", "" + Config.mark_geo_x);
        Log.d("received geoY", "" + Config.mark_geo_y);
        try {
            JSONObject addressObject = new JSONObject();
            addressObject.put("province", reverseGeoCodeResult.getAddressDetail().province);
            addressObject.put("city", reverseGeoCodeResult.getAddressDetail().city);
            addressObject.put("district", reverseGeoCodeResult.getAddressDetail().district);
            addressObject.put("township", reverseGeoCodeResult.getAddressDetail().street);
            addressObject.put("detail", reverseGeoCodeResult.getAddressDetail().streetNumber);
            addressObject.put("adetail", getDetailAddress());
            address = addressObject.toString();
            if (listener != null)
                listener.onChanged();
            Log.d("address json", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAddressJson() {
        return address;
    }

    class RecyclerAdapter extends BaseQuickAdapter<SuggestionResult.SuggestionInfo> {


        public RecyclerAdapter(Context context, ArrayList<SuggestionResult.SuggestionInfo> data) {
            super(context, R.layout.search_result_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, SuggestionResult.SuggestionInfo s) {
            baseViewHolder.setText(R.id.text, s.key).setText(R.id.district, s.city + s.district);
        }


    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    public interface AddressChangedListener {
        public void onChanged();
    }
}
