package cc.catface.clibrary.util.net.http.retrofit;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface IRetrofit {


    @GET
    Call<String> get(@Url String url);

    @GET()
    Call<String> get(@Url String url, @JSONField String json);

    @GET()
    Call<String> get(@Url String url, @QueryMap Map<String, String> map);

    @FormUrlEncoded



    @POST()
    Call<String> post(@Url String url, @JSONField String json);

    @POST()
    Call<String> post(@Url String url, @QueryMap Map<String, String> map);


    /**
     * post with map & files
     */
    @Multipart
    @POST
    Call<String> upload(@Url String url, @QueryMap Map<String, String> map, @PartMap Map<String, RequestBody> fileMap);

}
