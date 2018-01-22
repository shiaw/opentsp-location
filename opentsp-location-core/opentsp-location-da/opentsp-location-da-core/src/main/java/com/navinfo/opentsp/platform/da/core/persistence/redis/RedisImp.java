package com.navinfo.opentsp.platform.da.core.persistence.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class RedisImp implements IRedis {
	private static Logger logger = LoggerFactory.getLogger(RedisImp.class);
	@Override
	public byte[] string2byte(String string) {
		try {
			return string.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	public String byte2string(byte[] bytes) {
		try {
			return new String(bytes,"utf-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	@Override
	public byte[] object2byte(Object object) {
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

	@Override
	public Object byte2object(byte[] bytes) {
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

	@Override
	public long del(String... keys) {
		Jedis jedis = null;
		boolean isBroken = false;
		long result = 0;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			List<byte[]> temp = new ArrayList<byte[]>();
			for (String string : keys) {
				temp.add(this.string2byte(string));
			}
			byte[][] _byte_keys = new byte[temp.size()][];
			temp.toArray(_byte_keys);
			result = jedis.del(_byte_keys);
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return result;
	}

	@Override
	public byte[][] string2byte(String... strings) {
		if(strings!=null){
			byte [][] bt=new byte[strings.length][];
			for(int i=0;i<strings.length;i++){
				bt[i]=strings[i].getBytes();
			}
			return bt;
		}

		return null;
	}

	@Override
	public boolean setKeyExpire(String key, int seconds) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getKetTtl(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[][] object2byte(Object... objects) {
		byte[][] result = new byte[objects.length][];
		for(int i = 0 , length = objects.length ; i < length ; i++){
			result[i] = this.object2byte(objects[i]);
		}
		return result;
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
