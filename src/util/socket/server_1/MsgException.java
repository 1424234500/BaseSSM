package util.socket.server_1;

import java.util.Arrays;

import org.apache.log4j.Logger;


public class MsgException extends Exception{
	private static Logger log = Logger.getLogger(MsgException.class); 

	/**
	 * 
	 */
	private static final long serialVersionUID = 5002931016898035011L;

	public MsgException(String str){
		super(str);
	}
	public MsgException(Object...objs){
		this(Arrays.toString(objs));
		log.warn(Arrays.toString(objs));
	}
	
}
