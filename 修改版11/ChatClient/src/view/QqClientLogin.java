/**
 * 功能:qq客户端登录界面
 */
package view;

import total.*;
import major.*;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QqClientLogin extends JFrame implements ActionListener {
	JLabel jbl1;// 北部标签
	JPanel jp2;// 中间面板
	JLabel jp2_jbl1, jp2_jbl2;// QQ号码标签，QQ密码标签
	JTextField jp2_jtf;// QQ号码框
	JPasswordField jp2_jpf;// QQ密码框
	JPanel jp1;// 南部面板
	JButton jp1_jb1, jp1_jb2;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		QqClientLogin qqClientLogin = new QqClientLogin();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
	}

	public QqClientLogin() {
		// 北部
		jbl1 = new JLabel(new ImageIcon("image/QQ1.jpg"));

		// 中部
		JPanel panel_1 = new JPanel();
		jp2_jbl1 = new JLabel("QQ号码");
		jp2_jbl2 = new JLabel("QQ密码");
		jp2_jtf = new JTextField();
		jp2_jpf = new JPasswordField();

		jp2_jbl1.setBounds(70, 53, 51, 15);
		jp2_jbl2.setBounds(70, 91, 51, 15);
		panel_1.setLayout(null);
		panel_1.add(jp2_jbl1);
		panel_1.add(jp2_jbl2);
		jp2_jtf.setBounds(131, 50, 121, 21);
		panel_1.add(jp2_jtf);
		jp2_jtf.setColumns(10);
		jp2_jpf.setBounds(131, 88, 121, 21);
		panel_1.add(jp2_jpf);

		// 南部
		jp1 = new JPanel();
		jp1_jb1 = new JButton("登录");
		jp1_jb2 = new JButton("取消");
		// 响应用户点击登录
		jp1_jb1.addActionListener(this);
		jp1.add(jp1_jb1);
		jp1_jb2.addActionListener(this);
		jp1.add(jp1_jb2);

		this.add(jbl1, "North");
		this.add(panel_1, BorderLayout.CENTER);
		this.add(jp1, "South");

		setBounds(100, 100, 400, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == jp1_jb1)// 登录
		{
			QqClientUser qqClientUser = new QqClientUser();
			User u = new User();
			u.setUserId(jp2_jtf.getText().trim());
			u.setPasswd(new String(jp2_jpf.getPassword()));
			if (qqClientUser.checkUser(u)) {
				try {

					QqFriendList qqList = new QqFriendList(u.getUserId());

				} catch (Exception e) {
					e.printStackTrace();
				}
				// 关闭掉登录界面
				// this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "用户名密码错误");
			}
		} else if (arg0.getSource() == jp1_jb2)// 取消
		{
			System.exit(0);

		}
	}

}
