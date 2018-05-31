package com.example.coamaster.coamasteruser;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MypageFragment extends Fragment {

    private EditText passwordText;
    private AlertDialog dialog;
    private boolean validate = false;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MypageFragment() {
        // Required empty public constructor
    }

    public static MypageFragment newInstance(String param1, String param2) {
        MypageFragment fragment = new MypageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public String user_id_get;
    public String user_name_get;
    public String user_email_get;
    public String user_pw_get;
    public String user_pw_set;
    public String prepwtext;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SaveActivity userInfo = (SaveActivity)getActivity().getApplication();
        user_id_get = userInfo.getUser_id();
        user_name_get = userInfo.getUser_name();
        user_email_get = userInfo.getUser_email();
        user_pw_get = userInfo.getUser_pw();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mypage, container, false);
        TextView idTextview = (TextView)rootView.findViewById(R.id.myidview);
        idTextview.setText(" 아이디 : " + user_id_get);
        TextView nameTextview = (TextView)rootView.findViewById(R.id.mynameview);
        nameTextview.setText(" 이름 : "+ user_name_get);
        TextView emailTextview = (TextView)rootView.findViewById(R.id.myemailview);
        emailTextview.setText(" 이메일 : "+ user_email_get);

        final EditText pre_pwText = (EditText)rootView.findViewById(R.id.pre_pw);
        final Button pwOkButton = (Button)rootView.findViewById(R.id.pwokButton);
        final Button rePwButton = (Button)rootView.findViewById(R.id.editButton);
        final EditText rePwText = (EditText)rootView.findViewById(R.id.repwText);
        final EditText rePwOkText = (EditText)rootView.findViewById(R.id.repwokText);

        pwOkButton.setOnClickListener(new View.OnClickListener() {
           //
            @Override
            public void onClick(View v) {
                prepwtext = pre_pwText.getText().toString();
                if(prepwtext.equals(user_pw_get)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("비밀번호 확인 완료")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    pre_pwText.setEnabled(false);
                    validate = true;
                    pre_pwText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    pwOkButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("비밀번호를 다시 확인해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                }
            }
        });

        rePwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("먼저 비밀번호를 확인해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                else{
                    if(rePwText.getText().toString().equals(rePwOkText.getText().toString())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        dialog = builder.setMessage("비밀번호가 변경되었습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        user_pw_set = rePwOkText.getText().toString();
                        SaveActivity userInfo = (SaveActivity)getActivity().getApplication();
                        userInfo.setUser_pw(user_pw_set);
                        Response.Listener<String> inforesponseLister = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        };
                        RePwRequest rePwRequest = new RePwRequest(user_id_get,rePwOkText.getText().toString(), inforesponseLister);
                        RequestQueue repwqueue = Volley.newRequestQueue(getActivity());
                        repwqueue.add(rePwRequest);

                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        dialog = builder.setMessage("비밀번호가 일치하지 않습니다.")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("MY PAGE");
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
