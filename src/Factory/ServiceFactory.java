package Factory;

import liwei.ClassLoaderUtil;

public class ServiceFactory {
	@SuppressWarnings("unchecked")
	public static <T>T getService(Class<T> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".")+1,className.length());
		if(className.startsWith("I")&&className.endsWith("SV")){
			String s = className.substring("I".length());
			s.substring(0, s.length()-"SV".length());
			className = s+"Impl";
			return (T) ClassLoaderUtil.loadClass(className, ServiceFactory.class).newInstance();
		}
		return clazz.newInstance();
	}
}
