package com.yhx.loan.bean.xybank;

/**
 * 还款计划
 */

import java.io.Serializable;

public class XYRepayPlan implements Serializable{
	private String transSeq; 			// 交易流水号3
	private String psPerdNo; 			// 期号1
	private String psDueDt; 			// 到期日2
	private Double psInstmAmt; 			// 期供金额4
	private Double psPrcpAmt; 			// 本金5
	private Double psNormIntAmt; 		// 利息6
	private Double psOdIntAmt; 			// 罚息7
	private Integer psCommOdInt; 		// 复利
	private Double psRemPrcp; 			// 剩余本金
	private Double setlPrc; 			// 已还本金
	private Double setlNormInt; 		// 已还利息
	private Double setlOdIntAmt; 		// 已还罚息
	private Double setlCommOdInt; 		// 已还复利
	private Double psFee; 				// 应还滞纳金
	private Double setlFee; 			// 已还滞纳金
	private Double psFeeAmt2; 			// 应还账户管理费
	private Double setlFeeAmt2; 		// 已还账户管理费
	private Double rdu01amt; 			// 已减免账户管理费
	private Double psWvNmInt; 			// 减免利息
	private Double psWvOdInt; 			// 减免罚息
	private Double psWvCommInt; 		// 减免复利
	private Double feeAmt; 				// 分期手续费
	private Double setlFeeAmt; 			// 已还分期手续费
	private Double rdu06amt; 			// 减免手续费
	private String setlInd; 			// 结清标志
	private Double setlCommAmt; 		// 已还佣金
	private Double psCommAmt; 			// 应还佣金
	private String sysUpdateTime; 		// 当前时间
	private String customerName; 		// 还款人姓名
	private String beginDate; 			//
	private String endDate; 			//

	public String getTransSeq() {
		return transSeq;
	}

	public void setTransSeq(String transSeq) {
		this.transSeq = transSeq;
	}

	public String getPsPerdNo() {
		return psPerdNo;
	}

	public void setPsPerdNo(String psPerdNo) {
		this.psPerdNo = psPerdNo;
	}

	public String getPsDueDt() {
		return psDueDt;
	}

	public void setPsDueDt(String psDueDt) {
		this.psDueDt = psDueDt;
	}

	public Double getPsInstmAmt() {
		return psInstmAmt;
	}

	public void setPsInstmAmt(Double psInstmAmt) {
		this.psInstmAmt = psInstmAmt;
	}

	public Double getPsPrcpAmt() {
		return psPrcpAmt;
	}

	public void setPsPrcpAmt(Double psPrcpAmt) {
		this.psPrcpAmt = psPrcpAmt;
	}

	public Double getPsNormIntAmt() {
		return psNormIntAmt;
	}

	public void setPsNormIntAmt(Double psNormIntAmt) {
		this.psNormIntAmt = psNormIntAmt;
	}

	public Double getPsOdIntAmt() {
		return psOdIntAmt;
	}

	public void setPsOdIntAmt(Double psOdIntAmt) {
		this.psOdIntAmt = psOdIntAmt;
	}

	public Integer getPsCommOdInt() {
		return psCommOdInt;
	}

	public void setPsCommOdInt(Integer psCommOdInt) {
		this.psCommOdInt = psCommOdInt;
	}

	public Double getPsRemPrcp() {
		return psRemPrcp;
	}

	public void setPsRemPrcp(Double psRemPrcp) {
		this.psRemPrcp = psRemPrcp;
	}

	public Double getSetlPrc() {
		return setlPrc;
	}

	public void setSetlPrc(Double setlPrc) {
		this.setlPrc = setlPrc;
	}

	public Double getSetlNormInt() {
		return setlNormInt;
	}

	public void setSetlNormInt(Double setlNormInt) {
		this.setlNormInt = setlNormInt;
	}

	public Double getSetlOdIntAmt() {
		return setlOdIntAmt;
	}

	public void setSetlOdIntAmt(Double setlOdIntAmt) {
		this.setlOdIntAmt = setlOdIntAmt;
	}

	public Double getSetlCommOdInt() {
		return setlCommOdInt;
	}

	public void setSetlCommOdInt(Double setlCommOdInt) {
		this.setlCommOdInt = setlCommOdInt;
	}

	public Double getPsFee() {
		return psFee;
	}

	public void setPsFee(Double psFee) {
		this.psFee = psFee;
	}

	public Double getSetlFee() {
		return setlFee;
	}

	public void setSetlFee(Double setlFee) {
		this.setlFee = setlFee;
	}

	public Double getPsFeeAmt2() {
		return psFeeAmt2;
	}

	public void setPsFeeAmt2(Double psFeeAmt2) {
		this.psFeeAmt2 = psFeeAmt2;
	}

	public Double getSetlFeeAmt2() {
		return setlFeeAmt2;
	}

	public void setSetlFeeAmt2(Double setlFeeAmt2) {
		this.setlFeeAmt2 = setlFeeAmt2;
	}

	public Double getRdu01amt() {
		return rdu01amt;
	}

	public void setRdu01amt(Double rdu01amt) {
		this.rdu01amt = rdu01amt;
	}

	public Double getPsWvNmInt() {
		return psWvNmInt;
	}

	public void setPsWvNmInt(Double psWvNmInt) {
		this.psWvNmInt = psWvNmInt;
	}

	public Double getPsWvOdInt() {
		return psWvOdInt;
	}

	public void setPsWvOdInt(Double psWvOdInt) {
		this.psWvOdInt = psWvOdInt;
	}

	public Double getPsWvCommInt() {
		return psWvCommInt;
	}

	public void setPsWvCommInt(Double psWvCommInt) {
		this.psWvCommInt = psWvCommInt;
	}

	public Double getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(Double feeAmt) {
		this.feeAmt = feeAmt;
	}

	public Double getSetlFeeAmt() {
		return setlFeeAmt;
	}

	public void setSetlFeeAmt(Double setlFeeAmt) {
		this.setlFeeAmt = setlFeeAmt;
	}

	public Double getRdu06amt() {
		return rdu06amt;
	}

	public void setRdu06amt(Double rdu06amt) {
		this.rdu06amt = rdu06amt;
	}

	public String getSetlInd() {
		return setlInd;
	}

	public void setSetlInd(String setlInd) {
		this.setlInd = setlInd;
	}

	public Double getSetlCommAmt() {
		return setlCommAmt;
	}

	public void setSetlCommAmt(Double setlCommAmt) {
		this.setlCommAmt = setlCommAmt;
	}

	public Double getPsCommAmt() {
		return psCommAmt;
	}

	public void setPsCommAmt(Double psCommAmt) {
		this.psCommAmt = psCommAmt;
	}

	public String getSysUpdateTime() {
		return sysUpdateTime;
	}

	public void setSysUpdateTime(String sysUpdateTime) {
		this.sysUpdateTime = sysUpdateTime;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
