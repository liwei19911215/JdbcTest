package control;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import liwei.ClassLoaderUtil;

@SuppressWarnings("unchecked")
public class ControlAction extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		  	request.setCharacterEncoding("UTF-8");  
	        response.setCharacterEncoding("UTF-8"); 
	        String action = request.getParameter("action");  
	        String actionMethod = request.getParameter("actionMethod");  
	        Class actionClass;
			try {
				actionClass = ClassLoaderUtil.loadClass(action, this.getClass());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("aciton类未找到");
			}
	        Object args[] = {request, response};
	        Method method;
			try {
				method = actionClass.getMethod(actionMethod, classType);
				method.invoke(actionClass.newInstance(),args);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("aciton方法未找到");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RuntimeException("aciton方法异常");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException("aciton方法异常");
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RuntimeException("aciton方法异常");
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new RuntimeException("aciton方法异常");
			}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}
	
	
	private static final Class[] classType = {javax.servlet.http.HttpServletRequest.class,javax.servlet.http.HttpServletResponse.class};

}
