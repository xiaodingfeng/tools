package org.xiaobai.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author xdf
 */
@Service
public class MailUtil {

    @Value("${spring.mail.username}")
    private String userName;

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendSimpleMail(String to, String title, String content) {
        if (!StringUtils.hasText(to) || !StringUtils.hasText(title)) {
            return;
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("一条消息" + '<' + userName + '>');
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(content);
        javaMailSender.send(simpleMailMessage);
    }
}
