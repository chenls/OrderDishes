package chenls.orderdishes.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;

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
        boolean netState = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        netState = true;
                        break;
                    }
                }
            }
        }
        return netState;
    }
}
