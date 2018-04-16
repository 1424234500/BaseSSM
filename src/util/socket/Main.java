package util.socket;

public class Main {

	public static void main(String[] args) {
		
		Server server = new ServerHashmapImpl(new SocketIO());
		server.start();
	}

}
