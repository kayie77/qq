/**
 * 这是客户端连接服务器的后台
 */
package major;

import java.util.*;
import java.net.*;
import java.io.*;
import total.*;
public class QqClientConServer {

	
	public  Socket s;
	
	//发送第一次请求
	public boolean sendLoginInfoToServer(Object o)
	{
		boolean b=false;
		try {
			s=new Socket("localhost",8000);
//			s=new Socket("10.5.107.247",5000);
			//客户端先发送数据
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(o);
			
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			
			Message ms=(Message)ois.readObject();
			//这里就是验证用户登录的地方
			if(ms.getMesType().equals("1"))
			{
				//就创建一个该qq号和服务器端保持通讯连接得线程
				ClientConServerThread ccst=new ClientConServerThread(((User)o).getUserId(),s);
				//启动该通讯线程
				
				new Thread(ccst).start();
				ManageClientConServerThread.addClientConServerThread(((User)o).getUserId(), ccst);
				b=true;
			}else{
				//关闭Scoket
				s.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			
		}
		return b;
	}
	
	public void SendInfoToServer(Object o)
	{
		/*try {
			Socket s=new Socket("127.0.0.1",9999);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			
		}*/
	}
}
