package transmissionDataServer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class transmissionReceiveFile {

	private static boolean OverSign = false;

	// private boolean StartSign = false;
	public transmissionReceiveFile(Socket transmissionSocket, String folderPath) {
		try {
			System.out.println("start receive file");
			DataInputStream dis = new DataInputStream(transmissionSocket.getInputStream());
			
			String fileName = dis.readUTF();
			System.out.println(fileName);

			long fileLength = dis.readLong();

			
			File newfile = new File(folderPath + "/" + fileName);
			if (newfile.exists()) {
				System.out.println(fileName + " is already exists and delete it");
				newfile.delete();
			}
			FileOutputStream fos = new FileOutputStream(newfile);

			long current = 0;

			byte[] buf = new byte[1024];
			int num = 0;
			int time = 0;
			while (current < fileLength) {
				num = dis.read(buf);
				fos.write(buf, 0, num);
				fos.flush();
				time++;
				current += num;
				System.out.println(current + " " + fileLength + " " + 100 * current / fileLength + "%..");
				//System.out.println("num: " + num + "  time:" + time);
			}
			System.out.println("--------OVER--------");
			//dis.close();
			fos.close();
			OverSign = true;
		} catch (FileNotFoundException e) {
			System.out.println("Exception: File Not Found");
		} catch (IOException e) {
			System.out.println("Exception: IO");
		}
	}

	public static boolean startTransmissionReceiveFile(Socket transmissionSocket, String folderPath) {
		new transmissionReceiveFile(transmissionSocket, folderPath);
		if (OverSign == true) {
			return true;
		} else {
			return false;
		}
	}
}
