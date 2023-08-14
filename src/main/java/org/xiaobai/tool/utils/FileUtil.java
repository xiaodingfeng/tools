package org.xiaobai.tool.utils;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xiaobai.core.config.TranscodeConfig;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.FFmpegUtil;
import org.xiaobai.core.utils.PicUtils;
import org.xiaobai.core.utils.RandomPwdUtil;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class FileUtil {

    @Resource
    private FFmpegUtil fFmpegUtil;

    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.url}")
    private String fileUrl;


    @Value("${file.pathTemp}")
    private String pathTemp;

    @Value("${file.baseUrl}")
    private String baseUrl;

    public File createMarkDownFile(List<String> result, String fileName) {
        String dateDirPath = pathTemp + "/" + fileName + ".md";
        File dateDir = new File(dateDirPath);
        cn.hutool.core.io.FileUtil.writeLines(result, dateDir, StandardCharsets.UTF_8, true);
        return dateDir;
    }

    public FileInfo upload(String suffix, MultipartFile file, String fileName) throws IOException {
        String dateDirPath = filePath + "/" + suffix;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) {
            dateDir.mkdirs();
        }
        final File realFile = new File(dateDir, fileName);
        FileInfo fileInfo = new FileInfo();
        if (isImageExtension(fileName)) {
            String realFile1Name = RandomPwdUtil.randomPassword() + fileName;
            final File realFile1 = new File(dateDir, realFile1Name);
            file.transferTo(realFile1);
            PicUtils.compressPicForScale(realFile1, realFile,
                    1200, 0.8, 2560, 1440);
            fileInfo.setTitle(fileName);
            fileInfo.setUrl(getFileUrls(suffix, fileName));
            fileInfo.setRealUrl(getFileUrls(suffix, realFile1Name));
        } else if (isVideoExtension(fileName)){
            fileInfo = uploadVideo(file);
        } else {
            file.transferTo(realFile);
            fileInfo.setTitle(fileName);
            fileInfo.setUrl(getFileUrls(suffix, fileName));
            fileInfo.setRealUrl(getFileUrls(suffix, fileName));
        }
        return fileInfo;
    }

    @Data
    public class FileInfo {
        private String poster;
        private String url;
        private String realUrl;
        private String title;
    }

    public FileInfo uploadVideo(MultipartFile video) throws IOException {
        /**    参数传UUID去数据库查询需要转换的视频地址 进行入参
         public ResponseData upload (@RequestParam("uuid") String uuid) throws Exception {
         TranscodeConfig transcodeConfig = new TranscodeConfig();
         FastDfsFile fastDfsFile = sectionService.getSectionByUUID(uuid);
         if(fastDfsFile.getFastDfsFileUrl() == null){
         LOGGER.info("请上传视频！！");
         return ResponseData.warnWithMsg("请选择要上传的视频！");
         }

         MultipartFile video = UrlToMultipartFile.urlToMultipartFile(fastDfsFile.getFastDfsFileUrl());
         */
        TranscodeConfig transcodeConfig = new TranscodeConfig();
        log.info("文件信息：title={}, size={}", video.getOriginalFilename(), video.getSize());
        log.info("转码配置：{}", transcodeConfig);

        // 原始文件名称，也就是视频的标题
        String title = video.getOriginalFilename();

        // io到临时文件
        Path tempFile = Paths.get(pathTemp).resolve(title);
        log.info("io到临时文件：{}", tempFile.toString());

        String videoFolder = filePath + "/video";
        try {
            video.transferTo(tempFile);

            // 删除后缀
            title = title.substring(0, title.lastIndexOf(".")) + "-" + UUID.randomUUID().toString().replaceAll("-", "");

            // 按照日期生成子目录
            String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());

            // 尝试创建视频目录
            Path targetFolder = Files.createDirectories(Paths.get(videoFolder, today, title));

            log.info("创建文件夹目录：{}", targetFolder);
            Files.createDirectories(targetFolder);

            // 执行转码操作
            log.info("开始转码");
            try {
                fFmpegUtil.transcodeToM3u8(tempFile.toString(), targetFolder.toString(), transcodeConfig);
            } catch (Exception e) {
                e.printStackTrace();
                throw new TipException(e.getMessage());
            }
            FileInfo fileInfo = new FileInfo();
            fileInfo.setTitle(title);
            fileInfo.setUrl(baseUrl + "/video" + String.join("/", "", today, title, "ts/index.m3u8"));
            fileInfo.setPoster(baseUrl + "/video" + String.join("/", "", today, title, "poster.jpg"));

            return fileInfo;
        } finally {
            // 始终删除临时文件
            Files.delete(tempFile);
        }
    }


    public boolean remove(String suffix, String fileName) throws IOException {
        String dateDirPath = filePath + "/" + suffix;
        File dateDir = new File(dateDirPath);
        return cn.hutool.core.io.FileUtil.del(new File(dateDir, StrUtil.str(Base64.getDecoder().decode(fileName), "utf-8")));
    }

    public InputStream getInputStream(String urlPath) throws IOException {
        // 统一资源
        URL url = new URL(urlPath);
        // 连接类的父类，抽象类
        URLConnection urlConnection = url.openConnection();
        // http的连接类
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
        // 设定请求的方法，默认是GET
        httpURLConnection.setRequestMethod("GET");
        // 设置字符编码
        httpURLConnection.setRequestProperty("Charset", "UTF-8");
        // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
        httpURLConnection.connect();
        // 文件大小
        int fileLength = httpURLConnection.getContentLength();
        return httpURLConnection.getInputStream();
    }

    public String upload(String suffix, String fileName, InputStream inputStream) throws IOException {
        String dateDirPath = filePath + "/" + suffix;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) {
            dateDir.mkdirs();
        }
        // 文件名
        BufferedInputStream bin = new BufferedInputStream(inputStream);

        String path = dateDirPath + "/" + fileName;
        OutputStream out = new FileOutputStream(path);
        int size = 0;
        int len = 0;
        byte[] buf = new byte[1024];
        while ((size = bin.read(buf)) != -1) {
            len += size;
            out.write(buf, 0, size);
        }
        bin.close();
        out.close();
        return getFileUrls(suffix, fileName);
    }

    public String upload(String suffix, String urlPath, String fileName) throws IOException {
        return upload(suffix, fileName, getInputStream(urlPath));
    }

    public void down(String suffix, HttpServletResponse response, String fileName) {
        FileInputStream is = null;
        ServletOutputStream os = null;
        try {
            String str = fileName;
            String dateDirPath = filePath + "/" + suffix + "/" + str;
            File file = new File(dateDirPath);
            is = new FileInputStream(file);
            //获得响应流
            os = response.getOutputStream();
            if (str.contains("jpg") || str.contains("png")) {
                response.setContentType("image/png");
            } else {
                //设置响应头信息
                response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder
                        .encode(file.getName(), StandardCharsets.UTF_8));
                response.setCharacterEncoding("utf-8");
            }

            //通过响应流将文件输入流读取到的文件写出
            IOUtils.copy(is, os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void down(File file, HttpServletResponse response) {
        FileInputStream is = null;
        ServletOutputStream os = null;
        try {
            is = new FileInputStream(file);
            //获得响应流
            os = response.getOutputStream();
            if (file.getName().contains("jpg") || file.getName().contains("png")) {
                response.setContentType("image/png");
                //通过响应流将文件输入流读取到的文件写出
                IOUtils.copy(is, os);
            } else {
                //设置响应头信息
                response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder
                        .encode(file.getName(), StandardCharsets.UTF_8));
                response.setCharacterEncoding("utf-8");
                //通过响应流将文件输入流读取到的文件写出
                IOUtils.copy(is, os);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void down(String filePath, HttpServletResponse response) {
        FileInputStream is = null;
        ServletOutputStream os = null;
        try {
            File file = new File(filePath);
            is = new FileInputStream(file);
            //获得响应流
            os = response.getOutputStream();
            if (filePath.contains("jpg") || filePath.contains("png")) {
                response.setContentType("image/png");
                //通过响应流将文件输入流读取到的文件写出
                IOUtils.copy(is, os);
                os.flush();
            } else {
                //设置响应头信息
                response.setHeader("content-disposition", "attachment;fileName=" + file.getName());
                response.setCharacterEncoding("utf-8");
                //通过响应流将文件输入流读取到的文件写出
                IOUtils.copy(is, os);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    private boolean isImageExtension(String fileName) {
        // 在这里可以根据需要自定义允许的图片扩展名列表
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif"};
        return fileName != null && Arrays.stream(imageExtensions).anyMatch(fileName::contains);
    }

    private boolean isVideoExtension(String fileName) {
        // 在这里可以根据需要自定义允许的图片扩展名列表
        String[] videoExtensions = {"mp4", "avi", "mpeg", "m4v", "wmv", "webm", "ogm"};
        return fileName != null && Arrays.stream(videoExtensions).anyMatch(fileName::contains);
    }

    public List<File> getFiles(String suffix) {
        return cn.hutool.core.io.FileUtil.loopFiles(filePath + "/" + suffix);
    }

    public String getFileName(String fileName) {
        return StrUtil.str(Base64.getEncoder().encode(fileName.getBytes()), "utf-8");
    }

    public String getFileUrls(String suffix, String fileName) {
        return fileUrl + "?suffix=" + suffix + "&fileName=" + fileName;
    }

    public String down(String url, String suffix, String fileName) throws IOException {
        return this.upload(suffix, url, fileName);
    }

    public File getFile(String filePath) {
        return new File(this.filePath + "/" + filePath);
    }

    public File getFile(String fileName, String suffix) {
        return new File(this.filePath + "/" + suffix + "/" + fileName);
    }
}
