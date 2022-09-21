package org.xiaobai.service;

import org.xiaobai.response.BingImage;
import org.xiaobai.response.HistoryToday;
import org.xiaobai.response.Ktccy;
import org.xiaobai.response.QQInfoResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xdf
 */
public interface ApiService {
    /**
     * 每日一句
     * @return String
     */
    String oneWord() throws IOException;

    /**
     * 获取QQ信息
     * @param qq
     * @return
     */
    QQInfoResponse qInfo(String qq) throws IOException;

    void qrCode(String msg, Integer width, Integer height, HttpServletResponse response) throws IOException;

    /**
     * 发送邮件
     * @param to
     * @param title
     * @param content
     */
    void sendMail(String to, String title, String content) throws Exception;

    /**
     * bing图片
     * @param isSendRedirect
     * @return
     */
    BingImage bingImage(Integer isSendRedirect, HttpServletResponse response) throws IOException;

    /**
     * 历史上今天
     * @return
     */
    List<HistoryToday> historyToday() throws IOException;

    /**
     * 看图猜成语
     * @return
     */
    Ktccy ktccy() throws IOException;
}
