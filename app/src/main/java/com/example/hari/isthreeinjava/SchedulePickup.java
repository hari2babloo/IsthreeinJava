package com.example.hari.isthreeinjava;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.Tariff;
import com.example.hari.isthreeinjava.Models.Userprofile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SchedulePickup extends AppCompatActivity {

    TextView selecteddate,adress,landmark,city,state,pin,phone,textView5;
    Button datebtn,changeadress,confirmpickup;
    Calendar myCalendar;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    String mMessage,custid;
    List<Userprofile> userprofiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_pickup);
        datebtn = (Button)findViewById(R.id.datebtn);
        changeadress = (Button)findViewById(R.id.changeadress);
        confirmpickup = (Button)findViewById(R.id.confirm);
        selecteddate = (TextView)findViewById(R.id.dates);
        adress = (TextView)findViewById(R.id.address);
        landmark = (TextView)findViewById(R.id.landmark);
        city = (TextView)findViewById(R.id.city);
        state = (TextView)findViewById(R.id.state);
        pin = (TextView)findViewById(R.id.pin);
        phone = (TextView)findViewById(R.id.phone);
        textView5 = (TextView)findViewById(R.id.textView5);
        changeadress.setVisibility(View.GONE);
        confirmpickup.setVisibility(View.GONE);
        adress.setVisibility(View.GONE);
        landmark.setVisibility(View.GONE);
        city.setVisibility(View.GONE);
        state.setVisibility(View.GONE);
        pin.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        textView5.setVisibility(View.GONE);
        selecteddate.setVisibility(View.GONE);
        myCalendar = Calendar.getInstance();



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SchedulePickup.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        changeadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SchedulePickup.this, "ll be updated Soon", Toast.LENGTH_SHORT).show();
            }
        });
        confirmpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleProcess();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.e("selectdate",sdf.format( myCalendar.getTime()));
        selecteddate.setVisibility(View.VISIBLE);
        selecteddate.setText("Selected Date:" +sdf.format(myCalendar.getTime()));
        getaddress();

    }

    private void getaddress() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("email", "test@test.com");
            postdat.put("phoneNo", "8074219509");

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/getUserProfile")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mmessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {

               mMessage = response.body().string();
               Log.e("mfddsf",mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Userprofile>>(){}.getType();
                             userprofiles = (List<Userprofile>) gson.fromJson(mMessage,listType);
                            for(int i = 0; i < userprofiles.size(); i++){

                                changeadress.setVisibility(View.VISIBLE);
                                confirmpickup.setVisibility(View.VISIBLE);
                                adress.setVisibility(View.VISIBLE);
                                landmark.setVisibility(View.VISIBLE);
                                city.setVisibility(View.VISIBLE);
                                state.setVisibility(View.VISIBLE);
                                pin.setVisibility(View.VISIBLE);
                                phone.setVisibility(View.VISIBLE);
                                textView5.setVisibility(View.VISIBLE);
                                adress.setText("Door/Street: "+userprofiles.get(i).getAddress());
                                landmark.setText("Landmark: "+userprofiles.get(i).getLandMark());
                                city.setText("City: "+userprofiles.get(i).getCity());
                                state.setText("State: "+userprofiles.get(i).getState());
                                pin.setText("Pincode: "+userprofiles.get(i).getPincode());
                                phone.setText("Phone: "+userprofiles.get(i).getPhoneNo());
                                custid = userprofiles.get(i).getUserName();
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

    private void ScheduleProcess() {


        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

            postdat.put("customerId", custid);
            postdat.put("status", "PICKUP-REQUESTED");
            postdat.put("jobid", timeStamp);
            postdat.put("createdAt", timeStamp2);

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/schedulePickup")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mmessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                mMessage = response.body().string();
                Log.e("mfddsf",mMessage);
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
                                    Toast.makeText(SchedulePickup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(SchedulePickup.this,Dashpage.class);
//                                    startActivity(intent);
                                    Log.e("json",sss);
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
