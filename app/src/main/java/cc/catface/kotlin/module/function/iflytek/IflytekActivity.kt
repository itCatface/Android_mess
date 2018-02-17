package cc.catface.kotlin.module.function.iflytek

import android.os.Bundle
import cc.catface.clibrary.base.BaseActivity
import cc.catface.clibrary.util.extension.d
import cc.catface.iflytek.recognizer.RecognizerT
import cc.catface.iflytek.recognizer.i.IRecognizerListener
import cc.catface.iflytek.tts.TtsT
import cc.catface.kotlin.R
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SynthesizerListener
import kotlinx.android.synthetic.main.activity_iflytek.*

class IflytekActivity : BaseActivity(R.layout.activity_iflytek) {

    var mTtsT:TtsT? = null
    var mRecognizerT: RecognizerT? = null

    val words = "路上只我一个人，背着手踱着。这一片天地好像是我的；我也像超出了平常的自己，到了另一个世界里。我爱热闹，也爱冷静；爱群居，也爱独处。像今晚上，一个人在这苍茫的月下，什么都可以想，什么都可以不想，便觉是个自由的人。白天里一定要做的事，一定要说的话，现 在都可不理。这是独处的妙处，我且受用这无边的荷香月色好了。"


    override fun create() {
        initTtsAndRecognize()

        initEvent()
    }


    private fun initTtsAndRecognize() {
        mTtsT = TtsT.getInstance(applicationContext)    // tts为公有云
        mTtsT?.setSynthesizerListener(object : SynthesizerListener{
            override fun onBufferProgress(p0: Int, p1: Int, p2: Int, p3: String?) {

            }

            override fun onSpeakBegin() {
                d("tts-->onSpeakBegin")
            }

            override fun onSpeakProgress(p0: Int, p1: Int, p2: Int) {
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
            }

            override fun onSpeakPaused() {
            }

            override fun onSpeakResumed() {
            }

            override fun onCompleted(p0: SpeechError?) {
                d("tts-->onCompleted")
            }

        })


        mRecognizerT = RecognizerT.getInstance(applicationContext, RecognizerT.USE_PUBLIC_CLOUD)
        mRecognizerT?.setMscListener(object : IRecognizerListener {
            override fun onVolumeChanged(i: Int, bytes: ByteArray?) {
            }

            override fun onBeginOfSpeech() {
                d("onBeginOfSpeech")
            }

            override fun onEndOfSpeech() {
            }

            override fun onResult(result: String?, b: Boolean) {
                d("result is: $result")
                runOnUiThread {
                    tv_recognize_result.text = result
                }
            }

            override fun onError(error: String?) {
            }

            override fun onEvent(i: Int, i1: Int, i2: Int, bundle: Bundle?) {
            }

            override fun onWakeUp(s: String?, i: Int) {
            }

        })
    }

    fun initEvent() {
        bt_start_tts.setOnClickListener {
            TtsT.getInstance(applicationContext).startSynthesizer(words)
        }

        bt_pause_tts.setOnClickListener {
            TtsT.getInstance(applicationContext).pausedSynthesizer()
        }

        bt_stop_tts.setOnClickListener {
            TtsT.getInstance(applicationContext).stopSynthesizer()
        }

        bt_public_recognize.setOnClickListener {
            mRecognizerT?.start()
        }


        bt_tts_msc.setOnClickListener { TtsT.getInstance(applicationContext).startSynthesizer(words) }
        bt_tts_msc_xtts.setOnClickListener {
            d("xtts播报")
            TtsT.getInstance(applicationContext).setParam(SpeechConstant.ENG_TTS, "x")
            TtsT.getInstance(applicationContext).startSynthesizer(words)
        }
        bt_tts_aip.setOnClickListener { TtsT.getInstance(applicationContext).startSynthesizer(words) }
    }
}
