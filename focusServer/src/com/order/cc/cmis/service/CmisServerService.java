package com.order.cc.cmis.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.order.cc.cmis.form.CmisServerForm;
import com.order.cc.util.CommUtils;
import com.order.cc.util.DateTime;
import com.order.cc.util.HttpUtils;
import com.order.cc.util.MD5Util;
import com.order.cc.util.PropertiesUtil;
import com.order.cc.util.RedisAPI;
import com.order.cc.util.XmlUtil;


public class CmisServerService {
	public static Logger logger = Logger.getLogger("focusServer");
	
	/**
	 * 获取token
	 * @return
	 */
	public String getToken(){
		String token = null;
		try {
			boolean exists = RedisAPI.exists("access_token");
			if(exists){//redis缓存里有token
				token = RedisAPI.get("access_token");
				logger.info("从reids缓存中获取token:"+token+"-剩余生存时间:"+RedisAPI.ttl("access_token").intValue());
			}else{
				String url = CommUtils.URL_TOKEN;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("userId", CommUtils.TOKEN_USERID);
				jsonObject.put("password", CommUtils.TOKEN_PASSWORD);
				String urlPostJson = HttpUtils.URLPostJson(url,jsonObject.toString(),"UTF-8");
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				token = fromObject.getString("access_token");
			    if(token!=null){
			    	logger.info("从url缓存中获取token:"+token);
			    	RedisAPI.set("access_token", token);
	            	RedisAPI.set("access_token_time", new Date().toString());
	            	RedisAPI.expire("access_token",60*30);
	            	logger.info("redis设置token为："+token);
			    }
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("获取token失败");
		}
		return token;
	}
    /**
     * 身份核实
     * @param param
     * @return
     */
	public JSONObject identityVerify(JSONObject param) {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			String url = CommUtils.URL_CUSTOMER_040100063+token;
			String urlPostJson = HttpUtils.URLPostJson(url,CommUtils.requestParam(param,"040100063").toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				if(head.getString("successCode").equals("E00000")){//交易成功
					JSONObject body = fromObject.getJSONObject("body");
					result.put("code", CommUtils.SUCCESS);
					result.put("msg", head.getString("errorMessage"));
					result.put("custid", body.getString("custid"));
					result.put("phoneno", body.getString("phoneno"));
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errorMessage"));
					result.put("msgCode", head.getString("successCode"));
					result.put("content",head.getString("errorMessage"));
				}
			}
		}
		return result;
	}
	
