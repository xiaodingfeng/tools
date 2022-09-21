package org.xiaobai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xiaobai.utils.FileUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileUtil fileUtil;

    @RequestMapping("/upload")
    public void upload(String suffix, MultipartFile file, String fileName) throws IOException {
        fileUtil.upload(suffix, file, fileName);
    }

    @RequestMapping("/down")
    public void down(HttpServletResponse response,String suffix, String fileName) throws IOException {
        fileUtil.down(suffix, response, fileName);
    }
}
