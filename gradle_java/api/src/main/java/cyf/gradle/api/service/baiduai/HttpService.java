package cyf.gradle.api.service.baiduai;

import com.alibaba.fastjson.JSONObject;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.io.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2019-01-14 11:10
 **/
@Service
@Slf4j
public class HttpService {

    public Object getAccessToken() {
        HttpRequest request = HttpRequest.get("https://aip.baidubce.com/oauth/2.0/token")
                .query(
                        "grant_type", "client_credentials",
                        "client_id", "SYDFpsDisZsy2S6y6hS18m0m",
                        "client_secret", "NVgCTKlxSpmdI3XWkYTYkMCBSePfwBaR"
                );
        HttpResponse response = request.send();
        Map<String, Object> map = JSONObject.parseObject(response.body()).getInnerMap();
        return map.get("access_token");
    }

    /**
     * 质量检测
     *
     * @return
     */
    @SneakyThrows(IOException.class)
    public Map<String, Object> detect() {
        byte[] bytes = FileUtil.readBytes("D:/卡同头像.jpeg");
        String encode = Base64Utils.encodeToString(bytes);
        HttpRequest request = HttpRequest.post("https://aip.baidubce.com/rest/2.0/face/v3/detect")
                .form(
                        "access_token", "24.9c359f436fb488437e68047d3c4170e9.2592000.1550040601.282335-15413918",
                      /*  "image", "https://xclub.oss-cn-shanghai.aliyuncs.com/3000/image/header/2018/5/25/4a657724-4f71-11e8-a7e9-109836a3b20f.jpg",
                        "image_type", "URL",*/
                      "image",encode,
                        "image_type", "BASE64",
                        "face_field", "quality,eye_status,emotion,face_shape,gender,glasses",
                        //最多处理人脸的数目
                        "max_face_num", "1",
                        /**
                         * LIVE表示生活照：通常为手机、相机拍摄的人像图片、或从网络获取的人像图片等
                         * IDCARD表示身份证芯片照：二代身份证内置芯片中的人像照片
                         * WATERMARK表示带水印证件照：一般为带水印的小图，如公安网小图
                         * CERT表示证件照片：如拍摄的身份证、工卡、护照、学生证等证件图片
                         * 默认LIVE
                         */
                        "face_type", "LIVE"
                );
        HttpResponse response = request.send();
        Map<String, Object> map = JSONObject.parseObject(response.body()).getInnerMap();
//        List list = (List) ((Map) map.get("result")).get("face_list");
//        list.forEach(p-> System.out.println(p));
        return map;
    }

    /**
     * 头像审核
     */
    @SneakyThrows(IOException.class)
    public Map<String, Object> faceAudit(String url) {

//        Files.readAllBytes()
        byte[] bytes = FileUtil.readBytes("D:/带微信号.jpg");
        String encode = Base64Utils.encodeToString(bytes);

        HttpRequest request = HttpRequest.post("https://aip.baidubce.com/rest/2.0/solution/v1/face_audit")
                .form(
                        "access_token", "24.9c359f436fb488437e68047d3c4170e9.2592000.1550040601.282335-15413918",
//                        "imgUrls", url
                        "images",encode
                ).header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        HttpResponse response = request.send();
        Map<String, Object> map = JSONObject.parseObject(response.bodyText()).getInnerMap();
        return map;
    }

    public void faceMatch() {

       /* Map<String, Object> map = new HashMap<>();
        map.put()*/


        HttpRequest request = HttpRequest.post("https://aip.baidubce.com/rest/2.0/face/v3/match")
                .form("", "").header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        HttpResponse response = request.send();
        Map<String, Object> map = JSONObject.parseObject(response.bodyText()).getInnerMap();
    }
}
