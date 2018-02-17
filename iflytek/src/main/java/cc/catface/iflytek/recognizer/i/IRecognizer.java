package cc.catface.iflytek.recognizer.i;

/**
 * Created by wyh
 */

public interface IRecognizer {

    void start();
    void stop();
    void release();
    String parse(String json);                  // (only in public cloud recognizer)
    void setContinueRecognize(boolean flag);    // (only in private cloud recognizer)
    void setMscListener(IRecognizerListener mscListener);

}
