package chenls.orderdishes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.pgyersdk.crash.PgyCrashManager;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class MyApplication extends Application {

    public static final String APP_ID = "3f3105ded1ca7e96d3fc71a853b60c63";

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化BmobSDK
        Bmob.initialize(getApplicationContext(), APP_ID);
        initConfig(getApplicationContext());
        // 使用推送服务时的初始化操作
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("push", true)) {
            BmobInstallation.getCurrentInstallation(this).save();
            // 启动推送服务
            BmobPush.startWork(this);
        } else {
            // 停止推送服务
            BmobPush.stopWork();
        }
        // 初始化PgySDK
        PgyCrashManager.register(this);
    }

    /**
     * 初始化文件配置
     *
     * @param context context
     */
    public static void initConfig(Context context) {
        BmobConfiguration config = new BmobConfiguration.Builder(context).customExternalCacheDir("dish").build();
        BmobPro.getInstance(context).initConfig(config);
    }

}
