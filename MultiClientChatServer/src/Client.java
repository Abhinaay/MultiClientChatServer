import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
public class Client implements MouseListener,ActionListener
{
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	JList usersList;
	
	JFrame jf;
	JPanel p1;
	JTextArea jta1,jta2;
	JTextField jtf1;
	JButton send,login,logout,exit;
	JLabel jl1,jl2;
	
	Client()
	{
		jf=new JFrame();
		jf.setLayout(null);
		jf.setContentPane(new JLabel(new ImageIcon("bg33.jpg")));
		
		usersList=new JList();
		jf.add(p1=new JPanel());
			p1.setBounds(10,45,780,2);
			p1.setBackground(Color.DARK_GRAY);
		
		jf.add(jta1=new JTextArea());
			jta1.setBounds(10, 55, 500, 250);
			jta1.setBorder(BorderFactory.createTitledBorder("<html><font size=5 color=orange>BroadCast</font> <font size=5 color=Black>Messages</font></html>"));
			jta1.setFont(new Font("Cooper",Font.PLAIN,14));
			
		jf.add(jta2=new JTextArea());
			jta2.setBounds(520, 55, 270, 250);
			jta2.setBorder(BorderFactory.createTitledBorder("<html><font size=5 color=orange>Online</font> <font size=5 color=Black>Users</font></html>"));
			jta2.add(new JScrollPane(usersList));
			
		jf.add(jtf1=new JTextField());
			jtf1.setBounds(10,315,650,30);
			jtf1.setBorder(null);
			
		jf.add(send=new JButton("Send"));
			send.setBounds(670, 315, 120, 30);
			send.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
			send.setForeground(Color.MAGENTA);
			send.setFont(new Font("Cooper",Font.BOLD,20));
			send.setContentAreaFilled(false);
			
		jf.add(jl1=new JLabel("BroadCast Messages"));
		jf.add(jl2=new JLabel("OnlineUsers"));
		
			
		jf.add(login=new JButton("Login"));
			login.setBounds(690, 10, 100, 30);
			login.setOpaque(false);
			login.setContentAreaFilled(false);
			login.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
			login.setForeground(Color.MAGENTA);
			login.setFont(new Font("Cooper",Font.BOLD,20));
		jf.add(logout=new JButton("Logout"));
			logout.setVisible(false);
			logout.setBounds(690, 10, 100, 30);
			logout.setOpaque(false);
			logout.setContentAreaFilled(false);
			logout.setBorder(BorderFactory.createLineBorder(Color.white));
			logout.setForeground(Color.MAGENTA);
			logout.setFont(new Font("Cooper",Font.BOLD,20));
		jf.add(exit=new JButton("<html><font size=6 color=black alignment=center>x</font></html>"));
			exit.setBorder(null);
			exit.setContentAreaFilled(false);
			exit.setBounds(10, 11, 25, 25);
		
		
		jf.setLocation(260, 200);
		jf.setUndecorated(true);
		jf.setVisible(true);
		jf.setSize(800, 360);
		
		exit.addMouseListener(this);
		exit.addActionListener(this);
		login.addMouseListener(this);
		login.addActionListener(this);
		logout.addMouseListener(this);
		logout.addActionListener(this);
		send.addMouseListener(this);
		send.addActionListener(this);
		
		//clientChat();
	}
	
