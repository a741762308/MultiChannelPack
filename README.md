:running:【Android多渠道打包与升级】:running:
====
#配置【本例集成umeng、360、百度、小米】
####详情可以参考 http://www.jianshu.com/p/5993310dd643?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io
##manifest
```java
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.GET_TASKS" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.READ_LOGS" />
    <uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="com.xiaomi.market.sdk.UPDATE" />

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
##libs
###添加如下jar（请自行到各应用市场下载lib）
![](https://github.com/a741762308/MultiChannelPack/blob/master/sreenshot/libs.png)
## build.grade
###将百度升级的sdk library导入Module（也可将lib与资源文件拷贝）
```java
dependencies {
   compile project(':bDIntegrationSDK')
 }
```

```java
android {

  buildTypes {
        release {
            minifyEnabled false//是否混淆
            zipAlignEnabled true
            shrinkResources true//移除未使用的资源文件
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            applicationVariants.all { variant ->
                variant.outputs.each { output ->
                    def outputFile = output.outputFile;
                    if (outputFile != null && outputFile.name.endsWith('.apk')) {
                        File outputDirectory = new File(outputFile.parent);
                        def fileName
                        if (variant.buildType.name == "release") {
                            fileName = "appName_v${defaultConfig.versionName}_${packageTime()}_${variant.productFlavors[0].name}.apk"
                        } else {
                            fileName = "appName_v${defaultConfig.versionName}_${packageTime()}_beta.apk"
                        }
                        output.outputFile = new File(outputDirectory, fileName)
                    }
                }
            }
        }
    }
 defaultConfig {
        buildConfigField "String", "AUTO_TYPE", "\"baidu\""
    }
    productFlavors {
        xiaomi {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "xiaomi"]
            buildConfigField "String", "AUTO_TYPE", "\"xiaomi\""
        }
        c360 {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "360"]
            buildConfigField "String", "AUTO_TYPE", "\"360\""
        }
        umeng {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "umeng"]
            buildConfigField "String", "AUTO_TYPE", "\"umeng\""
        }
        baidu {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "baidu"]
            buildConfigField "String", "AUTO_TYPE", "\"baidu\""
        }
    }
}
```

```java
def packageTime() {
    return new Date().format("yyyy-MM-dd", TimeZone.getTimeZone("UTC"))
}
```
到这里配置已基本完成，接下来就是clean 和rebuild项目
#打包
###点击Build菜单下的Generate Signed APK
选择新建或者使用已存在的签名<br>
![](https://github.com/a741762308/MultiChannelPack/blob/master/sreenshot/generate.png)
<br>成功后在app下会有相应的应用包<br>
![](https://github.com/a741762308/MultiChannelPack/blob/master/sreenshot/out2.png)
<br>到这里渠道打包完成
#渠道升级
打包会生成如下文件<br>
![](https://github.com/a741762308/MultiChannelPack/blob/master/sreenshot/out1.png)
我们在java文件中添加如下代码
```java
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
            //其他升级
        }
```
好了，我们重新打包大功告成！<br>
![](https://github.com/a741762308/MultiChannelPack/blob/master/sreenshot/out2.png)
#License

    Copyright 2015 a741762308

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

