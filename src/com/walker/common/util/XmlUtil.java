package com.walker.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * xml工具类
 * @author copy by walker
 * @version $Id$
 */
public class XmlUtil {
	private static Logger log = Logger.getLogger("Xml"); 
	public static final String LINE = "\r\n";
    /** 节点名称：根节点 */
    public static final String NODE_NAME_ROOT = "root";
    /** 节点名称：列表节点 */
    public static final String NODE_NAME_LIST = "item";
    
    /**
     * 将xml字符串数据转为Map
     * @param str xml字符串 
     * @return Map实体
     **/
    public static Map toMap(String str) {
        Map<String, Object> bean = new HashMap<>();
        if (str != null && str.length() > 0) {
            Document doc;
            try {
                doc = DocumentHelper.parseText(str);
                Element root = doc.getRootElement();
                List<Element> items = root.elements();
                for (Element item : items) {
                    if (item.isTextOnly()) {
                        bean.put(item.getName(), item.getText());
                    } else {
                        bean.put(item.getName(), parseNode(item));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }

        }
        return bean;
    }
    
    /**
     * 递归解析XML对象到数据对象
     * @param element xml节点对象
     * @return 数据对象
     */
    @SuppressWarnings("unchecked")
    private static Object parseNode(Element element) {
        Map bean = new HashMap();
        List<Object> beanList = new ArrayList<Object>();
        String name = element.getName();
        List<Element> list = element.elements();
        boolean isList = false;
        for (Element item : list) {
            String iName = item.getName();
            if (item.isTextOnly()) {
                bean.put(iName, item.getText());
            } else if (name.equals(iName)) { //子节点名与父节点名相同，说明是数组
                isList = true;
                beanList.add(parseNode(item));
            } else {
                bean.put(iName, parseNode(item));
            }
        }
        if (isList) {
            return beanList;
        } else {
            return bean;
        }
    }
    
    /**
     * bean转化为xml字符串，带xml头信息
     * @param bean  Map对象
     * @return  xml字符串
     */
    public static String toFullXml(Map bean) {
        return toFullXml(NODE_NAME_ROOT, bean);
    }
    
    /**
     * bean转化为xml字符串，带xml头信息
     * @param rootnName 根节点名称
     * @param bean  Map对象
     * @return  xml字符串
     */
    public static String toFullXml(String rootnName, Object bean) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(LINE);
        sb.append(toXml(rootnName, bean));
//        log.debug(sb.toString());
        return sb.toString();
    }
    
    /**
     * 对象转化为xml字符串，支持嵌套，自动识别类型：Map、List、Java simple Object
     * @param   nodeName    根节点名称
     * @param   obj         需要被转换的对象
     * @return  xml字符串
     */
    public static String toXml(final String nodeName, Object obj) {
    	final StringBuilder sb = new StringBuilder();
		LangUtil.onObject(obj, new LangUtil.Call() {
			@Override
			public void onString(String str) {
		        sb.append("<").append(nodeName).append(">");    
		        sb.append(encode(str));
                sb.append("</").append(nodeName).append(">").append(LINE);
	        }
			@Override
			public void onMap(Map<?, ?> map) {
                sb.append("<").append(nodeName).append(">").append(LINE);
                for (Object key : map.keySet()) {
                    sb.append(toXml((String)key, map.get(key)));
                }
                sb.append("</").append(nodeName).append(">").append(LINE);
			}
			@Override
			public void onList(List<?> list) {
                sb.append("<").append(nodeName).append(">").append(LINE);
                for (Object item : list) {
                    sb.append(toXml((NODE_NAME_LIST), item));
                }
                sb.append("</").append(nodeName).append(">").append(LINE);
	        }
		});
        
		return sb.toString();
    }
    
    /**
     * 对XML中的特殊字符进行判断和标记，确保XML规则输出
     * @param s 字符串
     * @return 字符串
     */
    public static String encode(String s) {
        if (s.matches("[\\d\\D\\s]*[<>&'\"]+[\\d\\D\\s]*")) {
            s = "<![CDATA[" + s + "]]>";
        }
        return s;   
     }
    
}
