package receiverFileFunction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class transmissionSendFile {

	private static boolean OverSign = false;

	// private boolean StartSign = false;
	public transmissionSendFile(Socket transmissionSocket, File transmissionFile, String filename) {
		try {
			System.out.println("start send file");
			DataOutputStream dos;
			DataInputStream dis;
			FileInputStream fis;
			dis = new DataInputStream(transmissionSocket.getInputStream());
			fis = new FileInputStream(transmissionFile);
			OutputStream out = transmissionSocket.getOutputStream();
			dos = new DataOutputStream(out);

			long current = 0;
			long sum = transmissionFile.length();

			dos.writeUTF(filename);
			dos.flush();

			dos.writeLong(transmissionFile.length());
			dos.flush();

			byte[] buf = new byte[1024];
			int num = 0;
			int time = 0;
			while (true) {
				num = fis.read(buf);
				if(num == -1){
					break;
				}
				dos.write(buf, 0, num);
				dos.flush();
				
				time++;
				current += num;
				System.out.println(current + " " + sum + " " + 100 * current / sum + "%..");
				//System.out.println("num: " + num + "  time:" + time);
				
			}
			// dos.write(0);
			System.out.println("-------OVER-------");
			// dos.flush();
			fis.close();
			// dos.close();
			OverSign = true;
		} catch (FileNotFoundException e) {
			System.out.println("Exception: File Not Found");
		} catch (IOException e) {
			System.out.println("Exception: IO");
		}
	}

	public static boolean startTransMissionSendFile(Socket transmissionSocket, File transmissionFile, String filename) {
		new transmissionSendFile(transmissionSocket, transmissionFile, filename);
		if (OverSign == true) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		// new transmissionFile();
	}
}
