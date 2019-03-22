package util.socket.server_1;

/**
 * 构造各种消息格式
 * @author walker
 *
 */
public class MsgBuilder {
	public static Msg getString(String str) {
		Msg msg = new Msg()
				.setOk(false)
//				.setTo("self") 	//接收方不需要知道to
				.setInfo(str);
		return msg;
	}
	public static Msg getException(Exception e) {
		Msg msg = new Msg()
				.setOk(false)
//				.setTo("self") 	//接收方不需要知道to
				.setInfo(e.getMessage());
		return msg;
	}
	
	
	
}
