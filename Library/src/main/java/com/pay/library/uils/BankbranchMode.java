package com.pay.library.uils;

public class BankbranchMode {

	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	private String bankbranch;
	
	public String getBankbranch() {
		return bankbranch;
	}
	public void setBankbranch(String bankbranch) {
		this.bankbranch = bankbranch;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	
}
