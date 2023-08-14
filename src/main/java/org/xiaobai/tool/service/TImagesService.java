package org.xiaobai.tool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.tool.po.TImagesPO;
import org.xiaobai.tool.request.ImageListRequest;
import org.xiaobai.tool.request.ImageUploadRequest;

import java.io.IOException;
import java.util.List;

public interface TImagesService extends IService<TImagesPO> {
    String upload(List<TImagesPO> list) throws IOException;

    List<TImagesPO> upload(ImageUploadRequest request) throws IOException;

    boolean delete(Long id) throws IOException;

    boolean deleteAll(String password) throws IOException;

    IPage<TImagesPO> listPage(ImageListRequest request);

    List<String> category();
}
