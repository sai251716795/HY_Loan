package com.pay.library.bean;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 保存程序异常退出时用户状态
 * 
 * @author Ding
 * 
 */
public class SaveUserState implements Serializable {
	public SaveUserState() {
	}

//	private SaveUserState(Parcel in) {
//		this.amtT0 = in.readString();
//		this.amtT1 = in.readString();
//		this.amtT1y = in.readString();
//		this.bindStatus = in.readInt();
//		// this.cardInfo
//		this.cardNum = in.readInt();
//		int temp = in.readInt();
//		if (temp == 0) {
//			login = true;
//		} else {
//			login = false;
//		}
//		// boolean[] temp = new boolean[] {};
//		// in.readBooleanArray(temp);
//		// this.login = temp[0];
//
//	}

//	public static final Parcelable.Creator<SaveUserState> CREATOR = new Parcelable.Creator<SaveUserState>() {
//		public SaveUserState createFromParcel(Parcel in) {
//			return new SaveUserState(in);
//		}
//
//		public SaveUserState[] newArray(int size) {
//			return new SaveUserState[size];
//		}
//	};
	/**
	 * 
	 */
	private static final long serialVersionUID = -2017774521443640789L;
	private boolean login;
	private String uAccount;
	private String uId;
	private String uName;
	private String sign;
	
	/**
	 * 认证状态（0未认证，1审核中，2审核通过，3审核不通过）
	 */
	private int uStatus;
	/**
	 * 银行卡数量
	 */
	private int cardNum;
	/**
	 * 已绑定数量
	 */
	private int termNum;

	/**
	 * pos绑定状态 0-未绑定 1-已绑定
	 */
	private int bindStatus;

	/**
	 * 即时到账余额
	 */
	private String amtT0;
	/**
	 * 未到账余额
	 */
	private String amtT1;

	/**
	 * 隔天到账余额
	 */
	private String amtT1y;
	/**
	 * 账户总余额
	 */
	private String totalAmt;

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public String getuAccount() {
		return uAccount;
	}

	public void setuAccount(String uAccount) {
		this.uAccount = uAccount;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getuStatus() {
		return uStatus;
	}

	public void setuStatus(int uStatus) {
		this.uStatus = uStatus;
	}

	public int getCardNum() {
		return cardNum;
	}

	public void setCardNum(int cardNum) {
		this.cardNum = cardNum;
	}

	public int getTermNum() {
		return termNum;
	}

	public void setTermNum(int termNum) {
		this.termNum = termNum;
	}

	public int getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(int bindStatus) {
		this.bindStatus = bindStatus;
	}

	public String getAmtT0() {
		return amtT0;
	}

	public void setAmtT0(String amtT0) {
		this.amtT0 = amtT0;
	}

	public String getAmtT1() {
		return amtT1;
	}

	public void setAmtT1(String amtT1) {
		this.amtT1 = amtT1;
	}

	public String getAmtT1y() {
		return amtT1y;
	}

	public void setAmtT1y(String amtT1y) {
		this.amtT1y = amtT1y;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(totalAmt);
//		dest.writeString(amtT0);
//		dest.writeString(amtT1);
//		dest.writeString(amtT1y);
//		dest.writeString(sign);
//		dest.writeString(uAccount);
//		dest.writeString(uId);
//		dest.writeString(uName);
//		dest.writeInt(bindStatus);
//		dest.writeInt(cardNum);
//		dest.writeInt(termNum);
//		dest.writeInt(uStatus);
//		// dest.writeInt(uStatus)
//		if (login)
//			dest.writeInt(0);
//		else {
//			dest.writeInt(1);
//		}
//		dest.writeBooleanArray(new boolean[] { login });
//
//	}
//
//	@Override
//	public int describeContents() {
//		// TODO 自动生成的方法存根
//		return 0;
//	}

}
