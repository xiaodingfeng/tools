package org.xiaobai.core.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Objects;

/**
 * ip 地址库工具类
 */
@Service
public class IpUtil {

    private static Searcher searcher = null;

    @PostConstruct
    public void initSearchTree() {
        try {
            // 获取当前记录地址位置的文件
            String dbPath = "/home/tools/ip2region/ip.db";
            File file = new File(dbPath);
            //如果当前文件不存在，则从缓存中复制一份
            if (!file.exists()) {
                dbPath = "/home/tools/temp/ip.db";
                file = new File(dbPath);
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("static/ip2region.xdb")), file);
            }
            //创建查询对象
            searcher = Searcher.newWithFileOnly(dbPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String searchIp(String ip) {
        try {
            return searcher.searchByStr(ip);
        } catch (Exception e) {
            return null;
        }
    }

    public static String searchIpCity(String ip) {
        try {
            String searchIp = searchIp(ip);
            if (StrUtil.isEmpty(searchIp)) {
                return null;
            }
            String[] split = searchIp.split("\\|");
            String city = split[split.length - 2];
            String province = split[split.length - 3];
            System.out.println(province + "-" + city);
            return province + "-" + city;
        } catch (Exception e) {
            return null;
        }
    }
}
