<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>截图工具设置</title>
		<style type="text/css">
		#fontset{
			font-weight: bolder; font-size: 8pt; color: red;
		}
		</style>
	</head>
	<script type="text/javascript" src="./js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript">
function fileset() {
	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var file = fso.GetFile(filePath);
	// 打开文件
	var ts = fso.OpenTextFile(filePath, 1);
	// 读取文件一行内容到字符串
	var fileContent = ts.ReadLine();
	alert(fileContent);
	alert(document.getElementById("upfile"));
	//document.getElementById("upfile").value = 1;
	var WshShell = new ActiveXObject("WScript.Shell");
	WshShell.sendKeys(fileContent);
}

//简化写法
function $(id) {
	return document.getElementById(id);
}
//崋值方法(就是将文本框的值赋值file对象)
function set1() {
	//将text1中的值复制到剪贴板中
	window.clipboardData.setData('text', 'fdf');
	//创建Shell(需要添加到信任站点或解除未签名的ActiveX控件的禁用
	var WshShell = new ActiveXObject("WScript.Shell");
	//拿到焦点
	$('f4').focus();
	//Ctrl + A 操作
	WshShell.sendKeys("^a");
	//Ctrl + V 操作(sendKeys对于中文赋值操作显得无力,所以只能模拟键盘操作)
	WshShell.sendKeys("^v");
}
///////////////////////////////////////////////////////////////////////////s
var filePath = "c://screener.txt";
//读取文件内容
function tips() {
	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var file = fso.GetFile(filePath);
	// 打开文件
	var ts = fso.OpenTextFile(filePath, 1);
	// 读取文件一行内容到字符串
	var fileContent = ts.ReadLine();
	document.all['fileSpan'].innerText = fileContent;
}
setTimeout('tips()', 1000 * 1);

//保存
function save() {
	var upfile = getrealpath(document.getElementById("upfile")); 
	if (upfile) {
		var fso = new ActiveXObject("Scripting.FileSystemObject");
		var a = fso.CreateTextFile(filePath, true);
		a.WriteLine(upfile);
		a.Close();
		window.close();
	} else {
		alert("请选择截图工具路径。");
	}
}
//选中
function upfileChange() {
	var upfile = getrealpath(document.getElementById("upfile"));      
	var fileExt = upfile.substring(upfile.lastIndexOf(".")).toLowerCase();
    if (!fileExt.match(/.exe/i)) {
	   alert("您选中的文件不是截图软件,请重新选择！");
       return false;
    }else{
    	alert("upfile"+upfile);
		document.all['fileSpan'].innerText = upfile;
	}
}

function getrealpath(obj){
	//判断浏览器类型 2          
	var isIE = (document.all) ? true : false;
	var isIE6 = isIE && (navigator.userAgent.indexOf('MSIE 6.0') != -1);         
	var path = null;
	if (isIE6) {
		path = obj.value; 
	}else{
		obj.select();//获取欲上传的文件路径             
		obj.blur();
		path = document.selection.createRange().text;            
		document.selection.empty();
	}
   return path
}
</script>
	<body>
		<table border="0"  width="96%" align="center">
			<div>
				<font style="font-weight: bolder; font-size: 10pt; color: red;">截图工具设置提示：<br>
					1、下载截图工具软件：<a href="${ctx}/biz/datamanage/monitor/screener_plugin.exe" target="_blank">截图工具下载</a><br> 
					2、选择所下载软件的路径(screener_plugin.exe)，点击“保存”按钮,设置完毕。<br> </font>
				<font style="font-weight: bolder; font-size: 10pt; color: Grey;">如出现"C:\fakepath\xxx"这样的情况，请修改：Internet选项 -> 安全 -> 自定义级别 -> 将本地文件上载至服务器时包含本地目录路径 -> 选“启动” -> 确定。</font>
			</div>
<%--			<form>--%>
				<%-- 类型enctype用multipart/form-data，这样可以把文件中的数据作为流式数据上传，不管是什么文件类型，均可上传。--%>
				
				<br />
				<font style="font-weight: bolder; font-size: 10pt; color: blue;">请选择截图工具位置： 
				<span id="fileSpan"></span></font>
				<input type="file" id="upfile" size="30" onchange="upfileChange()" style="opacity: 1; zindex: 66; width: 66;" />
				<br />
				<br />
				<div align="center">
					<input type="button" value=" 保 存 " onclick="save()">
					<input type="button" value=" 关 闭 " onclick="window.close();">
				</div>
<%--			</form>--%>
		</table>
	</body>
</html>

