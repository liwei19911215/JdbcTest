package liwei;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@SuppressWarnings("unchecked")
public class ClassLoaderUtil {
	
	//º”‘ÿ¿‡
	public static  Class loadClass(String className,Class clazz) throws ClassNotFoundException{
		try{
			return Thread.currentThread().getContextClassLoader().loadClass(className);
		}catch(ClassNotFoundException e){
			try{
				return Class.forName(className);
			}catch(ClassNotFoundException ex){
				try{
					return ClassLoaderUtil.class.getClassLoader().loadClass(className);
				}catch(ClassNotFoundException exx){
					return clazz.getClassLoader().loadClass(className);
				}
			}
		}
	}
	
	
	public static URL getResource(String resourceName,Class clazz){
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
		
		if(url==null){
			url = ClassLoaderUtil.class.getClassLoader().getResource(resourceName);
		}
		if(url==null){
			ClassLoaderUtil.class.getClassLoader().getResource(resourceName);
		}
		if(url==null){
			ClassLoader cl = clazz.getClassLoader();
			if(cl!=null){
				url = cl.getResource(resourceName);
			}
		}
		if ((url == null) && (resourceName != null) && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) { 
            return getResource('/' + resourceName, clazz);
	    }
		return url;
		
	}
	
	public static InputStream getResourceAsStream(String resourceName,Class clazz){
		URL url = getResource(resourceName, clazz);
		try {
			return (url!=null) ? url.openStream() : null;
		} catch (IOException e) {
			return null;
		}
	}
}
