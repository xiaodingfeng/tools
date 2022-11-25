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
        connect.ignoreContentType(true);
        Map<String, String> header = new HashMap<>(6);
        header.put("Host", url.split("/")[2]);
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        header.put("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        header.put("Connection", "keep-alive");
        header.put("Accept-Encoding", "gzip, deflate");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        header.put("Cache-Control", "no-cache");
        header.put("Cookie", "BIDUPSID=4B8A7C47E58C20E9698CD8FD35B07379; PSTM=1663807249; BD_UPN=12314753; ISSW=1; ISSW=1; MCITY=-289%3A; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BAIDUID=31F6AE5749929FE4FB1FEF8ADBDCBFE8:SL=0:NR=10:FG=1; BDUSS=E3UTlRTzlVTnlKbDJmSS1vM0xibHRScUF4TGl4ZXpBamJEMHEwNjF1c2JhS1ZqSVFBQUFBJCQAAAAAAAAAAAEAAACSmZVkeGRm0KHLpwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABvbfWMb231jUG; BDUSS_BFESS=E3UTlRTzlVTnlKbDJmSS1vM0xibHRScUF4TGl4ZXpBamJEMHEwNjF1c2JhS1ZqSVFBQUFBJCQAAAAAAAAAAAEAAACSmZVkeGRm0KHLpwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABvbfWMb231jUG; H_PS_PSSID=37782_36546_37513_36920_37773_37139_34812_37303_37819_37798_37741_26350_37479; ab_sr=1.0.1_OWM3YmJjNTA0NGZhMDc5N2EyOWEzOGY2MTFkY2IzNGM2ODhmYWViOWExMzljNzBkM2JlMGY1ZGFlZTJjNWUwZWQyZDA0OTZhNTdjNTI2YzFlN2M5NDIxMDY5ZDVhMzY4Y2U3NWYxOGUwY2RlZWIzZmI3NDUwOTdlMmQ1ZGQyOWUyMjU3ZGE3OTBlNjBmZTJlNGE5ZWY5NjBiN2JhZGZlNmUyZjA5MjYyNjY0NGQ0MGM1MGYxYjNhYTE3Njk3OTVh; BAIDUID_BFESS=31F6AE5749929FE4FB1FEF8ADBDCBFE8:SL=0:NR=10:FG=1; delPer=0; BD_CK_SAM=1; PSINO=5; BA_HECTOR=2h800024a4210k25ag210k9o1ho0rhm1f; ZFY=ie:AAgBqmYOaZlarbFqfXxkOHKN0PoT5E8tFBXuLvKmQ:C; baikeVisitId=d5b55077-e1e8-4cf2-a8dd-5fd39ed91bb6; sug=3; sugstore=0; ORIGIN=2; bdime=0; H_PS_645EC=2272ieCXe0UrB%2FHPXvVhr6TbYTO0%2FIYTOLgAby36ZxTdwexPFNww7WCs8Qc");
        return connect.headers(header);
    }

}
