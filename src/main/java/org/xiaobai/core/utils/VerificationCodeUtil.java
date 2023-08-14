package org.xiaobai.core.utils;

import java.util.Random;

/**
 * @ClassName VerificationCodeUtil
 * @Description 验证码工具类
 * @Author dingfeng.xiao
 * @Date 2023/6/30 15:11
 * @Version 1.0
 */
public class VerificationCodeUtil {
    public static String generateVerificationCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }
}
