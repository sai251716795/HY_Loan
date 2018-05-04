package com.pay.library.bean;

import java.io.Serializable;
import java.util.List;

public class BindDeviceInfo implements Serializable{
	
	public String getTermPayAmt() {
		return termPayAmt;
	}

	public void setTermPayAmt(String termPayAmt) {
		this.termPayAmt = termPayAmt;
	}

	public String getTermRecipient() {
		return termRecipient;
	}

	public void setTermRecipient(String termRecipient) {
		this.termRecipient = termRecipient;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3132165626170860663L;
	private String agentId;
	private String termNo;
	private int termPayFlag;
	private String type;
	private String termPayAmt;
	private String termRecipient;
	private String terminalNo;
	private String terminalType;
	
	
	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	private List<PosRate> rate;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getTermNo() {
		return termNo;
	}

	public void setTermNo(String termNo) {
		this.termNo = termNo;
	}

	public List<PosRate> getRate() {
		return rate;
	}

	public void setRate(List<PosRate> rate) {
		this.rate = rate;
	}

	public int getTermPayFlag() {
		return termPayFlag;
	}

	public void setTermPayFlag(int termPayFlag) {
		this.termPayFlag = termPayFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	

}
