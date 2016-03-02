package chenls.orderdishes.utils.ConsigneeMessage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class ConsigneeMessageUtil {
    private static final String CONSIGNEE_MESSAGE = "consignee_message";

    public static void SaveMessage(Context context, String name, String tel, String address) {
        SharedPreferences sp = context.getSharedPreferences(CONSIGNEE_MESSAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name);
        editor.putString("tel", tel);
        editor.putString("address", address);
        editor.commit();
    }

    public static Map<String, String> GetMessage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONSIGNEE_MESSAGE, Context.MODE_PRIVATE);
        Map<String, String> map = new HashMap<>();
        map.put("name", sp.getString("name", null));
        map.put("tel", sp.getString("tel", null));
        map.put("address", sp.getString("address", null));
        return map;
    }
}
