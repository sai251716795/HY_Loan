package com.yhx.loan.server;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.yhx.loan.base.MyApplication;


/**
 * Created by 25171 on 2017/10/30.
 */

public class GetLocation {
    private static final GetLocation INSTANCE = new GetLocation();

    private static class LazyHolder {
        public static GetLocation getThis() {
            return INSTANCE;
        }
    }

    public static final GetLocation getInstance() {
        return LazyHolder.getThis();
    }

    public GetLocation() {

    }

    public LocationService locationService = null;

    public GetLocation StartLocation() {

        locationService = MyApplication.getInstance().locationService;
        //注册监听
        locationService.registerListener(BDLocationListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        locationService.setLocationOption(locationService.getOption());
        // 开启定位SDK
        locationService.start();
        return this;
    }

    //停止服务
    public void stopServer(LocationLister locationLister) {
        locationService.unregisterListener(BDLocationListener);
        locationService.stop(); //停止定位服务
        locationLister = null;
    }

    private com.baidu.location.BDLocationListener BDLocationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            HouseAddress houseAddress = WriteLocation.RecordLocation(location);//将时时监听到的数据保存到单列中
            String city = houseAddress.city;
            if (!"".equals(city)) {
                //将的
                if (locationLister != null)
                    locationLister.onReceiveLocation(houseAddress, location, GetLocation.this);
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
            if (locationLister != null) locationLister.onConnectHotSpotMessage(s, i);
        }
    };

    public LocationLister locationLister;

    public void Receive(LocationLister locationLister) {
        this.locationLister = locationLister;
    }
}
