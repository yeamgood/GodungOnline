package com.yeamgood.godungonline.datatable;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class DataTablesRequest {
	
	private int	iDisplayStart;
	private int	iDisplayLength;
	private int	iColumns	;
	private String sSearch;	
	private boolean 	bRegex;	
	private boolean	bSearchable;
	private String	sEcho;
	
	private int	iSortingCols	;
	
	//Index of sort column
	private int iSortCol_0;
	
	//Index sort dir
	private String sSortDir_0; 
	
	//Set Max 10 column
	private boolean bSortable_0;
	private boolean bSortable_1;
	private boolean bSortable_2;
	private boolean bSortable_3;
	private boolean bSortable_4;
	private boolean bSortable_5;
	private boolean bSortable_6;
	private boolean bSortable_7;
	private boolean bSortable_8;
	private boolean bSortable_9;
	
	private String mDataProp_0;
	private String mDataProp_1;
	private String mDataProp_2;
	private String mDataProp_3;
	private String mDataProp_4;
	private String mDataProp_5;
	private String mDataProp_6;
	private String mDataProp_7;
	private String mDataProp_8;
	private String mDataProp_9;
	
	public String getNamecolumn() {
		String result = "";
		switch (this.iSortCol_0) {
		case 0:
			result = mDataProp_0;
			break;
		case 1:
			result = mDataProp_1;
			break;
		case 2:
			result = mDataProp_2;
			break;
		case 3:
			result = mDataProp_3;
			break;
		case 4:
			result = mDataProp_4;
			break;
		case 5:
			result = mDataProp_5;
			break;
		case 6:
			result = mDataProp_6;
			break;
		case 7:
			result = mDataProp_7;
			break;
		case 8:
			result = mDataProp_8;
			break;
		case 9:
			result = mDataProp_9;
			break;
		default:
			break;
		}
		System.out.println("this.iSortCol_0 : " + this.iSortCol_0);
		System.out.println("mDataProp_ result : " + result);
		return result;
	}
	
	public Direction getDirection() {
		if(this.sSortDir_0.equalsIgnoreCase("desc")) {
			return Sort.Direction.DESC;
		}else {
			return Sort.Direction.ASC;
		}
	}
	
	//SET GET Methods
	public int getiDisplayStart() {
		return iDisplayStart;
	}
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	public int getiDisplayLength() {
		return iDisplayLength;
	}
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}
	public int getiColumns() {
		return iColumns;
	}
	public void setiColumns(int iColumns) {
		this.iColumns = iColumns;
	}
	public String getsSearch() {
		return sSearch;
	}
	public void setsSearch(String sSearch) {
		this.sSearch = sSearch;
	}
	public boolean isbRegex() {
		return bRegex;
	}
	public void setbRegex(boolean bRegex) {
		this.bRegex = bRegex;
	}
	public boolean isbSearchable() {
		return bSearchable;
	}
	public void setbSearchable(boolean bSearchable) {
		this.bSearchable = bSearchable;
	}
	public String getsEcho() {
		return sEcho;
	}
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	public int getiSortingCols() {
		return iSortingCols;
	}
	public void setiSortingCols(int iSortingCols) {
		this.iSortingCols = iSortingCols;
	}
	public int getiSortCol_0() {
		return iSortCol_0;
	}
	public void setiSortCol_0(int iSortCol_0) {
		this.iSortCol_0 = iSortCol_0;
	}
	public String getsSortDir_0() {
		return sSortDir_0;
	}
	public void setsSortDir_0(String sSortDir_0) {
		this.sSortDir_0 = sSortDir_0;
	}
	public boolean isbSortable_0() {
		return bSortable_0;
	}
	public void setbSortable_0(boolean bSortable_0) {
		this.bSortable_0 = bSortable_0;
	}
	public boolean isbSortable_1() {
		return bSortable_1;
	}
	public void setbSortable_1(boolean bSortable_1) {
		this.bSortable_1 = bSortable_1;
	}
	public boolean isbSortable_2() {
		return bSortable_2;
	}
	public void setbSortable_2(boolean bSortable_2) {
		this.bSortable_2 = bSortable_2;
	}
	public boolean isbSortable_3() {
		return bSortable_3;
	}
	public void setbSortable_3(boolean bSortable_3) {
		this.bSortable_3 = bSortable_3;
	}
	public boolean isbSortable_4() {
		return bSortable_4;
	}
	public void setbSortable_4(boolean bSortable_4) {
		this.bSortable_4 = bSortable_4;
	}
	public boolean isbSortable_5() {
		return bSortable_5;
	}
	public void setbSortable_5(boolean bSortable_5) {
		this.bSortable_5 = bSortable_5;
	}
	public boolean isbSortable_6() {
		return bSortable_6;
	}
	public void setbSortable_6(boolean bSortable_6) {
		this.bSortable_6 = bSortable_6;
	}
	public boolean isbSortable_7() {
		return bSortable_7;
	}
	public void setbSortable_7(boolean bSortable_7) {
		this.bSortable_7 = bSortable_7;
	}
	public boolean isbSortable_8() {
		return bSortable_8;
	}
	public void setbSortable_8(boolean bSortable_8) {
		this.bSortable_8 = bSortable_8;
	}
	public boolean isbSortable_9() {
		return bSortable_9;
	}
	public void setbSortable_9(boolean bSortable_9) {
		this.bSortable_9 = bSortable_9;
	}
	public String getmDataProp_0() {
		return mDataProp_0;
	}
	public void setmDataProp_0(String mDataProp_0) {
		this.mDataProp_0 = mDataProp_0;
	}
	public String getmDataProp_1() {
		return mDataProp_1;
	}
	public void setmDataProp_1(String mDataProp_1) {
		this.mDataProp_1 = mDataProp_1;
	}
	public String getmDataProp_2() {
		return mDataProp_2;
	}
	public void setmDataProp_2(String mDataProp_2) {
		this.mDataProp_2 = mDataProp_2;
	}
	public String getmDataProp_3() {
		return mDataProp_3;
	}
	public void setmDataProp_3(String mDataProp_3) {
		this.mDataProp_3 = mDataProp_3;
	}
	public String getmDataProp_4() {
		return mDataProp_4;
	}
	public void setmDataProp_4(String mDataProp_4) {
		this.mDataProp_4 = mDataProp_4;
	}
	public String getmDataProp_5() {
		return mDataProp_5;
	}
	public void setmDataProp_5(String mDataProp_5) {
		this.mDataProp_5 = mDataProp_5;
	}
	public String getmDataProp_6() {
		return mDataProp_6;
	}
	public void setmDataProp_6(String mDataProp_6) {
		this.mDataProp_6 = mDataProp_6;
	}
	public String getmDataProp_7() {
		return mDataProp_7;
	}
	public void setmDataProp_7(String mDataProp_7) {
		this.mDataProp_7 = mDataProp_7;
	}
	public String getmDataProp_8() {
		return mDataProp_8;
	}
	public void setmDataProp_8(String mDataProp_8) {
		this.mDataProp_8 = mDataProp_8;
	}
	public String getmDataProp_9() {
		return mDataProp_9;
	}
	public void setmDataProp_9(String mDataProp_9) {
		this.mDataProp_9 = mDataProp_9;
	}
	
	@Override
	public String toString() {
		return "DataTablesRequest [iDisplayStart=" + iDisplayStart + ", iDisplayLength=" + iDisplayLength
				+ ", iColumns=" + iColumns + ", sSearch=" + sSearch + ", bRegex=" + bRegex + ", bSearchable="
				+ bSearchable + ", sEcho=" + sEcho + ", iSortingCols=" + iSortingCols + ", iSortCol_0=" + iSortCol_0
				+ ", sSortDir_0=" + sSortDir_0 + ", bSortable_0=" + bSortable_0 + ", bSortable_1=" + bSortable_1
				+ ", bSortable_2=" + bSortable_2 + ", bSortable_3=" + bSortable_3 + ", bSortable_4=" + bSortable_4
				+ ", bSortable_5=" + bSortable_5 + ", bSortable_6=" + bSortable_6 + ", bSortable_7=" + bSortable_7
				+ ", bSortable_8=" + bSortable_8 + ", bSortable_9=" + bSortable_9 + ", mDataProp_0=" + mDataProp_0
				+ ", mDataProp_1=" + mDataProp_1 + ", mDataProp_2=" + mDataProp_2 + ", mDataProp_3=" + mDataProp_3
				+ ", mDataProp_4=" + mDataProp_4 + ", mDataProp_5=" + mDataProp_5 + ", mDataProp_6=" + mDataProp_6
				+ ", mDataProp_7=" + mDataProp_7 + ", mDataProp_8=" + mDataProp_8 + ", mDataProp_9=" + mDataProp_9
				+ "]";
	}
	
}
