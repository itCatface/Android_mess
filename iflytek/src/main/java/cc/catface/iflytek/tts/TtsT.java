package cc.catface.iflytek.tts;

import android.content.Context;

import com.iflytek.cloud.SynthesizerListener;

import cc.catface.iflytek.tts.init.TtsImpl;


public class TtsT {

    private boolean IS_TTS_ON = true;

    private static TtsT mInstance;

    public static TtsT getInstance(Context ctx) {
        if (null == mInstance) {
            synchronized (TtsT.class) {
                if (null == mInstance) {
                    mInstance = new TtsT(ctx);
                }
            }
        }
        return mInstance;
    }


    private TtsT(Context ctx) {
        mTtsImpl = new TtsImpl(ctx);
    }


    private TtsImpl mTtsImpl;

    private SynthesizerListener mSynthesizerListener;


    public void setParam(String key, String value) {
        mTtsImpl.initParam(key, value);
    }

    public void setSynthesizerListener(SynthesizerListener synthesizerListener) {
        mSynthesizerListener = synthesizerListener;
    }

    public void destory() {
        mTtsImpl.destroy();
    }

    public void setPlayConfig(boolean isPlayer) {
        IS_TTS_ON = isPlayer;
    }

    public void startSynthesizer(String content) {
        if (IS_TTS_ON) mTtsImpl.startSynthesizer(content, mSynthesizerListener);

    }

    public void stopSynthesizer() {
        if (IS_TTS_ON) mTtsImpl.stopSynthesizer();

    }

    public void pausedSynthesizer() {
        if (IS_TTS_ON) mTtsImpl.pausedSynthesizer();

    }

    public void resumedSynthesizer() {
        if (IS_TTS_ON) mTtsImpl.resumedSynthesizer();

    }

    public boolean isSpeaking() {
        return mTtsImpl.isSpeaking();
    }
}
