package transmissionDataClient;

import java.io.*;
import java.net.*;
import java.util.*;

import receiverFileFunction.transmissionSendFile;
import sendFileFunction.transmissionReceiveFile;

public class client {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String serverName = "localhost";
		//120.27.51.6
		String folderPath = "/home/hus/javaspace/client";
		int port = 6066;
		try {
			Socket client = new Socket(serverName, port);
			OutputStream outToserver = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToserver);
			InputStream inFormServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFormServer);
			String write = "";
			String read = "";
			int fileLength = in.readInt();
			int m;
			String[] files = new String[fileLength];
			for (m = 0; m < fileLength; m++) {
				files[m] = in.readUTF();
			}

			for (String s : files) {
				System.out.println(s);
			}

			Scanner input = new Scanner(System.in);
			while (true) {
				write = input.nextLine();

				out.writeUTF(write);
				out.flush();

				read = in.readUTF();
				while (read.equals("send file to you")) {

					System.out.println("Server:send file to you");
					if (transmissionReceiveFile.startTransmissionReceiveFile(client, folderPath) == true) {
						System.out.println("File is ok");
					} else {
						System.out.println("transmission is fail");
					}
					read = in.readUTF();
				}

				if (read.equals("wait upload files")) {
					System.out.println("Server:wait upload files");
					write = write.replaceAll(" +", ",");
					String[] command = write.split(",");
					String path = "";
					int num = 1;
					int total = command.length - 1, success = 0, invalid = 0;
					File uploadfile;

					while (true) {
						path = command[num];
						uploadfile = new File(path);
						if (uploadfile.exists() && uploadfile.isDirectory() == false) {
							out.writeUTF("start upload file");
							out.flush();
							if (transmissionSendFile.startTransMissionSendFile(client, uploadfile,
									uploadfile.getName())) {
								System.out.println(uploadfile.getName() + " upload success");
								success++;
							}
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							invalid++;
							System.out.println(path + ", 该文件无法上传，请检查文件和路径");
							continue;
						}
						num++;
						if (num >= command.length) {

							out.writeUTF("over");
							out.flush();
							break;
						}
					}
					read = in.readUTF();
					System.out.println("total:" + total + " success:" + success + " invalid:" + invalid);
				}
				System.out.println("Server:" + read);
			}

			// client.close();
		} catch (IOException e) {
			System.out.println("Server Initing....服务器尚未开启，请等待");
		}
	}
}
