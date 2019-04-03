package com.walker.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class FileUtil {

	/**
	 * 文件集合排序
	 * 
	 * @param tempList
	 *            源files
	 * @param order
	 *            排序参数time name
	 * @param orderRe
	 *            是否倒序
	 * @return
	 */
	public File[] orderBy(File[] tempList, String order, boolean orderRe) {
		if (order == null)
			order = "name";
		if (order.equals("no"))
			return tempList;
		if (order.equals("null"))
			return tempList;

		final String or = order;
		final boolean orRe = orderRe;

		List<File> sortfiles = Arrays.asList(tempList);
		Collections.sort(sortfiles, new Comparator<File>() {
			public int compare(File o1, File o2) {
				int res = 0;
				if (or.equals("time")) {
					String str1 = String.valueOf(o1.lastModified());
					String str2 = String.valueOf(o2.lastModified());
					res = str1.compareTo(str2);
				} else if (or.equals("name")) {
					res = o1.getName().compareTo(o2.getName());
				}

				if (orRe) {
					res = 0 - res;
				}

				return res;
			}
		});
		for (int i = 0; i < sortfiles.size(); i++) {
			tempList[i] = sortfiles.get(i);
		}

		return tempList;
	}

	public static boolean mkfile(String path) {
		if (path == null)
			return false;
		File file = new File(path);
		if (file.exists() || file.isFile()) {
			return true;
		} else {
			try {
				mkdir(file.getParent());
				out("新建文件" + path);
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	public static boolean mkdir(String dir) {
		if (dir == null)
			return false;
		File file = new File(dir);
		if (file.exists()) {
			return true;
		}
		out("新建文件夹" + dir);
		return file.mkdirs();
	}

	/**
	 * 同步 树形展示目录及其文件 获取文件夹下所有的文件
	 * 
	 * @param dir
	 * @param funFileOrDir
	 *            遍历时处理回调可为空
	 * @return 文件list 及目录都有
	 */
	public static List<File> getAllFilesAsync(String dir) {
		return showDirAsync(dir, null);
	}

	/**
	 * 获取或操作 所有文件夹和文件 同步 dir可 ,分割多个dir
	 * 
	 * @param dir
	 * @param funFileOrDir
	 */
	public static List<File> showDirAsync(String dire, final Fun<File> funFileOrDir) {
		final List<File> files = new ArrayList<File>();
		String[] dirs = dire.split(",");
		for (final String dir : dirs) {
			longErgodic(new File(dir), files, funFileOrDir);// 把遍历得到的东西存放在files里面
		}
		return files;
	}

	/**
	 * 获取或操作 所有文件夹和文件 线程池异步 dir可 ,分割多个dir
	 * 
	 * @param dir
	 * @param funFileOrDir
	 */
	public static List<File> showDir(String dire, final Fun<File> funFileOrDir) {
		final List<File> files = new ArrayList<File>();
		String[] dirs = dire.split(",");
		for (final String dir : dirs) {
			ThreadUtil.execute(new Runnable() {
				public void run() {
					longErgodic(new File(dir), files, funFileOrDir);// 把遍历得到的东西存放在files里面
				}
			});
		}
		return files;
	}

	/**
	 * 递归遍历目录
	 * 
	 * @param file
	 *            当前处理文件
	 * @param files
	 *            结果集
	 * @param funFileOrDir
	 *            回调函数处理
	 */
	private static void longErgodic(File file, final List<File> files, final Fun<File> funFileOrDir) {
		if (file == null || !file.exists())
			return;
		files.add(file); // 添加当前 节点
		if (file.isDirectory()) { // 若是文件夹 则递归子节点 深度优先
			File[] fillArr = file.listFiles();
			if (fillArr == null)
				return;
			for (File file2 : fillArr) {
				longErgodic(file2, files, funFileOrDir);// 把遍历得到的东西存放在files里面
			}
		}
		if (funFileOrDir != null) { // 处理当前节点
			funFileOrDir.make(file);
		}
	}

	/**
	 * 以字节为单位读取文件，通常用于读取二进制文件，如图片
	 * 
	 * @param path
	 * @return
	 */
	public static String readByBytes(String path, Fun<String> fun) {
		String content = null;

		try {
			InputStream inputStream = new FileInputStream(path);
			StringBuffer sb = new StringBuffer();
			int c = 0;
			byte[] bytes = new byte[1024];
			/*
			 * InputStream.read(byte[] b)
			 * 
			 * Reads some number of bytes from the input stream and stores them
			 * into the buffer array b. 从输入流中读取一些字节存入缓冲数组b中 The number of bytes
			 * actually read is returned as an integer. 返回实际读到的字节数 This method
			 * blocks until input data is available, end of file is detected, or
			 * an exception is thrown. 该方法会一直阻塞，直到输入数据可以得到、或检测到文件结束、或抛出异常 --
			 * 意思是得到数据就返回
			 */
			String temp = "";

			if (fun != null)
				while ((c = inputStream.read(bytes)) != -1) {
					temp = new String(bytes, 0, c, "utf-8");
//					sb.append(temp);
					fun.make(temp);
				}
			else
				while ((c = inputStream.read(bytes)) != -1) {
					sb.append(new String(bytes, 0, c, "utf-8"));
				}

			content = sb.toString();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	/**
	 * 以行为单位读取文件，常用于读取面向行的格式化文件
	 * 
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	public static int readByLines(File file, Fun<String> fun) throws IOException {
		int lines = 0;
		LineIterator it = FileUtils.lineIterator(file, "utf-8");
		while(it.hasNext()) {
			String line = it.nextLine();
			lines++;
			fun.make(line);
		}
		it.close();
		return lines;
	}
	public static String readByLines(String path, Fun<String> fun) {
		String content = null;

		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), "utf-8"));

			StringBuffer sb = new StringBuffer();
			String temp = null;
			if (fun != null)
				while ((temp = bufferedReader.readLine()) != null) {
//					sb.append(temp);  //回调则返回null
					fun.make(temp);
				}
			else
				while ((temp = bufferedReader.readLine()) != null) {
					sb.append(temp).append("\r\n");
				}
			content = sb.toString();
			bufferedReader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	/**
	 * 以字符为单位读取文件，常用于读取文本文件
	 * 
	 * @param path
	 * @return
	 */
	public static String readByChars(String path, Fun<char[]> fun) {
		String content = null;

		try {

			Reader reader = new InputStreamReader(new FileInputStream(path), "utf-8");
			StringBuffer sb = new StringBuffer();

			char[] tempchars = new char[1024];
			if (fun == null)
				while (reader.read(tempchars) != -1) {
					sb.append(tempchars);
				}
			else
				while (reader.read(tempchars) != -1) {
					sb.append(tempchars);
					fun.make(tempchars);
				}

			content = sb.toString();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 把内容content写的path文件中
	 * 
	 * @param content
	 * @param path
	 * @return
	 */
	public static boolean saveAs(String content, String path) {

		FileWriter fw = null;

		// out("把内容：" + content + "， 写入文件：" + path);

		try {
			/**
			 * Constructs a FileWriter object given a File object. If the second
			 * argument is true, then bytes will be written to the end of the
			 * file rather than the beginning. 根据给定的File对象构造一个FileWriter对象。
			 * 如果append参数为true, 则字节将被写入到文件的末尾（向文件中追加内容）
			 *
			 * Parameters: file, a File object to write to 带写入的文件对象 append, if
			 * true, then bytes will be written to the end of the file rather
			 * than the beginning Throws: IOException - if the file exists but
			 * is a directory rather than a regular file, does not exist but
			 * cannot be created, or cannot be opened for any other reason
			 * 报异常的3种情况： file对象是一个存在的目录（不是一个常规文件） file对象是一个不存在的常规文件，但不能被创建
			 * file对象是一个存在的常规文件，但不能被打开
			 *
			 */
			fw = new FileWriter(new File(path), false);
			if (content != null) {
				fw.write(content);
				fw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	/**
	 * 
	 */
	public static void writeFile() throws Exception {
		//字符流写入字符到文件 char[] string
		FileWriter fw = new FileWriter(new File("aaa"), false);
		fw.write("aaaaaaaaa");
		fw.flush();
		
		//字符流类来处理字符数据 char[] string
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("bbbbb");
		bw.write(new char[]{'a','b'});
		bw.close();
		
		//原始二进制数据的字节流类 byte
		FileOutputStream op = new FileOutputStream(new File("bbb"));
		op.write("asdf".getBytes());
		op.close();
		 try (FileOutputStream fop = new FileOutputStream(new File("ccc"))) {
			 
		 }catch(Exception e) {
			 
		 }
	}
	
	/**
	 * 按照文件类型读取 path .xls .xlsx 表格 .c .txt .python 文本类型
	 */
	public static Object readByType(String path, Fun<String> fun, Fun<ArrayList<ArrayList<Object>>> excel) {
		if (check(path) != 0) {
			return "false";
		}
		String exts[] = new String[] { "txt", "c", "cpp", "html", "jsp", "java", "class", "py", "bat", "sh" };
		Arrays.sort(exts);
		String ext = getFileType(path);
		if (Arrays.binarySearch(exts, ext, String.CASE_INSENSITIVE_ORDER) >= 0) {
			return readByLines(path, fun);
		} else if (ext.equals("xls") || ext.equals("xlsx")) {
			ArrayList<ArrayList<Object>> res = ExcelUtil.readExcel(new File(path));
			if (excel != null)
				excel.make(res);
			return res;
		} else {
			return "File:" + path + " 不能识别";
		}
	}

	/**
	 * 全路径 明确路径 文件夹复制或者移动 使用org.apache.commons.io.FileUtils实现
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	private static void cpDir(String oldPath, String newPath, boolean ifMove) {
		if (oldPath.equals(newPath))
			return;
		out((ifMove ? "移动" : "复制") + "文件夹" + oldPath + "->" + newPath);
		try {
			if (ifMove) {
				FileUtils.moveDirectory(new File(oldPath), new File(newPath));
			} else {
				FileUtils.copyDirectory(new File(oldPath), new File(newPath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 定点文件复制或者移动 使用org.apache.commons.io.FileUtils实现
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	private static void cpFile(String oldPath, String newPath, boolean ifMove) {
		if (oldPath.equals(newPath))
			return;
		out((ifMove ? "移动" : "复制") + "文件" + oldPath + "->" + newPath);
		try {
			if (ifMove) {
				FileUtils.moveFile(new File(oldPath), new File(newPath));
			} else {
				FileUtils.copyFile(new File(oldPath), new File(newPath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 只支持同路径下的重命名? File.rename
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	private static boolean rename(String oldPath, String newPath) {
		if (oldPath.equals(newPath)) {
			out("源路径文件" + oldPath + "和目标文件路径相同");
			return false;
		}

		if (!new File(oldPath).exists()) {
			out("文件" + oldPath + " 不存在");
			return false;
		}
		boolean res = new File(oldPath).renameTo(new File(newPath));
		out("移动文件?" + oldPath + "->" + newPath + " 结果=" + res);
		return res;
	}

	/**
	 * 移动或复制文件 适配 类似linux shell命令
	 * 
	 * @param oldPath
	 * @param newPath
	 * @param ifMove
	 */
	private static void cpIfMove(String oldPath, String newPath, boolean ifMove) {

		try {
			int fromType = check(oldPath); // 0 文件 1文件夹 -1不存在
			if (fromType == -1) {
				out("文件操作失败", oldPath, newPath);
			} else if (fromType == 0) {// 文件 cp file1.txt dir1/ cp file1.txt
										// file1
				if (newPath.endsWith(File.separator)) { // 原名字 操作到 新目录下面
					newPath = newPath + File.separator + getFileName(oldPath);
				}
				cpFile(oldPath, newPath, ifMove);
			} else {// 文件夹 操作
				if (newPath.endsWith(File.separator)) { // 原名字 操作到 新目录下面
					newPath = newPath + File.separator + getFileName(oldPath);
				}
				cpDir(oldPath, newPath, ifMove);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * 复制 文件夹 或 文件 cp c:/dir1 c:/dir2 cp c:/file1 c:/dir2/ cp c:/file1 c:/file2
	 * 
	 * @param oldPath
	 *            原文件路径 如：c:/fqf
	 * @param newPathth
	 *            复制后路径 如：f:/fqf/ff
	 */
	public static void cp(String oldPath, String newPath) {
		cpIfMove(oldPath, newPath, false);
	}

	/**
	 * 移动文件 或文件夹
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static void mv(String oldPath, String newPath) {
		cpIfMove(oldPath, newPath, true);
	}

	/**
	 * 扫描文件夹下ext类型的文件信息，获取路径，名字，大小
	 * 
	 * @param dir
	 * @param ext
	 * @return
	 */
	public static List<Map<String, Object>> getDirFiles(String dir, String ext) {
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();

		// 扫描文件夹 读取数据
		out("扫描文件目录" + dir);
		File rootfile = null;
		try {
			rootfile = new File(dir);
		} catch (Exception e) {
			out("打开文件目录[" + dir + "] error ");
		}
		File coders[] = rootfile.listFiles(); // 用户各自文件夹名集合
		if (coders == null) {
			out("文件夹为null");
		} else {
			for (File coder : coders) {
				if (coder.isFile()) {
					// 后缀判定xxx.apk, about.txt, xxx.img
					String fileName = coder.getName();
					if (getFileType(fileName).equals(ext)) {
						res.add(fileToMap(coder));
					}
				}

			}
		}

		return res;
	}

	/**
	 * 获取某路径下所有文件 文件夹 ll
	 */
	public static List<Map> ls(String dir) {
		List<Map> res = new ArrayList<Map>();

		out("扫描文件目录:" + dir);
		File rootfile = new File(dir);
		File coders[] = rootfile.listFiles();
		int countFile = 0;
		for (File coder : coders) {
			if (coder.isDirectory()) {
				res.add(fileToMap(coder));
			}
		}
		for (File coder : coders) {
			if (coder.isFile()) {
				countFile++;
				res.add(fileToMap(coder));
			}
		}
		out("文件夹数量:" + (coders.length - countFile) + " \t文件数量:" + countFile);
		return res;
	}

	/**
	 * 获取某文件详情
	 */
	public static Map getFileMap(String file) {
		if (file != null && file.length() > 0) {
			File rootfile = new File(file);
			if (rootfile.exists()) {
				return fileToMap(rootfile);
			}
		}
		return new HashMap<>();
	}

	/**
	 * 获取文件 map样式键集合
	 */
	public static List<?> getFileMap() {
		List<String> res = new ArrayList<String>();
		res.add("PATH");
		res.add("NAME");
		res.add("SIZE");
		res.add("LENGTH");
		res.add("TIME");
		res.add("TYPE");
		res.add("CHILDS");
		return res;
	}

	public static Bean fileToMap(File coder) {
		Bean map = new Bean();
		map.put("PATH", coder.getAbsolutePath());
		map.put("NAME", coder.getName());
		map.put("SIZE", calcSize(coder.length()));
		map.put("LENGTH", coder.length());
		map.put("TIME", coder.lastModified());

		String type = "";
		int dirfiles = 0;
		if (coder.isFile()) {
			type = getFileType(coder.getAbsolutePath());
		} else if (coder.isDirectory()) {
			type = "dir";
			File[] f = coder.listFiles();
			if (f != null)
				dirfiles = f.length;
		}
		map.put("TYPE", type);
		map.put("CHILDS", dirfiles);

		return map;
	}

	// 通过字符串长度，计算大概的 流量大小 MB KB B char=B
	static String calcSize(long length) {
		long m = length / (1024 * 1024);
		long k = length % (1024 * 1024) / 1024;
		long b = length % (1024 * 1024) % 1024;
		return m > 0 ? m + "." + k / 100 + "MB" : k > 0 ? k + "." + b / 100 + "KB" : b + "B";
	}

	static String calcSize(int length) {
		int m = length / (1024 * 1024);
		int k = length % (1024 * 1024) / 1024;
		int b = length % (1024 * 1024) % 1024;
		return m > 0 ? m + "." + k / 100 + "MB" : k > 0 ? k + "." + b / 100 + "KB" : b + "B";
	}

	/**
	 * 检查文件类型
	 * 
	 * @param path
	 * @return 0 文件 1文件夹 -1不存在
	 */
	public static int check(String path) {
		int res = -1;
		File file = new File(path);
		if (file.exists()) {
			if (file.isFile()) {
				res = 0;
			} else if (file.isDirectory()) {
				res = 1;
			} else {
				out("文件：" + path + "不是文件夹也不是文件");
			}
		} else {
			out("文件：" + path + "不存在");
		}
		return res;
	}

	// 删除附件，Eg: Constant.fileupload目录,xxx-xxx.doc 则删除该目录下所有xxx-xxx.doc/exe/dll
	public static boolean delete(String dir, String filename) {
		String name = getFileName(filename);
		File file = new File(dir);
		if (file.exists()) {
			File ff[] = file.listFiles();
			for (File f : ff) {
				if (f.isFile()) {
					if (getFileName(f.getName()).equals(name)) {
						f.delete();
						return true;
					}
				}
			}

		}
		return false;
	}

	/**
	 * 递归删除 文件 或者 文件夹 所有
	 */
	public static boolean delete(String path) {
		FileUtil.showDirAsync(path, new Fun<File>() {
			@SuppressWarnings("unchecked")
			@Override
			public Object make(File file) {
				file.delete();
				return file;
			}
		});

		return false;
	}

	/**
	 * /sdcard/mycc/record/100-101020120120120.amr return amr
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileType(String path) {
		String res = "null";
		if (path == null) {
		} else {
			int ii = path.lastIndexOf(".");
			// out(""+ii);
			if (ii >= 0) {
				return path.substring(ii + 1);
			}
		}
		return res;
	}

	/**
	 * /sdcard/mycc/record/100-101020120120120.amr return asdfa
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileNameOnly(String path) {
		String res = "null";

		if (path == null) {
		} else {
			int ii = path.lastIndexOf(".");
			// out(""+ii);
			if (ii >= 0) {
				res = path.substring(0, ii); // F:/s/d/1000
				ii = path.lastIndexOf("\\");
				// out(""+ii);
				if (ii >= 0) {
					res = res.substring(ii + 1);
				}
			}
		}
		return res;
	}

	/**
	 * /sdcard/mycc/record/100-101020120120120.amr return asdfa.amr
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		String res = "null";

		if (path == null) {
		} else {
			int ii = path.lastIndexOf(File.separator);
			// out(""+ii);
			if (ii >= 0) {
				res = path.substring(ii + 1);
			}
		}
		return res;
	}

	/**
	 * /sdcard/mycc/record/100-101020120120120.amr return /sdcard/mycc/record/
	 * 
	 * @param path
	 * @return
	 */
	public static String getFilePath(String path) {
		String res = "";

		if (path == null) {
		} else {
			int ii = path.lastIndexOf(File.separator);
			// out(""+ii);
			if (ii >= 0) {
				res = path.substring(0, ii);
			}
		}
		return res;

	}

	public static void out(Object... objects) {
		Tools.out("FileUtil", objects);
	}

}