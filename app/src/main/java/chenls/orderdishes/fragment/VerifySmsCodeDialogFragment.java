package chenls.orderdishes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import chenls.orderdishes.BmobApplication;
import chenls.orderdishes.R;
import chenls.orderdishes.bean.MyUser;
import chenls.orderdishes.utils.CommonUtil;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;


public class VerifySmsCodeDialogFragment extends DialogFragment implements View.OnClickListener, Chronometer.OnChronometerTickListener {

    private OnVerifySmsCodeFragmentInteractionListener mListener;
    private EditText et_verify_sms_code;
    private ProgressBar progressBar;
    private TextView register;
    private String userName, userPwd, phoneNum;
    private TextView re_send;
    private Chronometer chronometer;

    public VerifySmsCodeDialogFragment() {
    }

    public static VerifySmsCodeDialogFragment newInstance(String param1, String param2, String param3) {
        VerifySmsCodeDialogFragment fragment = new VerifySmsCodeDialogFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        args.putString("param3", param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getContext(), BmobApplication.APP_ID);
        if (getArguments() != null) {
            userName = getArguments().getString("param1");
            userPwd = getArguments().getString("param2");
            phoneNum = getArguments().getString("param3");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_verify_dialog, container, false);
        et_verify_sms_code = (EditText) view.findViewById(R.id.et_verify_sms_code);
        register = (TextView) view.findViewById(R.id.register);
        register.setOnClickListener(this);
        re_send = (TextView) view.findViewById(R.id.re_send);
        re_send.setOnClickListener(this);
        EditText et_phone_number = (EditText) view.findViewById(R.id.et_phone_number);
        et_phone_number.setText(phoneNum);
        requestSmsCode(phoneNum);
        TextView register = (TextView) view.findViewById(R.id.register);
        register.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        chronometer.start();
        chronometer.setOnChronometerTickListener(this);
        return view;
    }

    private int time = 60;

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        if (time > 0)
            chronometer.setText(String.valueOf(time--));
        else {
            re_send.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                final String verify_sms_code = et_verify_sms_code.getText().toString().trim();
                if (TextUtils.isEmpty(verify_sms_code)) {
                    Toast.makeText(getContext(), "输入信息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!CommonUtil.checkNetState(getActivity())) {
                    return;
                }
                register.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                verifySmsCode(phoneNum, verify_sms_code);
                break;
            case R.id.re_send:
                requestSmsCode(phoneNum);
                time = 60;
                chronometer.start();
                chronometer.setText(String.valueOf(time));
                re_send.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVerifySmsCodeFragmentInteractionListener) {
            mListener = (OnVerifySmsCodeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnVerifySmsCodeFragmentInteractionListener {
        void onVerifySmsCodeSuccess();
    }

    /**
     * 请求短信验证码
     *
     * @param number 号码
     */
    private void requestSmsCode(String number) {
        if (!TextUtils.isEmpty(number)) {
            BmobSMS.requestSMSCode(getContext(), number, "注册模板", new RequestSMSCodeListener() {

                @Override
                public void done(Integer smsId, BmobException ex) {
                    if (ex == null) {//验证码发送成功
                        Toast.makeText(getContext(), "验证码发送成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "errorCode = " + ex.getErrorCode()
                                + ",errorMsg = " + ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        re_send.setVisibility(View.VISIBLE);
                        chronometer.setVisibility(View.INVISIBLE);
                        chronometer.stop();
                    }
                }
            });
        }
    }

    /**
     * 验证短信验证码
     *
     * @param phoneNum        电话
     * @param verify_sms_code 验证码
     */

    private void verifySmsCode(final String phoneNum, final String verify_sms_code) {
        if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(verify_sms_code)) {
            BmobSMS.verifySmsCode(getContext(), phoneNum, verify_sms_code, new VerifySMSCodeListener() {

                @Override
                public void done(BmobException ex) {
                    if (ex == null) {//短信验证码已验证成功
                        signUp(userName, userPwd, phoneNum);
                    } else {
                        register.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "验证失败：code =" + ex.getErrorCode() +
                                ",msg = " + ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 注册用户
     *
     * @param userName 用户名
     * @param userPwd  密码
     * @param phoneNum 号码
     */
    private void signUp(final String userName, final String userPwd, final String phoneNum) {
        final MyUser myUser = new MyUser();
        myUser.setUsername(userName);
        myUser.setPassword(userPwd);
        myUser.setMobilePhoneNumber(phoneNum);
        myUser.signUp(getContext(), new SaveListener() {

            @Override
            public void onSuccess() {
                mListener.onVerifySmsCodeSuccess();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(getContext(), "注册失败:" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
