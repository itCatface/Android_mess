package cc.catface.iflytek.recognizer.init;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import cc.catface.iflytek.recognizer.RecognizerT;
import cc.catface.iflytek.recognizer.i.IRecognizer;
import cc.catface.iflytek.recognizer.i.IRecognizerListener;

/**
 * Created by wyh
 *
 * @desc 公有云识别
 */

public class RecognizerPublic implements IRecognizer {


    private static RecognizerPublic mInstance;
    private SpeechRecognizer mRecognizer;


    /***************************** single instance & init recognizer *****************************/
    public static RecognizerPublic getInstance(Context ctx) {
        if (null == mInstance) {
            synchronized (RecognizerPublic.class) {
                if (null == mInstance) {
                    mInstance = new RecognizerPublic(ctx);
                }
            }
        }
        return mInstance;
    }


    private RecognizerPublic(Context ctx) {
        mRecognizer = SpeechRecognizer.createRecognizer(ctx, new InitListener() {
            @Override public void onInit(int code) {
                System.out.println(RecognizerT.TAG + "recognizer public - init code :: " + code);
            }
        });
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
            mRecognizer.stopListening();

            // need remove all symbols in the beginning
            String result = parse(recognizerResult.getResultString()).replaceAll("[\\p{Punct}\\p{Space}]+", "");
            System.out.println(RecognizerT.TAG + "recognizer public - onResult :: " + result);
            if (null != mMscListener) {
                mMscListener.onResult(result, b);
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
    };


    @Override public String parse(String json) {
        // parse the result of public cloud recognizer
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
                /**
                 * if need all array result, then open it
                 */
                /*for(int j = 0; j < items.length(); j++)
                {
					JSONObject obj = items.getJSONObject(j);
					ret.append(obj.getString("w"));
				}*/
            }
        } catch (Exception e) {
            System.out.println(RecognizerT.TAG + "recognizer public - onResult error :: " + e.toString());
        }
        return ret.toString();
    }

    @Override public void setContinueRecognize(boolean flag) {
        // no need in public cloud recognizer
    }

    @Override public void start() {
        mRecognizer.startListening(mRecognizerListener);
    }

    @Override public void stop() {
        if (null != mRecognizer) mRecognizer.stopListening();
    }


    /**
     * release source
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
