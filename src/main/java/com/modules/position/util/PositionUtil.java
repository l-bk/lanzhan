package com.modules.position.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.modules.position.dao.PositionDao;
import com.modules.position.entity.Position;

public class PositionUtil {
	
	public static Map<String, Integer>POSITIONDESC= new LinkedHashMap<String, Integer>();
	public static Map<String, String>POSITIONTYPEDESC= new LinkedHashMap<String, String>();
	
	
	public static void InitPositionDesc(PositionDao positionDao){
		List<Position>positions=positionDao.findAlltwo(new Position());
		for(Position p:positions){
			POSITIONDESC.put(p.getDescription(), p.getPosid());
		}
		
	}
	public static void InitPositionTypeDesc(){
		
		POSITIONTYPEDESC.put("首页焦点图", "0");
		POSITIONTYPEDESC.put("top热榜", "1");
		POSITIONTYPEDESC.put("列表页推荐文章", "2");
		POSITIONTYPEDESC.put("内容页推荐文章", "3");
		POSITIONTYPEDESC.put("首页妙招推荐栏", "4");
	}
	
	  //求两个数组的差集   
	   public static String[] minus(String[] arr1, String[] arr2) {   
	       LinkedList<String> list = new LinkedList<String>();   
	       LinkedList<String> history = new LinkedList<String>();   
	       String[] longerArr = arr1;   
	       String[] shorterArr = arr2;   
	       //找出较长的数组来减较短的数组   
	       if (arr1.length > arr2.length) {   
	           longerArr = arr2;   
	           shorterArr = arr1;   
	       }   
	       for (String str : longerArr) {   
	           if (!list.contains(str)) {   
	               list.add(str);   
	           }   
	       }   
	       for (String str : shorterArr) {   
	           if (list.contains(str)) {   
	               history.add(str);   
	               list.remove(str);   
	           } else {   
	               if (!history.contains(str)) {   
	                   list.add(str);   
	               }   
	           }   
	       }   
	 
	       String[] result = {};   
	       return list.toArray(result);   
	   }   
	}  
	

