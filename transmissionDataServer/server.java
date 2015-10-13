package transmissionDataServer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class server {
	private static ServerSocket serverSocket;
	
	//private ArrayList<String[]> filesMessage = null;
	//private String[][] file = null;
	
	public server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		/*
		filesMessage = listFilesANDFolders.getFileMessages();
		file = new String[filesMessage.size()][2];

		for (int i = 0; i < filesMessage.size(); i++) {
			file[i] = filesMessage.get(i).clone();
		}
		*/
	}


	public static void main(String[] args) {
		int port = 6066;
		try {
			new server(port);
		} catch (IOException e) {
			System.out.println("端口无法绑定");
		}
		Socket server;
		
		while (true) {
			try {
				server = serverSocket.accept();
				Thread t = new Thread(new connecThread(server));
				t.start();
			} catch (IOException e1) {
				System.out.println("线程开启失败");
			}
		}

	}

	
}