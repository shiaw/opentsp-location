package com.navinfo.opentsp.platform.dsa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/*
 * 配置文件实时更新
 */
public class ConfigUtils {

    protected static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    private static final String file = "/app.properties";

    public static long DEFAULT_CHECK_INTERNAL = 10000; // 默认10秒钟
    protected static String pFilePath = null;
    protected static long lastModified = 0;
    private static Properties pros = null;

    static {
        String internal = System.getProperty("lilian.internal", "10000");
        try {
            DEFAULT_CHECK_INTERNAL = Integer.parseInt(internal);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() throws FileNotFoundException {
//        File pFile = new File(ConfigUtils.class.getClassLoader().getResource(file).getPath());
        File pFile = new File(file);

        pFilePath = pFile.getAbsolutePath();
        lastModified = pFile.lastModified();

//        InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(file);
        InputStream inputStream = new FileInputStream(file);


        try {
            pros = new Properties();
            pros.load(inputStream);
            logger.info("读取配置文件成功:{}", file);
            FileChecker fileCheck = new FileChecker();
            Thread thread = new Thread(fileCheck, "lilian-file-check");
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        } catch (Exception e1) {
            logger.error("读取配置文件失败:{}\n{}", file, e1);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 新增实时获取配置参数属性接口
    public static String getProByRealTime(String param, String defaultValue) {
        return pros.getProperty(param, defaultValue);
    }

    // 同步锁写文件，避免并发写导致失败
    public synchronized static void writePros(String key, String value) throws FileNotFoundException {
        updatePros();
//        File pFile = new File(ConfigUtils.class.getClassLoader().getResource(file).getPath());
        File pFile = new File(file);
        pros.setProperty(key, value);
        OutputStream output = null;
        try {
            output = new FileOutputStream(pFile);
            pros.store(output, "设置" + key + "的值");
            logger.info("实时更新配置文件成功:{}-{}", key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static synchronized void updatePros() throws FileNotFoundException {
//        InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(file);
        InputStream inputStream = new FileInputStream(file);
        try {
            if (pros == null) {
                pros = new Properties();
            }
            pros.load(inputStream);
            // logger.info("实时读取配置文件成功:{}", file);
        } catch (Exception e1) {
            logger.error("实时读取配置文件失败:{}\n{}", file, e1);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

class FileChecker implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(ConfigUtils.DEFAULT_CHECK_INTERNAL);
                File pFile = new File(ConfigUtils.pFilePath);
                if (pFile.lastModified() != ConfigUtils.lastModified) {
                    ConfigUtils.updatePros();
                    ConfigUtils.lastModified = pFile.lastModified();
                }
            } catch (InterruptedException e) {
                ConfigUtils.logger.error("配置文件工具类出错:");
                e.printStackTrace();
            } catch (Throwable e) {
                ConfigUtils.logger.error("配置文件工具类出错:");
                e.printStackTrace();
            }
        }

    }
}
