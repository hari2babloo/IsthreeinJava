package com.example.hari.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
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

public class VerifyEmail extends AppCompatActivity {

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    Button validate;
    MaterialEditText email;
    String mMessage;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_email);

        validate = (Button)findViewById(R.id.submit);
        email = (MaterialEditText)findViewById(R.id.email);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(email.getText().toString())){

                    email.setError("Email is empty");

                }

                else {


                    ValidateEmail();
                }

            }
        });


    }

    private void ValidateEmail() {

        pd = new ProgressDialog(VerifyEmail.this);
        pd.setMessage("Validating your email..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("userEmail", email.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/changePasswordRequest")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();

                pd.cancel();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(VerifyEmail.this);
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
//                                             startActivity(intent);
                            }
                        });

                        openDialog.show();

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();

                Log.e("error",mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")){

                                    email.setError("Enter Valid Email ID");

                                    //Toast.makeText(VerifyEmail.this, "Enter Valid Email Id", Toast.LENGTH_LONG).show();
                                }

                                else if (s.equalsIgnoreCase("1")){


                                    Toast.makeText(VerifyEmail.this, "OTP will be sent to your registered mobile number", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(VerifyEmail.this,OtpValidation.class);
                                    intent.putExtra("email", email.getText().toString());

                                    startActivity(intent);
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
