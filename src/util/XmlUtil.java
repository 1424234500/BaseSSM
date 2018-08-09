package util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * xml工具类
 * @author copy by walker
 * @version $Id$
 */
public class XmlUtil {
    private static Log log = LogFactory.getLog(XmlUtil.class);
    /** 节点名称：根节点 */
    public static final String NODE_NAME_ROOT = "root";
    /** 节点名称：列表节点 */
    public static final String NODE_NAME_LIST = "list";
    /** 节点名称：支节点 */
    public static final String NODE_NAME_NODE = "node";

    /**
     * 私有构建体方法
     */
    private XmlUtil() {        
    }
    
    /**
     * 将xml字符串数据转为Map
     * @param str xml字符串 
     * @return Map实体
     **/
    @SuppressWarnings("unchecked")
    public static Map toMap(String str) {
        Map bean = new HashMap();
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
    public static String toFullXml(String rootnName, Map bean) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append(toXml(rootnName, bean));
        log.debug(sb.toString());
        return sb.toString();
    }
    
    
    /**
     * bean转化为xml字符串，支持嵌套
     * @param nodeName 节点名称
     * @param bean  Map对象
     * @return  xml字符串
     */
    public static CharSequence toXml1(String nodeName, Map bean) {
        StringBuilder sb = new StringBuilder(20000000);
        if ((nodeName != null) && (nodeName.length() > 0)) {
            sb.append("<").append(nodeName).append(">");
        }
        for (Object key : bean.keySet()) {
            Object value = bean.get(key);
            if (value != null) {
                if (value != null) {
                    sb.append("<").append(key).append(">");
                    if (value instanceof Map) {
                        sb.append(toXml(null, (Map) value));
                    } else if (value instanceof List) {
                        sb.append(toXml(String.valueOf(key), (List<?>) value));
                    } else if (value instanceof Map) {
                        sb.append(toXml(null, (Map<?, ?>) value));
                    } else {
                        sb.append(encode(String.valueOf(value)));
                    }
                    sb.append("</").append(key).append(">");
                }
            }
        } //end for
        if ((nodeName != null) && (nodeName.length() > 0)) {
            sb.append("</").append(nodeName).append(">").append("\r\n");
        }
        return sb;
    }    
    
    /**
     * bean转化为xml字符串，支持嵌套
     * @param nodeName 节点名称
     * @param bean	Map对象
     * @return	xml字符串
     */
    public static String toXml(String nodeName, Map bean) {
        StringBuilder sb = new StringBuilder();
        if ((nodeName != null) && (nodeName.length() > 0)) {
            sb.append("<").append(nodeName).append(">");
        }
        for (Object key : bean.keySet()) {
            Object value = bean.get(key);
            if (value != null) {
                sb.append("<").append(key).append(">");
                if (value instanceof Map) {
                    sb.append(toXml(null, (Map) value));
                } else if (value instanceof List) {
                    sb.append(toXml(String.valueOf(key), (List<?>) value));
                } else if (value instanceof Map) {
                    sb.append(toXml(null, (Map<?, ?>) value));
                } else {
                    sb.append(encode(String.valueOf(value)));
                }
                sb.append("</").append(key).append(">");
            }
        } //end for
        if ((nodeName != null) && (nodeName.length() > 0)) {
            sb.append("</").append(nodeName).append(">");
        }
        return sb.toString();
    }
    
    /**
     * list转化为xml字符串，支持嵌套
     * @param   nodeName 节点名称
     * @param   list	list对象
     * @return	xml字符串
     */
    public static String toXml(String nodeName, List<?> list) {
        StringBuilder sb = new StringBuilder();
        if ((nodeName == null) || (nodeName.length() == 0)) {
            nodeName = NODE_NAME_LIST;
        }
        for (Object bean : list) {
            if (bean != null) {
                sb.append("<").append(nodeName).append(">");
                sb.append(toXml(null, (Map) bean));
                sb.append("</").append(nodeName).append(">").append("\r\n");
            }
        }
    	return sb.toString();
    }
     
    
    /**
     * 对象转化为xml字符串，支持嵌套，自动识别类型：Map、List、Map
     * @param   nodeName    节点名称
     * @param   obj         需要被转换的对象
     * @return  xml字符串
     */
    public static String toXml(String nodeName, Object obj) {
        if (obj == null) {
            return  "";
        } else if (obj instanceof List<?>) {
            return toXml(nodeName, (List<?>) obj);
        } else if (obj instanceof Map<?, ?>) {
            return toXml(nodeName, (Map<?, ?>) obj);
        } else {
            throw new RuntimeException("wrong json ojbect type");
        }
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
    
    
    public static Map<String, String> getMap(String xml){
		Map<String, String> res = new HashMap<String, String>();

    	try{
    	  // 定义一个文档
		   Document document = new SAXReader().read(new ByteArrayInputStream(xml .getBytes("UTF-8")));
		   // 得到xml的根节点(message)
		   Element root = document.getRootElement(); 
		   List<Element> listElement=root.element("body") .elements();//所有一级子节点的list    
		   for(Element e : listElement){//遍历所有一级子节点    
			   Tools.out(  e.getName(), e.getText());
			   res.put(e.getName(), e.getText());
		   }
		   Tools.out(res);
		}catch(Exception e){
		   e.printStackTrace();
		}
    	return res;
    }
    
    
    
}
