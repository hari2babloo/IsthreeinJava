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

import com.example.hari.isthreeinjava.Models.TinyDB;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ChangeAddress extends AppCompatActivity {

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    MaterialEditText door,landmark,city,pin;
    Button changeaddr;
    String mMessage;

    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_address);

        tinyDB = new TinyDB(this);


        door = (MaterialEditText)findViewById(R.id.door);
        landmark = (MaterialEditText)findViewById(R.id.land);
        city = (MaterialEditText)findViewById(R.id.city);
        pin = (MaterialEditText)findViewById(R.id.pin);
        changeaddr = (Button)findViewById(R.id.change);

        changeaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(door.getText().toString())){

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
            postdat.put("customerID",tinyDB.getString("custid"));
            postdat.put("address", door.getText().toString());
             postdat.put("city",city.getText().toString());
            postdat.put("country", "INDIA");
             postdat.put("landMark", landmark.getText().toString());
            postdat.put("lat", "0.00");
            postdat.put("longi", "0.00");
             postdat.put("pincode", pin.getText().toString());
            postdat.put("state", "Telangana");



        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/changeAddress")
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

                Log.e("Respomse",mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonResponse = null;
                            try {

                                JSONArray itemArray = new JSONArray(mMessage);
                                for (int i = 0; i < itemArray.length(); i++) {
                                    // int value=itemArray.getInt(i);
                                    String sss = itemArray.getString(i);
                                    jsonResponse = new JSONObject(sss);

                                    jsonResponse.getString("status");
                                    jsonResponse.getString("statusCode");

                                    Log.e("Json", jsonResponse.getString("status"));
                                    if (jsonResponse.getString("statusCode").equalsIgnoreCase("0")){

                                        final Dialog openDialog = new Dialog(ChangeAddress.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("Error");
                                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText(jsonResponse.getString("status"));
                                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
                                        dialogno.setVisibility(View.GONE);

                                        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
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

                                    else if (jsonResponse.getString("statusCode").equalsIgnoreCase("1")){



                                        final Dialog openDialog = new Dialog(ChangeAddress.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("Success");
                                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText(jsonResponse.getString("status"));
                                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
                                        dialogno.setVisibility(View.GONE);

                                        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openDialog.dismiss();

                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ChangeAddress.this,SchedulePickup.class);
                                                startActivity(intent);
                                            }
                                        });
                                        openDialog.show();
                                        //

                                    }



                                }


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
