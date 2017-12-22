package cc.catface.clibrary.util.net.http.retrofit;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitT {



    /**
     * 主机地址
     */
    public static final String HOST_URL = "http://xkapi.com/";


    /**
     * ==
     */
    public static void get(final String suffixUrl, final Callback<String> callback) {
        get(HOST_URL, suffixUrl, callback);
    }
    public static void get(final String baseUrl, final String suffixUrl, final Callback<String> callback) {
        new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(IRetrofit.class)
                .get(suffixUrl)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        callback.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
                        callback.onFailure(call, throwable);
                    }
                });
    }
}
