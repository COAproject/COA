package com.example.coamaster.coamasteruser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class IdViewActivity extends AppCompatActivity {

    public String user_id;
    public String success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_view);

        Intent findidIntent = getIntent();
        final String user_email = findidIntent.getStringExtra("user_email");
        final TextView idviewtext = (TextView) findViewById(R.id.useridview);

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
                        user_id = object2.getString("user_id");
                        idviewtext.setText(user_id);
                    }
                    else{
                        idviewtext.setText("등록된 이메일 정보가 없습니다.");
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        IdViewRequest idViewRequest = new IdViewRequest(user_email, responseListener);
        RequestQueue queue = Volley.newRequestQueue(IdViewActivity.this);
        queue.add(idViewRequest);

        Button relogin = (Button)findViewById(R.id.relogin);
        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reloginintent = new Intent(IdViewActivity.this, LoginActivity.class);
                IdViewActivity.this.finish();
                IdViewActivity.this.startActivity(reloginintent);
            }
        });

        Button findpw = (Button)findViewById(R.id.findpw);
        findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findpwIntent = new Intent(IdViewActivity.this, FindPwActivity.class);
                IdViewActivity.this.finish();
                IdViewActivity.this.startActivity(findpwIntent);
            }
        });
    }
}