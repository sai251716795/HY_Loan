package com.yhx.loan.server;

import com.baidu.location.BDLocation;

/**
 * Created by 25171 on 2017/11/1.
 */

public interface LocationLister {
    void onReceiveLocation(HouseAddress houseAddress, BDLocation var1, GetLocation getLocation);

    void onConnectHotSpotMessage(String var1, int var2);
}
