package com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisImp;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisStatic;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisMapDao;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;


public class RedisMapDaoImpl extends RedisImp implements IRedisMapDao {
    private Logger logger = LoggerFactory.getLogger(RedisMapDaoImpl.class);

    @Override
    public boolean put(String mapName, String field, Object value) {
        Jedis jedis = null;
        boolean isBroken = false;
        long status = 0;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            status = jedis.hset(super.string2byte(mapName), super.string2byte(field), super.object2byte(value));
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        if (status == 0 || status == 1)
            return true;
        return false;
    }

    public boolean saveTostaticRedis(String mapName, String field, byte[] value) {
        Jedis jedis = null;
        boolean isBroken = false;
        long status = 0;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            status = jedis.hset(super.string2byte(mapName), super.string2byte(field), value);
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        if (status == 0 || status == 1)
            return true;
        return false;
    }

    public boolean saveTostaticRedisInBatch(String mapName, List<String> fields, List<byte[]> values) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            Pipeline pipelined = jedis.pipelined();
            for (int i = 0; i < fields.size(); i++) {
                pipelined.hset(super.string2byte(mapName), super.string2byte(fields.get(i)), values.get(i));
                if (i % 5000 == 0) {
                    pipelined.sync();
                }
            }
            pipelined.sync();
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }

        return true;
    }

	/*
     * public List<byte[]> getLastestLcFromStaticRedis(String mapName, String
	 * field) { Jedis jedis = null; boolean isBroken = false; try { jedis =
	 * RedisStatic.getInstance().getJedis(); List<byte[]> result =
	 * jedis.hmget(super.string2byte(mapName), super.string2byte(field)); return
	 * result; } catch (JedisException e) { isBroken = true;
	 * logger.error(e.getMessage(),e); return null; }finally{
	 * RedisStatic.getInstance().release(jedis,isBroken); } }
	 */

    public Map<byte[], byte[]> getFromStaticRedis(String mapName) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            Map<byte[], byte[]> result = jedis.hgetAll(super.string2byte(mapName));
            return result;
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
    }

    @Override
    public Object get(String mapName, String field) {
        Jedis jedis = null;
        boolean isBroken = false;
        byte[] result = null;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            result = jedis.hget(super.string2byte(mapName), super.string2byte(field));
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        if (result != null)
            return super.byte2object(result);
        return null;
    }

    @Override
    public List get(String mapName, String... fields) {
        List<Object> result = new ArrayList<Object>();
        Jedis jedis = null;
        boolean isBroken = false;
        List<byte[]> list = null;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            list = jedis.hmget(super.string2byte(mapName), super.string2byte(fields));
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        if (list != null) {
            for (byte[] bytes : list) {
                if (bytes != null) {
                    result.add(super.byte2object(bytes));
                } else {
                    // result.add(null);
                }

            }
        }
        return result;
    }

    @Override
    public Map<String, Object> get(String mapName) {
        Map<String, Object> result = new ConcurrentHashMap<String, Object>();
        Jedis jedis = null;
        boolean isBroken = false;
        Map<byte[], byte[]> map = null;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            map = jedis.hgetAll(super.string2byte(mapName));
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        if (map != null) {
            for (Entry<byte[], byte[]> e : map.entrySet()) {
                result.put(super.byte2string(e.getKey()), super.byte2object(e.getValue()));
            }
        }
        return result;
    }

    @Override
    public List allValues(String mapName) {
        List<Object> result = new ArrayList<Object>();
        Jedis jedis = null;
        boolean isBroken = false;
        List<byte[]> list = null;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            list = jedis.hvals(super.string2byte(mapName));
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        if (list != null) {
            for (byte[] bs : list) {
                result.add(super.byte2object(bs));
            }
        }
        return result;
    }

    @Override
    public List<String> allKeys(String mapName) {
        List<String> result = new ArrayList<String>();
        Jedis jedis = null;
        boolean isBroken = false;
        Set<byte[]> keys = null;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            keys = jedis.hkeys(super.string2byte(mapName));
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        if (keys != null) {
            for (byte[] bs : keys) {
                result.add(super.byte2string(bs));
            }
        }
        return result;
    }

    @Override
    public long del(String key, String... field) {
        Jedis jedis = null;
        boolean isBroken = false;
        long result = 0;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            List<byte[]> temp = new ArrayList<byte[]>();
            for (String string : field) {
                temp.add(this.string2byte(string));
            }
            byte[][] _byte_fields = new byte[temp.size()][];
            temp.toArray(_byte_fields);
            result = jedis.hdel(this.string2byte(key), _byte_fields);
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        return result;
    }

    public long del(String key) {
        Jedis jedis = null;
        boolean isBroken = false;
        long result = 0;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            result = jedis.del(this.string2byte(key));
            // result = jedis.hdel(this.string2byte(key));
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        return result;
    }

    @Override
    public boolean put(String mapName, String[] fields, Object[] values) {
        if (fields.length != values.length) {
            return false;
        }
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            jedis.hmset(super.string2byte(mapName), this.convertBatchPutsParameter(fields, values));
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        return true;
    }

    private Map<byte[], byte[]> convertBatchPutsParameter(String[] fields, Object[] values) {
        Map<byte[], byte[]> result = new ConcurrentHashMap<byte[], byte[]>();
        for (int i = 0, length = fields.length; i < length; i++) {
            result.put(super.string2byte(fields[i]), super.object2byte(values[i]));
        }
        return result;
    }

    @Override
    public byte[] hget(String mapName, String field) {
        Jedis jedis = null;
        boolean isBroken = false;
        byte[] result = null;
        try {
            jedis = RedisStatic.getInstance().getJedis();
            result = jedis.hget(super.string2byte(mapName), super.string2byte(field));
            return result;
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        return null;
    }

    public Map<Long, LCLocationData.LocationData> pipeline(String mapName, List<Long> tids) {
        Jedis jedis = null;
        boolean isBroken = false;
        Map<Long, LCLocationData.LocationData> map = new HashMap<Long, LCLocationData.LocationData>();
        try {
            jedis = RedisStatic.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();
            for (Long tid : tids) {
                pipeline.hget(super.string2byte(mapName), super.string2byte(String.valueOf(tid)));
            }
            List<Object> all = pipeline.syncAndReturnAll();
            for (int i = 0, size = all.size(); i < size; i++) {
                Object o = all.get(i);
                try {
                    if (o != null) {
                        map.put(tids.get(i), LCLocationData.LocationData.parseFrom((byte[]) o));
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }

            }
            return map;
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        return null;
    }

    public Map<Long, Object> pipelineObjects(String mapName, List<Long> tids) {
        Jedis jedis = null;
        boolean isBroken = false;
        Map<Long, Object> map = new HashMap<Long, Object>();
        try {
            jedis = RedisStatic.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();
            for (Long tid : tids) {
                pipeline.hget(super.string2byte(mapName), super.string2byte(String.valueOf(tid)));
            }
            List<Object> all = pipeline.syncAndReturnAll();
            for (int i = 0, size = all.size(); i < size; i++) {
                Object o = all.get(i);
                try {
                    if (o != null) {
                        map.put(tids.get(i), super.byte2object((byte[]) o));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return map;
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        return null;
    }

    public List<Object> pipelineGetObjects(String mapName, List<Long> tids,List<Long> areaIds) {
        Jedis jedis = null;
        boolean isBroken = false;
        List<Object> map = new ArrayList<>();
        try {
            jedis = RedisStatic.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();
            List<String> Keys=new ArrayList<>();
            for (int i=0,size=tids.size();i<size;i++){
                Keys.add(tids.get(i)+"_"+areaIds.get(i));
            }
            for (String tid : Keys) {
                pipeline.hget(super.string2byte(mapName), super.string2byte(String.valueOf(tid)));
            }
            List<Object> all = pipeline.syncAndReturnAll();
            for (int i = 0, size = all.size(); i < size; i++) {
                Object o = all.get(i);
                try {
                    if (o != null) {
                        map.add(super.byte2string ((byte[])o));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return map;
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
        return null;
    }
}
