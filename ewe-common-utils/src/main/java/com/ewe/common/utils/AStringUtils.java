package com.ewe.common.utils;

public class AStringUtils {
    /**
     * 字符串空值转换为默认值
     *
     * @param ori Object 原始字符串
     * @param def String 默认字符串
     * @return String 转换结果
     */
    public static String nvl(Object ori, String def) {
        if (ori == null) {
            return def;
        } else {
            return ori.toString();
        }
    }
}
