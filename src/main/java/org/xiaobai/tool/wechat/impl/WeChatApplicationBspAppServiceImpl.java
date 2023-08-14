package org.xiaobai.tool.wechat.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.JsoupUtil;
import org.xiaobai.tool.po.TImagesPO;
import org.xiaobai.tool.service.TImagesService;
import org.xiaobai.tool.wechat.WeChatApplicationService;
import org.xiaobai.tool.wechat.dto.BspAppCategoryDTO;
import org.xiaobai.tool.wechat.dto.BspImageDTO;

import java.io.IOException;
import java.util.*;

/**
 * 蜡笔小新应用
 * url https://api.bspapp.com/client
 */
@Service("bspAppService")
public class WeChatApplicationBspAppServiceImpl implements WeChatApplicationService {

    private static final String BASE_URL = "https://api.bspapp.com/client";

    private static final Map<String, String> INIT_HEADER = initHeader();

    private static final Long TIME_STAMP = 1687428266264L;

    private static final String SPACE_ID = "fe59db17-e885-4fb3-abad-070625380206";
    @Autowired
    private TImagesService tImagesService;

    private static String tokenParameter() {
        return "{\"method\":\"serverless.auth.user.anonymousAuthorize\"" +
                ",\"params\":\"{}\"" +
                ",\"spaceId\":\"" + SPACE_ID + "\"" +
                ",\"timestamp\":" + TIME_STAMP + "}";
    }

