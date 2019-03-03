/**
 * 这是客户端和服务器端保持通讯的线程.
 */
package major;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import total.ManageClientConServerThread;
import total.ManageQqChat;
import view.*;
import total.*;

public class ClientConServerThread extends JFrame implements Runnable {//

	private Socket socket;
	public Socket s, ss;
	ServerSocket serverSocket;
	DataInputStream inFromServer;
	DataOutputStream outToServer;
	DataInputStream inFromClient;
	DataOutputStream outToClient;
	String friendnum;
	String owner;
	File file, file1;

	public ClientConServerThread(String owner,Socket s) {
		this.owner=owner;
		socket = s;
	}

	public void run() {
		while (true) {
			
			try {

				ObjectInputStream ois = new ObjectInputStream(
						socket.getInputStream());
				Message m = (Message) ois.readObject();

				if (m.getMesType().equals("3")) {
					QqChat qqChat = ManageQqChat.getQqChat(m.getGetter() + " "
							+ m.getSender());
					qqChat.showMessage(m);
				}else if (m.getMesType().equals("20")) {
					ManageQqGroup.getQqGroup(owner).showMessage(m);
				}
				// 发送文件
				else if (m.getMesType().equals("4")) {// 启动发送文件线程
					// 启动文件发送线程
					FileSendingThread sendThread = new FileSendingThread(m,
							file);
					sendThread.start();
				} else if (m.getMesType().equals("6")) {// 选择发送什么文件
					JFileChooser jfc = new JFileChooser(".");
					if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
						file = jfc.getSelectedFile();
						m.setMesType("5");
						try {
							ObjectOutputStream oos = new ObjectOutputStream(
									ManageClientConServerThread
											.getClientConServerThread(
													m.getSender()).getS()
											.getOutputStream());
							oos.writeObject(m);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (m.getMesType().equals("5")) {// 启动接收文件线程
					// 启动接收文件线程
					ReceiveFileThread RecThread = new ReceiveFileThread(m);
					RecThread.start();

					QqChat qqChat = ManageQqChat.getQqChat(m.getGetter() + " "
							+ m.getSender());
					qqChat.showMessage1(m);
				} else if (m.getMesType().equals("7")) {// 选择发送什么文件夹
					
					JFileChooser jfc = new JFileChooser(".");
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
						file1 = jfc.getSelectedFile();
						m.setMesType("8");
						try {
							ObjectOutputStream oos = new ObjectOutputStream(
									ManageClientConServerThread
											.getClientConServerThread(
													m.getSender()).getS()
											.getOutputStream());
							oos.writeObject(m);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (m.getMesType().equals("8")) {
					
					QqChat qqChat = ManageQqChat.getQqChat(m.getGetter() + " "+ m.getSender());
					qqChat.showMessage3(m);
				} else if (m.getMesType().equals("9")) {
					
					m.setMesType("10");
					try {
						ObjectOutputStream oos = new ObjectOutputStream(
								ManageClientConServerThread
										.getClientConServerThread(m.getSender())
										.getS().getOutputStream());
						oos.writeObject(m);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					FileSendingThread2 sendThread = new FileSendingThread2(
							file1);
					sendThread.start();
					
				}

				else if (m.getMesType().equals("10")) {
					
					ReceiveFileThread2 RecThread = new ReceiveFileThread2(m);
					RecThread.start();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class FileSendingThread extends Thread {// 发送文件线程
		Message m;
		File file;

		public FileSendingThread(Message m, File file) {
			this.m = m;
			this.file = file;
		}

		public void run() {
			try {

				Socket s = new Socket("localhost", 5500);
				inFromServer = new DataInputStream(s.getInputStream());
				outToServer = new DataOutputStream(s.getOutputStream());

				FileInputStream fis;
				fis = new FileInputStream(file);
				byte buf[] = new byte[4096];
				int n;

				// 先将文件名发送过去
				String fileName = file.getName();
				outToServer.writeUTF(fileName);

				// 发送文件的大小
				long fileLength = file.length();
				outToServer.writeLong(fileLength);
				// 开始发送文件
				while ((n = fis.read(buf, 0, buf.length)) != -1) {

					System.out.println("client:" + n);

					outToServer.write(buf, 0, n);
					outToServer.flush();
					Thread.sleep(2000);
				}
				fis.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class FileSendingThread2 extends Thread {// 发送文件夹线程
		File file;
		byte buf[];

		int t = 0;
		ServerSocket SendFileFolderSocket;
		Socket ss;
		DataOutputStream dataOutToServer;
		BufferedInputStream readcontent;

		public FileSendingThread2(File file) {
			this.file = file;
			buf = new byte[1024];
			try {
				SendFileFolderSocket = new ServerSocket(2000);
				ss = SendFileFolderSocket.accept();
				dataOutToServer = new DataOutputStream(ss.getOutputStream());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				transmit(file);
				SocketClose();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void transmit(File f) throws IOException {
			dataOutToServer.write("Folder_Name".getBytes());
			dataOutToServer.flush();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			String str = f.getName();
			dataOutToServer.write(str.getBytes());
			dataOutToServer.flush();
			File[] ff = f.listFiles();
			if (ff.length > 0)
				for (int i = 0; i < ff.length; i++) {
					if (ff[i].isFile()) {
						dataOutToServer.write("File_Name".getBytes());
						dataOutToServer.flush();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						str = ff[i].getName();
						System.out.println("正在发送" + str);

						dataOutToServer.write(str.getBytes());
						dataOutToServer.flush();
						readcontent = new BufferedInputStream(
								new DataInputStream(new FileInputStream(ff[i])));

						while ((t = readcontent.read(buf)) != -1) {
							dataOutToServer.write(buf);
							dataOutToServer.flush();
						}
						readcontent.close();

						dataOutToServer.write("File_end".getBytes());
						dataOutToServer.flush();
					}

					else if (ff[i].isDirectory()) {
						File newfile = ff[i];

						System.out.println(ff[i].getName());
						transmit(newfile);
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			dataOutToServer.write("Folder_End".getBytes());
			dataOutToServer.flush();

		}

		public void SocketClose() {
			try {
				SendFileFolderSocket.close();
				dataOutToServer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	class ReceiveFileThread extends Thread {// 接收文件线程
		Message m;
		public ReceiveFileThread(Message m) {
			this.m = m;
		}
		public void run() {
			try {
				// 创建服务器套接字
				serverSocket = new ServerSocket(5500);
				// 侦听客户端的连接
				while (true) {
					ss = serverSocket.accept();
					new ReceiveFileThread_1(ss,m);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;
		}
	}

	public class ReceiveFileThread_1 extends Thread {// 接收文件线程
		private Socket ss;
		Message m;
		public ReceiveFileThread_1(Socket ss,Message m) throws IOException {
			// 创建输入流、输出流
			this.ss = ss;
			this.m = m;
			inFromClient = new DataInputStream(ss.getInputStream());
			outToClient = new DataOutputStream(ss.getOutputStream());
			start();
		}

		public void run() {
			try {
				String fileName = inFromClient.readUTF(); // 先接收文件名
				long fileLength = inFromClient.readLong(); // 再接收文件大小

				FileOutputStream fos = new FileOutputStream(fileName);
				byte buf[] = new byte[4096];
				int n, total = 0;

				while (true) {
					n = inFromClient.read(buf, 0, buf.length);
					total += n;
					System.out.println("当前读取：" + n + "总读取：" + total);

					fos.write(buf, 0, n);
					fos.flush();

					if (total >= fileLength)
						break;
					Thread.sleep(2000);

				}
				
				fos.close();
				inFromClient.close();
				outToClient.close();
				ss.close();
				QqChat qqChat = ManageQqChat.getQqChat(m.getGetter() + " "+ m.getSender());
				qqChat.showMessage2(m);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;

		}
	}

	class ReceiveFileThread2 extends Thread {// 接收文件夹线程

		private ArrayList<String> Directorylist;
		private Socket newsocket;
		private DataInputStream diii;
		private BufferedOutputStream dataout;
		private FileOutputStream fos;
		private boolean flag = true;
		byte[] buftemp_1 = new byte[1024];
		String path;
		byte buf[] = new byte[1024];
		Message m;

		public ReceiveFileThread2(Message m) {
			this.m=m;
			try {
				newsocket = new Socket("localhost", 2000);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Directorylist = new ArrayList<String>();

			try {
				this.diii = new DataInputStream(newsocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			try {
				while (true) {
					int tt = diii.read(buf);
					String path1 = new String(buf, 0, tt);
					System.out.println(path1);
					if (path1.equals("File_Name")) {
						String dir = Directorylist.get(0);
						if (Directorylist.size() > 1) {
							for (int i = 1; i < Directorylist.size(); i++)
								dir = dir + File.separator
										+ Directorylist.get(i);
						}
						int t = diii.read(buf);
						System.out.println("已经接收");
						String ss = new String(buf, 0, t);
						File ff = new File("F:" + File.separator + dir+ File.separator + ss);
						if (!ff.createNewFile()) {
							ff.createNewFile();
						}
						System.out.println(ff.getPath());

						dataout = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(ff)));
						boolean flage = true;
						while (flage) {
							t = diii.read(buf);
							if (t <= 20)
								if (new String(buf, 0, t).equals("File_end")) {
									System.out.println("file_end");
									flage = false;
								}
							dataout.write(buf, 0, t);
							dataout.flush();
						}
						dataout.close();

					} else if (path1.equals("Folder_End")) {
						int count = Directorylist.size();
						if (count > 0)
							Directorylist.remove(count - 1);
						System.out.println(Directorylist.size());
						if (Directorylist.size() == 0) {
							break;
						}
					}

					else if (path1.equals("Folder_Name")) {

						tt = diii.read(buftemp_1);
						System.out.println("****" + tt + "*****");
						String path2 = new String(buftemp_1, 0, tt);
						System.out.println("正在接收" + path2 + "文件夹");
						Directorylist.add(path2);
						String dir = Directorylist.get(0);
						if (Directorylist.size() > 1) {
							for (int i = 1; i < Directorylist.size(); i++)
								dir = dir + File.separator
										+ Directorylist.get(i);
						}

						File f = new File("F:" + File.separator + dir);
						f.mkdir();
					}

				}
				System.out.println("文件夹传输结束");
				diii.close();
				QqChat qqChat = ManageQqChat.getQqChat(m.getGetter() + " "+ m.getSender());
				QqChat qqChat1 = ManageQqChat.getQqChat(m.getSender() + " "+ m.getGetter());
				qqChat.showMessage2(m);
				qqChat1.showMessage2(m);

			} catch (IOException e) {
			}
			SocketClose();
		}

		public void SocketClose() {
			try {
				newsocket.close();
				Directorylist.clear();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public Socket getS() {
		return socket;
	}

	public void setS(Socket s) {
		socket = s;
	}

}
