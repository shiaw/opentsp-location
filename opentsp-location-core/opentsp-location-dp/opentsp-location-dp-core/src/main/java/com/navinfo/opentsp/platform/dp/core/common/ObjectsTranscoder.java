package com.navinfo.opentsp.platform.dp.core.common;

import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 2016/9/27.
 */
public class ObjectsTranscoder
{
    private static Logger log = Logger.getLogger(ObjectsTranscoder.class);
    
    /**
     * 编码
     * 
     * @param value
     * @return
     */
    public static byte[] serialize(RuleEntity value)
    {
        if (value == null)
        {
            throw new NullPointerException("Can't serialize null");
        }
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try
        {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            // for (RuleEntity ruleEntity : value)
            // {
            // os.writeObject(ruleEntity);
            // }
            os.writeObject(value);
            os.writeObject(null);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Non-serializable object", e);
        }
        finally
        {
            close(os);
            close(bos);
        }
        return rv;
    }
    
    public static void close(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (Exception e)
            {
                log.error(e.getMessage());
            }
        }
    }
    
    /**
     * 解码list
     * 
     * @param inList
     * @return
     */
    public static List<RuleEntity> deserializeList(List<byte[]> inList)
    {
        List<RuleEntity> list = new ArrayList<RuleEntity>();
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try
        {
            for (byte[] in : inList)
            // if (in != null)
            {
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                while (true)
                {
                    RuleEntity ruleEntity = (RuleEntity)is.readObject();
                    if (ruleEntity == null)
                    {
                        break;
                    }
                    else
                    {
                        list.add(ruleEntity);
                    }
                }
                is.close();
                bis.close();
            }
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
            
        }
        catch (ClassNotFoundException e)
        {
            log.error(e.getMessage());
        }
        finally
        {
            close(is);
            close(bis);
        }
        return list;
    }
    
    /**
     * 解码Entity
     * 
     * @param in
     * @return
     */
    public static RuleEntity deserialize(byte[] in)
    {
        RuleEntity entity = new RuleEntity();
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try
        {
            if (in != null)
            {
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                while (true)
                {
                    RuleEntity ruleEntity = (RuleEntity)is.readObject();
                    if (ruleEntity == null)
                    {
                        break;
                    }
                    else
                    {
                        entity = ruleEntity;
                    }
                }
                is.close();
                bis.close();
            }
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
            
        }
        catch (ClassNotFoundException e)
        {
            log.error(e.getMessage());
        }
        finally
        {
            close(is);
            close(bis);
        }
        return entity;
    }
}
