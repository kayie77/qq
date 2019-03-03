// 这是一个管理客户端和服务器保持通讯的线程类
package total;

import java.util.*;

import major.SerConClientThread;
public class ManageClientThread {

	public static HashMap hm=new HashMap<String, SerConClientThread>();
	
	//向hm中添加一个客户端通讯线程
	public static  void addClientThread(String uid,SerConClientThread ct)
	{
		hm.put(uid, ct);
	}
	
	public static SerConClientThread getClientThread(String uid)
	{
		return (SerConClientThread)hm.get(uid);
	}
	
}
