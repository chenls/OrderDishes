package chenls.orderdishes.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CommonUtil {

    /**
     * 在ViewGroup中根据id进行查找
     *
     * @param vg ViewGroup
     * @param id 如：R.id.tv_name
     * @return View
     */
    public static View findViewInViewGroupById(ViewGroup vg, int id) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            if (v.getId() == id) {
                return v;
            } else {
                if (v instanceof ViewGroup) {
                    return findViewInViewGroupById((ViewGroup) v, id);
                }
            }
        }
        return null;
    }

    public static boolean checkNetState(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        try {
            Toast.makeText(context, "网络不可用！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //是否连接WIFI
    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo.isAvailable();

    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
