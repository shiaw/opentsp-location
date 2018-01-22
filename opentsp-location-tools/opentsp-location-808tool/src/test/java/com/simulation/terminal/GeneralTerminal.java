package com.simulation.terminal;

import java.io.FileWriter;
import java.math.BigInteger;

/**
 * Created by xubh on 2017/3/31.
 */
public class GeneralTerminal {

    public static void main(String[] args) {
        try {
            FileWriter fw = new FileWriter("e:\\terminal.txt");
            BigInteger a=new BigInteger("20000000001");
            int tiidStart = 383551;
            //INSERT INTO `LC_TERMINAL_INFO` VALUES ('383550', '14807394928', '200110', '101001', '132700', '1490239793', '1', '1', '2304928', '0', '0');
            for (int i=0 ; i < 200000;i++) {
                fw.write("INSERT INTO LC_TERMINAL_INFO VALUES ("+ tiidStart+",'"+String.valueOf(a.add(BigInteger.valueOf(i)))+"', '200110', '101001', '132700', '1490239793', '1', '1', '2304928', '0', '0');");
                tiidStart++;
                fw.write("\r\n");
            }
            fw.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
