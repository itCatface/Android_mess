package cc.catface.clibrary.util.net.http.okhttp.full;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

public class OHHandler extends Handler {

    static final int MESSAGE_REQUEST_SUCCESS = 1;   // request server success
    static final int MESSAGE_REQUEST_FAILURE = 2;   // request server failure


    OHHandler(Looper looper) {
        super(looper);
    }


    @Override public void handleMessage(Message message) {
        OHCallback callback;
        String result;
        switch (message.what) {
            case MESSAGE_REQUEST_SUCCESS:
                callback = (OHCallback) message.obj;
                result = message.getData().getString("result");
                if (null != callback) {
                    callback.onSuc(result);
                }
                break;

            case MESSAGE_REQUEST_FAILURE:
                callback = (OHCallback) message.obj;
                result = message.getData().getString("result");
                if (null != callback && !TextUtils.isEmpty(result)) {
                    callback.onErr(result);
                }
                break;
            default:
                break;
        }
    }
}
