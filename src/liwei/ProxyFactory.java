package liwei;

public class ProxyFactory {
	private ProxyFactory(){
		
	}
	public static ConnManagerProxy getconnManagerProxy() throws Exception{
		return ConnManagerProxy.ConnManagerProxy;
	}
}
