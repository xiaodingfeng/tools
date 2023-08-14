package org.xiaobai.tool.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Configuration
@EnableScheduling
public class SaticScheduleTask {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    private MailProperties mailProperties;
    @Value("${spring.url}")
    private String fromUrl;

    //每天5点20分
//    @Scheduled(cron = "0 20 5 * * ?")
    //三十秒
//    @Scheduled(fixedRate = 3000)
    private void configureTasks() throws MessagingException, IOException {
        sendHtml();
    }

    /**
     * @return void
     * @Author xiaodingfeng
     * @Description 邮件发送
     * @Date 13:10 2021/3/6
     * @Param []
     **/
    public void sendHtml() throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("一封信息");//标题
        helper.setFrom(mailProperties.getUsername(), mailProperties.getJndiName());//邮箱和昵称

        helper.setBcc(new String[]{"mrxiaodfeng@163.com", "xiaodf@cares.sh.cn"});
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if (isFirstSunday) {
            weekDay = weekDay - 1;
            if (weekDay == 0) {
                weekDay = 7;
            }
        }
        helper.setSentDate(date);
        Context context = new Context();
        context.setVariable("username", "xxxx");//对方称谓
        context.setVariable("user", "xxxx");//我方称谓
        context.setVariable("date", calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) +
                "月" + calendar.get(Calendar.DATE) + "日" +
                calendar.get(Calendar.HOUR_OF_DAY) + "时" +
                calendar.get(Calendar.MINUTE) + "分");
        context.setVariable("tishi", weekDay >= 1 && weekDay <= 5 ? "今天是上课工作时间哦，一天的学习工作要开始了哦！" :
                "今天是周末哦，经过一个星期的学习工作也要适当休息哦！");
        String content = getContent("今天又是爱你的一天！");
        context.setVariable("content", content);
        context.setVariable("fromurl", fromUrl);
        String process = templateEngine.process("template", context);
        helper.setText(process, true);
        // 发送消息
        javaMailSender.send(mimeMessage);
        log.info("发送一条邮件, 内容为：" + content);
    }

    /**
     * @return java.lang.String
     * @Author xiaodingfeng
     * @Description 获取服务器上文本第一行句子，删除该句子，保证不重复，当不存在句子时，返回默认句子
     * @Date 12:57 2021/3/6
     * @Param []
     **/
    public String getContent(String temp) throws IOException {
        String str = null;
        ArrayList<String> list = new ArrayList<>();
//        getClass().getClassLoader().getResourceAsStream("static/meiriyiju.txt");
        File file = ResourceUtils.getFile("/home/love.txt");
//        File file = ResourceUtils.getFile("C:\\Users\\XiaoBai\\Desktop\\love.txt"); //绝对路径
//        ClassPathResource classPathResource = new ClassPathResource("love.txt"); //当前项目下
        InputStream is = new FileInputStream(file);
//        InputStream is = classPathResource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String data = null;
        while ((data = br.readLine()) != null) {
            list.add(data);
        }
        br.close();
        isr.close();
        is.close();
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                if (!s.equals("") && s.length() != 0) {
                    str = s;
                    list.remove(i);
                    break;
                }
            }
        }
        if (list.size() != 0) {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);
            for (String string : list) {
                if (!string.equals("") && string.length() != 0) {
                    bw.write(string);
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        }
        return str == null ? temp : str;
    }
}
