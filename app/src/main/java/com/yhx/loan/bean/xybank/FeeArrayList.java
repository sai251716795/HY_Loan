package com.yhx.loan.bean.xybank;

import java.io.Serializable;

/**
 * Created by 25171 on 2018/5/11.
 */

public class FeeArrayList implements Serializable{
  private   FeeArray  struct;

    public FeeArray getStruct() {
        return struct;
    }

    public void setStruct(FeeArray struct) {
        this.struct = struct;
    }
}
