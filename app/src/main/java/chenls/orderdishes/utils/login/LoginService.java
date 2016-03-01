package chenls.orderdishes.utils.login;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import chenls.orderdishes.utils.CommonUtil;

public class LoginService {
    public static String loginPost(String userName, String userPwd) {
        String path = "http://192.168.191.1:8080/Login/LoginServlet";
        try {

            String data = "account=" + URLEncoder.encode(userName, "utf-8") + "&password="
                    + URLEncoder.encode(userPwd, "utf-8");
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64)" +
                    " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            connection.setRequestProperty("Content-Length", data.length() + "");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            int code = connection.getResponseCode();
            if (code == 200) {
                String re = CommonUtil.inputStreamToString(connection.getInputStream());
                return re;
            } else {
                String re = CommonUtil.inputStreamToString(connection.getInputStream());
                return re;
//                return "登录失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "网络超时";
    }
}
