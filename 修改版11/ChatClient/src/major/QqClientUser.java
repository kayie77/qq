package major;

import total.*;
public class QqClientUser {
	//响应客户需求的各种事物的类，比如说删除或者添加好友，发送文件等等，就是业务逻辑层次
	//检查用户登录成功与否
	public boolean checkUser(User u)
	{
		return new QqClientConServer().sendLoginInfoToServer(u);
	}
	
}
