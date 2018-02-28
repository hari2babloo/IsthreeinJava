package com.example.hari.isthreeinjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Models.Sigin;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class Dashpage extends AppCompatActivity {

    ImageButton pick,wallet;
    String custid,mMessage;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashpage);
        Bundle bundle = getIntent().getExtras();
        custid = bundle.getString("custid");
        pick = (ImageButton)findViewById(R.id.pickup);
        wallet = (ImageButton)findViewById(R.id.wallet);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(Dashpage.this, "", Toast.LENGTH_SHORT).show();
                FindJobId();
//                Intent mainIntent = new Intent(Dashpage.this,Puckup.class);
//                Dashpage.this.startActivity(mainIntent);
//                Dashpage.this.finish();
            }
        });
    }

    private void FindJobId() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", custid);

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/getJobStatus")
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
                            try {
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")){

                                    Intent intent = new Intent(Dashpage.this,Signin.class);
                                    startActivity(intent);
                                }

                                else if (s.equalsIgnoreCase("1")){

                                    Log.e("resfsdf",mMessage);
                                    Toast.makeText(Dashpage.this, "Job ID Already Exists", Toast.LENGTH_SHORT).show();

                                }
                                Log.e("s",s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
}
