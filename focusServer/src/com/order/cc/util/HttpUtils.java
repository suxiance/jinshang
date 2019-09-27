package com.order.cc.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * HTTP工具类
 * 
 */
public class HttpUtils {

	static Logger log = Logger.getLogger("focusServer");
	/**
	 * 定义编码格式 UTF-8
	 */
	public static final String URL_PARAM_DECODECHARSET_UTF8 = "UTF-8";

	/**
	 * 定义编码格式 GBK
	 */
	public static final String URL_PARAM_DECODECHARSET_GBK = "GBK";

	private static final String URL_PARAM_CONNECT_FLAG = "&";

	private static final String EMPTY = "";

	private static MultiThreadedHttpConnectionManager connectionManager = null;

	private static int connectionTimeOut = 25000;

	private static int socketTimeOut = 25000;

	private static int maxConnectionPerHost = 20;

	private static int maxTotalConnections = 20;

	private static HttpClient client;

	static {
		connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
		connectionManager.getParams().setSoTimeout(socketTimeOut);
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(
				maxConnectionPerHost);
		connectionManager.getParams().setMaxTotalConnections(
				maxTotalConnections);
		client = new HttpClient(connectionManager);
	}

	/**
	 * POST方式提交数据
	 * 
	 * @param url
	 *            待请求的URL
	 * @param params
	 *            要提交的数据
	 * @param enc
	 *            编码
	 * @return 响应结果
	 * @throws IOException
	 *             IO异常
	 */
	public static String URLPost(String url, Map<String, String> params,
			String enc) {

		String response = EMPTY;
		PostMethod postMethod = null;
		try {
			log.info(url+"请求报文= " + params);
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=" + enc);
			// 将表单的值放入postMethod中
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				String value = params.get(key);
				postMethod.addParameter(key, value);
			}
			// 执行postMethod
			int statusCode = client.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				response = postMethod.getResponseBodyAsString();
				log.info(url+"返回报文= " + response);
			} else {
				log.error("响应状态码 = " + postMethod.getStatusCode());
			}
		} catch (HttpException e) {
			log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("发生网络异常", e);
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
				postMethod = null;
			}
		}

		return response;
	}
	public static String URLPostJson(String url, String json,String enc) {

		String response = EMPTY;
		PostMethod postMethod = null;
		try {
			log.info(url+"请求报文= " + json);
			postMethod = new PostMethod(url);
			// 执行postMethod
			RequestEntity se = new StringRequestEntity (json,"application/json" ,enc);
			postMethod.setRequestEntity(se);
			postMethod.setRequestHeader("Content-Type","application/json");
			//默认的重试策略
		    postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		    postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);//设置超时时间
			int statusCode = client.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				response = postMethod.getResponseBodyAsString();
				log.info(url+"返回报文= " + response);
			} else {
				log.error(url+"响应状态码 = " + postMethod.getStatusCode());
			}
		} catch (HttpException e) {
			log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("发生网络异常", e);
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
				postMethod = null;
			}
		}

		return response;
	}
	/**
	 * GET方式提交数据
	 * 
	 * @param url
	 *            待请求的URL
	 * @param params
	 *            要提交的数据
	 * @param enc
	 *            编码
	 * @return 响应结果
	 * @throws IOException
	 *             IO异常
	 */
	public static String URLGet(String url, Map<String, String> params,
			String enc) {

		String response = EMPTY;
		GetMethod getMethod = null;
		StringBuffer strtTotalURL = new StringBuffer(EMPTY);
		if (strtTotalURL.indexOf("?") == -1) {
			strtTotalURL.append(url).append("?").append(getUrl(params, enc));
		} else {
			strtTotalURL.append(url).append("&").append(getUrl(params, enc));
		}
		log.info("GET请求URL = /n" + strtTotalURL.toString());

		try {
			getMethod = new GetMethod(strtTotalURL.toString());
			getMethod.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=" + enc);
			// 执行getMethod
			int statusCode = client.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				response = getMethod.getResponseBodyAsString();
			} else {
				log.debug("响应状态码 = " + getMethod.getStatusCode());
			}
		} catch (HttpException e) {
			log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("发生网络异常", e);
			e.printStackTrace();
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
				getMethod = null;
			}
		}
         log.info(response);
		return response;
	}

	/**
	 * 据Map生成URL字符串
	 * 
	 * @param map
	 *            Map
	 * @param valueEnc
	 *            URL编码
	 * @return URL
	 */
	private static String getUrl(Map<String, String> map, String valueEnc) {

		if (null == map || map.keySet().size() == 0) {
			return (EMPTY);
		}
		StringBuffer url = new StringBuffer();
		Set<String> keys = map.keySet();
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			String key = it.next();
			if (map.containsKey(key)) {
				String val = map.get(key);
				String str = val != null ? val : EMPTY;
				try {
					str = URLEncoder.encode(str, valueEnc);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				url.append(key).append("=").append(str).append(
						URL_PARAM_CONNECT_FLAG);
			}
		}
		String strURL = EMPTY;
		strURL = url.toString();
		if (URL_PARAM_CONNECT_FLAG.equals(EMPTY
				+ strURL.charAt(strURL.length() - 1))) {
			strURL = strURL.substring(0, strURL.length() - 1);
		}

		return (strURL);
	}
	public static void main(String[] arg) {
		try {
			String url = "http://20.4.16.24:7080/auth/token";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userId", "robot");
			jsonObject.put("password", "it_robot@123");
			String urlPostJson = HttpUtils.URLPostJson(url,jsonObject.toString(),"UTF-8");
			JSONObject fromObject = JSONObject.fromObject(urlPostJson);
			String token = fromObject.getString("access_token");
			System.out.println(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}