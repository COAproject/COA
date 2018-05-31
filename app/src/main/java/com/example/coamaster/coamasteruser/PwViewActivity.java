package com.example.coamaster.coamasteruser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class PwViewActivity extends AppCompatActivity {

    public String user_pw;
    public String success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pw_view);

        Intent findpwIntent = getIntent();
        final String user_id = findpwIntent.getStringExtra("user_id");
        final String user_email = findpwIntent.getStringExtra("user_email");
        final TextView pwviewtext = (TextView) findViewById(R.id.userpwview);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    JSONObject object = jsonArray.getJSONObject(count);
                    success = object.getString("success");

                    if(success.equals("true")) {
                        count=1;
                        JSONObject object2 = jsonArray.getJSONObject(count);
                        user_pw = object2.getString("user_pw");
                        pwviewtext.setText(user_pw);
                    }
                    else{
                        pwviewtext.setText("일치하는 정보가 없습니다.");
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        PwViewRequest pwViewRequest = new PwViewRequest(user_id, user_email, responseListener);
        RequestQueue queue = Volley.newRequestQueue(PwViewActivity.this);
        queue.add(pwViewRequest);

        Button relogin = (Button)findViewById(R.id.pwlogin);
        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reloginintent = new Intent(PwViewActivity.this, LoginActivity.class);
                PwViewActivity.this.finish();
                PwViewActivity.this.startActivity(reloginintent);
            }
        });
    }


}

