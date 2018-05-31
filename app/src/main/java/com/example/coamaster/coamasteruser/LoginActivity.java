package com.example.coamaster.coamasteruser;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog;

    private boolean saveLoginData;
    private String id;
    private String password;

    private EditText idText;
    private EditText passwordText;
    private CheckBox checkBox;
    private Button loginButton;

    public String user_name_set;
    public String user_email_set;
    public String user_pw_set;


    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView registerText = (TextView) findViewById(R.id.registerText);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        TextView findidText = (TextView) findViewById(R.id.findid);
        findidText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findidIntent = new Intent(LoginActivity.this, FindIdActivity.class);
                LoginActivity.this.startActivity(findidIntent);
            }
        });

        TextView findpwText = (TextView) findViewById(R.id.findpw);
        findpwText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findpwIntent = new Intent(LoginActivity.this, FindPwActivity.class);
                LoginActivity.this.startActivity(findpwIntent);
            }
        });
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        loginButton = (Button) findViewById(R.id.loginButton);

        if (saveLoginData) {
            idText.setText(id);
            passwordText.setText(password);
            checkBox.setChecked(saveLoginData);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_id = idText.getText().toString();
                String user_pw = passwordText.getText().toString();
                save();

                Response.Listener<String> responseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("로그인 되었습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.putExtra("user_id",user_id);
                                                startActivity(intent);
                                                LoginActivity.this.finish();
                                                finish();

                                            }

                                        })
                                        .create();
                                dialog.show();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("아이디 또는 비밀번호를 다시 확인하세요.")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };


                LoginRequest loginRequest = new LoginRequest(user_id, user_pw, responseLister);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

                Response.Listener<String> inforesponseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            int count = 0;
                            JSONObject object = jsonArray.getJSONObject(count);
                            user_name_set = object.getString("username");
                            user_email_set = object.getString("useremail");
                            user_pw_set = passwordText.getText().toString();

                            SaveActivity userInfo = (SaveActivity) getApplication();
                            userInfo.setUser_name(user_name_set);
                            userInfo.setUser_email(user_email_set);
                            userInfo.setUser_pw(user_pw_set);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                UserinfoRequest userinfoRequest = new UserinfoRequest(user_id, inforesponseLister);
                RequestQueue infoqueue = Volley.newRequestQueue(LoginActivity.this);
                infoqueue.add(userinfoRequest);

            }
        });


    }

    private void save() {
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("ID", idText.getText().toString().trim());
        editor.putString("PWD", passwordText.getText().toString().trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        id = appData.getString("ID", "");
        password = appData.getString("PWD", "");
    }


    @Override
    protected void onStop(){
        super.onStop();
        if(dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }
}