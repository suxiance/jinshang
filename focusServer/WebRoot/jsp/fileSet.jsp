<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>��ͼ��������</title>
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
	// ���ļ�
	var ts = fso.OpenTextFile(filePath, 1);
	// ��ȡ�ļ�һ�����ݵ��ַ���
	var fileContent = ts.ReadLine();
	alert(fileContent);
	alert(document.getElementById("upfile"));
	//document.getElementById("upfile").value = 1;
	var WshShell = new ActiveXObject("WScript.Shell");
	WshShell.sendKeys(fileContent);
}

//��д��
function $(id) {
	return document.getElementById(id);
}
//��ֵ����(���ǽ��ı����ֵ��ֵfile����)
function set1() {
	//��text1�е�ֵ���Ƶ���������
	window.clipboardData.setData('text', 'fdf');
	//����Shell(��Ҫ��ӵ�����վ�����δǩ����ActiveX�ؼ��Ľ���
	var WshShell = new ActiveXObject("WScript.Shell");
	//�õ�����
	$('f4').focus();
	//Ctrl + A ����
	WshShell.sendKeys("^a");
	//Ctrl + V ����(sendKeys�������ĸ�ֵ�����Ե�����,����ֻ��ģ����̲���)
	WshShell.sendKeys("^v");
}
///////////////////////////////////////////////////////////////////////////s
var filePath = "c://screener.txt";
//��ȡ�ļ�����
function tips() {
	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var file = fso.GetFile(filePath);
	// ���ļ�
	var ts = fso.OpenTextFile(filePath, 1);
	// ��ȡ�ļ�һ�����ݵ��ַ���
	var fileContent = ts.ReadLine();
	document.all['fileSpan'].innerText = fileContent;
}
setTimeout('tips()', 1000 * 1);

//����
function save() {
	var upfile = getrealpath(document.getElementById("upfile")); 
	if (upfile) {
		var fso = new ActiveXObject("Scripting.FileSystemObject");
		var a = fso.CreateTextFile(filePath, true);
		a.WriteLine(upfile);
		a.Close();
		window.close();
	} else {
		alert("��ѡ���ͼ����·����");
	}
}
//ѡ��
function upfileChange() {
	var upfile = getrealpath(document.getElementById("upfile"));      
	var fileExt = upfile.substring(upfile.lastIndexOf(".")).toLowerCase();
    if (!fileExt.match(/.exe/i)) {
	   alert("��ѡ�е��ļ����ǽ�ͼ���,������ѡ��");
       return false;
    }else{
    	alert("upfile"+upfile);
		document.all['fileSpan'].innerText = upfile;
	}
}

function getrealpath(obj){
	//�ж���������� 2          
	var isIE = (document.all) ? true : false;
	var isIE6 = isIE && (navigator.userAgent.indexOf('MSIE 6.0') != -1);         
	var path = null;
	if (isIE6) {
		path = obj.value; 
	}else{
		obj.select();//��ȡ���ϴ����ļ�·��             
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
				<font style="font-weight: bolder; font-size: 10pt; color: red;">��ͼ����������ʾ��<br>
					1�����ؽ�ͼ���������<a href="${ctx}/biz/datamanage/monitor/screener_plugin.exe" target="_blank">��ͼ��������</a><br> 
					2��ѡ�������������·��(screener_plugin.exe)����������桱��ť,������ϡ�<br> </font>
				<font style="font-weight: bolder; font-size: 10pt; color: Grey;">�����"C:\fakepath\xxx"��������������޸ģ�Internetѡ�� -> ��ȫ -> �Զ��弶�� -> �������ļ�������������ʱ��������Ŀ¼·�� -> ѡ�������� -> ȷ����</font>
			</div>
<%--			<form>--%>
				<%-- ����enctype��multipart/form-data���������԰��ļ��е�������Ϊ��ʽ�����ϴ���������ʲô�ļ����ͣ������ϴ���--%>
				
				<br />
				<font style="font-weight: bolder; font-size: 10pt; color: blue;">��ѡ���ͼ����λ�ã� 
				<span id="fileSpan"></span></font>
				<input type="file" id="upfile" size="30" onchange="upfileChange()" style="opacity: 1; zindex: 66; width: 66;" />
				<br />
				<br />
				<div align="center">
					<input type="button" value=" �� �� " onclick="save()">
					<input type="button" value=" �� �� " onclick="window.close();">
				</div>
<%--			</form>--%>
		</table>
	</body>
</html>

