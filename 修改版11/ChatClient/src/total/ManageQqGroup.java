package total;

import java.util.HashMap;

import view.QqGroup;


public class ManageQqGroup {
private static HashMap hm=new HashMap<String, QqGroup>();
	
	//加入
	public static void addQqGroup(String loginId,QqGroup QqGroup)
	{
		hm.put(loginId, QqGroup);
	}
	//取出
	public static QqGroup getQqGroup(String loginId)
	{
		return (QqGroup)hm.get(loginId);
	}
	public ManageQqGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
}
