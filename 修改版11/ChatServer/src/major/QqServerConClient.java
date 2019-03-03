/**
 * 这是qq服务器，它在监听，等待某个qq客户端，来连接
 */
package major;

import total.*;
import java.net.*;
import java.io.*;
import java.util.*;

import total.ManageClientThread;

public class QqServerConClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QqServerConClient qscc = new QqServerConClient();
	}

	public QqServerConClient() {
		try {
			System.out.println("服务器正在8000监听");
			ServerSocket ss = new ServerSocket(8000);

			while (true) {
				Socket s = ss.accept();
				ObjectInputStream ois = new ObjectInputStream(
						s.getInputStream());// 接收客户端发来的信息.
				User u = (User) ois.readObject();// 封装成User对象，因为第一次接受到的对象是要用来验证登录的
				Message m = new Message();
				ObjectOutputStream oos = new ObjectOutputStream(
						s.getOutputStream());
				if (u.getPasswd().equals("000")) {
					m.setMesType("1"); // 返回成功登陆信息报
					oos.writeObject(m); // 把m对象写回客户端去
					// 登录成功
					SerConClientThread scct = new SerConClientThread(s);// 每个socket单独开一个线程
					// 然后把新单开的线程放到ManageClientThread里面去管理
					ManageClientThread.addClientThread(u.getUserId(), scct);// 因为addClientThread方法是静态的，要用类名直接去引用
					// 启动
					scct.start();
				} else {
					m.setMesType("2");
					oos.writeObject(m);
					s.close();
					ois.close();
					oos.close();

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
