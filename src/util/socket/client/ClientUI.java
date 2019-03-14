package util.socket.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.Tools;
import util.socket.server_0.Msg;

public class ClientUI extends JFrame  {

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
	
	Client client;
	public ClientUI(Client cc, String name) {
		super(name);
		init(cc, name);
	}
	public ClientUI(Client cc) {
		this(cc, "模拟客户端");
	}
	public void init(Client cc, String name){
		this.client = cc;
		this.client.setUI(this);
		this.client.start(); 
		btStart = new JButton("关闭");
		btSend = new JButton("发送");
		btLogin = new JButton("登录");

		jtfSend = new JTextField(20);
		jtfSend1 = new JTextField(6);
		jtfSend2 = new JTextField(6);
		jtfSend1.setText("raspberrypi");
		jtfSend2.setText("0");
//		jtfSend.setText("{\"cmd\":12,\"value0\":\"group\",\"value1\":\"100000\",\"value2\":\"text\",\"value3\":\"2017-05-24 00:03:31\",\"value4\":\"消息 "+ Tools.getNowTimeL() +"\"}");
		jtfSend.setText("{\"sy\":\""+ name + "\"}");

		taShow = new JTextArea();


		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client != null) {
					client.start();
					btStart.setText("关闭");
				} else {
					client.stop();
					client = null;
					btStart.setText("启动");
				}
			}

			
		});
		btSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mmsg = ("" + jtfSend.getText());// 写入发送流到 客户端去
				
				String sysKey = jtfSend1.getText();	//发往系统
				String key = jtfSend2.getText();	//发往用户连接
				Msg msg = new Msg();
				msg.setMsgType(Msg.DATA);
				msg.setToKey(key);
				msg.setToSysKey(sysKey);
				msg.setFromSysKey(sysKey);
				msg.put("data", mmsg);
				client.send(msg.getData());
			}
		});
		btLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sysKey = jtfSend1.getText();	//发往系统
				String key = jtfSend2.getText();	//发往用户连接
				Msg msg = new Msg();
				msg.setMsgType(Msg.LOGIN);
				msg.setToKey(key);
				msg.setToSysKey(sysKey);
				client.send(msg.getData());
				
			}
		});
		jbshowrooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out(client.show());
				Msg msg = new Msg();
				msg.setMsgType(Msg.SHOW);
				client.send(msg.getData());
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
	//	test();
	}

	public void out(Object...objects) {
		String s = Tools.objects2string(objects);
		if (s != null) {// 输出当服务端的界面上去显示
			if (s.length() > 600)
				s = Tools.tooLongCut(s); // 太长的数据 
			if (this.taShow.getText().length() >= 14000) {
				this.taShow.setText("");
			}
			this.taShow.append(this.client.getClass().getName() + " " + s + "\n");
			
			if(this.jcbscroll.isSelected())
				this.taShow.setCaretPosition(this.taShow.getText().length()); // 锁定最底滚动

//			Tools.out(s);
		}
	}

	public void onReceive(String readLine) {
		out("读取", readLine);
	}

}