package chenls.orderdishes.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.utils.login.LoginService;
import chenls.orderdishes.utils.login.LoginPassword;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private CheckBox cb_remember_pwd;
    private EditText et_username;
    private EditText et_userpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        cb_remember_pwd = (CheckBox) findViewById(R.id.cb_remember_pwd);
        cb_remember_pwd.setOnClickListener(this);
        et_username = (EditText) findViewById(R.id.et_username);
        et_userpwd = (EditText) findViewById(R.id.et_userpwd);
        Map<String, String> map = LoginPassword.GetPwd(this);
        if (map != null) {
            et_username.setText(map.get("userName"));
            et_userpwd.setText(map.get("userPwd"));
        }
    }

    @Override
    public void onClick(View view) {
        if (!isConnected())
            return;
        final String userName = et_username.getText().toString().trim();
        final String userPwd = et_userpwd.getText().toString().trim();
        switch (view.getId()) {
            case R.id.bt_login:
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
                    Toast.makeText(this, "用户名或密码为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                rememberPassword(userName, userPwd);
                //Login
                new Thread() {
                    @Override
                    public void run() {
                        final String re = LoginService.loginPost(userName, userPwd);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, re, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
                break;
            case R.id.cb_remember_pwd:
                rememberPassword(userName, userPwd);
                break;
        }
    }

    private void rememberPassword(String userName, String userPwd) {
        if (cb_remember_pwd.isChecked()) {
            LoginPassword.SavePwd(this, userName, userPwd);
        } else {
            LoginPassword.ClearPwd(this);
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        } else {
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
