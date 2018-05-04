package com.yhx.loan.server;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by administor on 2017/9/27.
 */

public class HouseAddress {
    private HouseAddress() {

    }

    private static final HouseAddress INSTANCE = new HouseAddress();

    private static class LazyHolder {
        public static HouseAddress getThis() {
            return INSTANCE;
        }
    }

    public static final HouseAddress getInstance() {
        return LazyHolder.getThis();
    }

    public String country = "china";// 国家名称
    public String countryCode;// 国家码
    public String province = "";//省
    public String city = "";// 城市
    public String cityCode;// 城市编码
    public String district = "";// 区
    public String street ="";// 街道
    public String streetNumber;// 街道编号
    public String address =""; //地址信息

    public String mTime;//时间
    public int mLocType;// 定位类型

    public double mLatitude;// 纬度
    public double mLongitude;// 经度
    public float mRadius; //半径

    public String mDescription;//定位类型说明
    public String mSemaAptag;//位置义化信息

    public Address dbAddress;
    public List<Poi> mPoiList;
    public BDLocation dbLocation;

    @Override
    public String toString() {

        String str1 = "HouseAddress{" +
//                "\n mTime='" + mTime + '\'' +
//                "\n mDescription='" + mDescription + '\'' +
//                "\n mLocType=" + mLocType +
//                "\n mLatitude=" + mLatitude +
//                "\n mLongitude=" + mLongitude +
//                "\n mRadius=" + mRadius +
//                "\n country='" + country + '\'' +
//                "\n countryCode='" + countryCode + '\'' +
//                "\n province='" + province + '\'' +
//                "\n city='" + city + '\'' +
//                "\n cityCode='" + cityCode + '\'' +
//                "\n district='" + district + '\'' +
//                "\n street='" + street + '\'' +
//                "\n streetNumber='" + streetNumber + '\'' +
                "\n address='" + address + '\'' +
//                "\n mSemaAptag='" + mSemaAptag + '\'' +
                '}';
        String str2 = "poi:";
//        if (mPoiList != null && !mPoiList.isEmpty()) {
//            for (int i = 0; i < mPoiList.size(); i++) {
//                Poi poi = (Poi) mPoiList.get(i);
//                str2 += poi.getName() + ";";
//            }
//        }
        return str1 + "\n" + str2 + "\n";
    }
}
