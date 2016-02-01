package com.jsqix.multichannelpack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.qihoo.appstore.updatelib.UpdateManager;
import com.umeng.update.UmengUpdateAgent;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;

public class MainActivity extends AppCompatActivity {
    String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ("baidu".equalsIgnoreCase(BuildConfig.AUTO_TYPE)) {
            BDAutoUpdateSDK.silenceUpdateAction(this);//百度静默更新
            Log.e(TAG, "baidu更新");
        } else if ("360".equalsIgnoreCase(BuildConfig.AUTO_TYPE)) {
            UpdateManager.setTestMode(2);//测试模式 1有更新 2有更新并省流量
            UpdateManager.setDebug(true);//打印日志
            UpdateManager.checkUpdate(this);
            Log.e(TAG, "360更新");
        } else if ("xiaomi".equalsIgnoreCase(BuildConfig.AUTO_TYPE)) {
            XiaomiUpdateAgent.update(this);
            Log.e(TAG, "小米更新");
        } else if ("umeng".equalsIgnoreCase(BuildConfig.AUTO_TYPE)) {
            UmengUpdateAgent.update(this);
            Log.e(TAG, "umeng更新");
        } else {

        }
    }

}
