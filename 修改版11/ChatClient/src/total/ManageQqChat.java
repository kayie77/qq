/**
 * 这是一个管理用户聊天界面的类
 */
package total;

import java.util.*;
import view.*;
public class ManageQqChat {
    //String 为一个loginIdAnFriendId 即为哪个用户登录的ID号
	private static HashMap hm=new HashMap<String, QqChat>();
	
	//加入
	public static void addQqChat(String loginIdAnFriendId,QqChat qqChat)
	{
		hm.put(loginIdAnFriendId, qqChat);
	}
	//取出
	public static QqChat getQqChat(String loginIdAnFriendId )
	{
		return (QqChat)hm.get(loginIdAnFriendId);
	}
	
}
