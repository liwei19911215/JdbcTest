package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import liwei.ProxyFactory;
import service.impl.StudentImpl;
import service.interfaces.IStudentSV;
import bean.Student;

import com.alibaba.fastjson.JSONObject;

public class StudentAction {

	/**
	 * Constructor of the object.
	 */
	public void getStudent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");  
		String id = request.getParameter("id");
		
		try {
			IStudentSV sv = (IStudentSV) ProxyFactory.getconnManagerProxy().newProxyInstance(new StudentImpl());
			Student[] student = sv.getStudentById(id);
			JSONObject obj = new JSONObject();
			obj.put("student", student[0]);
			response.getWriter().print(obj.toJSONString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
