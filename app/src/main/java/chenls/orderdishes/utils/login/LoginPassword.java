package chenls.orderdishes.utils.login;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class LoginPassword {
    public static void SavePwd(Context context, String userName, String userPwd) {
        SharedPreferences sp = context.getSharedPreferences("login_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName", userName);
        editor.putString("userPwd", userPwd);
        editor.commit();
    }

    public static Map<String, String> GetPwd(Context context) {
        SharedPreferences sp = context.getSharedPreferences("login_config", Context.MODE_PRIVATE);
        Map<String, String> map = new HashMap<>();
        map.put("userName", sp.getString("userName", null));
        map.put("userPwd", sp.getString("userPwd", null));
        return map;
    }
}
