/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.aier.ardemo.module;

import java.util.HashMap;

import com.baidu.ar.ARController;
import com.aier.ardemo.arview.ARControllerManager;
import com.baidu.ar.tts.TTSCallback;
import com.baidu.ar.tts.TTSStatus;
import com.baidu.ar.tts.Tts;
import com.baidu.ar.arplay.util.MsgParamsUtil;
import com.baidu.ar.util.UiThreadUtil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * tts控制器
 * 参考case:
 */
public class TtsController {

    /**
     * 用户自定义设置appId，appKey，secretKey
     * http://ai.baidu.com/sdk#tts
     */
    protected String APPID = "com.baidu.speech.APP_ID";
    protected String APIKEY = "com.baidu.speech.API_KEY";
    protected String SECRETKEY = "com.baidu.speech.SECRET_KEY";

    private Tts mTts;

    private Context context;

    public TtsController(Context context) {
        this.context = context;
        mTts = new Tts(context, getMetaData(APPID), getMetaData(APIKEY), getMetaData(SECRETKEY));
    }

    public void parseMessage(HashMap<String, Object> luaMsg) {
        if (null != luaMsg) {
            int id = MsgParamsUtil.obj2Int(luaMsg.get("id"), -1);
            switch (id) {
                case MsgType.MSG_TYPE_TTS_SPEAK:
                    startTts(luaMsg);
                    break;
                case MsgType.MSG_TYPE_TTS_STOP:
                    stop();
                    break;
                case MsgType.MSG_TYPE_TTS_PAUSE:
                    pause();
                    break;
                case MsgType.MSG_TYPE_TTS_RESUME:
                    resume();
                    break;
            }

        }
    }

    public void startTts(HashMap<String, Object> luaMsg) {
        String text = (String) luaMsg.get("tts");
        String speaker = String.valueOf(luaMsg.get("speaker"));
        String speed = String.valueOf(luaMsg.get("speed"));
        String volume = String.valueOf(luaMsg.get("volume"));
        if (text != null) {
            mTts.setup(speaker, speed, volume);
            mTts.speak(text, new TTSCallback() {
                @Override
                public void onTtsStarted() {
                    // TODO: 2018/5/23 开始播放
                    HashMap hashMap = new HashMap<>();
                    hashMap.put("id", MsgType.MSG_TYPE_TTS_SPEAK);
                    hashMap.put("status", TTSStatus.BEGINNINGOFTTS);
                    sendMessageToLua(hashMap);
                }

                @Override
                public void onTtsFinish() {
                    // TODO: 2018/5/23 播放完成
                    HashMap hashMap = new HashMap<>();
                    hashMap.put("id", MsgType.MSG_TYPE_TTS_SPEAK);
                    hashMap.put("status", TTSStatus.ENDOFTTS);
                    sendMessageToLua(hashMap);
                }

                @Override
                public void onTtsError(int errorCode) {
                    if (errorCode == -4 || errorCode == -8) {
                        UiThreadUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context.getApplicationContext(), "appid和appkey的鉴权失败", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                        return;
                    }
                    // TODO: 2018/5/23 播放异常
                    HashMap hashMap = new HashMap<>();
                    hashMap.put("id", MsgType.MSG_TYPE_TTS_SPEAK);
                    hashMap.put("status", TTSStatus.ERROR);
                    hashMap.put("error_code", errorCode);
                    sendMessageToLua(hashMap);
                }
            });
        }
    }

    public void stop() {
        mTts.stop();
    }

    public void pause() {
        mTts.pause();
    }

    public void resume() {
        mTts.resume();
    }

    public void release() {
        mTts.release();
        context = null;
    }

    /**
     * 发送tts播放状态给lua
     *
     * @param hashMap
     */
    private void sendMessageToLua(HashMap hashMap) {
        ARController controller = ARControllerManager.getInstance(context).getArController();
        if (null != controller) {
            ARControllerManager.getInstance(context).getArController().sendMessage2Lua(hashMap);
        }
    }

    public String getMetaData(String key) {
        String value = null;
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager()
                    .getApplicationInfo(context.getApplicationContext().getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (null != info) {
            value = info.metaData.getString(key);
        }
        return value;
    }
}
