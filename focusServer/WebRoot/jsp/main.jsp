<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'test.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
2014-11-20
 

  <script type="text/javascript">
  		function myrefresh(){
  			//document.all["zoom"].style.zoom = parseInt(document.all["zoom"].style.zoom)+'%';
			window.location.reload();
		}

  		var refreshTime=2*1000;
		//setTimeout('myrefresh()',1000*1);
		var int=setInterval('myrefresh()',refreshTime);
		function stopRf(){
			if(int>0){
				int=window.clearInterval(int);
				document.all['stopRf'].innerText="开始";
			}else{
				int=setInterval('myrefresh()',refreshTime);
				document.all['stopRf'].innerText="停止";
			}
		}
	var zoomRate = 20;
	var maxRate = 500;
	var minRate = 50;
	var currZoom = 100;
	SetCookie("zoomVal","100");
	//document.onkeypress = getKey;
	//window.onload = initZoom;
	
	function GetCookie(name){
		if (document.cookie != "") {
			zoomc = document.cookie.split("; ");
			for (var i=0; i < zoomc.length; i++) {
                zoomv = zoomc[i].split("="); 
                if (zoomv[0] == name) {
				    return  unescape(zoomv[1]);
                }
			}        
		}else{
			return "";
		}
	}

	function SetCookie(name,value){
		document.cookie = name + "=" + escape (value)+";";
	}

	function zoomInOut(contentid, how) {
		int=window.clearInterval(int);//放大缩小时停止刷新
		if(GetCookie("zoomVal") != null && GetCookie("zoomVal") != ""){
			document.all[contentid].style.zoom = GetCookie("zoomVal");
			currZoom=GetCookie("zoomVal");
		}
		else{
			document.all[contentid].style.zoom = '100%'; 

			currZoom = '100%';
		}
		if (((how == "in") && (parseInt(currZoom) >= maxRate)) || ((how == "out") && (parseInt(currZoom) <= minRate)) ) {
			return; 
		}
		if (how == "in") {
			document.all[contentid].style.zoom = parseInt(document.all[contentid].style.zoom)+zoomRate+'%';
		}
		else {
			document.all[contentid].style.zoom = parseInt(document.all[contentid].style.zoom)-zoomRate+'%'
		}
		SetCookie("zoomVal",document.all[contentid].style.zoom);
		if(document.all[contentid].style.zoom == '100%'){
			int=setInterval('myrefresh()',refreshTime);//恢复100%自动开启刷新
		}
		showCurrZoom(contentid);
	}
	function showCurrZoom(contentid) {
	    document.all['showZoom'].innerText = document.all[contentid].style.zoom;
	}
 //-->
 
 	function openDialog1(w,h,id){
		var left,top;
		left = (screen.availWidth - w)/2;
		top = (screen.availHeight - h)/2;
	//	window.open("addmod.do?oper=mod&id="+id,"_blank");
		window.open("curveactiveformprojDialog.jsp?id="+id,"_blank","left="+left+",top="+top+",width="+w+",height="+h+",resizable=yes,scrollbars=yes,location=yes");
	}
 
 	
	 //打开曲线索引页面
	 function curveactiveformproj(siteid){
			window.open('${ctx}/biz/datamanage/curve/curveactiveformproj.jsp?siteid='+siteid+'','_blank','height=600,width=1000,location=no,resizable=yes');
			//window.location("${ctx}/biz/datamanage/curve/curveactiveformprojDialog.jsp");
		}
	 
	 //setTimeout('myrefresh()',1000*2);
	function screener(){
		 try{ 
			var filePath = "c://screener.txt";
			var fso = new ActiveXObject("Scripting.FileSystemObject");
			var file = fso.GetFile(filePath);
			// 打开文件
			var ts = fso.OpenTextFile(filePath, 1);
			// 读取文件一行内容到字符串
			var fileContent = ts.ReadLine();
			//new ActiveXObject("Wscript.Shell").run(fileContent);
			new ActiveXObject("Wscript.Shell").run("E://screener_plugin.exe");
		 }catch(e){   
	  	 	alert("调用出错，请将文件放到E盘目录下");
	  	 	//screenerSet(700,300);
	    }   
	}
    function prWrite(){  
	    alert("start");  
	    var fso = new ActiveXObject("Scripting.FileSystemObject");   
	    var a = fso.CreateTextFile("c://", true);   
	    a.WriteLine("This is a test.");   
	    a.Close();  
    }  
	
    function screenerSet(w,h){
    		var width = screen.availWidth;
			var height = screen.availHeight;
			var left = (width - w) / 2;
			var top = (height - h) / 2;
			var url = "<%=basePath %>jsp/fileSet.jsp";
			alert(url);
			window.open(url,"_blank","left="+left+",top="+top+",width="+w+",height="+h+",resizable=yes,satusbar=no,scrollbars=yes");
			
    	//window.open('${ctx}/biz/datamanage/monitor/fileSet.jsp','_blank','height=400,width=700,location=no,resizable=yes');
    }
   

 
   </script>

  <body>
  <table width="90%" border="0" align="center" style="border:0px solid blue";>
		<tr>
			<td align="center">
				<input type="button" value="停止" onclick="stopRf()" name="stopRf" />
				<input type="button" value="缩小" onclick="zoomInOut('zoom','out')" />
				<font style="font-weight: bolder; font-size: 10pt;"><span id="showZoom" value="100%">100%</span></font>
				<input type="button" value="放大" onclick="zoomInOut('zoom','in')" />
				<input type="button" value="截图" onclick="screener()" />
			</td>
		</tr>
  </table>
<div id="zoom">
      <table width="90%" border="0" align="center" style="border:0px solid blue";>
      <tr>
      <td height="74">&nbsp;</td>
      <td valign="top">
      <table width="490" border="0" cellpadding="0" cellspacing="0" style="border:1px solid blue">
        <tr>
          <td width="80"></td>
          <td width="100"></td>
          <td width="270"></td>
          <td width="93"></td>
          <td width="130"></td>
          <td width="24"></td>
        </tr>
      
        <tr>
          <td height="164" align="left" valign="middle"><table width="90" height="23" border="0">
      <tr>
        <td width="60" height="17"><font size="2">正常状态:</font></td>
        <td width="15" >111</td>
      </tr>
      <tr><td height="10"></td><td height="10"></td></tr>
      <tr>
        <td width="60" height="17"><font size="2">dsfsdf</font></td>
        <td width="15" >111</td>
      </tr>
      <tr><td height="10"></td><td height="10"></td></tr>
      <tr>
        <td width="60" height="17"><font size="2">sdfdsf</font></td>
        <td width="15" >111</td>
      </tr>
      <tr><td height="10"></td><td height="10"></td></tr>
      <tr>
        <td width="60" height="17"><font size="2">sdfsdf</font></td>
        <td width="15" >111</td>
      </tr>
    </table>
          
            
      </td>
      <td>&nbsp;</td>
    </tr>
   
  </table>
      
        
  </div>
  </body>
</html>