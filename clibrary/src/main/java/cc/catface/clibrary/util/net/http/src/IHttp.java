package cc.catface.clibrary.util.net.http.src;

import java.util.Map;

/**
 * Created by wyh
 */

public interface IHttp {

    String get(String url, Map<String, String> params);

    String post(String url, Map<String, String> params);

}
