package util.socket.client;

public class ClientTest {

	public static void main(String[] args) {
		
		new ClientUI(new ClientIO("127.0.0.1", 8090), "io-io");
		new ClientUI(new ClientIO("127.0.0.1", 8091), "io-nio");
		new ClientUI(new ClientIO("127.0.0.1", 8090), "nio-io");
		new ClientUI(new ClientIO("127.0.0.1", 8091), "nio-nio");
 		
		
		
	}

}
