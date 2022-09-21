package org.xiaobai.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JsoupUtil {

    @Autowired
    private FileUtil fileUtil;

    public static Connection connect(String url) {
        Connection connect = Jsoup.connect(url);
        Map<String, String> header = new HashMap<>(6);
        header.put("Host", url.split("/")[2]);
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        header.put("Accept", "text/*, application/*+xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        header.put("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        header.put("Connection", "keep-alive");
        header.put("Accept-Encoding", "gzip, deflate");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        header.put("Cache-Control", "no-cache");
        header.put("Cookie", "Hm_lvt_8754302607a1cfb0d1d9cddeb79c593d=1663721351,1663726190; wzws_cid=e6217dcb7ea8d08fe5b6d304a1388ccbdb17f4a508a0be073d535b328f2fa0e3bece6e4bb2a005e1240865a383a3b064eb3d00fbdeb4019f8111c5b105b29e7d46f43f2b240645ebdbafe6f406e77d6a; Hm_lpvt_8754302607a1cfb0d1d9cddeb79c593d=1663727638");
        return connect.headers(header);
    }

}
