package cc.catface.iflytek.recognizer.init;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.aipsdk.asr.RecognizerListener;
import com.iflytek.aipsdk.asr.RecognizerResult;
import com.iflytek.aipsdk.asr.SpeechRecognizer;
import com.iflytek.aipsdk.common.InitListener;
import com.iflytek.aipsdk.util.SpeechConstant;
import com.iflytek.aipsdk.util.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import cc.catface.iflytek.R;
import cc.catface.iflytek.recognizer.RecognizerT;
import cc.catface.iflytek.recognizer.i.IRecognizer;
import cc.catface.iflytek.recognizer.i.IRecognizerListener;


/**
 * Created by wyh
 *
 * @desc 私有云识别
 */

public class RecognizerPrivate implements IRecognizer {

    private static RecognizerPrivate mInstance;
    private SpeechRecognizer mRecognizer;


    /***************************** single instance & init recognizer *****************************/
    public static RecognizerPrivate getInstance(Context ctx) {
        if (null == mInstance) {
            synchronized (RecognizerPrivate.class) {
                if (null == mInstance) {
                    mInstance = new RecognizerPrivate(ctx);
                }
            }
        }
        return mInstance;
    }


    private RecognizerPrivate(Context ctx) {
        mRecognizer = SpeechRecognizer.createRecognizer(ctx, new InitListener() {
            @Override public void onInit(int code) {
                System.out.println(RecognizerT.TAG + "recognizer private - init code :: " + code);
            }
        });

        mRecognizer.setParameter(SpeechConstant.PARAM, ctx.getString(R.string.rcg_params));
    }


    /***************************** init and callback listener *****************************/
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override public void onVolumeChanged(int i, byte[] bytes) {
            if (null != mMscListener) {
                mMscListener.onVolumeChanged(i, bytes);
            }
        }

        @Override public void onBeginOfSpeech() {
            if (null != mMscListener) {
                mMscListener.onBeginOfSpeech();
            }
        }

        @Override public void onEndOfSpeech() {
            if (null != mMscListener) {
                mMscListener.onEndOfSpeech();
            }
        }

        @Override public void onResult(RecognizerResult recognizerResult, boolean b) {

            /**
             * always keep in recognizing, if you don't need, stop by this
             */
            if (!flag) mRecognizer.stopListening();

            try {
                String result = new JSONObject(recognizerResult.getResultString()).optString("result").replaceAll("[\\p{Punct}\\p{Space}]+", "");
                System.out.println(RecognizerT.TAG + "recognizer private - onResult :: " + result);
                if (null != mMscListener) {
                    mMscListener.onResult(result, b);
                }
            } catch (JSONException e) {
                System.out.println(RecognizerT.TAG + "recognizer private - onResult error :: " + e.toString());
            }
        }

        @Override public void onError(SpeechError speechError) {
            if (null != mMscListener) {
                mMscListener.onError(speechError.getErrorDescription());
            }

        }

        @Override public void onEvent(int i, int i1, int i2, Bundle bundle) {
            if (null != mMscListener) {
                mMscListener.onEvent(i, i1, i2, bundle);
            }
        }

        /* this callback method only in private cloud recognizer */
        @Override public void onWakeUp(String s, int i) {
            if (null != mMscListener) {
                mMscListener.onWakeUp(s, i);
            }
        }
    };


    @Override public String parse(String json) {
        // no need in private cloud recognizer
        return null;
    }

    private boolean flag = false;

    @Override public void setContinueRecognize(boolean flag) {
        this.flag = flag;
    }

    @Override public void start() {
        mRecognizer.startListening(mRecognizerListener);
    }

    @Override public void stop() {
        if (null != mRecognizer) mRecognizer.stopListening();
    }


    /**
     * release recognizer
     */
    @Override public void release() {
        if (null != mRecognizer) {
            mRecognizer.cancel();
            mRecognizer.destroy();
        }
    }


    /**
     * add listener
     */
    private IRecognizerListener mMscListener;

    @Override public void setMscListener(IRecognizerListener mscListener) {
        mMscListener = mscListener;
    }
}
