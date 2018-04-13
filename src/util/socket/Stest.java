package util.socket;

import java.net.Socket;

public class Stest {

	public static void main(String[] args) {
		
		Server server = new ServerImpl() {

			@Override
			public boolean start() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean stop() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		FrameSocket fs = new FrameSocket(server);
		
		fs.start();
		
	}

}
