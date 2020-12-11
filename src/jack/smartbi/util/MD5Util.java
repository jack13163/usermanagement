package jack.smartbi.util;

import java.security.MessageDigest;
import java.util.Random;

/**
 * 加密工具类
 */
public class MD5Util {
    private static char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static void main(String[] args) throws Exception {
        String input = "123456";
        System.out.println("MD5加密" + "\n"
                + "明文：" + input + "\n"
                + "无盐密文：" + MD5WithoutSalt(input));
        System.out.println("带盐密文：" + MD5WithSalt(input,0));
    }

    /**
     * 自定义简单生成盐，是一个随机生成的长度为16的字符串，每一个字符是随机的十六进制字符
     *
     * @return
     */
    public static String salt() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < sb.capacity(); i++) {
            sb.append(hex[random.nextInt(16)]);
        }
        return sb.toString();
    }

    /**
     * 从库中查找到的hash值提取出的salt
     *
     * @param hash
     * @return
     */
    public static String getSaltFromHash(String hash) {
        StringBuilder sb = new StringBuilder();
        char[] h = hash.toCharArray();
        for (int i = 0; i < hash.length(); i += 3) {
            sb.append(h[i + 1]);
        }
        return sb.toString();
    }

    /**
     * 不加盐MD5
     * @param inputStr
     * @return
     */
    public static String MD5WithoutSalt(String inputStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return byte2HexStr(md.digest(inputStr.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

    }

    /**
     * inputStr是输入的明文;type是处理类型，0表示注册存hash值到库时，1表示登录验证时
     *
     * @param inputStr
     * @param type
     * @return
     */
    public static String MD5WithSalt(String inputStr, int type) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            String salt = null;
            if (type == 0) {
                salt = salt();
            } else if (type == 1) {
                // 登录验证时，使用从库中查找到的hash值提取出的salt
                String queriedHash = null;
                salt = getSaltFromHash(queriedHash);
            }
            //加盐，输入加盐
            String inputWithSalt = inputStr + salt;
            String hashResult = byte2HexStr(md.digest(inputWithSalt.getBytes()));
            System.out.println("加盐密文：" + hashResult);

            // 将salt存储到hash值中，用于登陆验证密码时使用相同的盐
            char[] cs = new char[48];
            for (int i = 0; i < 48; i += 3) {
                cs[i] = hashResult.charAt(i / 3 * 2);
                // 输出带盐，存储盐到hash值中;每两个hash字符中间插入一个盐字符
                cs[i + 1] = salt.charAt(i / 3);
                cs[i + 2] = hashResult.charAt(i / 3 * 2 + 1);
            }
            hashResult = new String(cs);
            return hashResult;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * 将字节数组转换成十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String byte2HexStr(byte[] bytes) {
        int len = bytes.length;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < len; i++) {
            byte byte0 = bytes[i];
            result.append(hex[byte0 >>> 4 & 0xf]);
            result.append(hex[byte0 & 0xf]);
        }
        return result.toString();
    }
}
