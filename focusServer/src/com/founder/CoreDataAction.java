package com.founder;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;


public class CoreDataAction extends DispatchAction {
	public static final String BASE_PATH = "D:";
	//public static final String BASE_PATH = "/opt/coreData/";

	public ActionForward cutPicture(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		return mapping.findForward("cutPicture");
	
	}

	public void demo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CutPicture cutPicture = new CutPicture();
		String left = request.getParameter("left");
		String top = request.getParameter("top");
		String w = request.getParameter("w");
		String h = request.getParameter("h");
		
		try {
			ScreenShotWindow ssw=new ScreenShotWindow();
			ssw.setVisible(true);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		cutPicture.cut(Integer.valueOf(left),Integer.valueOf(top),Integer.valueOf(w),
//				Integer.valueOf(h));
		responseJson(response,"成功");
		
	}
	/**
	 * 更新数据库
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public void updateHistory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UpdateCity updateCity = new UpdateCity();
		//执行修改OPERATION_CASE_BASE  ORDER_ACCUSE表
		String ip = request.getParameter("ip");
		if(!"".equals(ip)&&null != ip){
			try {
				updateCity.executeUpdate(ip);
				responseJson(response,"调用成功");
			} catch (Exception e) {
				// TODO: handle exception
				responseJson(response,"调用失败");
			}
		}else{
			responseJson(response,"请输入ip");
		}
		
	}
	
	/**
	 * 核心服务测试
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public void coreQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("调用核心开始");
		String id = "2015";
		if(id != null){
//		System.out.println("cardno="+cardno);
//		JSONObject cardObj = JSONObject.fromObject(cardno);
//		Object transHeader = cardObj.get("transHeader");
//		JSONObject transHeaderObj = JSONObject.fromObject(transHeader.toString());
//		String transType = (String) transHeaderObj.get("transType");
		String transType = id;
//		System.out.println("request:"+cardno);
		String json="{\"transHeader\":{\"requestId\":\"8287701547592995545\","
				+  "\"requestTime\":\"2017-12-27 12:28:04\",\"transType\":2001,\"transResultCode\":1},"
				//+  "\"requestTime\":\"2017-12-27 12:28:04\",\"transType\":2001,\"transResultDesc\":\"你哦\",\"transResultCode\":1},"
				+"\"transResponse\":{" +
				"\"extensionObject\":{" +
				"\"@typeName\":\"QueryReception\"," +
				"\"QueryDetailResult\":";
	//	"\"QueryReceptionDetailResult\":";
		json += getJsonString(transType);
		
		json += "}}}";
		System.out.println("调用核心成功.....");
		responseJson(response,json);
		}else{
			System.out.println("调用核心失败.....");
			responseJson(response,"参数错误");
		}
	}
	
	
	//读取excel 数据 拼接成 json 数据 字符串
	public String getJsonString(String transType){
        ReadExcelUtil readExcelUtil = new ReadExcelUtil();
		//System.out.println(BASE_PATH);
		File file = new File(BASE_PATH+transType+".xls");
		
		JSONArray jsonArray = new JSONArray();
		try {
			String[][] data = readExcelUtil.getData(file, 0);
			for (int i = 1; i < data[0].length-1; i++) {
				JSONObject jsonObject = new JSONObject();
				for (int j = 0; j < data.length; j++) {
					//变小写,放到json对象里
					jsonObject.put(data[j][0].toLowerCase(), data[j][i]);
				}
				jsonArray.add(jsonObject);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//System.out.println(jsonArray.toString());
		
		return jsonArray.toString();
	
	}
	
	/**
	 * 相应Json
	 * 
	 * @param response
	 */
	public void responseJson(HttpServletResponse response, String json) {
		try {
			// 将JSON数据写回客户端
			response.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
 public static void main(String[] args) {
		
		ReadExcelUtil readExcelUtil = new ReadExcelUtil();
		
		File file = new File(BASE_PATH+"2001"+".xls");
		
		JSONArray jsonArray = new JSONArray();
		try {
			String[][] data = readExcelUtil.getData(file, 0);
			for (int i = 1; i < data[0].length-1; i++) {
				JSONObject jsonObject = new JSONObject();
				for (int j = 0; j < data.length; j++) {
					//变小写,放到json对象里
					jsonObject.put(data[j][0].toLowerCase(), data[j][i]);
				}
				jsonArray.add(jsonObject);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		for (Object object : jsonArray) {
			System.out.println(object.toString());
		}
	   
	   
	    
	 
	
	}
	
	
	
	
}
