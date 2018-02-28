package com.example.hari.isthreeinjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.Signupmodel;
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
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

MaterialEditText firstname,email,phone,altphone,pass,cnfpass,door,landmark,city,pin;
Button signup;
String mMessage;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    List<Signupmodel> modelsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        firstname = (MaterialEditText)findViewById(R.id.name);
        email = (MaterialEditText)findViewById(R.id.email);
        phone = (MaterialEditText)findViewById(R.id.phone);
        altphone = (MaterialEditText)findViewById(R.id.altphone);
        pass = (MaterialEditText)findViewById(R.id.pass);
        cnfpass = (MaterialEditText)findViewById(R.id.cnfpass);
        door = (MaterialEditText)findViewById(R.id.door);
        landmark = (MaterialEditText)findViewById(R.id.land);
        city = (MaterialEditText)findViewById(R.id.city);
        pin = (MaterialEditText)findViewById(R.id.pin);
        signup = (Button)findViewById(R.id.signupbtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                        "[a-zA-Z0-9+._%-+]{1,256}" +
                                "@" +
                                "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                                "(" +
                                "." +
                                "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                                ")+"
                );


                if (TextUtils.isEmpty(firstname.getText().toString()))

                {
                    firstname.setError("Empty");
                } else if (TextUtils.isEmpty(email.getText().toString()))

                {
                    email.setError("Empty");
                } else if (phone.getText().toString().length() != 10) {

                    phone.setError("enter 10 Digit Mobile Number");

                } else if (altphone.getText().toString().length() != 10) {

                    altphone.setError("enter 10 Digit Mobile Number");
                } else if (TextUtils.isEmpty(pass.getText().toString())) {

                    pass.setError("Password should not be empty");
                } else if (!pass.getText().toString().matches(cnfpass.getText().toString())) {

                    cnfpass.setError("not matching");
                }

                else if (TextUtils.isEmpty(door.getText().toString())){

                    door.setError("please fill this field");
                }
                else if (TextUtils.isEmpty(landmark.getText().toString())){

                    landmark.setError("please fill this field");
                }
                else if (TextUtils.isEmpty(city.getText().toString())){

                    city.setError("please fill this field");
                }
                else if (TextUtils.isEmpty(pin.getText().toString())){

                    pin.setError("please fill this field");
                }

                else{

                    Submitdata();
                }







            }
        });


    }

    private void Submitdata() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("address", door.getText().toString());
            postdat.put("altPhone", altphone.getText().toString());
            postdat.put("city",city.getText().toString());
            postdat.put("country", "INDIA");
            postdat.put("email",email.getText().toString());
            postdat.put("landMark", landmark.getText().toString());
            postdat.put("lat", "0.00");
            postdat.put("longi", "0.00");
            postdat.put("name", firstname.getText().toString());
            postdat.put("password",pass.getText().toString());
            postdat.put("phoneNo", phone.getText().toString());
            postdat.put("pic", "xxx");
            postdat.put("picFileType", "xxx");
            postdat.put("pincode", pin.getText().toString());
            postdat.put("state", "Telangana");



        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/signup")
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


                            Log.e("res",mMessage);
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
        Signupmodel signupmodel = new Gson().fromJson(mMessage,Signupmodel.class);

        Log.e("gson", String.valueOf(signupmodel.getStatusCode()));


        Integer status = signupmodel.getStatusCode();

        if (status.equals(0)){

            Toast.makeText(this, signupmodel.getStatus(), Toast.LENGTH_SHORT).show();

        }

        else if (status.equals(1)){

            Toast.makeText(this, signupmodel.getStatus(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Signup.this,Signin.class);
                startActivity(intent);

        }

//        for(int j = 0; j < modelsignup.size(); j++){
//
//
//            Integer status = modelsignup.get(j).getStatusCode();
//            //  modelsignin.get(j).getStatus();
//
//            if (status.equals(0)){
//                Toast.makeText(this, modelsignup.get(j).getStatus(), Toast.LENGTH_SHORT).show();
//            }
//            else if (status.equals(1)){
//
//                Toast.makeText(this, modelsignup.get(j).getStatus(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Signup.this,Dashpage.class);
//                startActivity(intent);
//                //  Log.e("status", String.valueOf(modelsignin.get(j).getStatus()));
//
//            }
//
//
//        }



    }
}
