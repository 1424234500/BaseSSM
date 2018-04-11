package util;

import java.io.ByteArrayInputStream;

/**
 * 封装解析业务类
 */

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XmlUtil {
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
    
	public static void main(String [] args){
		  
			   // TODO Auto-generated method stub
			   //定义要解析的XML字符串
			   String transMessage = "<?xml version=\"1.0\" encoding=\"GBK\"?><root>"
			     + "<body>" 
			     + "<ticket id=\"6000012007051000000231\" dealTime=\"20070510165423\" status=\"0000\" message=\"成功,系统处理正常\"/>"
			     + "<ticket id=\"6000012007051000000232\" dealTime=\"20070510165424\" status=\"2012\" message=\"禁止倍投\"/>"
			      + "</body></root>";
			   String t2 = "<?xml version=\"1.0\" encoding=\"GBK\"?><root>"
					     + "<body>" 
					     + "<ticket id=\"6000012007051000000231\" dealTime=\"20070510165423\" status=\"0000\" message=\"成功,系统处理正常\"/>"
					     + "<ticket id=\"6000012007051000000232\" dealTime=\"20070510165424\" status=\"2012\" message=\"禁止倍投\"/>"
					     +  "</body></root>";
					   
			   
			   String str = "<SCJS ><head><khdid>KHDUPDATE</khdid><deviceid>UPDATE</deviceid><version>0.0.0.0</version><ipaddr>0.0.0.0</ipaddr></head><body><id>5103211993</id><roomStat>1</roomStat></body></SCJS>";

			   getMap(str);
			   
		}
}