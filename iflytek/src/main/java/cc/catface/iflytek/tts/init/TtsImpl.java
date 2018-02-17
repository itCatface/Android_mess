package cc.catface.iflytek.tts.init;

import android.content.Context;
import android.media.AudioManager;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;


public class TtsImpl {

    private Context mCtx;   // 上下文
    private SpeechSynthesizer mTts; // SpeechSynthesizer对象
    private String mVoicer = "jiajia";  // 默认发音人

    public TtsImpl(Context context) {
        mCtx = context;
        mTts = SpeechSynthesizer.createSynthesizer(mCtx, new InitListener() {
            @Override public void onInit(int code) {
                if (ErrorCode.SUCCESS != code) System.out.println("初始化语音合成失败，错误码：" + code);
            }
        });


        initParam();
    }

    /**
     * 设置语音合成参数
     */
    private void initParam() {
        //设置引擎类型
        initParam(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // TYPE_LOCAL
        //设置发音人资源路径
        initParam(ResourceUtil.TTS_RES_PATH, getResourcePath(mVoicer));
        //设置发音人
        initParam(SpeechConstant.VOICE_NAME, mVoicer);

        // XTTS
        initParam(SpeechConstant.ISE_ENT, "x");
        initParam(SpeechConstant.VOICE_NAME, "xiaoxue");


        //设置语速
//        initParam(SpeechConstant.SPEED, "55");
        initParam(SpeechConstant.VOLUME, "60");
        // 设置播放器音频流类型
        initParam(SpeechConstant.STREAM_TYPE, String.valueOf(AudioManager.STREAM_MUSIC));
        //设置合成音频保存位置（可自定义保存位置） ，保存在“./sdcard/iflytek.pcm”
//		initParam(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
    }

    /**
     * local tts resource
     */
    private String getResourcePath(String voicer) {
        StringBuffer sb = new StringBuffer();
        sb.append(ResourceUtil.generateResourcePath(mCtx, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        sb.append(";");
        sb.append(ResourceUtil.generateResourcePath(mCtx, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + voicer + ".jet"));
        return sb.toString();
    }


    /* ----------------------- 暴露的公共方法 ----------------------- */
    public void initParam(String key, String value) {
        if (mTts != null) mTts.setParameter(key, value);
    }

    public void startSynthesizer(String content, SynthesizerListener synthesizerListener) {
        int code = 0;
        if (null != mTts) code = mTts.startSpeaking(content, synthesizerListener);

        if (0 != code) System.out.println("TtsImp -> startSynthesizer error code is: " + code);
    }

    public void stopSynthesizer() {
        if (mTts != null) mTts.stopSpeaking();
    }

    public void pausedSynthesizer() {
        if (mTts != null) mTts.pauseSpeaking();
    }

    public void resumedSynthesizer() {
        if (mTts != null) mTts.resumeSpeaking();
    }

    public boolean isSpeaking() {
        return null != mTts ? mTts.isSpeaking() : false;
    }

    public void destroy() {
        if (null != mTts) {
            mTts.stopSpeaking();
            mTts.destroy();
            mTts = null;
        }
    }
}
