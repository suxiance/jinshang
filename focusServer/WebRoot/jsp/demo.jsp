<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@page isELIgnored="false" %>    
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
<title>title</title>
<meta http-equiv=”content-type” content=”text/html;charset=GBK″ />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="./js/jquery-1.4.2.min.js"></script>
 <script type="text/javascript" src="./js/html2canvas.js"></script>  
<script type="text/javascript" charset="gbk">
/* $(function(){
        var x=window.screenLeft?window.screenLeft: window.screenX ; 
        var y=window.screenTop?window.screenTop: window.screenY; 
        var w=window.innerWidth;
        var h=window.innerHeight;
        alert(x+" "+y); 
        alert(w+" "+h); 
 
});

function cut(){
        var x=window.screenLeft?window.screenLeft: window.screenX ; 
        var y=window.screenTop?window.screenTop: window.screenY; 
        var w=window.innerWidth;
        var h=window.innerHeight;
   $.ajax({
		type : 'POST',
		url : 'coreData.do?method=demo',
		data: {'left':x,'top':y,'w':w,'h':h},
		datatype : 'json',
		success : function(data) {
			alert(data);
		}
	});

} 
}); */ 
$(function(){

	$("#creat").click(function(){
    html2canvas($("body"),{ // $(".myImg")是你要复制生成canvas的区域，可以自己选
               onrendered:function(canvas){
              dataURL =canvas.toDataURL("image/png");
              $("body").append(canvas);
              console.log(dataURL);

              //下载图片
              $('#down_button').attr( 'href' ,  dataURL ) ;
              $('#down_button').attr( 'download' , 'myjobdeer.png' ) ;
          },
          width:320,
          height:400
      });
  });

});

</script>


	
</head>
<body>	
         <a type="button" id="down_button">下载</a><button id="creat">点击复制</button>
             <div class="myImg" style="position:relative;">
                <textarea rows="" cols=""></textarea>
             </div>
</body>
