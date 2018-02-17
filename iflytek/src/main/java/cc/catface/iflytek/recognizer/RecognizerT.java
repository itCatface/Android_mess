package cc.catface.iflytek.recognizer;

import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import cc.catface.iflytek.R;
import cc.catface.iflytek.recognizer.i.IRecognizerListener;
import cc.catface.iflytek.recognizer.init.RecognizerPrivate;
import cc.catface.iflytek.recognizer.init.RecognizerPublic;

/**
 * Created by wyh
 */

public class RecognizerT {

    public static final String TAG = "iflytek";

    private boolean isPublicCloud = false; // default use public cloud recognizer
    public static final boolean USE_PUBLIC_CLOUD = true;
    public static final boolean USE_PRIVATE_CLOUD = false;


    private static RecognizerPublic mRecognizerPublic;
    private static RecognizerPrivate mRecognizerPrivate;

    private static RecognizerT mInstance;

    public static RecognizerT getInstance(Context ctx, boolean isPublicCloud) {

        if (null == mInstance) {
            synchronized (RecognizerT.class) {
                if (null == mInstance) {
                    mInstance = new RecognizerT(ctx, isPublicCloud);
                }
            }
        }
        return mInstance;
    }


    /**
     * init public/private cloud recognizer
     */
    public static void init(Context ctx) {
        /* init of public cloud recognizer */
        StringBuffer param = new StringBuffer();
        param.append("appid=" + R.string.app_id);
        param.append(",");
        param.append("lib_name=msc");
        param.append(",");
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(ctx, param.toString());
        System.out.println(TAG + "首次初始化公有云成功");

        /* init of private cloud recognizer */
        com.iflytek.aipsdk.common.SpeechUtility.createUtility(ctx, "ca_path=ca.jet,res=0");
        System.out.println(TAG + "首次初始化私有云成功");
    }


    private RecognizerT(Context ctx, boolean isPublicCloud) {
        this.isPublicCloud = isPublicCloud;

        if (this.isPublicCloud && null == mRecognizerPublic) {
            mRecognizerPublic = RecognizerPublic.getInstance(ctx);
            System.out.println(RecognizerT.TAG + "recognizer :: 公有云语音识别初始化成功...");
        } else if (null == mRecognizerPrivate) {
            mRecognizerPrivate = RecognizerPrivate.getInstance(ctx);
            System.out.println(RecognizerT.TAG + "recognizer :: 私有云语音识别初始化成功...");
        }
    }


    public void start() {
        if (isPublicCloud && null != mRecognizerPublic) mRecognizerPublic.start();
        else if (null != mRecognizerPrivate) mRecognizerPrivate.start();
    }


    public void stop() {
        if (isPublicCloud && null != mRecognizerPublic) mRecognizerPublic.stop();
        else if (null != mRecognizerPrivate) mRecognizerPrivate.stop();
    }


    public void setMscListener(IRecognizerListener listener) {
        if (isPublicCloud && null != mRecognizerPublic) mRecognizerPublic.setMscListener(listener);
        else if (null != mRecognizerPrivate) mRecognizerPrivate.setMscListener(listener);
    }
}
