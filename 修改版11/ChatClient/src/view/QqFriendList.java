package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import total.ManageQqChat;
import total.ManageQqGroup;

public class QqFriendList extends JFrame implements ActionListener,
		MouseListener {
	private JPanel contentPane;
	JLabel[] jbls;
	String owner;
	File file;
	QqGroup group = null;

	JButton button1, button2;
	private JPanel panel;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
	}

	public QqFriendList(String ownerId) {// 锟斤拷锟皆硷拷锟侥憋拷糯锟斤拷锟斤拷锟�

		new ManageQqGroup();
		this.owner = ownerId;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 191, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel jp1 = new JPanel(new GridLayout(50, 1, 4, 4));
		jbls = new JLabel[10];
		for (int i = 0; i < jbls.length; i++) {
			jbls[i] = new JLabel(i + 1 + "", new ImageIcon("image/QQ头像.jpg"),
					JLabel.LEFT);
			jbls[i].addMouseListener(this);
			jp1.add(jbls[i]);

		}

		JScrollPane jspl = new JScrollPane(jp1);

		contentPane.add(jspl, "Center");

		getContentPane().add(contentPane, "Center");
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		button1 = new JButton("好友列表");
		panel.add(button1, BorderLayout.NORTH);
		button2 = new JButton("群聊");
		panel.add(button2, BorderLayout.WEST);
		button2.addActionListener(this);
		this.setTitle(ownerId);
		this.setSize(201, 500);
		this.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getClickCount() == 2) {
			String friendnum = ((JLabel) arg0.getSource()).getText();// 得到好友编号
			QqChat qqChat = new QqChat(this.owner, friendnum);
			ManageQqChat.addQqChat(this.owner + " " + friendnum, qqChat);// 对应的聊天界面加入到ManageQqChat管理类中
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel j1 = (JLabel) arg0.getSource();
		j1.setForeground(Color.blue);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel j1 = (JLabel) arg0.getSource();
		j1.setForeground(Color.black);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == button2) {
			if (group == null)
				group = new QqGroup(owner);
			ManageQqGroup.addQqGroup(owner, group);

		}
	}
}
