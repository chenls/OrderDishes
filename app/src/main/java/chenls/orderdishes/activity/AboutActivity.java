package chenls.orderdishes.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import chenls.orderdishes.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText(getString(R.string.version, getVersion(this)));
    }

    /**
     * 获取版本号
     *
     * @param context context
     * @return 版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return context.getString(R.string.get_version_fail);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        final MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider provider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(item);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content));
        provider.setShareIntent(intent);
        return true;
    }
}
