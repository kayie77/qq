/**
 * 这是用户信息类
 */
package total;

public class User implements java.io.Serializable {
//implements 这个Serializable这个接口  将user序列化才能够在用ObjectInputStream/ObjectOutputStream网络上传输这个u对象
	private String userId;
	private String passwd;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
}
