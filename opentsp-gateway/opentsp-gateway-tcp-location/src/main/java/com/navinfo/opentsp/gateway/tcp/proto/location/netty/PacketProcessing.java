package com.navinfo.opentsp.gateway.tcp.proto.location.netty;

import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class PacketProcessing {

    public static long getCacheBlockId(String uniqueMark, int blockId) {
        return Long.parseLong(uniqueMark + blockId);
    }

    private static int _block_id = 0;

    public static synchronized int getBlockId() {
        _block_id = _block_id + 1;
        if (_block_id > 0xffff) {
            _block_id = 1;
        }
        return _block_id;
    }

    /**
     * 数据分包 <br>
     * 如果最小包长为0,则不做验证
     *
     * @param bytes         {@link Byte}[] 源数据
     * @param pkBegin       {@link Byte} 包头
     * @param pkEnd         {@link Byte} 包尾
     * @param leastPkLength {@link Integer} 最小包长
     * @return {@link List}<{@link Byte}[]>
     */
    public static List<byte[]> subpackage(byte[] bytes, byte pkBegin,
                                          byte pkEnd, int leastPkLength) {
        List<byte[]> result = new ArrayList<byte[]>();
        int index = -1;
        for (int i = 0, length = bytes.length; i < length; i++) {
            if (index == -1) {// 寻找包头
                if (pkBegin == bytes[i]) {
                    index = i;
                }
            } else {
                if (pkEnd == bytes[i]) {// 寻找包尾
                    if (leastPkLength > 0) {// 是否做最小包长验证
                        if (i - index + 1 < leastPkLength) {
                            index = i;
                        } else {
                            result.add(ArraysUtils.subarrays(bytes, index, i
                                    - index + 1));
                            index = -1;
                        }
                    } else {
                        result.add(ArraysUtils.subarrays(bytes, index, i
                                - index + 1));
                        index = -1;
                    }
                }
            }
        }
        if (index != -1) {
            result.add(ArraysUtils
                    .subarrays(bytes, index, bytes.length - index));
        }
        return result;
    }

    /**
     * 数据分割
     *
     * @param content {@link Byte} 需要分割数据
     * @param maxPart {@link Integer} 最大数据块
     * @return
     */
    public static List<byte[]> dataSegmentation(byte[] content, int maxPart) {
        int length = content.length;// 本次下发数据体总长度
        int count = length / maxPart + 1;
        List<byte[]> result = new ArrayList<byte[]>(count);
        for (int i = 0; i < count; i++) {
            byte[] bytes = ArraysUtils.subarrays(content, i * maxPart, maxPart);
            result.add(bytes);
        }
        return result;
    }

    /**
     * 数据检验
     *
     * @param data  {@link Byte}[] 源数据
     * @param begin {@link Integer} 开始字节
     * @param end   {@link Integer} 结束字节
     * @return {@link Byte}
     */
    public static byte checkPackage(byte[] data, int begin, int end) {
        byte crc = 0x00;
        for (int i = begin; i <= end; i++) {
            crc ^= data[i];
        }
        return crc;
    }

    /**
     * 数据转义
     *
     * @param data {@link Byte} 源数据
     * @param from {@link Byte}[] 需要转义的字节
     * @param to   {@link Byte}[][] 转义后的字节
     * @return
     */
    public static byte[] escape(byte[] data, byte[] from, byte[][] to) {
        // 统计转义次数
        int count = 0;
        for (int i = 0, length = data.length; i < length; i++) {
            for (byte b : from) {
                if (data[i] == b) {
                    count++;
                }
            }
        }
        int index = 0;
        byte[] result = new byte[count + data.length];
        for (int i = 0, i_length = data.length; i < i_length; i++) {
            boolean isEscape = false;// 标识当前字节是否转义
            for (int j = 0, j_length = from.length; j < j_length; j++) {
                if (data[i] == from[j]) {
                    result[index++] = to[j][0];
                    result[index++] = to[j][1];
                    isEscape = true;
                }
            }
            if (!isEscape) {
                result[index++] = data[i];
            }
        }
        return result;
    }

    /**
     * 数据转义还原
     *
     * @param data {@link Byte} 源数据
     * @param from {@link Byte}[] 需要转义还原的字节
     * @param to   {@link Byte}[][] 转义还原后的字节
     * @return
     */
    public static byte[] unEscape(byte[] data, byte[][] from, byte[] to) {
        int count = 0;// 累计还原次数
        byte[] temp_result = new byte[data.length];
        int index = 0;
        for (int i = 0, i_length = data.length; i < i_length; i++) {
            boolean isUnEscape = false;
            for (int j = 0, j_length = from.length; j < j_length; j++) {
                if (data[i] == from[j][0]) {
                    if (i + 1 < i_length) {
                        if (data[i + 1] == from[j][1]) {
                            temp_result[index++] = to[j];
                            i++;
                            count++;
                            isUnEscape = true;
                        }
                    }
                }
            }
            if (!isUnEscape) {
                temp_result[index++] = data[i];
            }
        }
        // 如果没有转义还原,直接返回,否则去除最后空节字数据
        if (count == 0) {
            return temp_result;
        }

        byte[] result = new byte[data.length - count];
        System.arraycopy(temp_result, 0, result, 0, result.length);
        return result;
    }

    /**
     * @param socketAddress {@link SocketAddress}
     * @return array[0]:ip<br>
     * array[1]:port
     */
    public static String[] getIpByRemoteAddress(SocketAddress socketAddress) {
        if (socketAddress == null) {
            throw new NullPointerException("socketAddress");
        }
        String remoteAddress = socketAddress.toString();
        String[] array = remoteAddress.split(":");
        if (array.length == 2) {
            return new String[]{array[0].substring(1), array[1]};
        } else {
            throw new IllegalArgumentException("socketAddress");
        }

    }

    private static byte[] escapeByte = null;
    private static byte[][] toEscapeByte = null;

    static {
        toEscapeByte = new byte[4][2];
        toEscapeByte[0][0] = 0x7d;
        toEscapeByte[0][1] = 0x01;
        toEscapeByte[1][0] = 0x7d;
        toEscapeByte[1][1] = 0x02;

        toEscapeByte[2][0] = 0x7A;
        toEscapeByte[2][1] = 0x01;
        toEscapeByte[3][0] = 0x7B;
        toEscapeByte[3][1] = 0x02;

        escapeByte = new byte[4];
        escapeByte[0] = 0x7e;
        escapeByte[1] = 0x7d;
        escapeByte[2] = 0x7A;
        escapeByte[3] = 0x7B;
    }

    public static void main(String[] args) {
        // String msg = "017E0203047E00857A";
        // System.err.println("原始数据：" + msg);
        // byte[] bytes = Convert.hexStringToBytes(msg);
        // byte[] escapeBytes = escape(bytes, escapeByte, toEscapeByte);
        // System.err.println("转义后数据：" + Convert.bytesToHexString(escapeBytes));
        //
        // String msg1 = "017D010203047D0100857A01";
        // byte[] bytes1 = Convert.hexStringToBytes(msg1);
        // byte[] escapeBytes1 = unEscape(bytes1, toEscapeByte, escapeByte);
        // System.err.println("转义还原后数据：" +
        // Convert.bytesToHexString(escapeBytes1));
        byte[] bytes = Convert
                .hexStringToBytes("0000000000300200361350000486000EAE08DC3A108A5818A683C43620C3D7A51228F692C43630ABDFA51238F605403A489E0150898AE69705589453608E8AE697056800789E0109");
        System.err.println(checkPackage(bytes, 0, bytes.length - 1));
    }

}
