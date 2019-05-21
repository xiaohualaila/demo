package com.aier.ardemo.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import butterknife.OnClick;

public class TestActivity extends BaseActivity {
    private static final String TAG = "TestActivity";
    protected String appId = "16021623";//百度语音申请的appID
    protected String appKey = "ZI0SDxDIWvtMnHvs2scKXC2x";//appKey
    protected String secretKey = "ncNvjMB2QpFm6eaU9UGjkNxnk4oPxlIk";//secretKey
    private SpeechSynthesizer synthesizer;//语音合成对象


    @Override
    protected void initViews(){
        //获取实例
         synthesizer = SpeechSynthesizer.getInstance(); //设置当前的Context
         synthesizer.setContext(this);
//         synthesizer.setSpeechSynthesizerListener(new SpeechSynthesizerListener() {
//             @Override
//             public void onSynthesizeStart(String s) {
//
//             }
//
//             @Override
//             public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
//
//             }
//
//             @Override
//             public void onSynthesizeFinish(String s) {
//
//             }
//
//             @Override
//             public void onSpeechStart(String s) {
//
//             }
//
//             @Override
//             public void onSpeechProgressChanged(String s, int i) {
//
//             }
//
//             @Override
//             public void onSpeechFinish(String s) {
//
//             }
//
//             @Override
//             public void onError(String s, SpeechError speechError) {
//                  Log.i(TAG,s);
//             }
//         });
         synthesizer.setAppId(appId);
         synthesizer.setApiKey(appKey, secretKey);
         synthesizer.auth(TtsMode.ONLINE); // 纯在线 // 设置参数
//        synthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, SpeechSynthesizer.AUDIO_ENCODE_PCM);
//        synthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, SpeechSynthesizer.AUDIO_BITRATE_PCM);
//        synthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        synthesizer.initTts(TtsMode.ONLINE);//纯在线模式


    }

    @Override
    protected void initDate(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_test;
    }


    @OnClick({R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                speak("123456");
                Log.i("sss","sssssssssssssssssssss");
                break;
        }
    }

    /**
     * 播放。
     */
    private void speak(String content) {
         int result = synthesizer.speak(content);
         Log.d("test", "speak" + result);
     }
    /**
     * 暂停播放。仅调用speak后生效
     */
    private void pause() {
         int result = synthesizer.pause();
         Log.d("test", "pause:" + result);
     }

    @Override
    protected void onDestroy() {
        synthesizer.release();
        Log.i(TAG, "释放资源成功");
        super.onDestroy();
    }
}