    private static Map<String, String> initHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Host", "api.bspapp.com");
        header.put("Connection", "keep-alive");
        header.put("Content-Length", "141");
        header.put("referer", "https://servicewechat.com/wx9883d1ba34366cdf/1/page-frame.html");
        header.put("xweb_xhr", "1");
        header.put("x-serverless-sign", "f3fd748c97411c2712d5d69cedc068e4");
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36 MicroMessenger/7.0.20.1781(0x6700143B) NetType/WIFI MiniProgramEnv/Windows WindowsWechat/WMPF XWEB/6939");
        header.put("Content-Type", "application/json");
        header.put("Accept", "*/*");
        header.put("Sec-Fetch-Site", "cross-site");
        header.put("Sec-Fetch-Mode", "cors");
        header.put("Sec-Fetch-Dest", "empty");
        header.put("Accept-Encoding", "gzip, deflate, br");
        header.put("Accept-Language", "zh-CN,zh");
        return header;
    }

    private static String categoryParameter(String token) {
        return "{\"method\":\"serverless.function.runtime.invoke\"" +
                ",\"params\":\"{\\\"functionTarget\\\":\\\"router\\\"" +
                ",\\\"functionArgs\\\":{\\\"$url\\\":\\\"client/wechat/list/pub/getCenter\\\"" +
                ",\\\"clientInfo\\\":{\\\"PLATFORM\\\":\\\"mp-weixin\\\",\\\"OS\\\":\\\"windows\\\"" +
                ",\\\"APPID\\\":\\\"__UNI__4847442\\\",\\\"LOCALE\\\":\\\"zh-Hans\\\"" +
                ",\\\"DEVICEID\\\":\\\"16874202743826654881\\\",\\\"CLIENT_SDK_VERSION\\\":\\\"1.0.8\\\"}" +
                ",\\\"uniCloudClientInfo\\\":\\\"%7B%22ak%22%3A%22__UNI__4847442%22%2C%22p%22%3A%22i%22%2C%22ut%22%3A%22wx%22%2C%22uuid%22%3A%22m0fgsi99q605874t7g11gs58q181t11o%22%2C%22fn%22%3A%22router%22%2C%22sid%22%3A%22fe59db17-e885-4fb3-abad-070625380206%22%2C%22pvd%22%3A%22a%22%7D\\\"" +
                ",\\\"uniCloudDeviceId\\\":\\\"16874202743826654881\\\"}}\"" +
                ",\"spaceId\":\"" + SPACE_ID + "\"" +
                ",\"timestamp\":" + TIME_STAMP +
                ",\"token\":\"" + token + "\"}";
    }

    private static String imageParameter(String cateId, String token, int pageIndex, int pageSize) {
        return "{\"method\":\"serverless.function.runtime.invoke\"" +
                ",\"params\":\"{\\\"functionTarget\\\":\\\"router\\\"" +
                ",\\\"functionArgs\\\":{\\\"$url\\\":\\\"client/wechat/list/pub/getList\\\"" +
                ",\\\"data\\\":{\\\"dbName\\\":\\\"uni-wx-cover\\\"" +
                ",\\\"pageIndex\\\":" + pageIndex + ",\\\"pageSize\\\":" + pageSize +
                ",\\\"whereJson\\\":{\\\"mode\\\":0" +
                ",\\\"status\\\":1,\\\"category_id\\\":\\\"" + cateId + "\\\"}" +
                ",\\\"sortArr\\\":[{\\\"name\\\":\\\"_add_time\\\"" +
                ",\\\"type\\\":\\\"desc\\\"}]}" +
                ",\\\"clientInfo\\\":{\\\"PLATFORM\\\":\\\"mp-weixin\\\",\\\"OS\\\":\\\"windows\\\"" +
                ",\\\"APPID\\\":\\\"__UNI__4847442\\\",\\\"LOCALE\\\":\\\"zh-Hans\\\"" +
                ",\\\"DEVICEID\\\":\\\"16874202743826654881\\\"" +
                ",\\\"CLIENT_SDK_VERSION\\\":\\\"1.0.8\\\"}" +
                ",\\\"uniCloudClientInfo\\\":\\\"%7B%22ak%22%3A%22__UNI__4847442%22%2C%22p%22%3A%22i%22%2C%22ut%22%3A%22wx%22%2C%22uuid%22%3A%22db8auo4sr93b9kvusrh2gu457u0gj9so%22%2C%22fn%22%3A%22router%22%2C%22sid%22%3A%22fe59db17-e885-4fb3-abad-070625380206%22%2C%22pvd%22%3A%22a%22%7D\\\"" +
                ",\\\"uniCloudDeviceId\\\":\\\"16874202743826654881\\\"}}\"" +
                ",\"spaceId\":\"" + SPACE_ID + "\"" +
                ",\"timestamp\":" + TIME_STAMP +
                ",\"token\":\"" + token + "\"}";
    }

    private static List<TImagesPO> initImage(String cateId, String token) throws IOException {
        int page = 1;
        int pageSize = 50;
        BspImageDTO imageDTO = new BspImageDTO();
        imageDTO.setHasMore(true);
        List<TImagesPO> result = new ArrayList<>();
        while (imageDTO.getHasMore()) {
            Connection connect = JsoupUtil.baseConnectPost(BASE_URL);
            connect.headers(INIT_HEADER);
            connect.requestBody(imageParameter(cateId, token, page++, pageSize));
            ResponseDTO response = getResponse(connect);
            imageDTO = JSON.parseObject(response.data, BspImageDTO.class);
            List<BspImageDTO.RowsDTO> rows = imageDTO.getRows();
            if (CollectionUtils.isEmpty(rows)) {
                break;
            }
            for (BspImageDTO.RowsDTO row : rows) {
                for (String image : row.getImages()) {
                    TImagesPO po = new TImagesPO();
                    po.setCategory(row.getTags());
                    po.setDescription(row.getName());
                    po.setUrl(image);
                    result.add(po);
                }
            }
        }
        return result;
    }

    private static List<String> initCategory(String token) throws IOException {
        Connection connect = JsoupUtil.baseConnectPost(BASE_URL);
        connect.headers(INIT_HEADER);
        connect.requestBody(categoryParameter(token));
        ResponseDTO response = getResponse(connect);
        BspAppCategoryDTO categoryDTO = JSON.parseObject(response.data, BspAppCategoryDTO.class);
        if (Objects.isNull(categoryDTO)) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        for (BspAppCategoryDTO.CateDTO.RowsDTO row : categoryDTO.getTags().getRows()) {
            result.add(row.getId());
        }
        for (BspAppCategoryDTO.CateDTO.RowsDTO row : categoryDTO.getBzcate().getRows()) {
            result.add(row.getId());
        }
        for (BspAppCategoryDTO.CateDTO.RowsDTO row : categoryDTO.getTxcate().getRows()) {
            result.add(row.getId());
        }
        return result;
    }

    private static String initToken() throws IOException {
        Connection connect = JsoupUtil.baseConnectPost(BASE_URL);
        connect.headers(INIT_HEADER);
        connect.requestBody(tokenParameter());
        ResponseDTO response = getResponse(connect);
        TokenDTO data = JSON.parseObject(response.getData(), TokenDTO.class);
        return data.getAccessToken();
    }

    private static ResponseDTO getResponse(Connection connection) {
        try {
            ResponseDTO responseDTO = JSON.parseObject(connection.execute().body(), ResponseDTO.class);
            if (!responseDTO.success) {
                String errorMessage = responseDTO.error.getMessage();
                if (Objects.equals(responseDTO.error.getCode(), "SignatureNotMatch")
                        && errorMessage.contains("The signature we calculated is")) {
                    errorMessage = errorMessage.replace("The signature we calculated is: ", "");
                    INIT_HEADER.put("x-serverless-sign", errorMessage);
                    connection.headers(INIT_HEADER);
                    return getResponse(connection);
                }
            }
            return responseDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TipException(e.getMessage());
        }
    }

    @Override
    public void downImage() throws IOException {
        String initToken = initToken();
        List<TImagesPO> list = new ArrayList<>();
        for (String cateId : initCategory(initToken)) {
            List<TImagesPO> tImagesPOList = initImage(cateId, initToken);
            if (CollectionUtils.isEmpty(tImagesPOList)) {
                continue;
            }
            list.addAll(tImagesPOList);
        }
        tImagesService.upload(list);
    }

    @NoArgsConstructor
    @Data
    private static class ResponseDTO {

        @JsonProperty("success")
        private Boolean success;
        @JsonProperty("error")
        private ErrorDTO error;
        @JsonProperty("data")
        private String data;
    }

    @NoArgsConstructor
    @Data
    private static class TokenDTO {
        @JsonProperty("accessToken")
        private String accessToken;
        @JsonProperty("expiresInSecond")
        private Integer expiresInSecond;
    }

    @NoArgsConstructor
    @Data
    private static class ErrorDTO {
        @JsonProperty("code")
        private String code;
        @JsonProperty("message")
        private String message;
    }
}
