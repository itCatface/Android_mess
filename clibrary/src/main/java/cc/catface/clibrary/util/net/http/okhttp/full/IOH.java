package cc.catface.clibrary.util.net.http.okhttp.full;

import java.util.Map;

import okhttp3.Response;

/**
 * Created by wyh
 */

public interface IOH {

    void get(String url, OHCallback callback);

    void get(String url, String json, OHCallback callback);

    void get(String url, Map<String, String> params, OHCallback callback);

    void post(String url, OHCallback callback);

    void post(String url, String json, OHCallback callback);

    void post(String url, Map<String, String> params, OHCallback callback);

    Response post(String url, Map<String, String> params);

    void upload(String url, Map<String, String> params, Map<String, String> fileMap, OHCallback callback);

    void download(String url, String fileDir, String fileName, OHCallback callback);
}
