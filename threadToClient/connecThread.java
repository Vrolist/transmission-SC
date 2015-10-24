package threadToClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import filesListInformation.listFilesANDFolders;
import receiverFileFunction.transmissionSendFile;
import sendFileFunction.transmissionReceiveFile;

public class connecThread implements Runnable {
	private Socket connection;

	// 存放文件信息的数组
	private ArrayList<String[]> filesMessage = null;
	private String[][] file = null;

	public connecThread(Socket connection) {
		this.connection = connection;

		if (filesMessage == null) {
			filesMessage = listFilesANDFolders.getFileMessages();
			file = new String[filesMessage.size()][2];

			for (int i = 0; i < filesMessage.size(); i++) {
				file[i] = filesMessage.get(i).clone();
			}
		}

	}

	@Override
	public void run() {
		try {
			DataInputStream in = new DataInputStream(connection.getInputStream());
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());

			// 文件信息传输
			out.writeInt(file.length);
			out.flush();
			for (int m = 0; m < file.length; m++) {
				out.writeUTF(file[m][0]);
				out.flush();
			}

			String re = "";
			while (true) {
				try {
					re = in.readUTF();
					System.out.println(connection + ": " + re);

					if (re.contains("download")) {
						int total = 0;
						int success = 0;
						String[] fileNames = new String[20];
						int index = 0;
						fileNames[index] = "FileNames";
						for (int i = 0; i < file.length; i++) {

							if (re.contains(file[i][0])) {
								boolean tf = false;
								System.out.println(re);
								System.out.println("i hava " + file[i][0] + ", and send this file to you now");
								out.writeUTF("send file to you");
								out.flush();
								index++;
								tf = transmissionSendFile.startTransMissionSendFile(connection, new File(file[i][1]),
										file[i][0]);
								total = total + 1;
								fileNames[index] = file[i][0];
								if (tf == true) {
									success = success + 1;
									re = re.replace(file[i][0], "*");
								}
							}
						}
						if (total == 0) {
							System.out.println("DownLoad 0 File");
							out.writeUTF("over");
						} else if (success != 0) {
							System.out.println("Total " + total + " Files have to Download");
							System.out.println("Success Download " + success + " Files");
							out.writeUTF("over");
							// re = in.readUTF();
						}

					} else if (re.contains("upload")) {
						out.writeUTF("wait upload files");
						out.flush();
						re = in.readUTF();
						while (re.equals("start upload file")) {
							if (transmissionReceiveFile.startTransmissionReceiveFile(connection,
									"/home/hus/javaspace/upload") == true) {
								System.out.println("upload success");
							} else {
								System.out.println("upload fail");
							}
							re = in.readUTF();
							if(re.equals("over")){
								out.writeUTF("over");
								out.flush();
								System.out.println("upload over");
							}
						}
					} else {
						out.writeUTF(re);
						out.flush();
					}

				} catch (SocketTimeoutException s) {
					System.out.println("socket timed out!");
				} catch (IOException e) {
					System.out.println(connection + ":连接异常，已断开");
					// e.printStackTrace();
					break;
				}

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println(connection + "断开连接");
			// e1.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// new connecThread();
	}
}
