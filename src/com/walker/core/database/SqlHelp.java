package com.walker.core.database;

import java.util.Map;

import com.walker.common.util.Tools;

/**
 * 数据库sql语句帮助
 * @author Walker
 * 2017年9月18日17:33:25
 */
public class SqlHelp{ 
	 
	
	/**
	 * 字符串模糊查询 添加符号  "%" + value + "%" 
	 */
	public static  String like(String value){
		return "%" + value + "%";
	}
	/**
	 * 字符串 单引号包围"'" + value + "'" 
	 */
	public static  String include(String value){
		return "'" + value + "'";
	}
	/**
	 * 年月日 时分秒 格式'yyyy-mm-dd hh24:mi:ss'
	 */
	public static String getTimeFormatL(){
		return "'yyyy-mm-dd hh24:mi:ss'";
	}
	/**
	 * 年月日 格式'yyyy-mm-dd'
	 */
	public static String getTimeFormatS(){
		return "'yyyy-mm-dd'";
	}
	/**
	 * 转换 to_date 年月日 时分秒 to_date('time','yyyy-mm-dd hh24:mi:ss')
	 */
	public static  String to_dateL(String time){
		return " to_date(" + include(time) + ", " + getTimeFormatL() + ") ";
	}
	/**
	 * 转换 to_date 年月日 时分秒 to_date(?,'yyyy-mm-dd hh24:mi:ss')
	 */
	public static  String to_dateL(){
		return " to_date(?, " + getTimeFormatL() + ") ";
	}
	/**
	 * 转换 to_date 年月日   to_date('time','yyyy-mm-dd')
	 */
	public static  String to_dateS(String time){
		return " to_date(" + include(time) + ", " + getTimeFormatS() + ") ";
	}
	/**
	 * 转换 to_date 年月日  to_date(?,'yyyy-mm-dd')
	 */
	public static  String to_dateS( ){
		return " to_date(?, " + getTimeFormatS() + ") ";
	}
	/**
	 * 转换 to_char 年月日 时分秒
	 */
	public  static String to_charL(String time){
		return " to_char(" + include(time) + ", " + getTimeFormatL() + ") ";
	}	
	/**
	 * 转换 to_char 年月日 时分秒
	 */
	public static  String to_charL( ){
		return " to_char(?, " + getTimeFormatL() + ") ";
	}
	/**
	 * 转换 to_char 年月日 
	 */
	public static  String to_charS(String time){
		return " to_char(" + include(time) + ", " + getTimeFormatS() + ") ";
	}
	/**
	 * 转换 to_char 年月日 
	 */
	public static  String to_charS( ){
		return " to_char(?, " + getTimeFormatS() + ") ";
	}
	/**
	 * 位数补齐 用c补齐到tolen位
	 */
	public static  String file(String value, int tolen, char c){
		return " lpad(" + value + ", " + tolen + ", '" + c + "') ";
	}
	
	
	/**
	 * ?, ?, ?
	 */
	public static String makeMapPosis(Map<?, ?> map){
		return makePosition("?", map.size());
	}
	/**
	 * id, value, name
	 */
	public static String makeMapKeys(Map<?, ?> map){
		String res = "  ";
		for(Object key : map.keySet()){
			res = res + key + ", ";
		}
		res = res.substring(0, res.length() - 2);
		return res;
	}
	/**
	 * id=?, value=?, name=?
	 */
	public static String makeMapKeyPosis(Map<?, ?> map){
		String res = "";
		for(Object key : map.keySet()){
			res = res + key + "=?, ";
		}
		res = res.substring(0, res.length() - 2);
		return res;
	}
	/**
	 * id1, value2, name3
	 */
	public static String makeMapValues(Map<?, ?> map){
		String res = "  ";
		for(Object key : map.keySet()){
			res = res + (map.get(key)).toString() + ", ";
		}
		res = res.substring(0, res.length() - 2);
		return res;
	}
	public static int getMapSize(Map<?, ?> map){
		return map.size();
	}
	/**
	 * 设置n个问号 ？ 占位符
	 */
	public static String makePosition(String posi, int num){
		String res = "";
		for(int i = 0; i < num - 1; i++){
			res = res + posi + ", ";
		}
		res = res + posi;
		return res;
	}
	/**
	 * sql占位符替换拼接参数 生成sql  仅作参考sql语句使用 易被sql注入
	 */
	public static String makeSql(String sql, Object...params){
		if(sql.length() <= 0)return sql;
		if(sql.charAt(sql.length() - 1) != ' '){	//最后面加空格 因为split最后的符号不分
			sql = sql + " ";
		}
		
		int needLen = sql.split("\\?").length - 1; 
		int realLen = 0;
		if (params != null &&  params.length > 0) {
			realLen = params.length;
		} 
		if(needLen != realLen){
			sql = "Error! "
					+ "\nsql: " + sql  
					+ "\nparams: " + Tools.objects2string(params) 
					+ "\n占位符个数[" + needLen + "]与实际参数个数[" + realLen + "]不同";
		}else if(needLen > 0){//sf s where id=? and name=? order by ? 。
			int t = -1;
			for (int i = 0; i < params.length; i++) {
				t = sql.indexOf("?"); 
				sql = sql.substring(0, t) + (params[i]==null?"null":"'" + params[i] + "'") + sql.substring(t+1);
			}
		}
		return sql;
	}
	
	 
}