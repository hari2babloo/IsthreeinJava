package com.example.hari.isthreeinjava;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.Tariff;
import com.example.hari.isthreeinjava.Models.TinyDB;
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
import java.text.ParseException;
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
    TinyDB tinyDB;
    View v1,v2;
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
        v1 = (View)findViewById(R.id.v1);
        v2 = (View)findViewById(R.id.v2);

        v1.setVisibility(View.GONE);

        v2.setVisibility(View.GONE);
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

tinyDB = new TinyDB(this);

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

                Intent  intent = new Intent(SchedulePickup.this,ChangeAddress.class);
                startActivity(intent);
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

        String timeStamp = new SimpleDateFormat("MM/dd/yy").format(Calendar.getInstance().getTime());


        try {
            Date now = sdf.parse(timeStamp);
            Date selected = sdf.parse(sdf.format(myCalendar.getTime()));
            if(selected.after(now)){
                System.out.println("Correct");
                selecteddate.setVisibility(View.VISIBLE);
                changeadress.setVisibility(View.VISIBLE);

                v1.setVisibility(View.VISIBLE);

                v2.setVisibility(View.VISIBLE);

                datebtn.setText(sdf.format(myCalendar.getTime()));
                //  selecteddate.setText("Selected Date:" +sdf.format(myCalendar.getTime()));
                selecteddate.setVisibility(View.GONE);
                getaddress();
            }
            // before() will return true if and only if date1 is before date2
            if(selected.before(now)){
                System.out.println("please select correct date");
                Toast.makeText(this, "You Cannot Select previous day", Toast.LENGTH_SHORT).show();
            }

            //equals() returns true if both the dates are equal
            if(selected.equals(now)){

                selecteddate.setVisibility(View.VISIBLE);
                changeadress.setVisibility(View.VISIBLE);

                v1.setVisibility(View.VISIBLE);

                v2.setVisibility(View.VISIBLE);

                datebtn.setText(sdf.format(myCalendar.getTime()));
                //  selecteddate.setText("Selected Date:" +sdf.format(myCalendar.getTime()));
                selecteddate.setVisibility(View.GONE);
                getaddress();
//                Toast.makeText(this, "Schedule not possible today", Toast.LENGTH_SHORT).show();
                System.out.println("You cannot select today's day");
            }

         } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void getaddress() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("customerId",tinyDB.getString("custid"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/getUserInfo")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mmessage = e.getMessage().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(SchedulePickup.this);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(SchedulePickup.this);
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



                                    final Dialog openDialog = new Dialog(SchedulePickup.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Schedule");
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
                                            Intent intent = new Intent(SchedulePickup.this,Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });

                                    openDialog.show();
           //                         Toast.makeText(SchedulePickup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();

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
