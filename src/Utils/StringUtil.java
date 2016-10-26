package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static String convert2SetFieldNameFunc(String dataSoureName){
		if(isEmpty(dataSoureName)){
			throw new RuntimeException("找不到该set方法");
		}
		StringBuffer sb = new StringBuffer();
		String[] str = dataSoureName.split("_");
		if(str!=null&&str.length>0){
			for (int i = 0; i < str.length; i++) {
				String resStr = str[i].toLowerCase();
				sb.append(replaceIndex(resStr, 0, str[i].substring(0,1)));
			}
		}
		return sb.toString();
	}
	
	public static String convert2FieldName(String dataSoureName){
		if(isEmpty(dataSoureName)){
			throw new RuntimeException("找不到该字段");
		}
		StringBuffer sb = new StringBuffer();
		String[] str = dataSoureName.split("_");
		if(str!=null&&str.length>0){
			sb.append(str[0].toLowerCase());
			if(str.length>1){
				for (int i = 1; i < str.length; i++) {
					String resStr = str[i].toLowerCase();
					sb.append(replaceIndex(resStr, 0, str[i].substring(0,1)));
				}
			}
		}
		return sb.toString();
	}
	
	public static String convert2DataSourceName(String fieldName){
		if(isEmpty(fieldName)){
			throw new RuntimeException("找不到该字段");
		}
		StringBuffer sb = new StringBuffer();
		List lowerCaseList = new ArrayList();
		Pattern p = Pattern.compile("[a-z]*");
		Matcher matcher = p.matcher(fieldName);
		while(matcher.find()){
			if(!StringUtil.isEmpty(matcher.group(0).trim())){
				lowerCaseList.add(matcher.group(0));
			}
		}
		List upperCaseList = new ArrayList();
		Pattern p2 = Pattern.compile("[A-Z]");
		Matcher matcher2 = p2.matcher(fieldName);
		while(matcher2.find()){
			if(!StringUtil.isEmpty(matcher2.group(0).trim())){
				upperCaseList.add(matcher2.group(0));
			}
		}
		for (int j = 0; j < upperCaseList.size(); j++) {
			sb.append(lowerCaseList.get(j)).append(upperCaseList.get(j));
		}
		if(upperCaseList.size()<lowerCaseList.size()){
			sb.append(lowerCaseList.get(lowerCaseList.size()-1));
		}
		String resStr = sb.toString().toUpperCase();
		resStr = replaceIndex(resStr, 0, resStr.substring(0,1).toUpperCase());
		return resStr;
	}
	
	
	public static Boolean isEmpty(String str){
		if(str==null||str.length()==0){
			return true;
		}
		return false;
	}
	
	
	public static String replaceIndex(String resStr,int index,String replaceStr){
		if(isEmpty(resStr)){
			throw new RuntimeException("被操作的字符串为空");
		}
		StringBuffer sb = new StringBuffer();
		return sb.append(resStr.substring(0, index)).append(replaceStr).append(resStr.substring(index+1)).toString();
		
	}
}
