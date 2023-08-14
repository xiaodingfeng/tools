package org.xiaobai.tool.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.xiaobai.tool.po.BaseIdPO;
import org.xiaobai.tool.po.TImagesPO;
import org.xiaobai.tool.request.ImageListRequest;
import org.xiaobai.tool.request.ImageUploadRequest;
import org.xiaobai.tool.response.Result;
import org.xiaobai.tool.service.TImagesService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Api(value = "图片信息", tags = {"图片信息"})
@RestController
@RequestMapping("/tool/images")
public class ImageController {

    @Resource
    private TImagesService tImagesService;

    @ApiOperation("图片上传")
    @PostMapping("/upload")
    public Result<List<TImagesPO>> upload(ImageUploadRequest request) throws IOException {
        return Result.success(tImagesService.upload(request));
    }

    @ApiOperation("所有图片删除")
    @PostMapping("/deleteAll")
    public Result<Boolean> delete(String password) throws IOException {
        return Result.success(tImagesService.deleteAll(password));
    }

    @ApiOperation("图片删除")
    @PostMapping("/delete")
    public Result<Boolean> delete(Long id) throws IOException {
        return Result.success(tImagesService.delete(id));
    }

    @ApiOperation("图片列表")
    @PostMapping("/list")
    public Result<IPage<TImagesPO>> list(@RequestBody ImageListRequest request) {
        return Result.success(tImagesService.listPage(request));
    }

    @ApiOperation("图片明细")
    @GetMapping("/detail")
    public Result<TImagesPO> detail(Long id) throws IOException {
        return Result.success(tImagesService.getById(id));
    }

    @ApiOperation("图片分类")
    @GetMapping("/category")
    public Result<List<String>> category() {
        return Result.success(tImagesService.category());
    }

    @ApiOperation("图片更新")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody TImagesPO request) throws IOException {
        return Result.success(tImagesService.update(new LambdaUpdateWrapper<TImagesPO>()
                .set(TImagesPO::getDescription, request.getDescription())
                .set(TImagesPO::getCategory, request.getCategory())
                .eq(BaseIdPO::getId, request.getId())));
    }
}
