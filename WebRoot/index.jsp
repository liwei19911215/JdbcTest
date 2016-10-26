<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="jspTool/getJsonValue.jsp"></jsp:include>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>

  <body>
      <form>
      		学生id：<input type="text" id="id"/>
      		学生姓名：<input type="text" id="name"/>
      		学生年龄：<input type="text" id="age"/>
      </form>
      <input type="button" id="button" name="show" value="查看学生" onclick="getStudent()"/>
  </body>
</html>


<script>
	
	function getStudent(){
		var id = document.getElementById("id").value;
		var x =  getJsonValue("action.StudentAction","getStudent","id="+id);
		document.getElementById("name").value = x.student.studentName;
		document.getElementById("age").value = x.student.age;
	}
	
	function Person(name){
                this.name=name;
            }
            
            Person.prototype.share=[];
            
            Person.prototype.printName=function(){
                alert(this.name);
            }
            var person1=new Person('Byron');
            var person2=new Person('Frank');
            
            person1.share.push(1);
            person2.share.push(2);
           alert(person2.share); //[1,2]
        	   person2.printName();
</script>