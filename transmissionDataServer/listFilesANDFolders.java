package transmissionDataServer;

import java.io.File;
import java.util.ArrayList;

public class listFilesANDFolders {
	
	private  static ArrayList<String[]> fileMessages = new ArrayList<String[]>();
	
	public listFilesANDFolders() {
		File file = new File("/home/hus/javaspace/server");
		isDir(file);

	}

	public void isDir(File file) {
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				isDir(files[i]);
			} else {
				String[] message = new String[2];
				message[0] = files[i].getName();
				message[1] = files[i].getAbsolutePath();
				fileMessages.add(message);
			}
		}
	}
	
	public static ArrayList<String[]> getFileMessages() {
		new listFilesANDFolders();
		return fileMessages;
	}

	public static void main(String[] args) {
		
		ArrayList<String[]> filesMessage = listFilesANDFolders.getFileMessages();
		String[] s = new String[2];
		for(int i=0 ;i<filesMessage.size();i++){
			s = filesMessage.get(i);
			System.out.println(s[0] + s[1]);
		}
	}

}
