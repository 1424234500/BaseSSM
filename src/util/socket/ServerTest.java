package util.socket;

public class ServerTest {

	public static void main(String[] args) {
		
		Server serverIO = new ServerHashmapImpl(new SocketIO());
		serverIO.start();
		Server serverNIO = new ServerHashmapImpl(new SocketNIO());
		serverNIO.start();
//		
		
		
	}

}
