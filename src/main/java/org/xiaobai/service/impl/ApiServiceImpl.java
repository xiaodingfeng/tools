package org.xiaobai.service.impl;

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
import org.xiaobai.constants.KeyEnum;
import org.xiaobai.response.BingImage;
import org.xiaobai.response.HistoryToday;
import org.xiaobai.response.Ktccy;
import org.xiaobai.response.QQInfoResponse;
import org.xiaobai.service.ApiService;
import org.xiaobai.utils.CacheUtil;
import org.xiaobai.utils.FileUtil;
import org.xiaobai.utils.JsoupUtil;
import org.xiaobai.utils.MailUtil;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
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
        QrCodeUtil.generate(msg, config,"",  outputStream);
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

        bingImage.setImage(Objects.requireNonNull(document.getElementById("preloadBg")).attr("href"));
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
    public List<HistoryToday> historyToday() throws IOException {
        String months = new SimpleDateFormat("MM").format(new Date());
        List<HistoryToday> list = new ArrayList<>();
        Document document = JsoupUtil.connect("https://baike.baidu.com/cms/home/eventsOnHistory/" + months +".json").get();
        JSONObject json = JSON.parseObject(document.text());
        JSONObject o = json.getJSONObject(months);
        JSONArray mMdd = o.getJSONArray(new SimpleDateFormat("MMdd").format(new Date()));
        for (int i = mMdd.size() - 1; i >= 0; i--) {
            list.add(JSON.toJavaObject(mMdd.getJSONObject(i), HistoryToday.class));
        }
        return list;
    }

    @Override
    public Ktccy ktccy() throws IOException {
        Ktccy ktccy = new Ktccy();
        List<File> list = fileUtil.getFiles("ktccy");
        if (CollectionUtils.isEmpty(list))  {
            
            for (int i = 1; i < 11; i++) {
                String url = "http://www.hydcd.com/cy/fkccy/index" + (i == 1 ? "": i) + ".htm";
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
                    fileUtil.down("http://www.hydcd.com/cy/fkccy/" + src,  "ktccy", msg + ".png");
                }
            }
            list = fileUtil.getFiles("ktccy");
        }
        File file = list.get(new Random().nextInt(list.size()));
        ktccy.setMsg(file.getName().split("\\.")[0]);
        ktccy.setUrl(fileUtil.getFileUrls("ktccy", file.getName()));
        return ktccy;
    }

}
