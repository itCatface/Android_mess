package cc.catface.iflytek.recognizer.i;

import android.os.Bundle;

/**
 * Created by wyh
 */

public interface IRecognizerListener {

    void onVolumeChanged(int i, byte[] bytes);
    void onBeginOfSpeech();
    void onEndOfSpeech();
    void onResult(String result, boolean b);
    void onError(String error);
    void onEvent(int i, int i1, int i2, Bundle bundle);
    void onWakeUp(String s, int i); // only in private cloud recognizer
}
