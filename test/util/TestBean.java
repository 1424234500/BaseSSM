package util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.walker.common.util.Bean;
import com.walker.common.util.Tools;

public class TestBean {

	@Test
	public void test() {
		Bean bean = Bean.getBean().put("key1", "v1").put("key2", "v2");
		List<String> list = new ArrayList<>();
		list.add("aaa");
		list.add("bbb");
		Bean bean1 = Bean.getBean().put("key1", "v1").put("key2", "v2").put("bean", bean).put("list", list);

		Tools.out(bean);
		Tools.out(bean.get("key1", ""));
		Tools.out(bean.get("key3", new HashMap()));
		Tools.out(bean1);
		Tools.out(bean1.get("bean", new HashMap()));
		Tools.out(bean1.get("list"));
		Tools.out(bean1.get("list2"));

	}
}
