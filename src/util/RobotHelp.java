package util;

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
 * @author Walker
 *
 */
public class RobotHelp {
	private static volatile RobotHelp robotHelp = null;
	public  Robot robot = null;
    private RobotHelp(){
    	try { 
    		robot = new Robot(); 
    	} 
    	catch (AWTException e) {
    		e.printStackTrace(); 
		} 
    }
    public static RobotHelp getSingleton(){
        if(robotHelp == null){
            synchronized (RobotHelp.class){
                if(robotHelp == null){
                	robotHelp = new RobotHelp();
                }
            }
        }
        return robotHelp;
    }   
	
    public static Point getMousePoint(){
    	return MouseInfo.getPointerInfo().getLocation();
    }
    
    
    public static Color getColor(){
    	return getColor(getMousePoint()); 
    } 
    public static Color getColor(Point point){
    	return getColor(point.x, point.y); 
    } 
    public static Color getColor(int x, int y){
    	return getSingleton().robot.getPixelColor(x, y); 
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
					ret = (String) clipTf
							.getTransferData(DataFlavor.stringFlavor);
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
	public static void setSysClipboardText(String writeMe) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(writeMe);
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

			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor))
					return image;
				throw new UnsupportedFlavorException(flavor);
			}

		};
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans,
				null);
	}

    
}