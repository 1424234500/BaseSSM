package util;

import java.util.Map;

public class TestMain {
	public static void main(String[] argv) {
		
		String str = FileUtil.readByLines("plugin.xml", null);
		Map map = XmlUtil.toMap(str);
		
		Tools.out(str);
		Tools.out(map);
		
		
	}
	
	
}
