package cc.catface.clibrary.util.net.http.src.progress;

/**
 * Created by wyh
 */

public interface HttpCallback {

    int STATE_PROGRESS = 50;
    int STATE_SUC= 0;
    int STATE_ERR = -1;

    void onProgress(String progress);
    void onSuc(String result);
    void onErr(String error);
}
