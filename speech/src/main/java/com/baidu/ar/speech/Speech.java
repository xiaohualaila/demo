/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ar.speech;

import java.util.Map;

import org.json.JSONObject;

import com.baidu.ar.speech.listener.IRecogListener;
import com.baidu.ar.speech.listener.RecogEventAdapter;
import com.baidu.ar.speech.listener.RecogResult;
import com.baidu.ar.speech.listener.SpeechRecogListener;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xgx on 2017/6/9.
 * 声音识别
 */
public class Speech {

    private Context mContext;
    /**
     * SDK 内部核心 EventManager 类
     */
    private EventManager asr;

    private SpeechRecogListener speechRecogListener;

    private int status = -1;

    public Speech(Context context, SpeechRecogListener speechRecogListener) {
        mContext = context;
        asr = EventManagerFactory.create(context, "asr");
        asr.registerListener(adapter);
        this.speechRecogListener = speechRecogListener;

    }

    public void start() {
        asr.send(SpeechConstant.ASR_START, "{}", null, 0, 0);
    }

    /**
     * 提前结束录音等待识别结果。
     */
    public void stop() {
        asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
    }

    /**
     * 取消本次识别，取消后将立即停止不会返回识别结果。
     * cancel 与stop的区别是 cancel在stop的基础上，完全停止整个识别流程，
     */
    public void cancel() {
        if (asr != null) {
            asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        }
    }

    RecogEventAdapter adapter = new RecogEventAdapter(new IRecogListener() {
        @Override
        public void onAsrReady() {
            if (speechRecogListener != null) {
                speechRecogListener.onSpeechRecog(SpeechStatus.READYFORSPEECH, null);
            }
        }

        @Override
        public void onAsrBegin() {
            if (speechRecogListener != null) {
                speechRecogListener.onSpeechRecog(SpeechStatus.BEGINNINGOFSPEECH, null);
            }
        }

        @Override
        public void onAsrEnd() {
            if (speechRecogListener != null) {
                speechRecogListener.onSpeechRecog(SpeechStatus.ENDOFSPEECH, null);
            }
        }

        @Override
        public void onAsrPartialResult(String[] results, RecogResult recogResult) {
            if (speechRecogListener != null && results.length > 0) {
                speechRecogListener.onSpeechRecog(SpeechStatus.PARTIALRESULT, results[0]);
            }
        }

        @Override
        public void onAsrFinalResult(String[] results, RecogResult recogResult) {
            if (speechRecogListener != null && results.length > 0) {
                speechRecogListener.onSpeechRecog(SpeechStatus.RESULT, results[0]);
            }
        }

        @Override
        public void onAsrFinish(RecogResult recogResult) {
        }

        @Override
        public void onAsrFinishError(int errorCode, int subErrorCode, String errorMessage, String descMessage,
                                     RecogResult recogResult) {
            if (errorCode == 4) {
                Toast.makeText(mContext.getApplicationContext(), "appid和appkey的鉴权失败", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            Log.e("speech", "errorCode = " + errorCode + "recogResult = " + subErrorCode);
            if (speechRecogListener != null) {
                speechRecogListener.onSpeechRecog(SpeechStatus.ERROR, errorMessage);
            }
        }

        @Override
        public void onAsrLongFinish() {

        }

        @Override
        public void onAsrVolume(int volumePercent, int volume) {

        }

        @Override
        public void onAsrAudio(byte[] data, int offset, int length) {

        }

        @Override
        public void onAsrExit() {

        }

        @Override
        public void onAsrOnlineNluResult(String nluResult) {

        }

        @Override
        public void onOfflineLoaded() {

        }

        @Override
        public void onOfflineUnLoaded() {

        }
    });
}
