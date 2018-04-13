package util.socket;


/**
 * 
 */

public interface SendCallback <Arg> { 
	
	public void sendCallback(Arg obj, String jsonstr);
	
	
} 