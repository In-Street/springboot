package cyf.gradle.interview.service.http.api;

import com.alibaba.fastjson.JSONObject;
import cyf.gradle.interview.http.api.CmsNewsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Cheng Yufei
 * @create 2019-08-27 15:27
 **/
@Service
@Slf4j
public class HttpApiService {

    @Autowired
    private CmsNewsApi cmsNewsApi;


    public JSONObject get(Long id) throws IOException {

        JSONObject body = cmsNewsApi.selectRecord(id).execute().body();
        log.info(body.toJSONString());
        return body;
    }
}
