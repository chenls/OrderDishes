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


public class RegisterDialogFragment extends DialogFragment implements View.OnClickListener {

    private OnRegisterFragmentInteractionListener mListener;
    private EditText et_userpwd;
    private EditText et_phone_number;
    private EditText et_username;
    private ProgressBar progressBar;
    private TextView register;

    public RegisterDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_register_dialog, container, false);
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_userpwd = (EditText) view.findViewById(R.id.et_userpwd);
        et_phone_number = (EditText) view.findViewById(R.id.et_phone_number);
        register = (TextView) view.findViewById(R.id.register);
        register.setOnClickListener(this);
        TextView register = (TextView) view.findViewById(R.id.register);
        register.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                String userName = et_username.getText().toString().trim();
                String userPwd = et_userpwd.getText().toString().trim();
                String phoneNum = et_phone_number.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd) || TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getActivity(), "输入信息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!CommonUtil.checkNetState(getActivity())) {
                    return;
                }
                register.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                mListener.onRegisterSuccess(userName, userPwd, phoneNum);
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
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

    public interface OnRegisterFragmentInteractionListener {
        void onRegisterSuccess(String userName, String userPwd, String phoneNum);
    }
}
