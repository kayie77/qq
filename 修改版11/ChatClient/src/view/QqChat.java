/**
 * 这是与好友聊天的界面
 * 因为客户端，要处于读取的状态，因此我们把它做成一个线程
 */
package view;

import total.*;
import major.*;
import major.ClientConServerThread.FileSendingThread;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class QqChat extends JFrame implements ActionListener{

	JTextArea jta;
	JTextField jtf;
	JButton jb,jb1,jb2,jb3;
	JPanel jp,jp1;
	String ownerId;
	String friendId;
	File file;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
	}
	
	public QqChat(String ownerId,String friend)
	{
		this.ownerId=ownerId;
		this.friendId=friend;
		jta=new JTextArea();
		jtf=new JTextField(30);
		jb=new JButton("发送消息");
		jb.addActionListener(this);
		jb1=new JButton("发送文件");
		jb1.addActionListener(this);
		jb3=new JButton("发送文件夹");
		jb3.addActionListener(this);
		jb2=new JButton("群聊");
		jb2.addActionListener(this);
		jp=new JPanel();
		jp.add(jtf);
		jp.add(jb);
		jp.add(jb1);
		jp.add(jb3);
		jp.add(jb2);
		
		this.add(jta,"Center");
		this.add(jp,"South");
		this.setTitle(ownerId+" 和 "+friend+" 的聊天窗口");
		this.setIconImage((new ImageIcon("image/qq.gif").getImage()));
		this.setSize(500, 450);
		this.setVisible(true); 
		
		
	}
	
	public void showMessage(Message m)
	{
		String info=m.getSender()+"说:"+m.getCon()+"\r\n";
		this.jta.append(info);
		jtf.setText("");
	}
    public void showMessage1(Message m)//确认接收文件
	{
    	int option=JOptionPane.showConfirmDialog(this,"客户端有文件发送过来，是否接收？", "文件发送提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(option==JOptionPane.YES_OPTION){						
			m.setMesType("4");
			try {
				ObjectOutputStream oos=new ObjectOutputStream
				(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
				oos.writeObject(m);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
    public void showMessage2(Message m)
	{
	
		int option=JOptionPane.showConfirmDialog(this,"文件已传输完毕", "文件接收完成提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(option==JOptionPane.YES_OPTION){
			this.jta.append("文件接收成功");
		}

	}
    
    public void showMessage3(Message m)//确认接收文件夹
   	{
       	int option1=JOptionPane.showConfirmDialog(this,"客户端有文件夹发送过来，是否接收？", "文件发送提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
   		if(option1==JOptionPane.YES_OPTION){						
   			m.setMesType("9");
   			try {
   				ObjectOutputStream oos=new ObjectOutputStream
   				(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
   				oos.writeObject(m);
   			} catch (Exception e) {
   				e.printStackTrace();
   			}	
   		}
   	}
    


	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==jb)//发送消息
		{
			Message m=new Message();
			m.setMesType("3");//MessageType.message_comm_mes
			m.setSender(this.ownerId);
			m.setGetter(this.friendId);
			m.setCon(jtf.getText());
			this.jta.append("我说:"+jtf.getText()+"\r\n");
			try {
				ObjectOutputStream oos=new ObjectOutputStream
				(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
				oos.writeObject(m);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
		}		
		 else if(arg0.getSource()==jb1) {//发送文件
			 Message m=new Message();
		     m.setMesType("6");//5
			 m.setSender(this.ownerId);
			 m.setGetter(this.friendId);	
			 this.jta.append("正在发送文件"+"\r\n");
					try {
						ObjectOutputStream oos=new ObjectOutputStream
						(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
						oos.writeObject(m);
					} catch (Exception e) {
						e.printStackTrace();
					}	
				}	
		 else if(arg0.getSource()==jb3) {//发送文件夹
			 Message m=new Message();
		     m.setMesType("7");
			 m.setSender(this.ownerId);
			 m.setGetter(this.friendId);	
			 this.jta.append("正在发送文件夹"+"\r\n");
			 try {
					ObjectOutputStream oos=new ObjectOutputStream
					(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
					oos.writeObject(m);
				} catch (Exception e) {
					e.printStackTrace();
				}	
		 }

			}

}
