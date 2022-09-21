package org.xiaobai.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class FileUtil {

    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.url}")
    private String fileUrl;

    public String upload(String suffix, MultipartFile file, String fileName) throws IOException {
        String dateDirPath = filePath + "/" + suffix;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) {
            dateDir.mkdirs();
        }
        file.transferTo(new File(dateDir, fileName));
        return getFileUrls(suffix, fileName);
    }

    public String upload(String suffix, BufferedInputStream file, String fileName) throws IOException {
        String dateDirPath = filePath + "/" + suffix;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) {
            dateDir.mkdirs();
        }
        BufferedInputStream bis = new BufferedInputStream(file);
        // 3.2 创建输出流,保存到本地 .
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dateDirPath + "/" + fileName));
        // 4. 读写数据
        byte[] b = new byte[1024 * 8];
        int len;
        while ((len = bis.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        //5. 关闭 资源
        bos.close();
        bis.close();
        return getFileUrls(suffix, fileName);
    }

    public void down(String suffix, HttpServletResponse response, String fileName) {
        FileInputStream is = null;
        ServletOutputStream os = null;
        try {
            String str = StrUtil.str(Base64.getDecoder().decode(fileName), "utf-8");
            String dateDirPath = filePath + "/" + suffix + "/" + str;
            File file = new File(dateDirPath);
            is = new FileInputStream(file);
            //获得响应流
            os = response.getOutputStream();
            if (str.contains("jpg") || str.contains("png")) {
                response.setContentType("image/png");
            } else {
                //设置响应头信息
                response.setHeader("content-disposition", "attachment;fileName=" + str);
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

    public List<File> getFiles(String suffix) {
        return cn.hutool.core.io.FileUtil.loopFiles(filePath + "/" + suffix);
    }

    public String getFileName(String fileName) {
        return StrUtil.str(Base64.getEncoder().encode(fileName.getBytes()), "utf-8");
    }

    public String getFileUrls(String suffix, String fileName) {
        return fileUrl + "?suffix=" + suffix + "&fileName=" + StrUtil.str(Base64.getEncoder().encode(fileName.getBytes()), "utf-8");
    }

    public String down(String url, String suffix, String fileName) throws IOException {
       return this.upload(suffix, JsoupUtil.connect(url).maxBodySize(5000000).method(Connection.Method.GET).ignoreContentType(true).execute().bodyStream(), fileName);
    }

    public File getFile(String filePath) {
        return new File(this.filePath + "/" + filePath);
    }
}
