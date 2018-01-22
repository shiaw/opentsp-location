package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.lang;

import java.io.*;

public class ObjectUtils {

	/**
	 * 序列化对象到本地文件
	 * 
	 * @param file
	 *            String 数据文件目录+数据文件名称
	 * @param object
	 *            Object 需要序列化的数据
	 * @throws IOException
	 */
	public final static void writeObject(String file, Object object)
			throws IOException {
		ObjectOutputStream outputStream = null;
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.writeObject(object);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从本地数据文件加载数据到内存
	 * 
	 * @param file
	 *            String 数据文件目录+数据文件名称
	 * @return Object 数据
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public final static Object readObject(String file)
			throws ClassNotFoundException, IOException {
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(file));
			return inputStream.readObject();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将一个对象转换成字节数组
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] objectToBytes(Object object) {
		ByteArrayOutputStream baos = null; // 构造一个字节输出
		ObjectOutputStream oos = null; // 构造一个类输出流
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(baos, oos);
		}
		return null;
	}
	public static void main(String[] args) {
		long b = System.currentTimeMillis();
		for(int i = 0 ; i < 10000 * 1000 ; i ++){
			objectToBytes("Addfasdfasdfa4157sf2157sf12asd5fasd4fsd1f2");
		}
		System.err.println((System.currentTimeMillis())-b);
	}

	/**
	 * 字节数组转换为对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object bytesToObject(byte[] bytes) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			close(bais, ois);
		}
		return null;
	}

	private static void close(ByteArrayInputStream bais, ObjectInputStream ois) {
		if (bais != null) {
			try {
				bais.close();
			} catch (IOException e) {
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static void close(ByteArrayOutputStream baos, ObjectOutputStream oos) {
		if (baos != null) {
			try {
				baos.close();
			} catch (IOException e) {
			} finally {
				if (oos != null) {
					try {
						oos.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
