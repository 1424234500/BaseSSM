package com.walker.core.annotation;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.walker.common.util.FunT;
import com.walker.common.util.Tools;

/**
 * 注解案例
 * 目标注解捕获 处理器
 * 
 * 处理数据库相关注解
 * 不能继承 上转 所以只能特定转换 处理
 * 
 */

public class DBTableTracker implements OnAnnotation{
	

	@Override
	public Status make(Annotation annotation, ElementType type, Object object, Class<?> cls) {
		Tools.out(this, annotation, type, object);
		if(type.equals(ElementType.TYPE))	
			track(annotation, (Class<?>)object);
		return null;
	} 
	
	public void track(Annotation anno, Class<?> cl){
//		DBTable dbTable = cl.getAnnotation(DBTable.class);
		DBTable dbTable = (DBTable)anno;
		Tools.out("dbtable make");
		
		String tname = dbTable.name();
		if(tname.length() < 1){ //默认类名
			tname = cl.getName().toUpperCase();
		}
		List<String> colDefs = new ArrayList<String>();
		for(Field field : cl.getDeclaredFields()){
			String colName = null;
			Annotation[] anns = field.getDeclaredAnnotations();
			if(anns.length < 1){
				continue;
			}
			
			//不能继承 上转 所以只能特定转换 处理
			if(anns[0] instanceof DBSQLInteger){
				DBSQLInteger sint = (DBSQLInteger)anns[0];
				if(sint.name().length() < 1){
					colName = field.getName().toUpperCase(); //默认变量名
				}
				colDefs.add(colName + " INT" + getConstraints(sint.DBConstraints()));
			}
			if(anns[0] instanceof DBSQLString){
				DBSQLString sint = (DBSQLString)anns[0];
				if(sint.name().length() < 1){
					colName = field.getName().toUpperCase(); //默认变量名
				}
				colDefs.add(colName + " VARCHAR(" + sint.value() + ") " + getConstraints(sint.DBConstraints()));
			}
		}
		StringBuilder sb = new StringBuilder(" Create table " + tname + "( ");
		for(String item : colDefs){
			sb.append("\n\t" + item + ",");
		}
		String sql = sb.substring(0, sb.length() - 1) + " );";
		Tools.out(sql);
		
		
	}
	private  String getConstraints(DBConstraints con){
		String res = "";
		if(!con.allowNull()){
			res += " not null";
		}
		if(con.primaryKey()){
			res += " primary key";
		}
		if(con.unique()){
			res += " unique";
		}
		return res;
	}
	
	
	
	
	
}