package cn.iyuxuan.poi.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StringUtils {

    /**
     * @param arg 需要判断的字符串
     * @return 是否是空/空字符串
     */
    public static boolean isEmpty(String arg) {
        return null == arg || arg.isEmpty();
    }

    public static boolean isNotEmpty(String arg) {
        return !isEmpty(arg);
    }

    public static String readFile2String(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在!");
        }
        FileInputStream inputStream = null;
        ByteArrayOutputStream resultStream = null;
        try {
            inputStream = new FileInputStream(file);
            resultStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                resultStream.write(buffer, 0, length);
            }
            return resultStream.toString("utf-8");
        } catch (Exception e) {
            return "";
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != resultStream) {
                try {
                    resultStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void log(Object obj) {
        System.out.println(obj);
    }

}
