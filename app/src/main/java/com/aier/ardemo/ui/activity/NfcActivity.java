package com.aier.ardemo.ui.activity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseNfcActivity;
import butterknife.ButterKnife;

public class NfcActivity  extends BaseNfcActivity {

    private ImageView iv_back;
    private TextView tv_scan,tv_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        iv_back =  findViewById(R.id.iv_back);
        tv_scan = findViewById(R.id.tv_scan);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("NFC芯片读取");
        iv_back.setOnClickListener(v -> finish());
        tv_scan.setOnClickListener(v -> {
            startActivity(new Intent(NfcActivity.this, ScanActivity.class));
            finish();
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        //1.获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //2.获取Ndef的实例
        Ndef ndef = Ndef.get(detectedTag);
        readNfcTag(intent);

    }

    protected int getLayout() {
        return R.layout.activity_nfc;
    }

    /**
     * 读取NFC标签文本数据
     */
    private void readNfcTag(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += msgs[i].toByteArray().length;
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    Log.i("sss","ssss textRecord  "+ new String(record.getPayload()));
                    String textRecord = new String(record.getPayload()).trim();
                    WebActivity.startActivity(this,"芯片溯源",textRecord,2);
                    finish();

                }
            } catch (Exception e) {
            }
        }
    }

}
