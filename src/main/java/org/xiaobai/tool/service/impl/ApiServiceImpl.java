package org.xiaobai.tool.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.xiaobai.core.utils.CacheUtil;
import org.xiaobai.core.utils.JsoupUtil;
import org.xiaobai.core.utils.MailUtil;
import org.xiaobai.tool.constants.KeyEnum;
import org.xiaobai.tool.response.*;
import org.xiaobai.tool.service.ApiService;
import org.xiaobai.tool.utils.FileUtil;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

/**
 * @author xdf
 */
@Slf4j
@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private CacheUtil<List<String>> cacheUtil;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public String oneWord() throws IOException {
        List<String> list = cacheUtil.get(KeyEnum.ONE_WORLD.getValue());
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(new Random().nextInt(list.size()));
        }
        list = new ArrayList<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/meiriyiju.txt");
        Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream));
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            list.add(line);
        }
        cacheUtil.put(KeyEnum.ONE_WORLD.getValue(), list);
        return list.get(new Random().nextInt(list.size()));
    }

    @Override
    public QQInfoResponse qInfo(String qq) throws IOException {
        if (!StringUtils.hasText(qq)) {
            return null;
        }
        JsoupUtil.connect("https://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=" + qq).get();
        return null;
    }

    @Override
    public void qrCode(String msg, Integer width, Integer height, HttpServletResponse response) throws IOException {
        if (!StringUtils.hasText(msg)) {
            return;
        }
        QrConfig config = new QrConfig(width == null ? 300 : width, height == null ? 300 : height);
        config.setMargin(3);
        config.setForeColor(Color.BLACK);
        config.setBackColor(Color.WHITE);
        OutputStream outputStream = response.getOutputStream();
        QrCodeUtil.generate(msg, config, "", outputStream);
        response.setCharacterEncoding("UTF-8");
        response.flushBuffer();
    }

    @Override
    public void sendMail(String to, String title, String content) throws Exception {
        mailUtil.sendSimpleMail(to, title, content);
    }

    @Override
    public BingImage bingImage(Integer isSendRedirect, HttpServletResponse response) throws IOException {
        BingImage bingImage = new BingImage();
        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String suffix = "bing";
        if (isSendRedirect != null && isSendRedirect == 1) {
            File file = fileUtil.getFile(suffix + "/" + yyyyMMdd + ".png");
            if (file.exists()) {
                fileUtil.down(file, response);
                return bingImage;
            }
        }
        Document document = JsoupUtil.connect("https://cn.bing.com/").get();
        String attr = Objects.requireNonNull(document.getElementById("preloadBg")).attr("href");
        if (!attr.contains("http")) {
            attr = "https://cn.bing.com" + attr;
        }

        bingImage.setImage(attr);
        if (!StringUtils.hasText(bingImage.getImage())) {
            return null;
        }


        Elements meta = document.getElementsByTag("meta");
        for (Element element : meta) {
            if ("og:title".equals(element.attr("property"))) {
                bingImage.setTitle(element.attr("content"));
            }
            if ("og:description".equals(element.attr("property"))) {
                bingImage.setDescription(element.attr("content"));
            }
        }
        bingImage.setDate(new Date());

        String fileurls = fileUtil.down(bingImage.getImage(), suffix, yyyyMMdd + ".png");
        if (isSendRedirect != null && isSendRedirect == 1) {
            File file = fileUtil.getFile(suffix + "/" + yyyyMMdd + ".png");
            if (file.exists()) {
                fileUtil.down(file, response);
            }
        }
        bingImage.setImage(fileurls);
        return bingImage;
    }

    @Override
    public List<HistoryToday> historyToday(String date) throws IOException {
        String yyyyMMdd = date;
        if (!StringUtils.hasText(yyyyMMdd)) {
            yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
        }
        String suffix = "historyToday";
        File file = fileUtil.getFile(suffix + "/" + yyyyMMdd + ".json");
        if (file.exists()) {
            return JSON.parseArray(cn.hutool.core.io.FileUtil.readString(file, Charset.defaultCharset()), HistoryToday.class);
        }
        String months = new SimpleDateFormat("MM").format(new Date());
        List<HistoryToday> list = new ArrayList<>();
        Connection.Response response = JsoupUtil.connect("https://baike.baidu.com/cms/home/eventsOnHistory/" + months + ".json").execute();
        JSONObject json = JSON.parseObject(response.body());
        JSONObject o = json.getJSONObject(months);
        JSONArray mMdd = o.getJSONArray(new SimpleDateFormat("MMdd").format(new Date()));
        for (int i = mMdd.size() - 1; i >= 0; i--) {
            list.add(JSON.toJavaObject(mMdd.getJSONObject(i), HistoryToday.class));
        }
        cn.hutool.core.io.FileUtil.writeString(JSON.toJSONString(list, true), file, Charset.defaultCharset());
        return list;
    }

    @Override
    public Ktccy ktccy() throws IOException {
        Ktccy ktccy = new Ktccy();
        List<File> list = fileUtil.getFiles("ktccy");
        if (CollectionUtils.isEmpty(list)) {

            for (int i = 1; i < 11; i++) {
                String url = "http://www.hydcd.com/cy/fkccy/index" + (i == 1 ? "" : i) + ".htm";
                Document document = JsoupUtil.connect(url).get();
                Element element = document.getElementById("table1");
                assert element != null;
                Element tbody = element.getElementsByTag("tbody").get(0);
                if (tbody == null) {
                    return null;
                }
                for (Element td : tbody.getElementsByTag("td")) {
                    Element img = td.getElementsByTag("img").get(0);
                    String msg = img.attr("alt");
                    String src = img.attr("src");
                    fileUtil.down("http://www.hydcd.com/cy/fkccy/" + src, "ktccy", msg + ".png");
                }
            }
            list = fileUtil.getFiles("ktccy");
        }
        File file = list.get(new Random().nextInt(list.size()));
        ktccy.setMsg(file.getName().split("\\.")[0]);
        ktccy.setUrl(fileUtil.getFileUrls("ktccy", file.getName()));
        return ktccy;
    }

    @Override
    public MeiRiYiWen meiriyiwen(String date) throws IOException {
        String yyyyMMdd = date;
        if (!StringUtils.hasText(yyyyMMdd)) {
            yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
        }
        String suffix = "meiriyiwen";
        File file = fileUtil.getFile(suffix + "/" + yyyyMMdd + ".json");
        if (file.exists()) {
            return JSON.parseObject(cn.hutool.core.io.FileUtil.readString(file, Charset.defaultCharset()), MeiRiYiWen.class);
        }
        String url = "https://interface.meiriyiwen.com/article/random?dev=1";
        Connection.Response document = JsoupUtil.connect(url).execute();
        JSONObject json = JSON.parseObject(document.body());
        MeiRiYiWen data = JSON.parseObject(JSON.toJSONString(json.get("data")), MeiRiYiWen.class);
        data.setDate(yyyyMMdd);
        cn.hutool.core.io.FileUtil.writeString(JSON.toJSONString(data, true), file, Charset.defaultCharset());
        return data;
    }

    @Override
    public boolean isBaiduCollect(String url) throws IOException {
        Document document = JsoupUtil.connect("https://www.baidu.com/s?wd=" + url).get();

        Element element = document.selectFirst("#content_left");

        Elements elements = null;
        if (element != null) {
            elements = element.getElementsByClass("result-molecule  new-pmd");
        }

        String text = null;
        if (elements != null) {
            text = elements.text();
        }
        return text == null || !text.contains("没有找到该URL");
    }
}
