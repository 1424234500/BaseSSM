package util.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * servlet request response printwriter help tools
 * @author Walker
 *
 */
public class WebUtil {

	/**
	 * 获取request所有参数Map
	 * @param request
	 * @return Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getRequestMap(HttpServletRequest request){
		Map res = new HashMap();
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String name=(String)enu.nextElement();  
			String value = request.getParameter(name);  
			res.put(name, value);
		}
		return res;
	}
	
	
}