	public static void main(String[] args) 
	{
		new Client();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		if(arg0.getComponent()==exit)
		{
		exit.setContentAreaFilled(true);
		exit.setBackground(Color.red);
		}
		if(arg0.getComponent()==login)
		{
		login.setContentAreaFilled(true);
		login.setBackground(new Color(55,4,102,255));
		}
		if(arg0.getComponent()==logout)
		{
		logout.setContentAreaFilled(true);
		logout.setBackground(new Color(55,4,102,255));
		}
		if(arg0.getComponent()==send)
		{
		send.setContentAreaFilled(true);
		send.setBackground(new Color(55,4,102,255));
		}
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		exit.setContentAreaFilled(false);
		login.setContentAreaFilled(false);
		logout.setContentAreaFilled(false);
		send.setContentAreaFilled(false);
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
	/*public void actionPerformed(ActionEvent arg0) 
	{
		if(arg0.getSource()==exit)
		{
			System.exit(0);
		}
		
		
		
		
	}
	*/
	public void actionPerformed(ActionEvent ev)
	{
	
	if(ev.getSource()==send)
	{
	if(s==null)
	 	{JOptionPane.showMessageDialog(jf,"u r not logged in. plz login first"); return;}
	try{
		dos.writeUTF(jtf1.getText());
		jtf1.setText("");
	     }
	catch(Exception excp){jta1.append("\nsend button click :"+excp);}
	}
	if(ev.getSource()==login)
	{
	String uname=JOptionPane.showInputDialog(jf,"Enter Your lovely nick name: ");
	if(uname!=null)
		clientChat(uname); 
	}
	if(ev.getSource()==logout)
	{
	if(s!=null)
		logoutSession();
	}
	if(ev.getSource()==exit)
	{
	if(s!=null)
	{
	JOptionPane.showMessageDialog(jf,"u r logged out right now. ","Exit",JOptionPane.INFORMATION_MESSAGE);
	logoutSession();
	}
	System.exit(0);
	}
	}
	///////////////////////////
	public void logoutSession()
	{
	if(s==null) return;
	try{
	dos.writeUTF(Server.LOGOUT_MESSAGE);
	Thread.sleep(500);
	s=null;
	}
	catch(Exception e){jta1.append("\n inside logoutSession Method"+e);}

	logout.setVisible(false);
	//logout.setEnabled(false);
	login.setVisible(true);
	//login.setEnabled(true);
	jf.setTitle("Login for Chat");
	}
	//////////////////////////
	public void clientChat(String uname)
	{
	try{
	     s=new Socket(InetAddress.getLocalHost(),Server.PORT);
	     dis=new DataInputStream(s.getInputStream());
	     dos=new DataOutputStream(s.getOutputStream());
	     ClientThread ct=new ClientThread(dis,this);
	     Thread t1=new Thread(ct);
	     t1.start();
	     dos.writeUTF(uname);
	     jf.setTitle(uname+" Chat Window");
	    }
	catch(Exception e){jta1.append("\nClient Constructor " +e);}
	//logout.setEnabled(true);
	//login.setEnabled(false);
	logout.setVisible(true);
	login.setVisible(false);
	}
	///////////////////////////////
/*	public MyClient()
	{
	  	displayGUI();
//		clientChat();
	}
*/
	///////////////////////////////
/*	public static void main(String []args)
	{
	new MyClient();
	}
*/	//////////////////////////
	}
	/*********************************/
	class ClientThread implements Runnable
	{
	DataInputStream dis;
	Client client;

	ClientThread(DataInputStream dis,Client client)
	{
	this.dis=dis;
	this.client=client;
	}
	////////////////////////
	public void run()
	{
		
	String s2="";
	do
	    {
		try{
			s2=dis.readUTF();
			if(s2.startsWith(Server.UPDATE_USERS))
			{	
				updateUsersList(s2);
				//client.jta2.setText(s2+"\n");
			}
			else if(s2.equals(Server.LOGOUT_MESSAGE))
				break;
			else
				client.jta1.append("\n"+s2);
			int lineOffset=client.jta1.getLineStartOffset(client.jta1.getLineCount()-1);
			client.jta1.setCaretPosition(lineOffset);
		     }
		catch(Exception e){client.jta1.append("\nClientThread run : "+e);}
	   }
	while(true);
	}
	//////////////////////////
	public void updateUsersList(String ul)
	{
	Vector ulist=new Vector();
	ulist.add(ul);
	ul=ul.replace("[","");
	ul=ul.replace("]","");
	ul=ul.replace(Server.UPDATE_USERS,"");
	ul=ul.replace(",","\n");
	client.jta2.setText(ul);
	/*Iterator i2=ulist.iterator();
	while(i2.hasNext())
	{
		client.jta2.append((String)i2.next());
	}
	*/
	
	//StringTokenizer st=new StringTokenizer(ul,",");
	/*while(st.hasMoreTokens())
	{
	String temp=st.nextToken();
	ulist.add(temp);
	}
	
	client.usersList.setListData(ulist);
	*/
	}
	/////////////////////////
	}



