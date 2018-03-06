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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
String mMessage,accepted;
ProgressDialog pd;
TextView termstext;
CheckBox terms;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    List<Signupmodel> modelsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

       termstext = (TextView)findViewById(R.id.termstext);

       termstext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               final Dialog openDialog = new Dialog(Signup.this);
               openDialog.setContentView(R.layout.terms);
               openDialog.setTitle("No Internet");
               TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.textView6);

               dialogTextContent.setText("Welcome to ISTHREE. \n\n If you continue to  use this app you are agreeing to comply with and be bound by the following terms and conditions of use, which together with our privacy policy govern 3X3Conect (Owner of ISTHREE) relationship with you in relation to this App and the Service (as described below).  \n" +
                       "\n" +

                       " PRECAUTIONS \n\n You can schedule a pickup through our mobile app, website or by calling us.You need to provide your Name, Mobile Number, Address and Pickup Date & Time Slot.You can schedule a pickup through our mobile app, website or by calling us.Our Rider will be at your doorstep at the mentioned time slot. However, we do not guarantee it.\n You need to count your clothes at the time of pickup and verify it on the pickup slip you will get from our Rider.The garments will be delivered on the date & time mentioned on the Pickup Slip. However, we do not take any guarantee of it. ISTHREE Management is not responsible for any delay.   We examine each garments after we receive them at our warehouse and notify if any damage is observed. However, we don't guarantee that every damage will be identified before processing.  ISTHREE may or may not call you before delivery. If you want us to call you before delivering, please inform our Rider during the Pickup.Any special instruction has to be provided to our Pickup Rider and must be mentioned on the Pickup Slip provided to you.We do not accept any accessory that is not to be processed.  We take no guarantee of color bleed in laundry. You are advised not to give clothes that may bleed color in laundry. However you may give it for dry-clean.The rates are subject to change without prior notice.Any issue with respected to Quality has to be reported to us within 24 hours of Delivery of garments.\n" +
                       "\n" +

                       " CANCELATION \n\n For cancelling your scheduled pickup, you need to call us and inform. Cancellation is not chargable as long as the Rider has not left our station to pickup your garments. Otherwise, the customer may have to pay 50 Rupees as conveyance charges.\n" +
                       "\n" +

                       " PAYMENT RELATED \n\n We accept payment in Cash upon Delivery of garments back to the Customer.You can estimate the bill amount at the time of handing over your garments to the Pickup Rider.The exact bill amount will be informed to the customer via SMS after garments are booked at Processing Centre.The Invoice will be handed over to the customer at the time of Delivery.We will soon incorporate Online Payment Option and the details will be able soon.All advance payment must be recorded in the Pickup Slip and signed by our Pickup Rider.");
              // dialogTextContent.setText("Looks like your device is offline");
//               ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
               Button dialogCloseButton = (Button)openDialog.findViewById(R.id.button);

//               Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
//
//               dialogno.setText("OK");


               dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       openDialog.dismiss();

                   }
               });

               openDialog.show();

           }
       });

        terms = (CheckBox)findViewById(R.id.check);


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
                    firstname.setError("please fill this field");
                } else if (TextUtils.isEmpty(email.getText().toString()))

                {
                    email.setError("please fill this field");
                } else if (phone.getText().toString().length() != 10) {

                    phone.setError("enter 10 Digit Mobile Number");

                }  else if (TextUtils.isEmpty(pass.getText().toString())) {

                    pass.setError("Password should not be empty");
                } else if (!pass.getText().toString().matches(cnfpass.getText().toString())) {

                    cnfpass.setError("Passwords not matching");
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

                else if (!terms.isChecked()){


                    terms.setError("accept");
                }


                else{

                    Submitdata();
                }









            }
        });


    }

    private void Submitdata() {

        pd = new ProgressDialog(Signup.this);
        pd.setMessage("Creating your account..");
        pd.setCancelable(false);
        pd.show();

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
                pd.dismiss();
                pd.cancel();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(Signup.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
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
                pd.dismiss();
                pd.cancel();
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

            final Dialog openDialog = new Dialog(Signup.this);
            openDialog.setContentView(R.layout.alert);
            openDialog.setTitle("Signup");
            TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
            dialogTextContent.setText(signupmodel.getStatus());
            ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
            Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
            dialogCloseButton.setVisibility(View.GONE);
            Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

            dialogno.setText("OK");


            dialogno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();
                    Intent intent = new Intent(Signup.this,Signin.class);
                    startActivity(intent);
//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                }
            });

            openDialog.show();

            //Toast.makeText(this, signupmodel.getStatus(), Toast.LENGTH_SHORT).show();

        }

        else if (status.equals(1)){

            final Dialog openDialog = new Dialog(Signup.this);
            openDialog.setContentView(R.layout.alert);
            openDialog.setTitle("Account Created");
            TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
            dialogTextContent.setText("You have succesfully registered with Isthree");
            ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
            Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
            dialogCloseButton.setVisibility(View.GONE);
            Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

            dialogno.setText("OK");


            dialogno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();
                    Intent intent = new Intent(Signup.this,Signin.class);
                    startActivity(intent);
//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                }
            });



            openDialog.show();

        //    Toast.makeText(this, signupmodel.getStatus(), Toast.LENGTH_SHORT).show();


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
