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
import chenls.orderdishes.utils.login.LoginHttp;
import chenls.orderdishes.utils.login.LoginPassword;


public class LoginDialogFragment extends DialogFragment implements View.OnClickListener {

    private OnLoginFragmentInteractionListener mListener;
    private EditText et_userpwd;
    private EditText et_username;
    private ProgressBar progressBar;
    private TextView login;

    public LoginDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_login_dialog, container, false);
        et_username = (EditText) view.findViewById(R.id.et_username);
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
                final String userName = et_username.getText().toString().trim();
                final String userPwd = et_userpwd.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
                    Toast.makeText(getActivity(), "用户名或密码为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!CommonUtil.checkNetState(getActivity())) {
                    Toast.makeText(getActivity(), "网络不可用！", Toast.LENGTH_SHORT).show();
                    return;
                }
                login.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                //Login
                new Thread() {
                    @Override
                    public void run() {
                        final String re = LoginHttp.loginPost(userName, userPwd);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //TODO 修改登录功能
                                if ("网络超时".equals(re)) {
                                    LoginPassword.SavePwd(getActivity(), userName, userPwd);
                                    mListener.onLoginSuccess();
                                } else {
                                    Toast.makeText(getActivity(), re, Toast.LENGTH_SHORT).show();
                                    login.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }.start();
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
}
