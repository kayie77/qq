/**
 * 功能：是服务器和某个客户端的通信线程
 */
package major;

import java.util.*;
import java.net.*;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import total.ManageClientThread;
import total.*;

public class SerConClientThread extends Thread {// extends JFrame implements
												// Runnable

	Socket socket;

	public SerConClientThread(Socket s) {
		socket = s;
	}

	public void run() {

		while (true) {
			// 这里该线程就可以接收客户端的信息.
			try {
				ObjectInputStream ois = new ObjectInputStream(
						socket.getInputStream());
				Message m = (Message) ois.readObject();

				// 对从客户端取得的消息进行类型判断，然后做相应的处理
				if (m.getMesType().equals("3"))// MessageType.message_comm_mes
				{
					// 完成转发.
					// 取得接收人的通信线程，即为从hashmap里面取出接收人的socket_id
					SerConClientThread sc = ManageClientThread
							.getClientThread(m.getGetter());
					ObjectOutputStream oos = new ObjectOutputStream(
							sc.socket.getOutputStream());
					oos.writeObject(m);
				} else if (m.getMesType().equals("20"))// MessageType.message_comm_mes
				{
					// 完成转发.
					// 取得接收人的通信线程，即为从hashmap里面取出接收人的socket_id
					// Iterator it = ManageClientThread.hm.keySet().iterator();
					Set<String> it = ManageClientThread.hm.keySet();
					for (String uid : it) {
						if (!m.getSender().equals(uid)) {
							System.out.println(uid);
							SerConClientThread sc = (SerConClientThread) ManageClientThread.hm
									.get(uid);
							ObjectOutputStream oos = new ObjectOutputStream(
									sc.socket.getOutputStream());
							oos.writeObject(m);
						}
					}
				}
				// 发送文件
				else if (m.getMesType().equals("6")) {// 表示准备发送文件
					SerConClientThread sc1 = ManageClientThread
							.getClientThread(m.getSender());// 取出的是发送人的线程
					ObjectOutputStream oos1 = new ObjectOutputStream(
							sc1.socket.getOutputStream());
					oos1.writeObject(m);
				} else if (m.getMesType().equals("4")) {// 发送文件

					SerConClientThread sc1 = ManageClientThread
							.getClientThread(m.getSender());// 取出的是发送人的线程
					ObjectOutputStream oos1 = new ObjectOutputStream(
							sc1.socket.getOutputStream());
					oos1.writeObject(m);
				} else if (m.getMesType().equals("5"))// 文件接收程序开始运行
				{
					SerConClientThread sc = ManageClientThread
							.getClientThread(m.getGetter());// 取出的是接收人的线程
					ObjectOutputStream oos = new ObjectOutputStream(sc.socket.getOutputStream());
					oos.writeObject(m);
				}
				// 发送文件夹
				else if (m.getMesType().equals("7")) {// 表示准备发送方文件夹
					SerConClientThread sc1 = ManageClientThread.getClientThread(m.getSender());// 取出的是发送人的线程
					ObjectOutputStream oos1 = new ObjectOutputStream(
							sc1.socket.getOutputStream());
					oos1.writeObject(m);
				} else if (m.getMesType().equals("8")) {// 文件夹接收程序开始运行
					SerConClientThread sc1 = ManageClientThread.getClientThread(m.getGetter());// 取出的是接收人的线程
					ObjectOutputStream oos1 = new ObjectOutputStream(
							sc1.socket.getOutputStream());
					oos1.writeObject(m);

				} else if (m.getMesType().equals("9")) {// 文件夹接收程序开始运行
					SerConClientThread sc1 = ManageClientThread
							.getClientThread(m.getSender());// 取出的是发送人的线程
					ObjectOutputStream oos1 = new ObjectOutputStream(
							sc1.socket.getOutputStream());
					oos1.writeObject(m);
				} else if (m.getMesType().equals("10")) {// 文件夹接收程序开始运行
					System.out.println("10");
					SerConClientThread sc1 = ManageClientThread
							.getClientThread(m.getGetter());// 取出的是接收人的线程
					ObjectOutputStream oos1 = new ObjectOutputStream(
							sc1.socket.getOutputStream());
					oos1.writeObject(m);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}

		}

	}
}