	/**
	 * 验证码验证
	 * @param param
	 * @return
	 */
	public JSONObject codeVerify(JSONObject param) {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			String url = CommUtils.URL_CUSTOMER_040100031+token;
			String urlPostJson = HttpUtils.URLPostJson(url,CommUtils.requestParam(param,"040100031").toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				//JSONObject body = fromObject.getJSONObject("body");
				if(head.getString("successCode").equals("E00000")){//交易成功
					result.put("code", CommUtils.SUCCESS);
					result.put("msg", head.getString("errorMessage"));
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errorMessage"));
					result.put("msgCode", head.getString("successCode"));
					result.put("content",head.getString("errorMessage"));
				}
			}
		}
		return result;
	}
	/**
	 * 修改手机号码
	 * @param param
	 * @return
	 */
	public JSONObject changePhoneno(JSONObject param) {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			String url = CommUtils.URL_CUSTOMER_040100054+token;
			String urlPostJson = HttpUtils.URLPostJson(url,CommUtils.requestParam(param,"040100054").toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				//JSONObject body = fromObject.getJSONObject("body");
				if(head.getString("successCode").equals("E00000")){//交易成功
					result.put("code", CommUtils.SUCCESS);
					result.put("msg", head.getString("errorMessage"));
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errorMessage"));
					result.put("msgCode", head.getString("successCode"));
					result.put("content",head.getString("errorMessage"));
				}
			}
		}
		return result;
	}
	/**
	 * 查询贷款信息
	 * @param param
	 * @return
	 * @throws ParseException 
	 */
	public JSONObject queryLoanInfo(JSONObject param) throws ParseException {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			String url = CommUtils.URL_CMIS_A1013061+token;
			String urlPostJson = HttpUtils.URLPostJson(url,param.toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				if(!head.has("errcode")){//成功
					if(fromObject.getString("code").equals("000")){//交易成功
						result.put("code", CommUtils.SUCCESS);
						result.put("msg", fromObject.getString("message"));
						StringBuffer content = new StringBuffer("您于");
						JSONObject data = fromObject.getJSONObject("data");
						JSONArray loanInfoList = data.getJSONArray("LoanInfo");
						if(null!=loanInfoList&&loanInfoList.size()>0){
							for (int i = 0; i < loanInfoList.size(); i++) {
								JSONObject loanInfo = (JSONObject) loanInfoList.get(i);
								String channelname = loanInfo.getString("channelname");//渠道别名
								String applydt = loanInfo.getString("applydt");//申请时间
								DateTime parseDate = DateTime.parseDate(applydt);
								applydt = parseDate.toDateTimeString(DateTime.DEFAULT_DATE_FORMAT_PATTERN2);
								String apprvamt = loanInfo.getString("apprvamt");//放款金额
								String creditlimit = loanInfo.getString("creditlimit");//授信金额
								String flag = loanInfo.getString("flag");//04取授信金额，其余的取放款金额
								if("04".equals(flag)){
									content.append(applydt).append("在").append(channelname).append("申请了一笔消费分期贷款，授信金额")
									.append(creditlimit).append("万元，");
								}else{
									content.append(applydt).append("在").append(channelname).append("申请了一笔消费分期贷款，放款金额")
									.append(apprvamt).append("万元，");
								}
							} 
							content.replace(content.length()-1,content.length(), "。");
							result.put("content",content.toString());
						}else{
							result.put("content","未查到相关贷款信息");
						}
					}else{
						result.put("code", CommUtils.FAIL);
						result.put("msg", fromObject.getString("message"));
						result.put("msgCode",fromObject.getString("code"));
						result.put("content",fromObject.getString("message"));
					}
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errmsg"));
					result.put("msgCode", head.getString("errcode"));
					result.put("content", head.getString("errmsg"));
				}
			}
		}
		return result;
	}
	 /**
	   * 查询还款信息
	   * @param param
	   * @return
	 * @throws ParseException 
	   */
	public JSONObject queryRepayment(JSONObject param) throws ParseException {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			int key = Integer.valueOf(param.getString("key"));
			switch (key) {
				case 1://全部本期还款金额
					result = queryRepayment1(param);
					break;
				case 2://同1
					result = queryRepayment2(param);
					break;
				case 3://还款银行卡号
					result = queryRepayment3(param);
					break;
				case 4://还款失败原因
					result = queryRepayment4(param);
					break;
				case 5://还款日期
					result = queryRepayment5(param);
					break;
				default:
					result.put("code", CommUtils.FAIL);
					result.put("msg","按键不正确");
					break;
			}
		}
		return result;
	}
	
	/**
	   * 查询还款信息1 您申请的**贷款本期还款金额为**元  （有当前欠款，以当前欠款金额为准）
	   * @param param
	   * @return
	   */
	public JSONObject queryRepayment1(JSONObject param) {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			StringBuffer content = new StringBuffer("您申请的");
			String url = CommUtils.URL_YCLOANS_A1023026+token;
			String urlPostJson = HttpUtils.URLPostJson(url,param.toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				if(head.getString("errcode").equals("E00000000000")){//交易成功
					JSONArray loanInfoList = fromObject.getJSONArray("loanInfoList");
                   if(null!=loanInfoList&&loanInfoList.size()>0){
                       for (int i = 0; i < loanInfoList.size(); i++) {
                    	   JSONObject loanInfo = (JSONObject) loanInfoList.get(i);
                    	   String chnl_source_name = loanInfo.getString("chnl_source_name");//渠道别名
                    	   String curr_per_amt = loanInfo.getString("curr_per_amt");//本地还款金额
                    	   content.append(chnl_source_name).append("贷款本期还款金额为").append(curr_per_amt).append("元，");
					   } 
                       content.replace(content.length()-1,content.length(), "。");
                   }
                   
                    result.put("code", CommUtils.SUCCESS);
					result.put("msg", head.getString("errmsg"));
					result.put("content", content.toString());
                   
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errmsg"));
					result.put("msgCode", head.getString("errcode"));
					result.put("content", head.getString("errmsg"));
				}
			}
		}	
		return result;
	}
	/**
	 * 查询还款信息2   您申请的**贷款全部还款金额为**元
	 * @param param
	 * @return
	 */
	public JSONObject queryRepayment2(JSONObject param) {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			StringBuffer content = new StringBuffer("您申请的");
			String url = CommUtils.URL_YCLOANS_A1023026+token;
			String urlPostJson = HttpUtils.URLPostJson(url,param.toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				if(head.getString("errcode").equals("E00000000000")){//交易成功
					JSONArray loanInfoList = fromObject.getJSONArray("loanInfoList");
					if(null!=loanInfoList&&loanInfoList.size()>0){
						for (int i = 0; i < loanInfoList.size(); i++) {
							JSONObject loanInfo = (JSONObject) loanInfoList.get(i);
							String chnl_source_name = loanInfo.getString("chnl_source_name");//渠道别名
							String all_amt = loanInfo.getString("all_amt");//本期还款金额
							content.append(chnl_source_name).append("贷款全部还款金额为").append(all_amt).append("元，");
						} 
						content.replace(content.length()-1,content.length(), "。");
					}
					
					result.put("code", CommUtils.SUCCESS);
					result.put("msg", head.getString("errmsg"));
					result.put("content", content.toString());
					
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errmsg"));
					result.put("msgCode", head.getString("errcode"));
					result.put("content", head.getString("errmsg"));
				}
			}
		}	
		return result;
	}
	/**
	 * 查询还款信息3   您申请的**贷款还款银行卡为尾号****的**银行卡
	 * @param param
	 * @return
	 */
	public JSONObject queryRepayment3(JSONObject param) {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			//查询银行卡接口
			Map<String,JSONObject> lmAcctInfoMap = new HashMap<String, JSONObject>();
			String url2 = CommUtils.URL_YCLOANS_A1023025+token;
			String urlPostJson2 = HttpUtils.URLPostJson(url2,param.toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson2)){
				JSONObject fromObject2 = JSONObject.fromObject(urlPostJson2);
				JSONObject head = fromObject2.getJSONObject("head");
				if(head.getString("errcode").equals("E00000000000")){//交易成功
					JSONArray lmAcctInfoList = fromObject2.getJSONArray("lmAcctInfoList");
					if(null!=lmAcctInfoList&&lmAcctInfoList.size()>0){
						for (int i = 0; i < lmAcctInfoList.size(); i++) {
							JSONObject lmAcctInfo = (JSONObject) lmAcctInfoList.get(i);
							lmAcctInfoMap.put(lmAcctInfo.getString("loan_no"),lmAcctInfo);
						} 
					}
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errmsg"));
					result.put("msgCode", head.getString("errcode"));
					result.put("content", head.getString("errmsg"));
					return result;
				}
			}
			StringBuffer content = new StringBuffer("您申请的");
			String url = CommUtils.URL_YCLOANS_A1023026+token;
			String urlPostJson = HttpUtils.URLPostJson(url,param.toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				if(head.getString("errcode").equals("E00000000000")){//交易成功
					JSONArray loanInfoList = fromObject.getJSONArray("loanInfoList");
					if(null!=loanInfoList&&loanInfoList.size()>0){
						for (int i = 0; i < loanInfoList.size(); i++) {
							JSONObject loanInfo = (JSONObject) loanInfoList.get(i);
							String chnl_source_name = loanInfo.getString("chnl_source_name");//渠道别名
							String loan_no = loanInfo.getString("loan_no");//借据号
							JSONObject lmAcctInfo = lmAcctInfoMap.get(loan_no);
							content.append(chnl_source_name).append("贷款还款银行卡为尾号").append(lmAcctInfo.getString("acct_card_no"))
							.append("的").append(lmAcctInfo.getString("acct_bank_nam")).append("银行卡，");
						} 
						content.replace(content.length()-1,content.length(), "。");
					}
					
					result.put("code", CommUtils.SUCCESS);
					result.put("msg", head.getString("errmsg"));
					result.put("content", content.toString());
					
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errmsg"));
					result.put("msgCode", head.getString("errcode"));
					result.put("content", head.getString("errmsg"));
				}
			}
		}	
		return result;
	}
	/**
	 * 查询还款信息4   您申请的**贷款还款失败原因为**
	 * @param param
	 * @return
	 */
	public JSONObject queryRepayment4(JSONObject param) {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			//查询贷款明细接口
			Map<String,JSONObject> loanInfoMap = new HashMap<String, JSONObject>();
			String url2 = CommUtils.URL_YCLOANS_A1023026+token;
			String urlPostJson2 = HttpUtils.URLPostJson(url2,param.toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson2)){
				JSONObject fromObject2 = JSONObject.fromObject(urlPostJson2);
				JSONObject head = fromObject2.getJSONObject("head");
				if(head.getString("errcode").equals("E00000000000")){//交易成功
					JSONArray loanInfoList = fromObject2.getJSONArray("loanInfoList");
					if(null!=loanInfoList&&loanInfoList.size()>0){
						for (int i = 0; i < loanInfoList.size(); i++) {
							JSONObject loanInfo = (JSONObject) loanInfoList.get(i);
							loanInfoMap.put(loanInfo.getString("loan_no"),loanInfo);
						} 
					}
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errmsg"));
					result.put("msgCode", head.getString("errcode"));
					result.put("content", head.getString("errmsg"));
					return result;
				}
			}
			StringBuffer content = new StringBuffer("您申请的");
			String url = CommUtils.URL_YCLOANS_A1023027+token;
			String urlPostJson = HttpUtils.URLPostJson(url,param.toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				if(head.getString("errcode").equals("E00000000000")){//交易成功
					JSONArray toPayIrList = fromObject.getJSONArray("toPayIrList");
					if(null!=toPayIrList&&toPayIrList.size()>0){
						for (int i = 0; i < toPayIrList.size(); i++) {
							JSONObject toPayIr = (JSONObject) toPayIrList.get(i);
							String loan_no = toPayIr.getString("loan_no");//借据号
							String fail_reason = toPayIr.getString("fail_reason");//还款失败原因
							
							JSONObject loanInfo = loanInfoMap.get(loan_no);
							String chnl_source_name = loanInfo.getString("chnl_source_name");//渠道别名
							content.append(chnl_source_name).append("贷款还款失败原因为").append(fail_reason).append("，");
						} 
						content.replace(content.length()-1,content.length(), "。");
					}
					
					result.put("code", CommUtils.SUCCESS);
					result.put("msg", head.getString("errmsg"));
					result.put("content", content.toString());
					
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errmsg"));
					result.put("msgCode", head.getString("errcode"));
					result.put("content", head.getString("errmsg"));
				}
			}
		}	
		return result;
	}
	/**
	 * 查询还款信息5   您申请的**贷款还款日期为**
	 * @param param
	 * @return
	 * @throws ParseException 
	 */
	public JSONObject queryRepayment5(JSONObject param) throws ParseException {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			StringBuffer content = new StringBuffer("您申请的");
			String url = CommUtils.URL_YCLOANS_A1023026+token;
			String urlPostJson = HttpUtils.URLPostJson(url,param.toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				if(head.getString("errcode").equals("E00000000000")){//交易成功
					JSONArray loanInfoList = fromObject.getJSONArray("loanInfoList");
					if(null!=loanInfoList&&loanInfoList.size()>0){
						for (int i = 0; i < loanInfoList.size(); i++) {
							JSONObject loanInfo = (JSONObject) loanInfoList.get(i);
							String chnl_source_name = loanInfo.getString("chnl_source_name");//渠道别名
							String last_setl_dt = "";
							if(loanInfo.has("last_setl_dt")){
								last_setl_dt = loanInfo.getString("last_setl_dt");//还款日期
								DateTime parseDate = DateTime.parseDate(last_setl_dt);
								last_setl_dt = parseDate.toDateTimeString(DateTime.DEFAULT_DATE_FORMAT_PATTERN2);
							}
							content.append(chnl_source_name).append("".equals(last_setl_dt)?"贷款无还款日期":"贷款还款日期为")
							 .append(last_setl_dt).append("，");
						} 
						content.replace(content.length()-1,content.length(), "。");
					}
					result.put("code", CommUtils.SUCCESS);
					result.put("msg", head.getString("errmsg"));
					result.put("content", content.toString());
					
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errmsg"));
					result.put("msgCode", head.getString("errcode"));
					result.put("content", head.getString("errmsg"));
				}
			}
		}	
		return result;
	}
	/**
	 * 发送短信
	 * @param param
	 * @return
	 * @throws DocumentException 
	 */
	public JSONObject sendMessage(Map<String, String> param) throws DocumentException {
		JSONObject result = new JSONObject();
		String url = CommUtils.URL_SEND_MSG;
		if(StringUtils.isNotEmpty(CommUtils.PARAM_SENDMESSAGE)){
			String[] args = CommUtils.PARAM_SENDMESSAGE.split("&");
			if(null!=args&&args.length>0){
				for (String string : args) {
					param.put(string.split("=")[0], string.split("=")[1]);
				}
			}
		}
		String urlPost = HttpUtils.URLPost(url,param,"UTF-8");
		String code = XmlUtil.getRootText(urlPost);
		if(null!=code&&code.length()>18){
			result.put("code", CommUtils.SUCCESS);
			result.put("msg", "发送成功："+code);
		}else{
			result.put("code", CommUtils.SUCCESS);
			result.put("msg", "发送失败："+code);
		}
		
		return result;
	}
	/**
	 * 获取手机验证码同时发送手机验证码
	 * @param param
	 * @return
	 */
	public JSONObject getCodeAndSend(JSONObject param) {
		String token = getToken();
		JSONObject result = new JSONObject();
		if(null!=token){
			String url = CommUtils.URL_CUSTOMER_040100004+token;
			String urlPostJson = HttpUtils.URLPostJson(url,CommUtils.requestParam(param,"040100004").toString(),"UTF-8");
			if(StringUtils.isNotEmpty(urlPostJson)){
				JSONObject fromObject = JSONObject.fromObject(urlPostJson);
				JSONObject head = fromObject.getJSONObject("head");
				if(head.getString("successCode").equals("E00000")){//交易成功
					JSONObject body = fromObject.getJSONObject("body");
					result.put("code", CommUtils.SUCCESS);
					result.put("msg", head.getString("errorMessage"));
					result.put("telcode", body.getString("telcode"));
				}else{
					result.put("code", CommUtils.FAIL);
					result.put("msg", head.getString("errorMessage"));
					result.put("msgCode", head.getString("successCode"));
					result.put("content", head.getString("errorMessage"));
				}
			}
		}
		return result;
	}
	/**
	 * 机器人ivr接口
	 * @param cmisServerForm
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public JSONObject serverRobot(CmisServerForm cmisServerForm) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		String text = cmisServerForm.getText();
		if(StringUtils.isNotEmpty(text)){
			Map<String, String> questionivrnodemap = CommUtils.questionIvrnodeMap;
			Set<String> keySet = questionivrnodemap.keySet();
			for (String string : keySet) {
				if(text.contains(string)){
					result.put("action","jump");
					result.put("ivr_node",questionivrnodemap.get(string));
					RedisAPI.del(cmisServerForm.getCall_id());//移除session会话id
					return result;
				}
			}
		}
		JSONObject robotObject = this.getObjectFromRobot(cmisServerForm);
		if(!robotObject.getString("msg").equals("success")){
			result.put("action","hangup");
			return result;
		}
		JSONObject clarify = robotObject.getJSONObject("clarify");
		String flag = "play_tts";
		String tts_text = "";
		Integer min = 1;
		Integer max = 18;
		String terminate_key = "#";
		String ivr_node = CommUtils.IVR_NODE;
		String sound_path = "";
		Integer receive_keys_timeout = 10;
		Integer retry_times = 3;
		if(StringUtils.isNotEmpty(CommUtils.RECEIVE_KEYS_TIMEOUT))
			receive_keys_timeout = Integer.valueOf(CommUtils.RECEIVE_KEYS_TIMEOUT);
		if(StringUtils.isNotEmpty(CommUtils.RETRY_TIMES))
		    retry_times = Integer.valueOf(CommUtils.RETRY_TIMES);
		boolean can_interrupt = false;
		if(!clarify.isNullObject()){
			tts_text = clarify.getString("answer");
			JSONObject DATA = clarify.getJSONObject("DATA");
			if(!DATA.isNullObject()&&!DATA.isEmpty()){
				min = 1;
				max = Integer.valueOf(DATA.getString("keyboard_length"));
				flag = "receive_keys";
			}
		}else{
			JSONObject info = robotObject.getJSONArray("info").getJSONObject(0);
			tts_text = info.getString("answer");
			if(info.has("link")&&info.getString("link").contains("#zjzrg"))
			flag = "jump";
		}
		if(tts_text.contains("BREAK#FORBID"))
			can_interrupt=false;
		if(tts_text.contains("ENDCALL#TRUE"))
			flag = "hangup";
		tts_text = tts_text.replace("TTS#","").
				replace("ENDCALL#TRUE", "").replace("BREAK#FORBID","").replace("/","").replace("\n", "");
		if("play_tts".equals(flag)){//播放TTS语音
			result.put("action","play_tts");
			result.put("tts_text",tts_text);
			result.put("can_interrupt",can_interrupt);
		}else if("play_sound".equals(flag)){//播放文件语音
			result.put("action","play_sound");
			result.put("sound_path:",sound_path);
			result.put("can_interrupt:",can_interrupt);
		}else if("receive_keys".equals(flag)){//采集按键
			result.put("action","receive_keys");
			result.put("tts_text",tts_text);
			result.put("min",min);
			result.put("max",max);
			result.put("receive_keys_timeout",receive_keys_timeout);
			result.put("retry_times",retry_times);
			result.put("terminate_key",terminate_key);
		}else if("hangup".equals(flag)){//挂机
			result.put("action","hangup");
			result.put("tts_text",tts_text);
			RedisAPI.del(cmisServerForm.getCall_id());//移除session会话id
		}else if("jump".equals(flag)){//跳转ivr节点
			result.put("action","jump");
			result.put("tts_text",tts_text);
			result.put("ivr_node",ivr_node);
			RedisAPI.del(cmisServerForm.getCall_id());//移除session会话id
		}else if("break".equals(flag)){//中断放音
			result.put("action","break");
		}
		return result;
	}
	/**
	 * 请求bot接口得相应对象
	 * @param cmisServerForm
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public JSONObject getObjectFromRobot(CmisServerForm cmisServerForm) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String call_id = cmisServerForm.getCall_id();
		String contact_id = cmisServerForm.getContact_id();
		boolean session_start = false;
		session_start = cmisServerForm.isSession_start();
		String sessionId = "";
		if(RedisAPI.exists(call_id)){
			if(session_start){
				 sessionId = UUID.randomUUID().toString();
				 RedisAPI.set(call_id,sessionId);
			}else{
			     sessionId = RedisAPI.get(call_id);
			}
		}else{
			sessionId = UUID.randomUUID().toString();
			RedisAPI.set(call_id,sessionId);
		}
		String text = "";//文本内容
		if(StringUtils.isNotEmpty(cmisServerForm.getText()))
		    text = cmisServerForm.getText();//文本内容
		if(StringUtils.isNotEmpty(cmisServerForm.getInput_keys()))
			text = cmisServerForm.getInput_keys();
		String url = CommUtils.URL_ROBOT;
		String account = contact_id;
		String pubkey = CommUtils.PUBKEY;
		String privatekey = CommUtils.PRIVATEKEY;
		String question = text;
		String rowStr = account+pubkey+question+sessionId+privatekey;
		String base64Str = DatatypeConverter.printBase64Binary(rowStr.getBytes("UTF-8"));
		/*String base64Str = encoder.encode(rowStr.getBytes("UTF-8"));
		BASE64Encoder encoder = new BASE64Encoder();
		base64Str = base64Str.replace("\r", "");
        base64Str = base64Str.replace("\n", "");*/
	    String sign = MD5Util.md5(base64Str);
	    JSONObject param = new JSONObject();
	    param.put("account",account);
	    param.put("pubkey",pubkey);
	    param.put("question",question);
	    param.put("sessionId",sessionId);
	    param.put("sign",sign); 
        String urlPostJson = HttpUtils.URLPostJson(url,param.toString(),"UTF-8");
        return JSONObject.fromObject(urlPostJson);
	}
	
}
