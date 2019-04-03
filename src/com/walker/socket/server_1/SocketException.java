package com.walker.socket.server_1;

import java.util.Arrays;

import org.apache.log4j.Logger;


public class SocketException extends Exception{
	private static Logger log = Logger.getLogger(SocketException.class); 

	/**
	 * 
	 */
	private static final long serialVersionUID = 5002931016898035011L;

	public SocketException(String str){
		super(str);
	}
	public SocketException(Object...objs){
		this(Arrays.toString(objs));
		log.error(Arrays.toString(objs), this);
	}
	
}
