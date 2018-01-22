package com.navinfo.opentsp.platform.location.protocol;

import java.io.*;
import java.util.List;

abstract class Generate {
	protected static String root = System.getProperty("user.dir")
			+ "\\opentsp-location-core\\opentsp-location-protocol\\protocol";

	protected static StringBuffer buffer = new StringBuffer();

	public static StringBuffer appending(String string) {
		buffer.append(string);
		buffer.append("\n");
		return buffer;
	}

	static long interval = 1 * 60 * 30;

	public static void getProtoFiles(File directory, List<File> result) {
		File[] fileList = directory.listFiles();
		long currentTime = System.currentTimeMillis() / 1000;

		for (File f : fileList) {
			if (f.isDirectory()) {
				getProtoFiles(f, result);
			} else {
				if (f.getPath().endsWith(".proto")) {
					long lastTime = f.lastModified() / 1000;
//					if (Math.abs(currentTime - lastTime) > interval) {
//						continue;
//					}
					result.add(f);
				}
			}
		}
	}

	public String write(String filename) {
		File file = new File(root + File.separator + filename + ".bat");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(buffer.toString());
			output.close();
			return file.getAbsolutePath();
		} catch (IOException e) {
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
				}
		}
		return "";
	}

	public void runBat(String batPath) {
		Process p;
		try {
			p = Runtime.getRuntime().exec(batPath);
			InputStream fis = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static final String outfile = System.getProperty("user.dir")
			+ "\\opentsp-location-core\\opentsp-location-protocol\\output";

	public static void getLatestJavas(File directory, List<File> result) {
		File[] fileList = directory.listFiles();
		long currentTime = System.currentTimeMillis() / 1000;
		for (File f : fileList) {
			if (f.isDirectory()) {
				getLatestJavas(f, result);
			} else {
				if (f.getPath().endsWith(".java")) {
					long lastTime = f.lastModified() / 1000;
//					if (Math.abs(currentTime - lastTime) > interval) {
//						continue;
//					}
					result.add(f);
				}
			}
		}
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}

	}

	public static void forJava(File f1, File f2) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(f1);
			out = new FileOutputStream(f2);
			byte[] buffer = new byte[(int) f1.getTotalSpace()];
			while (true) {
				int ins = in.read(buffer);
				if (ins != -1) {
					out.write(buffer, 0, ins);
				} else {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
