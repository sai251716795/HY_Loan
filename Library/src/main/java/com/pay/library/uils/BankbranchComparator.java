package com.pay.library.uils;

import java.util.Comparator;

public class BankbranchComparator implements Comparator<BankbranchMode>{
	public int compare(BankbranchMode o1, BankbranchMode o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}
