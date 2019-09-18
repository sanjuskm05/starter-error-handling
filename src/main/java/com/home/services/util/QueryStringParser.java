package com.home.services.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class QueryStringParser {

	//Constants
	public static final String QUERY_STRING_SPLIT_REGEX = "&";
	public static final String KEY_VALUE_PAIR_SEPARATOR = "=";
	public static final String URL_ENCODING_UTF_8 = "UTF-8";

	/**
	 * validates whether value based on Regular Expression
	 *
	 * @param paramValue
	 * @param regExp
	 * @return boolean
	 */
	public static boolean isValidPattern(String paramValue, String regExp) {
		boolean isValid = false;
		if (StringUtils.hasText(paramValue) && StringUtils.hasText(regExp)) {
			try {
				Pattern regExpPattern = Pattern.compile(regExp);
				Matcher regExpMatcher = regExpPattern.matcher(paramValue.trim());
				if (regExpMatcher.find()) {
					isValid = true;
				}
			} catch (Exception e) {
				isValid = false;
			}
		}
		return isValid;
	}

	/**
	 * Get the query parsed by key value pair parameters
	 *
	 * @param queryString the query string
	 * @return Map<String, String> query parsed
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static Map<String, String> getQueryParsed(String queryString) throws UnsupportedEncodingException {
		Map<String, String> queryKeyValuePairs = null;
		if (StringUtils.hasText(queryString)) {
			String[] pairs = queryString.split(QUERY_STRING_SPLIT_REGEX);
			if (pairs != null && pairs.length > 0) {
				queryKeyValuePairs = createQueryStringMap(pairs);
			}
		}
		return queryKeyValuePairs;
	}

	/**
	 * Create map object based on query string.
	 *
	 * @param pairs the pairs
	 * @return Map Object.
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static Map<String, String> createQueryStringMap(String[] pairs) throws UnsupportedEncodingException {
		Map<String, String> queryKeyValuePairs = new HashMap<>();
		if (pairs != null) {
			for (String pair : pairs) {
				if (pair != null) {
					String[] queryString = pair.split(KEY_VALUE_PAIR_SEPARATOR);
					if (queryString.length == 2) {
						String key = URLDecoder.decode(queryString[0], URL_ENCODING_UTF_8);
						String value = URLDecoder.decode(queryString[1], URL_ENCODING_UTF_8);
						queryKeyValuePairs.put(key, value);
					} else {
						return null;
					}
				}
			}
		}
		return queryKeyValuePairs;
	}

}
