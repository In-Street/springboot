import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Cheng Yufei
 * @create 2019-05-20 16:20
 **/
public class Http {

    /**
     * apache: 调用可传输数组形式参数
     *
     * @throws IOException
     */
    @Test
    public void send() throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost post = new HttpPost("http://");
        post.setHeader("","");

        JSONObject data = new JSONObject();
        data.put("token", "token");
        data.put("conversionTypes", JSONArray.parseArray(JSON.toJSONString("")));

        StringEntity entity = new StringEntity(data.toString(), Charset.forName("utf-8"));
        entity.setContentEncoding("utf-8");
        post.setEntity(entity);

        CloseableHttpResponse response = client.execute(post);
    }

}
