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
		responseJson(response,"�ɹ�");
		
	}
	/**
	 * �������ݿ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public void updateHistory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UpdateCity updateCity = new UpdateCity();
		//ִ���޸�OPERATION_CASE_BASE  ORDER_ACCUSE��
		String ip = request.getParameter("ip");
		if(!"".equals(ip)&&null != ip){
			try {
				updateCity.executeUpdate(ip);
				responseJson(response,"���óɹ�");
			} catch (Exception e) {
				// TODO: handle exception
				responseJson(response,"����ʧ��");
			}
		}else{
			responseJson(response,"������ip");
		}
		
	}
	
	/**
	 * ���ķ������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public void coreQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("���ú��Ŀ�ʼ");
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
				//+  "\"requestTime\":\"2017-12-27 12:28:04\",\"transType\":2001,\"transResultDesc\":\"��Ŷ\",\"transResultCode\":1},"
				+"\"transResponse\":{" +
				"\"extensionObject\":{" +
				"\"@typeName\":\"QueryReception\"," +
				"\"QueryDetailResult\":";
	//	"\"QueryReceptionDetailResult\":";
		json += getJsonString(transType);
		
		json += "}}}";
		System.out.println("���ú��ĳɹ�.....");
		responseJson(response,json);
		}else{
			System.out.println("���ú���ʧ��.....");
			responseJson(response,"��������");
		}
	}
	
	
	//��ȡexcel ���� ƴ�ӳ� json ���� �ַ���
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
					//��Сд,�ŵ�json������
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
	 * ��ӦJson
	 * 
	 * @param response
	 */
	public void responseJson(HttpServletResponse response, String json) {
		try {
			// ��JSON����д�ؿͻ���
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
					//��Сд,�ŵ�json������
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
