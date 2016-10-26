package liwei;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;


public enum ConnManagerProxy implements InvocationHandler{
	ConnManagerProxy;
	private ConnManagerProxy() {
	}
	
	
	private Object obj;
	@SuppressWarnings("unchecked")
	public Object newProxyInstance(Object obj){
		this.obj = obj;
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
	}
	public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {
		Object result = null;
		Connection conn = ConnectionManager.getConnection();
		try{
			ConnectionManager.beginTransction(conn);
			result = method.invoke(obj, args);
			ConnectionManager.endTransction(conn);
		}catch(Exception e){
			ConnectionManager.rollback(conn);
			throw new Exception("����ʧ��");
		}finally{
			ConnectionManager.recoverTransction(conn);
			ConnectionManager.close();
		}
		
		return result;
	}

}

