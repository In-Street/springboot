package cyf.gradle.interview.http.api;

import com.alibaba.fastjson.JSONObject;
import org.http.liar.annotation.ApiClient;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@ApiClient(url = "http://localhost:8092/sharing/")
public interface CmsNewsApi {

  /**
   * 参数：@Body / @Query / @Path / @Header
   * @param id
   * @return
   */
  @GET("selectRecordById/{id}")
  Call<JSONObject> selectRecord(@Path("id") Long id);

}
