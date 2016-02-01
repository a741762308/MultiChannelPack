:running:【Android多渠道打包与升级】:running:
====
#配置【本例集成umeng、360、百度、小米】
####详情可以参考 http://www.jianshu.com/p/5993310dd643?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io
##manifest
```java
       <!--BD-->
        <meta-data
            android:name="BDAPPID"
            android:value="3067515" /><!--百度应用市场的appid-->
        <meta-data
            android:name="BDAPPKEY"
            android:value="f3Os4GAOqxgm79GqbnkT9L8T" /><!--appkey-->

        <activity
            android:name="com.baidu.autoupdatesdk.ConfirmDialoigActivity"
            android:exported="false"
            android:screenOrientation="sensor" />
        <receiver
            android:name="com.baidu.autoupdatesdk.receiver.BDBroadcastReceiver"
            android:exported="false">
            <intent-filter>
                <action android:name="com.baidu.autoupdatesdk.ACTION_NEW_UPDATE" />
                <action android:name="com.baidu.autoupdatesdk.ACTION_DOWNLOAD_COMPLETE" />
                <action android:name="com.baidu.autoupdatesdk.ACTION_NEW_AS" />
                <action android:name="com.baidu.autoupdatesdk.ACTION_AS_DOWNLOAD_COMPLETE" />
            </intent-filter>
        </receiver>

        <!--UM-->
        <meta-data
            android:name="UMENG_APPKEY"
            android:value="4f83c5d852701564c0000011" /><!--友盟的appkey-->
        <meta-data
            android:name="UMENG_CHANNEL"
            android:value="${UMENG_CHANNEL_VALUE}" />

        <activity
            android:name="com.umeng.update.UpdateDialogActivity"
            android:theme="@android:style/Theme.Translucent.NoTitleBar"></activity>
        <service
            android:name="com.umeng.update.net.DownloadingService"
            android:process=":DownloadingService" />

        <!--360-->
        <service
            android:name="com.qihoo.appstore.updatelib.CheckUpdateService"
            android:exported="false" />

        <activity
            android:name="com.qihoo.appstore.updatelib.CheckUpdateAcitivty"
            android:exported="false"
            android:theme="@android:style/Theme.Translucent" />

        <!--小米-->
        <receiver android:name="com.xiaomi.market.sdk.DownloadCompleteReceiver">
            <intent-filter>
                <action android:name="android.intent.action.DOWNLOAD_COMPLETE" />
            </intent-filter>
        </receiver>
```
