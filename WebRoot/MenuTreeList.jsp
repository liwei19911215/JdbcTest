<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'MenuTreeList.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style> 
.test1 li { padding-left:15px; background:url("http://www.86image.cn/statics/images/tpl1/images/icon_fk.gif" ) no-repeat scroll 1px 8px; line-height:22px; list-style-type:none;} 
</style> 
  </head>
  
  <body>
  
  <UL>树
  	<LI>树1
		<UL>树1-1</UL>
		<UL>树1-2</UL>
	</LI>
	<LI>树2</LI>
  </UL>
  
  <ul>123
  	<li>
  		<ul>
		  	<li>1-1</li>
		  	<li>1-2</li>
		  	<li>1-3</li>
		  	<li>1-4</li>
		  	<li>1-5</li>
		</ul>
  	</li>
  	<li>2</li>
  	<li>3</li>
  	<li>4</li>
  	<li>5</li>
  </ul>
    <div id ='id'>
    </div>
  </body>
</html>
<script>


var obj = [
	{
		"title" : "树",
		"url"   : "",
		"children" : 
			[
				{
					"title" : "树1",
					"url"   : "/index.jsp",
					"children" : 
						[
							{
								"title" : "树1-1",
								"url"   : "/index.jsp"
							},
							{
								"title" : "树1-2",
								"url"   : "/index.jsp"
							}
						]
				},
				{
					"title" : "树2",
					"url"   : "/index.jsp"
				}
			]
	}	
]

if(obj.title!=""){
	var div = document.getElementById("id");
	addNode(obj,div,1);
}

function addNode(obj,node,flag){
	if(obj!=null&&obj.length>0){
		flag++;
		if(flag%2==1){
			for(var c=0;c<obj.length;c++){
					var childrenNode = document.createElement("li");
					childrenNode.innerHTML=obj[c].title;
					node.appendChild(childrenNode);
					if(obj[c].children!=undefined){
						addNode(obj[c].children,childrenNode,flag);
					}
			}
		}else{
				var childrenNode = document.createElement("ul");
				for(var c=0;c<obj.length;c++){
					var childrenNode1 = document.createElement("li");
					childrenNode1.innerHTML=obj[c].title;
					childrenNode.appendChild(childrenNode1);
					node.appendChild(childrenNode);
					if(obj[c].children!=undefined){
						addNode(obj[c].children,childrenNode,flag);
					}
				}
		}
		
	}
}
	
</script>
