package com.walker.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestMain {
	public static void main(String[] argv) {
		
		String str = FileUtil.readByLines(ClassLoader.getSystemResource("").getPath() + "plugin.json", null);
		Bean bean = JsonUtil.get(str);
		
		Tools.out(str);
		Tools.out(bean);
		

	}
	
	
}
