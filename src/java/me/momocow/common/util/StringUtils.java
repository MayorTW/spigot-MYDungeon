package me.momocow.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * preserve the spaces in the double quote as '&nbsp;'
	 * @return
	 */
	public static String preserveQuotedSpaces (String src) {
		BiFunction<String, List<String>, String> spacesPreserver = (matched, memorized) -> {
			return matched.replaceAll(" ", "&nbsp;");
		};
		return replaceEachBy(replaceEachBy(src, "\".*?\"", spacesPreserver), "'.*?'", spacesPreserver);
	}
	
	public static String replaceQuotes (String src, String replacement) {
		BiFunction<String, List<String>, String> spacesPreserver = (matched, memorized) -> {
			return matched.replaceAll("^[\"']", replacement).replaceAll("[\"']$", replacement);
		};
		return replaceEachBy(replaceEachBy(src, "\".*?\"", spacesPreserver), "'.*?'", spacesPreserver);
	}
	
	/**
	 * 
	 * @param src
	 * @param regex
	 * @param replacementFactory (matched, memorized) -> replacement
	 * @return
	 */
	public static String replaceEachBy (String src, String regex, BiFunction<String, List<String>, String> replacementFactory) {
		StringBuffer result = new StringBuffer();
		Matcher matcher = Pattern.compile(regex).matcher(src);
		
		while (matcher.find()) {
			List<String> memorized = new ArrayList<>();
			for (int i : Range.$(1, matcher.groupCount())) {
				memorized.add(matcher.group(i));
			}
			matcher.appendReplacement(result, replacementFactory.apply(matcher.group(), memorized));
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
	
	public static List<List<String>> pickFrom (String src, String regexp) {
		List<List<String>> picked = new ArrayList<>();
		Matcher matcher = Pattern.compile(regexp).matcher(src);
		
		while (matcher.find()) {
			List<String> tokens = new ArrayList<>();
			for (int i = 0; i <= matcher.groupCount(); i++) {
				tokens.add(matcher.group(i));
			}
			picked.add(tokens);
		}
		
		return picked;
	}
}
