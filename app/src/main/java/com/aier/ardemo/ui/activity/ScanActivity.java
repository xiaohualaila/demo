package com.aier.ardemo.ui.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aier.ardemo.R;
import com.aier.ardemo.model.WenzhangModel;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.NetUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanActivity extends AppCompatActivity {
    private CaptureManager capture;
    private ImageButton buttonLed;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean bTorch = false;



    private TextView tv_title,tv_nfc;
    private ImageView iv_back;
    protected NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明状态栏
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        barcodeScannerView = initializeContent();
        iv_back = findViewById(R.id.iv_back);
        buttonLed = findViewById(R.id.button_led);
        tv_nfc = findViewById(R.id.tv_nfc);
        /*根据闪光灯状态设置imagebutton*/
        barcodeScannerView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                // buttonLed.setBackground(getResources().getDrawable(R.drawable.image_bg_on));
                bTorch = true;
            }

            @Override
            public void onTorchOff() {
                //  buttonLed.setBackground(getResources().getDrawable(R.drawable.image_bg_off));
                bTorch = false;
            }
        });

        /*开关闪光灯*/
        buttonLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bTorch){
                    barcodeScannerView.setTorchOff();
                } else {
                    barcodeScannerView.setTorchOn();
                }

            }
        });

        capture = new CaptureManager(this, barcodeScannerView);

        capture.initializeFromIntent(getIntent(), savedInstanceState);

        capture.decode();
        mNfcAdapter =  NfcAdapter.getDefaultAdapter(this);
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
        tv_title =findViewById(R.id.tv_title);
        tv_title.setText("扫描二维码");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ifNFCUse()) {
                    return;
                }else {
                    startActivity(new Intent(ScanActivity.this, NfcActivity.class));
                    finish();
                }
            }
        });
    }


    protected int getLayout() {
        return R.layout.activity_scan;
    }

    /**
     * Override to use a different layout.
     *
     * @return the DecoratedBarcodeView
     */
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_scan);
        return (DecoratedBarcodeView)findViewById(R.id.dbv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
        barcodeScannerView.setTorchOff();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.iv_back,R.id.tv_nfc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_nfc:
                if (!ifNFCUse()) {
                    return;
                }else {
                    startActivity(new Intent(this, NfcActivity.class));
                    finish();
                }
                break;
        }

    }

    /**
     * 检测工作,判断设备的NFC支持情况
     *
     * @return
     */
    protected Boolean ifNFCUse() {
        if (mNfcAdapter == null) {
            Toast.makeText(this,"设备不支持NFC！",Toast.LENGTH_LONG).show();
            return false;
        }
        if (mNfcAdapter != null && !mNfcAdapter.isEnabled()) {
            Toast.makeText(this,"请在系统设置中先启用NFC功能",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
