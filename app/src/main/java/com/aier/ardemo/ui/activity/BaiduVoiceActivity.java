package com.aier.ardemo.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.adapter.ChatAdapter;
import com.aier.ardemo.baiduSpeechRecognition.AsrFinishJsonData;
import com.aier.ardemo.baiduSpeechRecognition.AsrPartialJsonData;
import com.aier.ardemo.bean.GloData;
import com.aier.ardemo.bean.GroupChatDB;
import com.aier.ardemo.bean.Person;
import com.aier.ardemo.dialog.SpeekView;
import com.aier.ardemo.netapi.HttpApi;
import com.aier.ardemo.netapi.URLConstant;
import com.aier.ardemo.ui.base.BaseActivity;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaiduVoiceActivity extends BaseActivity implements EventListener {

    private static final String TAG = "BaiduVoiceActivity";

    @BindView(R.id.btnStartRecord)
    TextView btnStartRecord;
    @BindView(R.id.ls)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.sv)
    SpeekView speekView;
    private EventManager asr;

    private String final_result;

    //当前群组的聊天记录
    private List<GroupChatDB> list = new ArrayList<>();
    private ChatAdapter adapter;
    //当前用户信息

    private String mTextMessage;
    public Person person;//个人信息
    private LinearLayoutManager linearLayoutManager;
    private boolean isStop = true;

    //百度语音合成
    protected String appId = "16021623";//百度语音申请的appID
    protected String appKey = "ZI0SDxDIWvtMnHvs2scKXC2x";//appKey
    protected String secretKey = "ncNvjMB2QpFm6eaU9UGjkNxnk4oPxlIk";//secretKey
    private SpeechSynthesizer synthesizer;//语音合成对象

    @Override
    protected void initDate(Bundle savedInstanceState) {
        tv_title.setText("智能语音助手");
        person = GloData.getPerson();
        list = getData();
        if (list != null && list.size() == 0) {
            GroupChatDB userGroupChat = new GroupChatDB();
            userGroupChat.username = "羽白";
            userGroupChat.uid = "112";
            userGroupChat.createtime = getDate();
            mTextMessage = "您好,我是羽白人工智能机器人小羽。";
            userGroupChat.message = mTextMessage;
            userGroupChat.save();
            list = getData();
        }
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        if(list.size()>8){
            linearLayoutManager.setStackFromEnd(true);
        }
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ChatAdapter(list,this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        //百度语音合成
        synthesizer = SpeechSynthesizer.getInstance(); //设置当前的Context
        synthesizer.setContext(this);
        synthesizer.setAppId(appId);
        synthesizer.setApiKey(appKey, secretKey);
        synthesizer.auth(TtsMode.ONLINE); // 纯在线 // 设置参数
//        synthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, SpeechSynthesizer.AUDIO_ENCODE_PCM);
//        synthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, SpeechSynthesizer.AUDIO_BITRATE_PCM);
//        synthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        synthesizer.initTts(TtsMode.ONLINE);//纯在线模式
    }

    @Override
    protected void initViews() {
        asr = EventManagerFactory.create(this, "asr");
        asr.registerListener(this); //  EventListener 中 onEvent方法
        btnStartRecord.setOnClickListener(v -> {
            if(isStop){
                synthesizer.stop();
                start();
            }else {
                stop();
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_baidu;
    }

    List<GroupChatDB> getData() {
        List<GroupChatDB> data= SQLite.select().from(GroupChatDB.class).queryList();
        return data;
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
            // 引擎准备就绪，可以开始说话
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN)) {
            // 检测到用户的已经开始说话
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_END)) {
            // 检测到用户的已经停止说话
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            // 临时识别结果, 长语音模式需要从此消息中取出结果
            parseAsrPartialJsonData(params);
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
            // 识别结束， 最终识别结果或可能的错误
            btnStartRecord.setEnabled(true);
            btnStartRecord.setBackground(getResources().getDrawable(R.drawable.speak_btn_white));
            btnStartRecord.setText("点击说话");
            speekView.setVisibility(View.GONE);
            isStop = true;
            asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
            Log.d(TAG, "Result Params:"+params);
            parseAsrFinishJsonData(params);
        }
    }

    private void start() {
        speekView.setVisibility(View.VISIBLE);
        btnStartRecord.setText("停止说话");
        btnStartRecord.setBackground(getResources().getDrawable(R.drawable.speak_btn_gray));
        isStop = false;
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START;
        params.put(SpeechConstant.PID, 1536); // 默认1536
        params.put(SpeechConstant.DECODER, 0); // 纯在线(默认)
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN); // 语音活动检测
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 2000); // 不开启长语音。开启VAD尾点检测，即静音判断的毫秒数。建议设置800ms-3000ms
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, false);// 是否需要语音音频数据回调
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);// 是否需要语音音量数据回调
        String json = null; //可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
    }

    private void stop() {
        btnStartRecord.setEnabled(false);
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        synthesizer.release();
    }

    private void parseAsrPartialJsonData(String data) {
        Log.d(TAG, "parseAsrPartialJsonData data:"+data);
        Gson gson = new Gson();
        AsrPartialJsonData jsonData = gson.fromJson(data, AsrPartialJsonData.class);
        String resultType = jsonData.getResult_type();
        Log.d(TAG, "resultType:"+resultType);
        if(resultType != null && resultType.equals("final_result")){
            final_result = jsonData.getBest_result();
            Log.i("sss","解析结果：" + final_result);

        }
    }

    private void parseAsrFinishJsonData(String data) {
        Log.d(TAG, "parseAsrFinishJsonData data:"+data);
        Gson gson = new Gson();
        AsrFinishJsonData jsonData = gson.fromJson(data, AsrFinishJsonData.class);
        String desc = jsonData.getDesc();
        if(desc !=null && desc.equals("Speech Recognize success.")){
            Log.i("sss","解析结果:" + final_result);
            if(final_result!=null){
                showMessage(final_result,person.getId(),"",person.getUsername(),person.getHeadimg());
                getQueryData(final_result);
            }
        }else{
            String errorCode = "\n错误码:" + jsonData.getError();
            String errorSubCode = "\n错误子码:"+ jsonData.getSub_error();
            String errorResult = errorCode + errorSubCode;
            Log.i("sss","解析错误,原因是:" + desc + "\n" + errorResult);
        }
    }

    private void showMessage(String data,String uid,String img,String name,String headImg) {
        GroupChatDB userGroupChat =new GroupChatDB();
        userGroupChat.createtime = getDate();
        userGroupChat.message = data;
        userGroupChat.username = name;
        userGroupChat.headimg = headImg;
        userGroupChat.image = img;
        userGroupChat.uid = uid;
        userGroupChat.save();
        list.add(userGroupChat);
        adapter.notifyDataSetChanged();
        if(list.size()>8){
            mRecyclerView.scrollToPosition(adapter.getItemCount()-1);
        }
    }

    private void getQueryData(String data) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLConstant.YUYIN_URL)
                .build();
        HttpApi service = retrofit.create(HttpApi.class);
        Call<ResponseBody> call = service.getYuyinDataForQuery("YUBAI",data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 已经转换为想要的类型了
                try {
                    String str = response.body().string();
                    if(str!=null){
                        JSONObject obj =new JSONObject(str);
                        Log.i("sss",obj.toString());
                        String result = obj.optString("result");
                        String image = obj.optString("imagereply");
                        showMessage(result,"112",image,"羽白","");
                        synthesizer.stop();
                        speak(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    //获取日期
    private String getDate() {
        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return myDateFormat.format(curDate);
    }

    /**
     * 播放语音。语音合成
     */
    private void speak(String content) {
        int result = synthesizer.speak(content);
        Log.d("test", "speak" + result);
    }
    /**
     * 暂停播放。仅调用speak后生效 语音合成
     */
    private void pause() {
        int result = synthesizer.pause();
        Log.d("test", "pause:" + result);
    }

}
