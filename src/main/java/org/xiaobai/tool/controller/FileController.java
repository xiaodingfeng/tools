package org.xiaobai.tool.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xiaobai.tool.utils.FileUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "文件信息", tags = {"文件信息"})
@RestController
@RequestMapping("/tool/file")
public class FileController {

    @Autowired
    private FileUtil fileUtil;

    @ApiOperation(value = "文件上传", httpMethod = "POST", response = void.class)
    @PostMapping("/upload")
    public void upload(String suffix, MultipartFile file, String fileName) throws IOException {
        fileUtil.upload(suffix, file, fileName);
    }

    @ApiOperation(value = "文件下载", httpMethod = "GET", response = void.class)
    @GetMapping("/down")
    public void down(HttpServletResponse response, String suffix, String fileName) throws IOException {
        fileUtil.down(suffix, response, fileName);
    }
}
