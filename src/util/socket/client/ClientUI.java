package util.socket.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.TaskInterface;
import util.TaskMake;
import util.ThreadUtil;
import util.Tools;
import util.socket.FrameSocket;
import util.socket.Msg;
import util.socket.ThreadUtilServer;

public class ClientUI extends JFrame  {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new ClientUI();
	}

	public JButton jbshowusers = new JButton("好友");
	public JButton jbshowrooms = new JButton("会话");
	public JButton jbtest = new JButton("自动喊话");
	public JCheckBox jcbscroll = new JCheckBox("锁定");

	public JButton btStart;// 启动服务器
	public JButton btSend;// 发送信息按钮
	public JButton btLogin;// 发送信息按钮
	public JTextField jtfSend;// 需要发送的文本信息
	public JTextField jtfSend1;// 需要发送的文本信息
	public JTextField jtfSend2;// 需要发送的文本信息
	public JTextArea taShow;// 信息展示
	

	 
	public ClientUI() {
		super("模拟客户端");
		btStart = new JButton("关闭");
		btSend = new JButton("发送");
		btLogin = new JButton("登录");

		jtfSend = new JTextField(20);
		jtfSend1 = new JTextField(6);
		jtfSend2 = new JTextField(6);
		jtfSend1.setText("101");
		jtfSend2.setText("123");
		jtfSend.setText("{\"cmd\":12,\"value0\":\"group\",\"value1\":\"100000\",\"value2\":\"text\",\"value3\":\"2017-05-24 00:03:31\",\"value4\":\"消息 "+ Tools.getNowTimeL() +"\"}");

		taShow = new JTextArea();


		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client == null) {
					startclient();
					btStart.setText("关闭");
				} else {
					stop();
					client = null;
					btStart.setText("启动");
				}
			}

			
		});
		btSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mmsg = ("" + jtfSend.getText());// 写入发送流到 客户端去
			   startSendTask(client, mmsg);
			}
		});
		btLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sysKey = jtfSend1.getText();
				String key = jtfSend2.getText();
				Msg msg = new Msg();
				msg.setMsgType(Msg.LOGIN_CLIENT);
				msg.setToKey(key);
				msg.setToSysKey(sysKey);
				startSendTask(client, msg.getData());
				
			}
		});
		jbshowrooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		jbtest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}

			
		});
		jbshowusers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				 
			}
		});
		JPanel top = new JPanel(new FlowLayout());
		top.add(jtfSend);
		top.add(btSend);

		top.add(jtfSend1);
		top.add(jtfSend2);
		top.add(btLogin);

		top.add(btStart);
		top.add(jbshowrooms);
		top.add(jbtest);
		top.add(jbshowusers);

		jcbscroll.setSelected(true);
		top.add(jcbscroll);
		this.add(top, BorderLayout.SOUTH);
		final JScrollPane sp = new JScrollPane();
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setViewportView(this.taShow);
		this.taShow.setEditable(false);
		this.add(sp, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(80, 80, 780, 280);
		this.setVisible(true);
		startclient(); 
	//	test();
	}
	
	Socket client = null;
	String ip = "127.0.0.1";
	int port = 8090;
	int reconnect = 998;	//重连次数
	long sleeptime = 1000;//重连间隔
	private void startclient()   {
		startConnectTask();
	}
	 
	 
	//开始连接任务
	private void startConnectTask(){
		TaskMake task = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				out("连接成功", client);
			}
			@Override
			public void onFalse() {
				out("连接多次后依然失败 -----");
			}
			@Override
			public void doTask() throws Exception {
				Socket socket = new Socket(ip, port);
				client = socket;
				startReadTask();
			}
		}, "连接服务器", sleeptime, reconnect);
		task.startTask();
	}  
	//开始读取任务
	private void startReadTask(){
		TaskMake taskMake = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				startReadTask();	//一个task(20延迟,最多连续两次异常读取)
			}
			@Override
			public void onFalse() {	//多次异常读取后就认为失联
				out("失联", client);
				startConnectTask();
			}
			@Override
			public void doTask() throws Exception {
				String readLine = read(client);
				if(Tools.isNull(readLine)){
					onReceive(client, readLine);
				}
			}
		}, "读取消息",1000, 5);	//最多5次读取
		taskMake.startTask();
	}  
	//开启发送任务
	public void startSendTask(final Socket socket, final String jsonstr){
		TaskMake task = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				out("发送成功", jsonstr);
			}
			@Override
			public void onFalse() {
				out("发送失败", jsonstr);
			}
			@Override
			public void doTask() throws Exception {
				send(socket, jsonstr);
			}
		});
		task.startTask();
	}
	//读取数据
	private String read(Socket socket) throws Exception{
		String res = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		res = reader.readLine();
		return res;
	}
	//发送数据
	public void send(Socket socket, String jsonstr) throws Exception {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter( socket.getOutputStream()));
		writer.write(jsonstr); 
		writer.newLine();
		writer.flush();
	}
	public void onReceive(Socket socket, String jsonstr) {
		out("<<<<<<<", jsonstr);
	}

	
	
	
	public void stop(){
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	public void out(Object...objects) {
		String s = Tools.objects2string(objects);
		if (s != null) {// 输出当服务端的界面上去显示
			if (s.length() > 600)
				s = Tools.tooLongCut(s); // 太长的数据 
			if (this.taShow.getText().length() >= 14000) {
				this.taShow.setText("");
			}
			this.taShow.append(s + "\n");
			
			if(this.jcbscroll.isSelected())
				this.taShow.setCaretPosition(this.taShow.getText().length()); // 锁定最底滚动

//			Tools.out(s);
		}
	}

}