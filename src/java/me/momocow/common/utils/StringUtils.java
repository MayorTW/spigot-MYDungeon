package me.momocow.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * preserve the spaces in the double quote as '&nbsp;'
	 * @return
	 */
	public static String preserveQuotedSpaces (String src) {
		StringBuffer result = new StringBuffer();
		Pattern pattern = Pattern.compile("[\"'].*?[\"']");
        Matcher matcher = pattern.matcher(src);

        while (matcher.find()) {
        	matcher.appendReplacement(result, matcher.group().replaceAll(" ", "&nbsp;"));
        }
        
        matcher.appendTail(result);
        
        return result.toString();
	}
	
	public static String recoverPreservedSpaces (String src) {
		return src.replaceAll("&nbsp;", " ");
	}
	
	public static String getLongestCommonPrefix(String a, String b) {
	    int minLength = Math.min(a.length(), b.length());
	    for (int i = 0; i < minLength; i++) {
	        if (a.charAt(i) != b.charAt(i)) {
	            return a.substring(0, i);
	        }
	    }
	    return a.substring(0, minLength);
	}
}
