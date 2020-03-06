import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Server
{
	ArrayList al=new ArrayList();
	ArrayList users=new ArrayList();
	ServerSocket ss;
	Socket s;

	public final static int PORT=10;
	public final static String UPDATE_USERS="updateuserslist:";
	public final static String LOGOUT_MESSAGE="@@#logoutme#@@:";
	public Server()
	{
		try{
			ss=new ServerSocket(PORT);
			System.out.println("Server Started "+ss);
			while(true)
			{
				s=ss.accept();
				MyThread r=new MyThread(s,al,users);	// ?
				Thread t=new Thread(r);
				t.start();
//	System.out.println("Total alive clients : "+ss.);
			}
		}
		catch(Exception e){System.err.println("Server constructor"+e);}
	}

	public static void main(String [] args)
	{
		new Server();
	}

}

class MyThread implements Runnable
{
	Socket s;
	ArrayList al;
	ArrayList users;
	String username;

	MyThread (Socket s, ArrayList al,ArrayList users)
	{
		this.s=s;
		this.al=al;
		this.users=users;
		try{
			DataInputStream dis=new DataInputStream(s.getInputStream());
			username=dis.readUTF();
			al.add(s);
			users.add(username);
			tellEveryOne("****** "+ username+" Logged in at "+(new Date())+" ******");
			sendNewUserList();
			
		}
		catch(Exception e){System.err.println("MyThread constructor  "+e);}
	}

	public void run()
	{
		String s1;
		try{
			DataInputStream dis=new DataInputStream(s.getInputStream());
			do
			{
				s1=dis.readUTF();	//utf jab data encrypt karke bhejna ho.
				if(s1.toLowerCase().equals(Server.LOGOUT_MESSAGE)) break;
              //	System.out.println("received from "+s.getPort());
				tellEveryOne(username+" said: "+" : "+s1);
			}
			while(true);
			DataOutputStream tdos=new DataOutputStream(s.getOutputStream());
			tdos.writeUTF(Server.LOGOUT_MESSAGE);
			tdos.flush();
			users.remove(username);
			tellEveryOne("****** "+username+" Logged out at "+(new Date())+" ******");
			sendNewUserList();
			al.remove(s);
			s.close();
			
		}
		catch(Exception e){System.out.println("MyThread Run"+e);}
	}

	public void sendNewUserList()
	{
		tellEveryOne(Server.UPDATE_USERS+users.toString());

	}

	public void tellEveryOne(String s1)	
	{
		Iterator i=al.iterator();
		while(i.hasNext())
		{
			try{
				Socket temp=(Socket)i.next();
				DataOutputStream dos=new DataOutputStream(temp.getOutputStream());
				dos.writeUTF(s1);
				dos.flush();
				
	//System.out.println("sent to : "+temp.getPort()+"  : "+ s1);
			}
			catch(Exception e){System.err.println("TellEveryOne "+e);}
		}
	}
	

}