package com.yeamgood.godungonline.constants;

public class Constants {
	
	private Constants() {
		throw new IllegalStateException("Utility class");
	}
	
	public static final String SESSION_USER = "user";
	public static final String MESSAGE_ERROR = "error:";
	public static final String STATUS_ERROR = "ERROR";
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String ACTION_LOAD_SUCCESS = "action.load.success";
	public static final String ACTION_LOAD_ERROR = "action.load.error";
	public static final String ACTION_SAVE_ERROR = "action.save.error";
	public static final String ACTION_SAVE_SUCCESS = "action.save.success";
	public static final String ACTION_DELETE_ERROR = "action.delete.error";
	public static final String ACTION_DELETE_SUCCESS = "action.delete.success";
	
	public static final String LOG_INPUT = "input:{}"; 

	public static final String MENU = "menu";
	public static final Long MENU_HOME_ID = (long) 1;
	public static final Long MENU_WAREHOUSE_ID = (long) 41;
	public static final Long MENU_PRODUCT_ID = (long) 42;
	public static final Long MENU_BRAND_ID = (long) 43;
	public static final Long MENU_MEASURE_ID = (long) 44;
	public static final Long MENU_CATEGORY_ID = (long) 45;
	public static final Long MENU_SUPPLIER_ID = (long) 51;
	public static final Long MENU_CUSTOMER_ID = (long) 52;
	public static final Long MENU_EMPLOYEE_ID = (long) 53;
	public static final Long MENU_PROFILE_ID = (long) 101;
	public static final Long MENU_ROLE_GODUNG_ID = (long) 102;
	public static final Long MENU_PURCHASE_REQUEST = (long) 11;

	public static final Long MENU_USER_ID = (long) 201;
	public static final Long MENU_ROLE_ID = (long) 202;
	
	public static final Long COUNTRY_THAILAND = (long) 217;
	
	public static final String PROVINCE_DROPDOWN = "provinceDropdown";
	public static final String COUNTRY_DROPDOWN = "countryDropdown";

}