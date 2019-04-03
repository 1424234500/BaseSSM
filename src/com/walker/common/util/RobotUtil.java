package com.walker.common.util;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


/**
 * win32工具窗口 鼠标
 * 
 * @author Walker
 *
 */
public class RobotUtil {
	public static volatile Robot robot = null;
	
	private RobotUtil() { }

	private static class SingletonFactory{           
		  private static Robot instance;
		  static {
			 try {
				instance = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
		  }
	}
	public static Robot getInstance(){           
	    return SingletonFactory.instance;           
	}  

	/**
	 * 按键按下
	 */
	public static void keyPress(int keyCode) {
		getInstance().keyPress(keyCode);
	}
	/**
	 * 按键松开
	 */
	public static void keyRelease(int keyCode) {
		getInstance().keyRelease(keyCode);
	}
	/**
	 * 获取鼠标点
	 */
	public static Point getMouse() {
		return MouseInfo.getPointerInfo().getLocation();
	}
	/**
	 * 设置鼠标点
	 */
	public static void setMouse(int x, int y) {
		getInstance().mouseMove(x, y);
	}
	/**
	 * 移动鼠标点
	 */
	public static void moveMouse(int dx, int dy) {
		Point p = getMouse();
		getInstance().mouseMove((int)p.getX() + dx, (int)p.getY() + dy);
	}
	/**
	 * 获取鼠标点颜色
	 */
	public static Color getColor() {
		Point p = getMouse();
		return getColor(p.x, p.y);
	}
	/**
	 * 获取指定点颜色
	 */
	public static Color getColor(int x, int y) {
		return getInstance().getPixelColor(x, y);
	}

	/**
	 * 从剪切板获得文字。
	 */
	public static String getSysClipboardText() {
		String ret = "";
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 获取剪切板中的内容
		Transferable clipTf = sysClip.getContents(null);

		if (clipTf != null) {
			// 检查内容是否是文本类型
			if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return ret;
	}

	/**
	 * 将字符串复制到剪切板。
	 */
	public static void setSysClipboardText(String str) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(str);
		clip.setContents(tText, null);
	}

	/**
	 * 从剪切板获得图片。
	 */
	public static Image getImageFromClipboard() throws Exception {
		Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable cc = sysc.getContents(null);
		if (cc == null)
			return null;
		else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor))
			return (Image) cc.getTransferData(DataFlavor.imageFlavor);
		return null;
	}

	/**
	 * 复制图片到剪切板。
	 */
	public static void setClipboardImage(final Image image) {
		Transferable trans = new Transferable() {
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor))
					return image;
				throw new UnsupportedFlavorException(flavor);
			}

		};
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
	}

	public static String getRuntime() {
		Runtime runtime = Runtime.getRuntime();
		String res = "Runtime: \n";
		res += " maxMemory: " + Tools.calcSize(runtime.maxMemory()) + " \n";
		res += " freeMemory: " + Tools.calcSize(runtime.freeMemory()) + " \n";
		res += " totalMemory: " + Tools.calcSize(runtime.totalMemory()) + " \n";
		
		return res;
	}

}