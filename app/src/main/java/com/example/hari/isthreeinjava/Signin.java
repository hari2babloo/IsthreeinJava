package com.example.hari.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.Tariff;
import com.example.hari.isthreeinjava.Models.TinyDB;
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
    TinyDB tinyDB;
    ProgressDialog pd;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);





        tinyDB = new TinyDB(this);

        Log.e("custid",tinyDB.getString("custid"));
        String s = tinyDB.getString("custid");
        if (s != null && !s.isEmpty()){

            Intent intent = new Intent(Signin.this,Dashpage.class);
            startActivity(intent);
        }

        userid  = (MaterialEditText)findViewById(R.id.userid);
        pass = (MaterialEditText)findViewById(R.id.pass);
        signin = (Button)findViewById(R.id.signin);
        signuptxt = (TextView)findViewById(R.id.signuptext);
        forgotpass = (TextView)findViewById(R.id.forgotpass);



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (userid.getText().toString().isEmpty()){

                    userid.setError("Please Enter User ID");
                }
                else if (pass.getText().toString().isEmpty()){

                    pass.setError("Please Enter Your Password");

                }
                else {
                    Validate();
                }

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
        pd = new ProgressDialog(Signin.this);
        pd.setMessage("Signing in..");
        pd.setCancelable(false);
        pd.show();

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

                pd.cancel();
                pd.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(Signin.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Something Went Wrong");
                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                        dialogno.setText("OK");


                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });



                        openDialog.show();

                    }
                });


                String mMessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {


                pd.cancel();
                pd.dismiss();
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

              final Dialog openDialog = new Dialog(Signin.this);
              openDialog.setContentView(R.layout.alert);
              openDialog.setTitle("Account Created");
              TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
              dialogTextContent.setText("Please Enter Valid Credentials");
              ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
              dialogImage.setBackgroundResource(R.drawable.failure);
              dialogImage.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.failure));
//              dialogImage.setBackground(this.getDrawable(ContextCompat.R.drawable.failure));
              Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
              dialogCloseButton.setVisibility(View.GONE);
              Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

              dialogno.setText("OK");


              dialogno.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                  }
              });



              openDialog.show();


          }
          else if (status.equals(1)){
              Intent intent = new Intent(Signin.this,Dashpage.class);
//              intent.putExtra("custid",modelsignin.get(j).getUserName());
              tinyDB.putString("custid",modelsignin.get(j).getUserName());
              startActivity(intent);
            //  Log.e("status", String.valueOf(modelsignin.get(j).getStatus()));

          }


        }




    }

    @Override
    public void onBackPressed() {
        final Dialog openDialog = new Dialog(Signin.this);
        openDialog.setContentView(R.layout.alert);
        openDialog.setTitle("Exit app");
        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
        dialogTextContent.setText("Do you want to Close the app?");
        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                Signin.this.finish();
//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
            }
        });

        dialogno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        openDialog.show();
    }
}
