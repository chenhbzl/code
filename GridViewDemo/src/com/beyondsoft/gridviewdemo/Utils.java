package com.beyondsoft.gridviewdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Utils { 
	public static int[] image = { R.drawable.mb5u1_mb5ucom, R.drawable.mb5u2_mb5ucom,
			R.drawable.mb5u3_mb5ucom, R.drawable.mb5u4_mb5ucom,
			R.drawable.mb5u5_mb5ucom, R.drawable.mb5u6_mb5ucom,
			R.drawable.mb5u7_mb5ucom, R.drawable.mb5u8_mb5ucom,
			R.drawable.mb5u9_mb5ucom, R.drawable.mb5u10_mb5ucom,
			R.drawable.mb5u11_mb5ucom, R.drawable.mb5u12_mb5ucom,
			R.drawable.mb5u13_mb5ucom, R.drawable.mb5u14_mb5ucom,
			R.drawable.mb5u15_mb5ucom, R.drawable.mb5u16_mb5ucom,
			R.drawable.mb5u17_mb5ucom, R.drawable.mb5u18_mb5ucom,
			R.drawable.mb5u19_mb5ucom, R.drawable.mb5u20_mb5ucom,
			R.drawable.mb5u21_mb5ucom, R.drawable.mb5u22_mb5ucom,
			R.drawable.mb5u23_mb5ucom, R.drawable.mb5u24_mb5ucom,
			R.drawable.mb5u25_mb5ucom, R.drawable.mb5u26_mb5ucom,
			R.drawable.mb5u27_mb5ucom, R.drawable.mb5u28_mb5ucom,
			R.drawable.mb5u29_mb5ucom, R.drawable.mb5u30_mb5ucom,
			R.drawable.mb5u31_mb5ucom, R.drawable.mb5u32_mb5ucom,
			R.drawable.mb5u33_mb5ucom, R.drawable.mb5u34_mb5ucom,
			R.drawable.mb5u35_mb5ucom, R.drawable.mb5u36_mb5ucom,
			R.drawable.mb5u37_mb5ucom, R.drawable.mb5u38_mb5ucom,
			R.drawable.mb5u39_mb5ucom, R.drawable.mb5u40_mb5ucom,
			R.drawable.mb5u41_mb5ucom, R.drawable.mb5u42_mb5ucom,
			R.drawable.mb5u43_mb5ucom, R.drawable.mb5u44_mb5ucom };
	
	public static ArrayList<Map<String, Object>> getData() {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < 23; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img", image[i]);
			list.add(map);

		}
		return list;
	}
}
