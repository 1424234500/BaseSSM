package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.controller.FileControll;

import net.sf.json.util.JSONUtils;


/**
 * Http tools
 * for:
 * 		http get
 * 		http post
 * with encode stream userAgent
 * 
 *
 */
public class HttpUtils {
	private static Logger log = Logger.getLogger(FileControll.class); 

	public static String encode(String str) throws Exception{
    	return URLEncoder.encode(str, "utf-8").replaceAll("\\+", "%20");
    }
	
	public static String get(String url, Map<?, ?> data) throws Exception{
		url = url + "?";
		String ddd = "";
		for(Object key : data.keySet()){
			String str = data.get(key)==null?"null":data.get(key).toString();
			ddd = ddd + key + "=" + encode(str) + "&";
		}
		ddd = ddd.substring(0, ddd.length() - 1);
//		ddd = encode(ddd);
//		ddd = URLEncoder.encode(ddd);
		url = url + ddd;
		return get(url, "utf-8");
	}
	public static String get(String url) throws URISyntaxException, IOException{
		return get(url, "utf-8");
	}
	
	public static String post(String url, List<?> listBean) throws Exception{
		return post(url, listBean, "utf-8", null);
	}
	public static String post(String url, Map<?, ?> bean) throws Exception{
		return post(url, bean, "utf-8", null);
	}
	public static String post(String url, String data) throws Exception{
		return post(url, data, "utf-8", null);
	}
	public static String post(String url, Map<?, ?> bean, String encode, String userAgent) throws Exception{
		return post(url, JsonUtil.makeJson(bean), encode, userAgent);
	}
	public static String post(String url, List<?> bean, String encode, String userAgent) throws Exception{
		return post(url, JsonUtil.makeJson(bean), encode, userAgent);
	}
	public static String post(String url, String data, String encode, String userAgent) throws Exception{
		String res = "";
		log.info(Arrays.toString(new String[]{"dopost", url, data, encode, userAgent}));

		
		HttpClient client = HttpClientUtils.createHttpClient();
		HttpPost post = new HttpPost();
        HttpEntity entity = new StringEntity(data, encode);
        post.setEntity(entity);
        if(userAgent != null && userAgent.length() > 0)
        	post.setHeader("User-Agent","Eschat;icbcobcp;");
		post.setURI(new URI(url));
		post.setHeader("Keep-Alive","15000");
		client.getParams().setIntParameter("http.socket.timeout", 15000);
		
		HttpResponse response = client.execute(post);
		res = HttpResponseUtils.getResponseContent(response, encode);
		
		return res;
	}

	public static String get(String url, String encode) throws URISyntaxException, IOException{
		String res = "";
		log.info(Arrays.toString(new String[]{"doget", url, encode}));
		HttpGetResponse resp = com.rh.core.util.http.HttpUtils.httpGet(url);
		InputStream is = resp.getInputStream();
		res = IOUtils.toString(is, encode);
		
		return res;
	}

}
