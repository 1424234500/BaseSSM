package util;

import org.junit.Test;

public class TestLang {

	@Test
	public void makeDay(){
		
		ClassUtil.doPackage("util.Tools.out", "aaaa");
		ClassUtil.doPackage("c", "aaaa", "bbb");
		ClassUtil.doPackage("", "aaaa", "bbb");
		
		int i = 0;
		long l = 1l;
		double d = 2d;
		boolean b = false;
		
		Integer ii = 3;
		Long ll = 4L;
		Double dd = 5D;
		Boolean bb = true;

		Bean bean = new Bean().put("int", ii).put("long", ll).put("double", dd).put("bool", bb)
				.put("string", "ssssss")
				.put("class", new Call() {
					@Override
					public void call() {
						Tools.out("accccccccccccccccccc");
					}
				})
				.put("bean", new Bean().put("string", "bean str").put("int", i));
		
		Tools.out(LangUtil.turn(bean.get("int"), -1));
		Tools.out(LangUtil.turn(bean.get("long"), -1L));
		Tools.out(LangUtil.turn(bean.get("double"), -1D));
		Tools.out(LangUtil.turn(bean.get("bool"), false));
		Tools.out(LangUtil.turn(bean.get("bean"), new Bean()));
		Tools.out(LangUtil.turn(bean.get("class"), new Bean()));
		
		Tools.out(LangUtil.turn(bean.get("bean"), 0));
		Tools.out(LangUtil.turn(bean.get("bean"), "ss"));

//		Tools.out(LangUtil.to(i, -1));
//		Tools.out(LangUtil.to(l, -1l));
//		Tools.out(LangUtil.to(d, -1d));
//		Tools.out(LangUtil.to(b, false));
//
//		Tools.out(LangUtil.to(ii, -1));
//		Tools.out(LangUtil.to(ll, -1l));
//		Tools.out(LangUtil.to(dd, -1d));
//		Tools.out(LangUtil.to(bb, false));
		
		Tools.out(LangUtil.turn(ii, -1));
		Tools.out(LangUtil.turn(ll, -1l));
		Tools.out(LangUtil.turn(dd, -1d));
		Tools.out(LangUtil.turn(bb, false));
		
		
		
		
		
	}
}
