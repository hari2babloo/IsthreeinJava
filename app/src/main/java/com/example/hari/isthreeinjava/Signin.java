package com.example.hari.isthreeinjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.Tariff;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Signin extends AppCompatActivity {

    Button signin;
    MaterialEditText userid,pass;
    TextView signuptxt,forgotpass;
    String mMessage;
    List<Sigin> modelsignin;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        userid  = (MaterialEditText)findViewById(R.id.userid);
        pass = (MaterialEditText)findViewById(R.id.pass);
        signin = (Button)findViewById(R.id.signin);
        signuptxt = (TextView)findViewById(R.id.signuptext);
        forgotpass = (TextView)findViewById(R.id.forgotpass);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });
        signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Signin.this,Signup.class);
                startActivity(intent);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this,VerifyEmail.class);
                startActivity(intent);

            }
        });
    }

    private void Validate() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("userName", userid.getText().toString());
            postdat.put("password", pass.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/login")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resy",mMessage);
                           // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            TraverseData();

                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });
    }

    private void TraverseData() {

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Sigin>>(){}.getType();
        modelsignin = (List<Sigin>)  gson.fromJson(mMessage,listType);

        for(int j = 0; j < modelsignin.size(); j++){


            Integer status = modelsignin.get(j).getStatus();
          //  modelsignin.get(j).getStatus();

          if (status.equals(0)){

              Toast.makeText(this, "Please Enter Valid Credentials", Toast.LENGTH_SHORT).show();


          }
          else if (status.equals(1)){
              Intent intent = new Intent(Signin.this,Dashpage.class);
              intent.putExtra("custid",modelsignin.get(j).getUserName());
              startActivity(intent);
            //  Log.e("status", String.valueOf(modelsignin.get(j).getStatus()));

          }


        }




    }
}
