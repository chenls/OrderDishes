package chenls.orderdishes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import chenls.orderdishes.R;
import chenls.orderdishes.utils.CommonUtil;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;


public class LoginDialogFragment extends DialogFragment implements View.OnClickListener {

    private OnLoginFragmentInteractionListener mListener;
    private EditText et_userpwd;
    private EditText et_user_data;
    private ProgressBar progressBar;
    private TextView login;

    public LoginDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_login_dialog, container, false);
        et_user_data = (EditText) view.findViewById(R.id.et_user_data);
        et_userpwd = (EditText) view.findViewById(R.id.et_userpwd);
        login = (TextView) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        TextView register = (TextView) view.findViewById(R.id.register);
        register.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                final String userData = et_user_data.getText().toString().trim();
                final String userPwd = et_userpwd.getText().toString().trim();
                if (TextUtils.isEmpty(userData) || TextUtils.isEmpty(userPwd)) {
                    Toast.makeText(getActivity(), "用户名或密码为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!CommonUtil.checkNetState(getActivity())) {
                    Toast.makeText(getActivity(), "网络不可用！", Toast.LENGTH_SHORT).show();
                    return;
                }
                login.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if (userData.matches("^1\\d{10}$"))
                    loginByPhonePwd(userData, userPwd);
                else
                    loginByUserName(userData, userPwd);
                break;
            case R.id.register:
                mListener.onRegisterButtonPressed();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginFragmentInteractionListener {
        void onLoginSuccess();

        void onRegisterButtonPressed();
    }

    /**
     * 通过手机号登录
     *
     * @param phoneNum 手机号
     * @param userPwd  密码
     */
    private void loginByPhonePwd(String phoneNum, String userPwd) {
        BmobUser.loginByAccount(getContext(), phoneNum, userPwd, new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (user != null) {
                    mListener.onLoginSuccess();
                } else {
//                    toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
                    Toast.makeText(getContext(), "登陆失败:", Toast.LENGTH_SHORT).show();
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 登陆用户
     *
     * @param userName 用户名
     * @param userPwd  密码
     */
    private void loginByUserName(String userName, String userPwd) {
        final BmobUser bu2 = new BmobUser();
        bu2.setUsername(userName);
        bu2.setPassword(userPwd);
        bu2.login(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                mListener.onLoginSuccess();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(getContext(), "登陆失败:" + msg, Toast.LENGTH_SHORT).show();
                login.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
