<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<script type="text/javascript" src="js/json2.js"></script>
<script>
	
	var xmlHttpRequest;
	var result;
	
		//默认同步
	function getJsonValue(actionPath,actionMethod,params,flag){
		createXMLHttpRequest();
		xmlHttpRequest.onreadystatechange=callback;
		var id = document.getElementById("id").value;
		var url = "<%=path%>/servlet/ControlAction?action="+actionPath+"&&actionMethod="+actionMethod+"&&" + params+"&&time="+<%=new Date().getTime()%>;
		if(flag==null||flag==""){
			flag = false;
		}
		xmlHttpRequest.open("GET", url, flag);  
  
        //POST方式请求的代码  
       // xmlHttpRequest.open("POST", "OriginalityAjaxAction", true);  
        //POST方式需要自己设置http的请求头  
       // xmlHttpRequest.setRequestHeader("Content-Type",  
        //        "application/x-www-form-urlencoded");  
        //POST方式发送数据  
       // xmlHttpRequest.send("id=" + id);  
  
        //4.发送数据，开始和服务器端进行交互  
        //同步方式下，send这句话会在服务器段数据回来后才执行完  
        //异步方式下，send这句话会立即完成执行  
        xmlHttpRequest.send(null);  
        
        return result;
	}
	function createXMLHttpRequest(){
		if(window.XMLHttpRequest){
			xmlHttpRequest = new XMLHttpRequest();
			if(xmlHttpRequest.overrideMimeType){
				xmlHttpRequest.overrideMimeType("text/xml");
			}
		}else if(window.ActiveXObject){
			var activexName = [ "MSXML2.XMLHTTP", "Microsoft.XMLHTTP" ];  
            for ( var i = 0; i < activexName.length; i++) {  
                try {  
                    //取出一个控件名进行创建，如果创建成功就终止循环  
                    //如果创建失败，回抛出异常，然后可以继续循环，继续尝试创建  
                    xmlHttpRequest = new ActiveXObject(activexName[i]);  
                    break;  
                } catch (e) {  
                }  
            }  
        }  
        //确认XMLHTtpRequest对象是否创建成功  
        if (!xmlHttpRequest) {  
            alert("XMLHttpRequest对象创建失败!!");  
            return false;  
        } else {  
            //0 - 本地响应成功  
            //alert(xmlhttp.readyState);  
            return true;  
        }  
	}
	
	
	
	function callback(){
		if(xmlHttpRequest.readyState==4){
			if(xmlHttpRequest.status==200){
				var resp = xmlHttpRequest.responseText;
				result = JSON.parse(resp);
			}
		}
	}
	
</script>