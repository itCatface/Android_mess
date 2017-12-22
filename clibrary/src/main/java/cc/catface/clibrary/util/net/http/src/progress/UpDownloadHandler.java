package cc.catface.clibrary.util.net.http.src.progress;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/9/14/014.
 */

public class UpDownloadHandler extends Handler {

    static final int MSG_REQUEST_SUC = 0;
    static final int MSG_REQUEST_ERR = -1;
    static final int MSG_REQUEST_PROGRESS = 50;



    public UpDownloadHandler(Looper looper) {
        super(looper);
    }


    @Override public void handleMessage(Message msg) {
        HttpCallback callback;
        String result;
        switch (msg.what) {
            case MSG_REQUEST_SUC:
                callback = (HttpCallback) msg.obj;
                result = msg.getData().getString("result");
                if (null != callback) {
                    callback.onSuc(result);
                }
                break;

            case MSG_REQUEST_PROGRESS:
                callback = (HttpCallback) msg.obj;
                result = msg.getData().getString("result");
                if (null != callback && !TextUtils.isEmpty(result)) {
                    callback.onProgress(result);
                }
                break;

            case MSG_REQUEST_ERR:
                callback = (HttpCallback) msg.obj;
                result = msg.getData().getString("result");
                if (null != callback && !TextUtils.isEmpty(result)) {
                    callback.onErr(result);
                }
                break;
        }
    }
}
