package util.socket;

public class ServerTest {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		
//		new ServerHashmapImpl(new SocketIO()).start();
//		new ServerHashmapImpl(new SocketNIO()).start();
		new ServerHashmapImpl(new SocketNetty()).start();

		
	}

}
