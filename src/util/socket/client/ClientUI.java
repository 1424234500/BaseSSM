package util.socket.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.ThreadUtil;
import util.TimeUtil;
import util.Tools;
import util.socket.server_0.Msg;

public class ClientUI extends JFrame  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AtomicLong count = new AtomicLong(0L);
	ScheduledFuture<?> future;
	
	public JButton jbshowusers = new JButton("好友");
	public JButton jbshowrooms = new JButton("会话");
	public JButton jbtest = new JButton("auto on");
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

		jtfSend = new JTextField(30);
		jtfSend1 = new JTextField(6);
		jtfSend2 = new JTextField(6);
		jtfSend1.setText("server_1");
		jtfSend2.setText(Tools.getRandomNum(10, 99, 2));
//		jtfSend.setText("{\"cmd\":12,\"value0\":\"group\",\"value1\":\"100000\",\"value2\":\"text\",\"value3\":\"2017-05-24 00:03:31\",\"value4\":\"消息 "+ Tools.getNowTimeL() +"\"}");
		jtfSend.setText("{type:message,to:\"all_socket\",from:222,data:{type:txt,body:hello} }");

		taShow = new JTextArea();


		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btStart.getText().equals("启动")) {
					btStart.setText("关闭");
					client.start();
				} else {
					btStart.setText("启动");
					client.stop();
				}
			}

			
		});
		btSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mmsg = ("" + jtfSend.getText());// 写入发送流到 客户端去
				String obj = mmsg;
				client.send(obj);
			}
		});
		btLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String key = jtfSend2.getText();	//发往用户连接
				client.send("{type:login,data:{user:" + key + ",pwd:123456} }");
			}
		});
		jbshowrooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out(client.show());
				client.send("{type:monitor,data:{type:show} }");
			}
		});
		jbtest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(jbtest.getText().equals("auto on")) {
					jbtest.setText("auto off");
					count.set(0L);
					future = ThreadUtil.scheduleAtFixedRate(new Runnable() {
						public void run() {
							count.addAndGet(1L);
							client.send("{data:{type:txt,body:" + count.get() + "hello" + TimeUtil.getTimeSequence() + "},type:message,to:\"all_socket\"}");				
						}
					}, 1000, 100, TimeUnit.MILLISECONDS);
				}else {
					jbtest.setText("auto on");
					future.cancel(true);
				}
			}

			
		});
		jbshowusers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out(client.show());
			}
		});
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				 client.stop();
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
		this.taShow.setEditable(true);
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