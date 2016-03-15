package chenls.orderdishes;

import android.app.Application;
import android.content.Context;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.pgyersdk.crash.PgyCrashManager;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

    public static final String APP_ID = "3f3105ded1ca7e96d3fc71a853b60c63";

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(getApplicationContext(), APP_ID);
        initConfig(getApplicationContext());
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
