package org.xiaobai.tool.service.impl;

import cn.hutool.core.lang.generator.UUIDGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.FileSizeCalculator;
import org.xiaobai.core.utils.ImagePixelExtractor;
import org.xiaobai.tool.dao.TImagesDao;
import org.xiaobai.tool.po.TImagesPO;
import org.xiaobai.tool.request.ImageListRequest;
import org.xiaobai.tool.request.ImageUploadRequest;
import org.xiaobai.tool.service.TImagesService;
import org.xiaobai.tool.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TImagesServiceImpl extends ServiceImpl<TImagesDao, TImagesPO> implements TImagesService {

    private final static String SUFFIX = "DRAWING_BED";
    @Autowired
    private FileUtil fileUtil;

    private boolean isImageExtension(String extension) {
        // 在这里可以根据需要自定义允许的图片扩展名列表
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif"};
        return extension != null && Arrays.asList(imageExtensions).contains(extension.toLowerCase());
    }

    private boolean existMD5(String md5) {
        return count(new LambdaQueryWrapper<TImagesPO>()
                .eq(TImagesPO::getMd5, md5)) > 0;
    }

    private TImagesPO getByMD5(String md5) {
        return getOne(new LambdaQueryWrapper<TImagesPO>()
                .eq(TImagesPO::getMd5, md5).last(" limit 1"));
    }

    @Override
    public String upload(List<TImagesPO> list) throws IOException {
        for (TImagesPO imagesPO : list) {
            try (InputStream inputStream = fileUtil.getInputStream(imagesPO.getUrl())) {
                String md5DigestAsHex = DigestUtils.md5DigestAsHex(inputStream);
                if (existMD5(md5DigestAsHex)) {
                    continue;
                }
                imagesPO.setMd5(md5DigestAsHex);
                String name = cn.hutool.core.io.FileUtil.getName(imagesPO.getUrl());
                log.info("下载url：" + imagesPO.getUrl());
                String next = new UUIDGenerator().next();
                String fileExtension = StringUtils.getFilenameExtension(name);
                String fileName = next + "." + fileExtension;
                String url = fileUtil.upload(SUFFIX, imagesPO.getUrl(), fileName);
                log.info("下载完成：url：" + imagesPO.getUrl());
                File file = fileUtil.getFile(fileName, SUFFIX);
                imagesPO.setSize(FileSizeCalculator.getFileSize(file));
                imagesPO.setRealFileName(name);
                String[] split = url.split("&fileName=");
                imagesPO.setFileName(split[split.length - 1]);
                imagesPO.setUrl(url);
                save(imagesPO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<TImagesPO> upload(ImageUploadRequest request) throws IOException {
        List<TImagesPO> list = new ArrayList<>();
        MultipartFile[] requestFiles = request.getFile();
        for (MultipartFile requestFile : requestFiles) {
            String contentType = requestFile.getContentType();
            // 获取文件扩展名
            String fileExtension = StringUtils.getFilenameExtension(requestFile.getOriginalFilename());
            // 判断文件类型是否为图片
            if (contentType != null && contentType.startsWith("image/") && isImageExtension(fileExtension)) {
                TImagesPO po = new TImagesPO();
                String next = new UUIDGenerator().next();
                String md5DigestAsHex = DigestUtils.md5DigestAsHex(requestFile.getInputStream());
                if (existMD5(md5DigestAsHex)) {
                    TImagesPO byMD5 = getByMD5(md5DigestAsHex);
                    po.setFileName(byMD5.getFileName());
                    po.setUrl(byMD5.getUrl());
                    po.setPixel(byMD5.getPixel());
                    po.setSize(byMD5.getSize());
                    po.setRealUrl(byMD5.getRealUrl());
                } else {
                    po.setPixel(Arrays.toString(ImagePixelExtractor.getImagePixel(requestFile.getInputStream())));
                    FileUtil.FileInfo fileInfo = fileUtil.upload(SUFFIX, requestFile, next + "." + fileExtension);
                    po.setRealUrl(fileInfo.getRealUrl());
                    po.setFileName(fileInfo.getTitle());
                    po.setUrl(fileInfo.getUrl());
                    po.setSize(FileSizeCalculator.getFileSize(requestFile));
                }
                po.setType(contentType);
                po.setMd5(md5DigestAsHex);
                po.setCategory(request.getCategory());
                po.setDescription(request.getDescription());
                po.setRealFileName(requestFile.getOriginalFilename());

                save(po);
                list.add(po);
            } else {
                TImagesPO po = new TImagesPO();
                String next = new UUIDGenerator().next();
                String md5DigestAsHex = DigestUtils.md5DigestAsHex(requestFile.getInputStream());
                if (existMD5(md5DigestAsHex)) {
                    TImagesPO byMD5 = getByMD5(md5DigestAsHex);
                    po.setFileName(byMD5.getFileName());
                    po.setUrl(byMD5.getUrl());
                    po.setRealUrl(byMD5.getRealUrl());
                    po.setPoster(byMD5.getPoster());
                    po.setSize(byMD5.getSize());
                } else {
                    FileUtil.FileInfo fileInfo = fileUtil.upload("FILE_BED", requestFile, next + "." + fileExtension);
                    po.setFileName(next + "." + fileExtension);
                    po.setUrl(fileInfo.getUrl());
                    po.setRealUrl(fileInfo.getRealUrl());
                    po.setPoster(fileInfo.getPoster());
                    po.setSize(FileSizeCalculator.getFileSize(requestFile));
                }
                po.setType(contentType);
                po.setMd5(md5DigestAsHex);
                po.setCategory(request.getCategory());
                po.setDescription(request.getDescription());
                po.setRealFileName(requestFile.getOriginalFilename());
                save(po);
                list.add(po);
            }
        }
        return list;
    }

    @Override
    public boolean delete(Long id) throws IOException {
        TImagesPO imagesPO = getById(id);
        if (Objects.isNull(imagesPO)) {
            return true;
        }
        if (fileUtil.remove(SUFFIX, imagesPO.getFileName())) {
            return removeById(id);
        }
        return false;
    }

    @Override
    public boolean deleteAll(String password) throws IOException {
        if (!"111111".equals(password)) {
            throw new TipException("验证错误");
        }
        List<TImagesPO> list = list();
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        for (TImagesPO tImagesPO : list) {
            if (fileUtil.remove(SUFFIX, tImagesPO.getFileName())) {
                removeById(tImagesPO.getId());
            }
        }
        return true;
    }

    @Override
    public IPage<TImagesPO> listPage(ImageListRequest request) {
        String keywords = request.getKeywords();
        boolean keywordsHasText = StringUtils.hasText(keywords);
        Page<TImagesPO> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<TImagesPO> queryWrapper = new LambdaQueryWrapper<TImagesPO>()
                .eq(StringUtils.hasText(request.getCategory()), TImagesPO::getCategory, request.getCategory())
                .and(keywordsHasText, v -> {
                    v.like(TImagesPO::getCategory, keywords)
                            .or()
                            .like(TImagesPO::getRealFileName, keywords)
                            .or()
                            .like(TImagesPO::getDescription, keywords);
                }).orderByDesc(TImagesPO::getId);
        return page(page, queryWrapper);
    }

    @Override
    public List<String> category() {
        return list(new QueryWrapper<TImagesPO>()
                .select("DISTINCT category"))
                .stream().map(TImagesPO::getCategory).filter(StringUtils::hasText).collect(Collectors.toList());
    }
}
