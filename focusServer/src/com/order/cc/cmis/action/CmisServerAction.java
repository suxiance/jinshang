package com.order.cc.cmis.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.order.cc.cmis.form.CmisServerForm;
import com.order.cc.cmis.service.CmisServerService;
import com.order.cc.util.CommUtils;

public class CmisServerAction extends DispatchAction {
	static Logger logger = Logger.getLogger("focusServer");
	private CmisServerService cmisServerService = null;
	   
    public void setCmisServerService(CmisServerService cmisServerService) {
		this.cmisServerService = cmisServerService;
	}


 /**
   * 身份核实(手机,身份证)
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   */
  public void identityVerify(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		 HttpServletResponse response) throws IOException{
	     logger.info("***********identityVerify-start*************************");
	     JSONObject result = new JSONObject();
	     try {
	    	 JSONObject param = new JSONObject();
	    	 param.put("custid","");
	    	 param.put("idno","");
	    	 param.put("phoneno","");
	    	
	    	 CommUtils.addParameterParam(param,CommUtils.PARAM_IDENTITYVERIFY);
	    	 
	    	 String custid = request.getParameter("custid");
	    	 String idno = request.getParameter("idno");
	    	 String phoneno = request.getParameter("phoneno");
	    	 if(StringUtils.isNotEmpty(custid))
	    	    param.put("custid",custid);
	    	 if(StringUtils.isNotEmpty(idno))
	    	    param.put("idno",idno);
	    	 if(StringUtils.isNotEmpty(phoneno))
	    	    param.put("phoneno",phoneno);
	    	 result = cmisServerService.identityVerify(param);
	    	 CommUtils.sendJsonResData(response,result);
		 } catch (Exception e) {
			  e.printStackTrace();
		     logger.error(e.toString());
		     CommUtils.sendJsonResData(response,result);
		 }
    	 logger.info("***********identityVerify-end***************************");
	    
   }
  /**
   * 验证手机验证码
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   */
  public void codeVerify(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		  HttpServletResponse response) throws IOException{
	  logger.info("***********codeVerify-start*************************");
	  JSONObject result = new JSONObject();
	  try {
	      JSONObject param = new JSONObject();
	      param.put("phoneno", "");
		  param.put("terminalid","");
		  param.put("sceflag","");
		  param.put("telcode","");
		  param.put("agreeForAppSts","");
    	 
		  CommUtils.addParameterParam(param,CommUtils.PARAM_CODEVERIFY);
    	  
    	  String phoneno = request.getParameter("phoneno");
		  String terminalid = request.getParameter("terminalid");
		  String sceflag = request.getParameter("sceflag");
		  String telcode = request.getParameter("telcode");
		  String agreeForAppSts = request.getParameter("agreeForAppSts");
    	  if(StringUtils.isNotEmpty(phoneno))
    		  param.put("phoneno",phoneno);
    	  if(StringUtils.isNotEmpty(terminalid))
		      param.put("terminalid",terminalid);
    	  if(StringUtils.isNotEmpty(sceflag))
		      param.put("sceflag", sceflag);
    	  if(StringUtils.isNotEmpty(telcode))
		      param.put("telcode", telcode);
    	  if(StringUtils.isNotEmpty(agreeForAppSts))
		      param.put("agreeForAppSts",agreeForAppSts);
		  result = cmisServerService.codeVerify(param);
		  CommUtils.sendJsonResData(response,result);
	  } catch (Exception e) {
		  e.printStackTrace();
		  logger.error(e.toString());
		  CommUtils.sendJsonResData(response,result);
	  }
	  logger.info("***********codeVerify-end***************************");
	  
  }
  /**
   * 获取手机验证码（同时发送）
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   */
  public void getCodeAndSend(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		  HttpServletResponse response) throws IOException{
	  logger.info("***********getCodeAndSend-start*************************");
	  JSONObject result = new JSONObject();
	  try {
		  JSONObject param = new JSONObject();
		  param.put("phoneno", "");
		  param.put("terminalid","");
		  param.put("sceflag", "");
		 
		  CommUtils.addParameterParam(param,CommUtils.PARAM_GETCODEANDSEND);
		 
		  String phoneno = request.getParameter("phoneno");
		  String terminalid = request.getParameter("terminalid");
		  String sceflag = request.getParameter("sceflag");
		  if(StringUtils.isNotEmpty(phoneno))
		     param.put("phoneno", phoneno);
		  if(StringUtils.isNotEmpty(terminalid))
		     param.put("terminalid",terminalid);
		  if(StringUtils.isNotEmpty(sceflag))
		     param.put("sceflag", sceflag);
		  result = cmisServerService.getCodeAndSend(param);
		  CommUtils.sendJsonResData(response,result);
	  } catch (Exception e) {
		  e.printStackTrace();
		  logger.error(e.toString());
		  CommUtils.sendJsonResData(response,result);
	  }
	  logger.info("***********getCodeAndSend-end***************************");
	  
  }
  /**
   * 更换手机号码
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   */
  public void changePhoneno(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		  HttpServletResponse response) throws IOException{
	  logger.info("***********changePhoneno-start*************************");
	  JSONObject result = new JSONObject();
	  try {
		  JSONObject param = new JSONObject();
		  param.put("custid", "");
		  param.put("newphoneno","");
		  param.put("chgusr","");
		  param.put("chgrmk","");
		  param.put("inputscore","");
		  param.put("inputseq","");
		  
		  CommUtils.addParameterParam(param,CommUtils.PARAM_CHANGEPHONENO);

		  String custid = request.getParameter("custid");
		  String newphoneno = request.getParameter("newphoneno");
		  String chgusr = request.getParameter("chgusr");
		  String chgrmk = request.getParameter("chgrmk");
		  String inputscore = request.getParameter("inputscore");
		  String inputseq = request.getParameter("inputseq");
		  if(StringUtils.isNotEmpty(custid))
		     param.put("custid", custid);
		  if(StringUtils.isNotEmpty(newphoneno))
		     param.put("newphoneno",newphoneno);
		  if(StringUtils.isNotEmpty(chgusr))
		     param.put("chgusr", chgusr);
		  if(StringUtils.isNotEmpty(chgrmk))
		     param.put("chgrmk", chgrmk);
		  if(StringUtils.isNotEmpty(inputscore))
		     param.put("inputscore", inputscore);
		  if(StringUtils.isNotEmpty(inputseq))
		     param.put("inputseq", inputseq);
		  result = cmisServerService.changePhoneno(param);
		  CommUtils.sendJsonResData(response,result);
	  } catch (Exception e) {
		  e.printStackTrace();
		  logger.error(e.toString());
		  CommUtils.sendJsonResData(response,result);
	  }
	  logger.info("***********changePhoneno-end***************************");
	  
  }
  /**
   * 查询还款信息
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   */
  public void queryRepayment(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		  HttpServletResponse response) throws IOException{
	  logger.info("***********queryRepayment-start*************************");
	  JSONObject result = new JSONObject();
	  try {
		  JSONObject param = new JSONObject();
		  param.put("id_no","");
		  param.put("key","0");
		  
		  CommUtils.addParameterParam(param,CommUtils.PARAM_QUERYREPAYMENT);
		  
		  String id_no = request.getParameter("id_no");
		  String key = request.getParameter("key");//按键
		  if(StringUtils.isNotEmpty(id_no))
		     param.put("id_no", id_no);
		  if(StringUtils.isNotEmpty(key))
		     param.put("key", key);
		  result = cmisServerService.queryRepayment(param);
		  CommUtils.sendJsonResData(response,result);
	  } catch (Exception e) {
		  e.printStackTrace();
		  logger.error(e.toString());
		  CommUtils.sendJsonResData(response,result);
	  }
	  logger.info("***********queryRepayment-end***************************");
  }
  /**
   * 查询贷款信息
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   */
  public void queryLoanInfo(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		  HttpServletResponse response) throws IOException{
	  logger.info("***********queryLoanInfo-start*************************");
	  JSONObject result = new JSONObject();
	  try {
		  JSONObject param = new JSONObject();
		  param.put("idno","");
		  CommUtils.addParameterParam(param,CommUtils.PARAM_QUERYLOANINFO);
		  
		  String idno = request.getParameter("idno");
		  if(StringUtils.isNotEmpty(idno))
			  param.put("idno", idno);
		  result = cmisServerService.queryLoanInfo(param);
		  CommUtils.sendJsonResData(response,result);
	  } catch (Exception e) {
		  e.printStackTrace();
		  logger.error(e.toString());
		  CommUtils.sendJsonResData(response,result);
	  }
	  logger.info("***********queryLoanInfo-end***************************");
  }
  /**
   * 发送短信
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   */
  public void sendMessage(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		  HttpServletResponse response) throws IOException{
	  logger.info("***********sendMessage-start*************************");
	  JSONObject result = new JSONObject();
	  try {
		  String pszMobis = request.getParameter("pszMobis");
		  String pszMsg = request.getParameter("pszMsg");
		  Map<String, String> param = new HashMap<String, String>();
		  param.put("pszMobis", pszMobis==null?"":pszMobis);
		  param.put("pszMsg", pszMsg==null?"":pszMsg);
		  result = cmisServerService.sendMessage(param);
		  CommUtils.sendJsonResData(response,result);
	  } catch (Exception e) {
		  e.printStackTrace();
		  logger.error(e.toString());
		  CommUtils.sendJsonResData(response,result);
	  }
	  logger.info("***********sendMessage-end***************************");
  }
  
  /**
   * 机器人接口
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   */
  public void serverRobot(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		  HttpServletResponse response) throws IOException{
	  logger.info("***********serverRobot-start*************************");
	  try {
		  CmisServerForm cmisServerForm = (CmisServerForm)form;
		  logger.info(cmisServerForm);
		  JSONObject jsonObject  = cmisServerService.serverRobot(cmisServerForm);
		  CommUtils.sendResData(response,jsonObject.toString());
	  } catch (Exception e) {
		  e.printStackTrace();
		  logger.error(e.toString());
		  JSONObject result = new JSONObject();
		  result.put("action","hangup");
		  CommUtils.sendResData(response,result.toString());
	  }
	  logger.info("***********serverRobot-end***************************");
	  
  }
}