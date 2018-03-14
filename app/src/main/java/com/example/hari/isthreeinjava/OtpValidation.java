package com.example.hari.isthreeinjava;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class OtpValidation extends AppCompatActivity {

    MaterialEditText otp,pass,cnfpass;
    Button submit;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    String mMessage,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_validation);
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        otp = (MaterialEditText) findViewById(R.id.otp);
        pass = (MaterialEditText) findViewById(R.id.pass);
        cnfpass = (MaterialEditText) findViewById(R.id.cnfpass);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(otp.getText().toString())){

                    otp.setError("Enter OTP");
                }
                else if (TextUtils.isEmpty(pass.getText().toString())){

                    pass.setError("Enter Password");
                }
                else if (!pass.getText().toString().matches(cnfpass.getText().toString())){

                    cnfpass.setError("Passwords do not Match");
                }
                else {

                    Resetpass();
                }
            }
        });



    }

    private void Resetpass() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("otp", otp.getText().toString());
            postdat.put("userEmail", email);
            postdat.put("password", pass.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("string",postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/changePassword")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(OtpValidation.this);
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
            }

            @Override
            public void onResponse(Response response) throws IOException {

                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")){

                                    final Dialog openDialog = new Dialog(OtpValidation.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("No Internet");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(json.getString("status"));
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
//                                    Toast.makeText(OtpValidation.this, "Your OTP is incorrect", Toast.LENGTH_LONG).show();
                                }

                                else if (s.equalsIgnoreCase("1")){

                                    final Dialog openDialog = new Dialog(OtpValidation.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("No Internet");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(json.getString("status"));
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

                                            Intent intent = new Intent(OtpValidation.this,Signin.class);
                                            startActivity(intent);
//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.show();

                                    Log.e("resfsdf",mMessage);
//                                    Toast.makeText(OtpValidation.this, "Your password has changed succesfully", Toast.LENGTH_LONG).show();

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
