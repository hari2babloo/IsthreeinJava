package com.example.hari.isthreeinjava;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.TinyDB;
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

    ImageButton pick,placeorder;
    String mMessage;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    TinyDB tinydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashpage);
        Bundle bundle = getIntent().getExtras();
        tinydb = new TinyDB(this);

        pick = (ImageButton)findViewById(R.id.pickup);
        placeorder = (ImageButton)findViewById(R.id.placeorder);



        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // Toast.makeText(Dashpage.this, "", Toast.LENGTH_SHORT).show();
                FindJobId();
//                Intent mainIntent = new Intent(Dashpage.this,Puckup.class);
//                Dashpage.this.startActivity(mainIntent);
//                Dashpage.this.finish();
            }
        });

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FindJobId2();

            }
        });
    }

    private void FindJobId2() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinydb.getString("custid"));

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


                                    tinydb.putString("jobid",json.getString("jobid"));
                                    final Dialog openDialog = new Dialog(Dashpage.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Initiate Pickup");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("You haven't initiated any pickup please initiate a pickup request");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();
                                        }
                                    });
                                    dialogCloseButton.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            openDialog.dismiss();
                                            Intent intent = new Intent(Dashpage.this,SchedulePickup.class);
                                            startActivity(intent);
                                        }
                                    });
                                    openDialog.show();

                                }

                                else if (s.equalsIgnoreCase("1")){

                                    Log.e("resfsdf",mMessage);
                                    tinydb.putString("jobid",json.getString("jobid"));

                                    final Dialog openDialog = new Dialog(Dashpage.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Alert");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Please place the Order only after the Arrival of the Delivery Person");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();
                                        }
                                    });

                                    dialogCloseButton.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            openDialog.dismiss();
                                            Intent intent = new Intent(Dashpage.this,Puckup.class);
                                            startActivity(intent);
                                        }
                                    });
                                    openDialog.show();

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

    private void FindJobId() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinydb.getString("custid"));

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


                                    tinydb.putString("jobid",json.getString("jobid"));
                                    Intent intent = new Intent(Dashpage.this,SchedulePickup.class);
                                    startActivity(intent);
                                }

                                else if (s.equalsIgnoreCase("1")){

                                    Log.e("resfsdf",mMessage);
                                    final Dialog openDialog = new Dialog(Dashpage.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Pickup Already Initiated");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("You have already initiated your pickup, your pickup is on the way");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
                                    dialogno.setVisibility(View.GONE);

                                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();
                                        }
                                    });
//                                    dialogCloseButton.setOnClickListener(new View.OnClickListener(){
//                                        @Override
//                                        public void onClick(View v) {
//                                            // TODO Auto-generated method stub
//                                            openDialog.dismiss();
//                                            Intent intent = new Intent(Dashpage.this,SchedulePickup.class);
//                                            startActivity(intent);
//                                        }
//                                    });
                                    openDialog.show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_item_one) {


            Intent intent = new Intent(Dashpage.this,Signin.class);
            startActivity(intent);


            // Do something
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
