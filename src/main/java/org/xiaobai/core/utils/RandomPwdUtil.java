package org.xiaobai.core.utils;

import java.util.Random;

/**
 * @ClassName RandomPwdUtil
 * @Description 随机密码
 * @Author dingfeng.xiao
 * @Date 2023/7/13 10:17
 * @Version 1.0
 */
public class RandomPwdUtil {
    /**
     * 生成随机密码：6位数字
     *
     * @return 密码字符串
     */
    public static String randomPassword() {
        char[] chars = new char[6];
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            chars[i] = (char) ('0' + rnd.nextInt(10));
        }
        return new String(chars);
    }
}
