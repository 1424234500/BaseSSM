package com.walker.socket.client;

public interface UiCall {

	public void onReceive(String readLine) ;
	public void out(Object...objects);

	
}
