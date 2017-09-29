package com.yeamgood.godungonline.utils;

import java.text.DecimalFormat;

public class DocumentUtils {
	
	private DocumentUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static String getStringSizeLengthFile(long size) {
		String sizeFileText = "";
	    DecimalFormat df = new DecimalFormat("0.00");

	    float sizeKb = 1024.0f;
	    float sizeMo = sizeKb * sizeKb;
	    float sizeGo = sizeMo * sizeKb;
	    float sizeTerra = sizeGo * sizeKb;

	   if(size < sizeMo)
	    		sizeFileText = df.format(size / sizeKb)+ " KB";
	    else if(size < sizeGo)
	    		sizeFileText = df.format(size / sizeMo) + " MB";
	    else if(size < sizeTerra)
	    		sizeFileText = df.format(size / sizeGo) + " GB";

	    return sizeFileText;
	}
	
}
