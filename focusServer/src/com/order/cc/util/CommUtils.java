package com.order.cc.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CommUtils {
	 static Logger logger = Logger.getLogger("focusServer");
	 
	public static final String URL_TOKEN = PropertiesUtil.getProperty("URL_TOKEN");
	public static final String TOKEN_USERID = PropertiesUtil.getProperty("TOKEN_USERID");
	public static final String TOKEN_PASSWORD = PropertiesUtil.getProperty("TOKEN_PASSWORD");
	
	//客户中心查询客户基本信息地址
	public static final String URL_CUSTOMER_040100063 = PropertiesUtil.getProperty("URL_CUSTOMER_040100063");
	//手机验证码验证地址
	public static final String URL_CUSTOMER_040100031 = PropertiesUtil.getProperty("URL_CUSTOMER_040100031");
	//更换手机号码
	public static final String URL_CUSTOMER_040100054 = PropertiesUtil.getProperty("URL_CUSTOMER_040100054");
	//获取手机验证码
	public static final String URL_CUSTOMER_040100004 = PropertiesUtil.getProperty("URL_CUSTOMER_040100004");
	//银行卡查询接口
	public static final String URL_YCLOANS_A1023025 = PropertiesUtil.getProperty("URL_YCLOANS_A1023025");
	//贷款明细接口
	public static final String URL_YCLOANS_A1023026 = PropertiesUtil.getProperty("URL_YCLOANS_A1023026");
	//扣款失败原因查询接口
	public static final String URL_YCLOANS_A1023027 = PropertiesUtil.getProperty("URL_YCLOANS_A1023027");
	//贷款信息查询
	public static final String URL_CMIS_A1013061 = PropertiesUtil.getProperty("URL_CMIS_A1013061");
	//发送短信地址
	public static final String URL_SEND_MSG = PropertiesUtil.getProperty("URL_SEND_MSG");
	//请求头参数配置
	public static final String PARAM_HEAD = PropertiesUtil.getProperty("PARAM_HEAD");
	//核身参数配置
	public static final String PARAM_IDENTITYVERIFY = PropertiesUtil.getProperty("PARAM_IDENTITYVERIFY");
	//验证手机验证码参数配置
	public static final String PARAM_CODEVERIFY = PropertiesUtil.getProperty("PARAM_CODEVERIFY");
	//修改手机号码参数配置
	public static final String PARAM_CHANGEPHONENO = PropertiesUtil.getProperty("PARAM_CHANGEPHONENO");
	//查询还款信息参数配置
	public static final String PARAM_QUERYREPAYMENT = PropertiesUtil.getProperty("PARAM_QUERYREPAYMENT");
	//查询贷款信息参数配置
	public static final String PARAM_QUERYLOANINFO = PropertiesUtil.getProperty("PARAM_QUERYLOANINFO");
	//发送短信参数配置
	public static final String PARAM_SENDMESSAGE = PropertiesUtil.getProperty("PARAM_SENDMESSAGE");
	//获取手机验证码参数配置
	public static final String PARAM_GETCODEANDSEND = PropertiesUtil.getProperty("PARAM_GETCODEANDSEND");
	
	//机器人接口url
	public static final String URL_ROBOT = PropertiesUtil.getProperty("URL_ROBOT");
	//私钥
	public static final String PRIVATEKEY = PropertiesUtil.getProperty("PRIVATEKEY");
	//公钥
	public static final String PUBKEY = PropertiesUtil.getProperty("PUBKEY");
	//ivr节点转人工
	public static final String IVR_NODE = PropertiesUtil.getProperty("IVR_NODE");
	//问题及ivr节点
	public static final String QUESTION_IVRNODE = PropertiesUtil.getProperty("QUESTION_IVRNODE");
	//返回系统升级中错误码
	public static final String ERROR_CODE = PropertiesUtil.getProperty("ERROR_CODE");
	//按键超时时间
	public static final String RECEIVE_KEYS_TIMEOUT = PropertiesUtil.getProperty("RECEIVE_KEYS_TIMEOUT");
	//按键重试最大次数
	public static final String RETRY_TIMES = PropertiesUtil.getProperty("RETRY_TIMES");
	 
	public static final String SUCCESS = "0";//成功
	 
	public static final String FAIL = "1";//失败
	
	public static final String ERROR = "2";//系统异常
	
	public static final String ERROR_CONTENT = "系统升级中";//
	
	public static final List<String> errorlist = new ArrayList<String>();
	public static final Map<String,String> questionIvrnodeMap = new HashMap<String, String>();
	static{
		errorlist.add("E00001");
		errorlist.add("E00023");
		if(StringUtils.isNotEmpty(ERROR_CODE)){
			String[] split = ERROR_CODE.split(";");
			if(null!=split&&split.length>0){
				for (String string : split) {
					errorlist.add(string);
				}
			}
		}
		if(StringUtils.isNotEmpty(QUESTION_IVRNODE)){
			String[] split = QUESTION_IVRNODE.split(";");
			if(null!=split&&split.length>0){
				for (String string : split) {
					if(string.split(",").length>1)
					questionIvrnodeMap.put(string.split(",")[0], string.split(",")[1]);
				}
			}
		}
	}
	 
    /**
	 * 返回码及描述
	 * @author sxc
	 */
	public static enum  CODE_DESCRIBE{//字段名
		//字符串
		E00000("交易成功","E00000"),
		E00001(ERROR_CONTENT,"E00001"),
		E00002("该客户/手机号已被冻结","E00002"),
		E00003("该账号跟上次登录账号终端ID不一致，请重新进行信息验证","E00003"),
		E00004("验证码不正确，请重新获取验证码","E00004"),
		E00005("注册失败，该手机号已存在，请尝试重置密码","E00005"),
		E00006("该手机号对应的客户已进行身份验证，且通过，不允许重新验证","E00006"),
		E00007("该手机号对应的客户已进行身份验证，且验证未通过","E00007"),
		E00008("原APP密码不正确，请重新输入","E00008"),
		E00009("该客户存在APP密码，请输入APP密码","E00009"),
		E000010("原交易密码不正确，请重新输入","E000010"),
		E000011("该客户未进行身份联网核查认证，不允许绑定银行卡信息","E000011"),
		E000012("该客户身份联网核查失败，不允许绑定银行卡信息","E000012"),
		E000013("绑定失败，不支持的银行卡","E000013"),
		E000014("绑定失败，同一张银行卡，5天内绑定超过12次","E000014"),
		E000015("解绑失败，不存在该银行卡","E000015"),
		E000016("解绑失败，主卡不能解绑，请先更换主卡","E000016"),
		E000017("主卡设定失败，不存在该银行卡","E000017"),
		E000018("主卡设定失败，该银行卡处于解绑状态，请先绑定","E000018"),
		E000019("主卡设定失败，借记卡不允许设置为主卡。","E000019"),
		E000020("该手机号跟录入的证件号码不匹配","E000020"),
		E000021("该证件号码已被其他手机号注册，不允许重复注册","E000021"),
		E000022("该客户不存在","E000022"),
    	E00023(ERROR_CONTENT,"E00023"),
    	E00000000000("交易成功","E00000000000"),
    	A10200I00001(ERROR_CONTENT,"A10200I00001"),
    	A10200I00100(ERROR_CONTENT,"A10200I00100"),
    	A10200I20000("贷款已结清","A10200I20000"),
    	A10200I20001("借据号不存在","A10200I20001"),
    	A10200I20002("账户信息不存在","A10200I20002"),
    	A10200I20003("还款金额不能小于等于零","A10200I20003"),
    	A10200I20004("还款授权号不存在","A10200I20004"),
    	A10200C90001(ERROR_CONTENT,"A10200C90001"),
    	A10200C90002("贷款信息验证错误","A10200C90002"),
    	A10200I90003("核算系统日间批扣中","A10200I90003"),
    	A10200I90004("核算系统日终中","A10200I90004"),
    	A10200Z99999(ERROR_CONTENT,"A10200Z99999")
    	;
        private String value;
        private String key;
        private CODE_DESCRIBE(String value,String key){
            this.value = value;
            this.key = key;
        }
        public static String get(String key) {  
        	CODE_DESCRIBE[] enums = values();  
            for (CODE_DESCRIBE enu : enums) {  
                if (enu.getKey().equals(key)) {  
                    return enu.getValue();  
                }  
            }  
            return null;  
        } 
        
        public static String getKey(String val) {  
        	CODE_DESCRIBE[] enums = values();  
            for (CODE_DESCRIBE enu : enums) {  
                if (enu.getValue().equals(val)) {  
                    return enu.getKey();  
                }  
            }  
            return null;  
        } 
        
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
	}   
	  /**
	   * 发送json数据到前端 
	   * @param response
	   * @param transHeader
	   * @param jsonObject
	   */
	  public static void sendJsonResData (HttpServletResponse response,
			JSONObject data) {
		    response.setCharacterEncoding("UTF-8");
		    JSONObject jsonObject = new JSONObject();
		    jsonObject.put("action", "set_vars");
		    if(!data.has("code")){
		    	data.put("code", CommUtils.ERROR);
		    	data.put("msg", CommUtils.ERROR_CONTENT);
		    	data.put("content", CommUtils.ERROR_CONTENT);
		    }
		    if(data.has("msgCode")&&errorlist.contains(data.getString("msgCode"))){
		    	data.put("code", CommUtils.ERROR);
		    	data.put("content", CommUtils.ERROR_CONTENT);
		    }
		    data.remove("msgCode");
		    jsonObject.put("vars", data);
		    try {
			PrintWriter writer = response.getWriter();
			writer.print(jsonObject);
			logger.info("返回json字符串-"+jsonObject.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		   }
	  
	  }
	  
	  /**
	   * 生成请求头信息
	   * @return
	   */
	  public static JSONObject requestParam(JSONObject param,String tranCode){
		  DateTime dateTime = new DateTime(new Date());
		  String date = dateTime.toDateTimeString(DateTime.DEFAULT_DATE_FORMAT_PATTERN);
		  String time = dateTime.toDateTimeString(DateTime.DEFAULT_TIME_FORMAT_PATTERN);
		  String datetime = dateTime.toDateTimeString(DateTime.DEFAULT_DATE_TIME_FORMAT_PATTERN);
		  JSONObject jsonObject = new JSONObject();
		  JSONObject head = new JSONObject();
		  head.put("tranCode",tranCode);
		  head.put("serno",UUID.randomUUID().toString());
		  head.put("tranDate",date);
		  head.put("tranTime",time);
		  head.put("chlGrp","");
		  head.put("bchCde","");
		  head.put("starecord","");
		  head.put("page","");
		  head.put("version","1.0");
		  head.put("trantime",datetime);
		  head.put("sysId","A402");
		  head.put("tradeCode",tranCode);
		  addParameterParam(head,PARAM_HEAD);
		  jsonObject.put("head",head);
		  jsonObject.put("body",param);
		  return jsonObject;
	  }
	  /**
	   * 将配置文件的参数用转成json对象的key-value
	   * @param jsonObject
	   * @param paramStr
	   */
	  public static void addParameterParam(JSONObject jsonObject,String paramStr) {
		    if(StringUtils.isEmpty(paramStr)||null == jsonObject){
		    	return;
		    }
		    String[] args = paramStr.split("&");
			if(null!=args&&args.length>0){
				for (String string : args) {
					jsonObject.put(string.split("=")[0], string.split("=")[1]);
				}
			}
	  }
	  
	  
	  public static void sendResData (HttpServletResponse response,String data) {
		  response.setCharacterEncoding("UTF-8");
		  try {
			  PrintWriter writer = response.getWriter();
			  writer.print(data);
			  logger.info("返回结果-"+data);
		  } catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  
	  }
	  /**
	   * 获取接口传递的JSON数据
	   * @explain
	   * @param request HttpServletRequest对象
	   * @return JSON格式数据
	   */
	  public static JSONObject getJsonReqData (HttpServletRequest request) {
		  // 获取Request对象
		  StringBuffer sb = new StringBuffer();
		  try {
			  request.setCharacterEncoding("UTF-8");
			  // json格式字符串
			  String jsonStr = "";
			  // 获取application/json格式数据，返回字符流
			  BufferedReader reader = request.getReader();
			  // 对字符流进行解析
			  while ((jsonStr = reader.readLine()) != null) {
				  sb.append(jsonStr);
			  }
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
		  // 将json字符串（jsonStr）-->json对象（JSONObject）
		  logger.info("请求json字符串-"+sb.toString());
		  JSONObject jo = JSONObject.fromObject(sb.toString()); 
		  return jo;
	  }
	  
	  /**
	   * 给对象赋值  下划线转驼峰
	   * by sxc
	   * @param obj
	   * @param map
	   */
	  public static void setObjectFromMap(Object obj,Map<String,Object> map){   
	        // 得到类对象
	        Class objCla = obj.getClass();
	        /*
	         * 得到类中的所有属性集合
	         */
	        Field[] fs = objCla.getDeclaredFields();
	        for(int i = 0;i < fs.length;i++){
	            Field f = fs[i];
	            f.setAccessible(true); // 设置些属性是可以访问的
	            String type = f.getType().toString();// 得到此属性的类型
	            if(type.endsWith("String")||type.endsWith("BigDecimal")){
	                try {
						f.set(obj,map.get(underscoreName(f.getName())));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  // 给属性设值
	            }else{
	                System.out.println(f.getType() + "类型不能被赋值");
	            }
	        }
	        
	    }
     
	  public static List<String> readCsv(String filepath) {
	        File csv = new File(filepath); // CSV文件路径
	        csv.setReadable(true);//设置可读
	        csv.setWritable(true);//设置可写
	        BufferedReader br = null;
	        try {
	            br = new BufferedReader(new FileReader(csv));
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        String line = "";
	        String everyLine = "";
	        List<String> allString = new ArrayList<String>();
	        try {
	            while ((line = br.readLine()) != null) // 读取到的内容给line变量
	            {
	                everyLine = line;
	                allString.add(everyLine);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return allString;
	        
	    } 
	  /**
	     * 从网络Url中下载文件
	     * @param urlStr
	     * @param fileName
	     * @param savePath
	     * @throws IOException
	     */
	    public static void  downLoadFromUrl(String urlStr,String filePath) throws IOException{
	        URL url = new URL(urlStr);  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	                //设置超时间为3秒
	        conn.setConnectTimeout(3*1000);
	        //防止屏蔽程序抓取而返回403错误
	        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

	        //得到输入流
	        InputStream inputStream = conn.getInputStream();  
	        //获取自己数组
	        byte[] getData = readInputStream(inputStream);    

	        //文件保存位置
	        /*File saveDir = new File(savePath);
	        if(!saveDir.exists()){
	            saveDir.mkdir();
	        }
	        File file = new File(saveDir+File.separator+fileName);    */
	        File file = new File(filePath);  
	        File fileParent = file.getParentFile();  
	        if(!fileParent.exists()){  
	            fileParent.mkdirs();  
	        }  
	        file.createNewFile();
	        
	        FileOutputStream fos = new FileOutputStream(file);     
	        fos.write(getData); 
	        if(fos!=null){
	            fos.close();  
	        }
	        if(inputStream!=null){
	            inputStream.close();
	        }

	    }


	    /**
	     * 从输入流中获取字节数组
	     * @param inputStream
	     * @return
	     * @throws IOException
	     */
	    public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	        while((len = inputStream.read(buffer)) != -1) {  
	            bos.write(buffer, 0, len);  
	        }  
	        bos.close();  
	        return bos.toByteArray();  
	    } 
        /**
         * 时间戳转时间字符串	    
         * @param timestamp
         * @return
         */
	    public static String dateTimefmt(long timestamp){
			return timestamp == 0?"":new DateTime(timestamp).toDateTimeString();
		}
	    /**
	     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	     * 例如：HELLO_WORLD->HelloWorld
	     * @param name 转换前的下划线大写方式命名的字符串
	     * @return 转换后的驼峰式命名的字符串
	     */
	    public static String camelName(String name) {
	        StringBuilder result = new StringBuilder();
	        // 快速检查
	        if (name == null || name.isEmpty()) {
	            // 没必要转换
	            return "";
	        } else if (!name.contains("_")) {
	            // 不含下划线，仅将首字母小写
	            return name.toLowerCase();
	        }
	        // 用下划线将原始字符串分割
	        String camels[] = name.split("_");
	        for (String camel :  camels) {
	            // 跳过原始字符串中开头、结尾的下换线或双重下划线
	            if (camel.isEmpty()) {
	                continue;
	            }
	            // 处理真正的驼峰片段
	            if (result.length() == 0) {
	                // 第一个驼峰片段，全部字母都小写
	                result.append(camel.toLowerCase());
	            } else {
	                // 其他的驼峰片段，首字母大写
	                result.append(camel.substring(0, 1).toUpperCase());
	                result.append(camel.substring(1).toLowerCase());
	            }
	        }
	        return result.toString();
	    }
	    /**
	     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
	     * 例如：HelloWorld->HELLO_WORLD
	     * @param name 转换前的驼峰式命名的字符串
	     * @return 转换后下划线大写方式命名的字符串
	     */
	    public static String underscoreName(String name) {
	        StringBuilder result = new StringBuilder();
	        if (name != null && name.length() > 0) {
	            // 将第一个字符处理成大写
	            result.append(name.substring(0, 1).toUpperCase());
	            // 循环处理其余字符
	            for (int i = 1; i < name.length(); i++) {
	                String s = name.substring(i, i + 1);
	                // 在大写字母前添加下划线
	                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
	                    result.append("_");
	                }
	                // 其他字符直接转成大写
	                result.append(s.toUpperCase());
	            }
	        }
	        return result.toString();
	    }
		
	public static void testGetBot() throws UnsupportedEncodingException{
		    String url = "http://20.4.32.20:8000/common/query";
		    //String url = "http://20.4.32.20:8000/common/query?pubkey=37GRztsxSob6%2Bn7mp%2BzwMIvvlFM99QSSCS0HdaDfATM&question=11&sign=eb8fae37bf8565a1649f683a20c4f157&account=18646589555&sessionId=18646589555";
		    String pubkey="37GRztsxSob6+n7mp+zwMIvvlFM99QSSCS0HdaDfATM";
			String privateKey = "abadf3c1bbcb0cad13a10df71c19be80";
			String sessionId = "18646589555";
			String question = "你好";
			String pubkeyEncode = URLEncoder.encode(pubkey,"UTF-8");
			String questionEncode = URLEncoder.encode(question,"UTF-8");
		   String s = "18646589555"+pubkey+question+sessionId+privateKey;
		   System.out.println(s);
	       String base64Str = DatatypeConverter.printBase64Binary(s.getBytes("UTF-8"));
	      /* BASE64Encoder encoder = new BASE64Encoder();
			String base64Str = encoder.encode(s.getBytes());
	                base64Str = base64Str.replace("\r", "");
	                base64Str = base64Str.replace("\n", "");*/
	       String md5 = MD5Util.md5(base64Str);
	       System.out.println(md5);
	       //JSONObject param = new JSONObject();
	       Map<String, String> param = new HashMap<String,String>();
	       param.put("pubkey",pubkeyEncode);
	       param.put("question",questionEncode);
	       param.put("account","18646589555");
	       param.put("sign",md5); 
	       param.put("sessionId",sessionId);
	       String urlPostJson = HttpUtils.URLGet(url,param,"UTF-8");
	       JSONObject fromObject = JSONObject.fromObject(urlPostJson);
	       System.out.println(fromObject);
	}    
	public static void testPostBot() throws UnsupportedEncodingException{
		String url = "http://20.4.32.20:8000/common/query";
		String pubkey="37GRztsxSob6+n7mp+zwMIvvlFM99QSSCS0HdaDfATM";
		String privateKey = "abadf3c1bbcb0cad13a10df71c19be80";
		String sessionId = "186465895551";
		String question = "你好";
		String s = "18646589555"+pubkey+question+sessionId+privateKey;
		System.out.println(s);
		String base64Str = DatatypeConverter.printBase64Binary(s.getBytes("UTF-8"));
		System.out.println(base64Str);
		/*BASE64Encoder encoder = new BASE64Encoder();
		String base64Str = encoder.encode(s.getBytes("UTF-8"));
		base64Str = base64Str.replace("\r", "");
		base64Str = base64Str.replace("\n", "");*/
		String md5 = MD5Util.md5(base64Str);
		System.out.println(md5);
		JSONObject param = new JSONObject();
		//Map<String, String> param = new HashMap<String,String>();
		param.put("pubkey",pubkey);
		param.put("question",question);
		param.put("account","18646589555");
		param.put("sign",md5); 
		param.put("sessionId",sessionId);
		String urlPostJson = HttpUtils.URLPostJson(url, param.toString(), "UTF-8");
		JSONObject fromObject = JSONObject.fromObject(urlPostJson);
		System.out.println(fromObject);
	}    
	public static void main(String[] args) throws Exception {
		//CommUtils.testGetBot();
		CommUtils.testPostBot();
	}

}
