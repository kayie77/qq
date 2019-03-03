package view;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import total.ManageClientConServerThread;
import total.Message;

public class QqGroup extends JFrame implements ActionListener {

	String ownerId;
	JTextArea jta;
	JTextField jtf;
	JButton jb;
	JPanel jp;

	public QqGroup(String ownerId) {
		super();
		this.ownerId = ownerId;
		this.jta = new JTextArea();
		this.jtf = new JTextField(30);
		this.jb = new JButton("发送消息");
		jb.addActionListener(this);
		this.jp = new JPanel();
		jp.add(jtf);
		jp.add(jb);
		this.add(jta, "Center");
		this.add(jp, "South");
		this.setTitle(ownerId + "群聊 ");
		this.setIconImage((new ImageIcon("image/qq.gif").getImage()));
		this.setSize(400, 300);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jb)// 发送消息
		{
			Message m = new Message();
			m.setMesType("20");// MessageType.message_comm_mes
			m.setSender(this.ownerId);
			m.setGetter("");
			m.setCon(jtf.getText());
			this.jta.append("我说:" + jtf.getText() + "\r\n");
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						ManageClientConServerThread
								.getClientConServerThread(ownerId).getS()
								.getOutputStream());
				oos.writeObject(m);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

	}

	public void showMessage(Message m) {
		String info = m.getSender() + "说:" + m.getCon() + "\r\n";
		this.jta.append(info);
		jtf.setText("");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
	}

}
