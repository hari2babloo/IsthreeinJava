package com.a3x3conect.mobile.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.JobOrder;
import com.example.hari.isthreeinjava.Models.Tariff;
import com.example.hari.isthreeinjava.Models.TinyDB;


import com.example.hari.isthreeinjava.Pickup;
import com.example.hari.isthreeinjava.R;
import com.example.hari.isthreeinjava.Signin;
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
import java.util.Collections;
import java.util.List;

public class ExistingData extends AppCompatActivity {

    ProgressDialog pd;
    String mMessage2;
    JSONObject json_data;
    private AdapterFish Adapter;
    double s;

    JSONArray jsonArray;
    List<Tariff> tarif;
    ArrayList<DataFish2> filterdata2=new ArrayList<DataFish2>();

    ArrayList<DataFish> filterdata=new ArrayList<DataFish>();
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> prize = new ArrayList<>();
    final ArrayList<String> dd = new ArrayList<>();
    TableLayout tableLayout;
    String price,type,quantity,amount;
    TextView btmtotal;
    RecyclerView mRVFishPrice;
    Spinner spinner;
    EditText qty;
    Button add,pay,cancel;
    ArrayList<String> fourdour = new ArrayList<>();

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    TinyDB tinyDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_data);

        tinyDB = new TinyDB(ExistingData.this);
        spinner  = (Spinner) findViewById(R.id.spinner);
        qty = (EditText)findViewById(R.id.qty);
        add = (Button)findViewById(R.id.add) ;
        pay = (Button)findViewById(R.id.pay);

        cancel = (Button)findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog openDialog = new Dialog(ExistingData.this);
                openDialog.setContentView(R.layout.alert);
                openDialog.setTitle("No Internet");
                TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                dialogTextContent.setText("Do you really want to cancel order???");
                ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);

                dialogCloseButton.setText("YES I want to");

                Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                dialogno.setText("NO");


                dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialog.dismiss();
                        Cancelschedule();
                    }
                });
                dialogno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog.dismiss();



//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                    }
                });



                openDialog.show();

            }
        });
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
        tableLayout = (TableLayout)findViewById(R.id.tabl);
      //  tableLayout.setVisibility(View.GONE);
        btmtotal = (TextView)findViewById(R.id.btmtotal);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Paydata();
            }
        });

        GetFormData();

    }

    private void Paydata() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        JSONArray itemType = new JSONArray();
        JSONArray unitPrice = new JSONArray();
        JSONArray subTotal = new JSONArray();
        JSONArray quantity = new JSONArray();

        for (int i=0;i<filterdata2.size();i++){

            itemType.put(filterdata2.get(i).item);
        }
        for (int i=0;i<filterdata2.size();i++){

            unitPrice.put(filterdata2.get(i).cost);
        }

        for (int i=0;i<filterdata2.size();i++){

            subTotal.put(filterdata2.get(i).amt);


        }
        float garmentscount = 0;
        for (int i=0;i<filterdata2.size();i++){


            float foo = Float.parseFloat(filterdata2.get(i).noofpieces);
            garmentscount+= foo;
            quantity.put(filterdata2.get(i).noofpieces);
        }


        float sum = 0;
        for (int i = 0; i < filterdata2.size(); i++) {

            Float dd = Float.parseFloat(filterdata2.get(i).amt);
            sum += dd;
        }
        Log.e("rererer", String.valueOf(sum));
        //   btmamt.setText("Sub Total = " +String.valueOf(sum));
        double s2 =  ((0/100) *sum)+sum;
        String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        try {
            //  postdat.put("status", "PICKUP-CONFIRMED");
            postdat.put("customerId",tinyDB.getString("custid"));
            postdat.put("jobId",tinyDB.getString("jobid"));
            postdat.put("jobOrderDateTime",timeStamp2);
            postdat.put("gstPercentage", "0");
            postdat.put("grandTotal",String.valueOf(s2));
            postdat.put("garmentsCount",garmentscount);
            postdat.put("itemType",itemType);
            postdat.put("unitPrice",unitPrice);
            postdat.put("quantity",quantity);
            postdat.put("subTotal",subTotal);


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("array", String.valueOf(postdat));
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/updateJobOrder")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(ExistingData.this);
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

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });



                        openDialog.show();

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

              final String   mMessage = response.body().string();

                Log.e("result",mMessage);

//                Log.e("resstsy",response.body().string());
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonResponse = null;
                            try {
                                jsonResponse = new JSONObject(mMessage);

                                if (jsonResponse.getString("statusCode").equalsIgnoreCase("0")){

                                    final Dialog openDialog = new Dialog(ExistingData.this);
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

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });

                                    openDialog.show();
                                }

                                else if (jsonResponse.getString("statusCode").equalsIgnoreCase("1")){



                                    final Dialog openDialog = new Dialog(ExistingData.this);
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

                                            //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ExistingData.this,SummaryReport.class);

                       //                     tinyDB.putListString("filterdata",filterdata2);
//                                            intent.putExtra("filterdata",filterdata2);
                                            startActivity(intent);
                                        }
                                    });
                                    //
                                    //  Log.e("json",sss);

                                    openDialog.show();
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

    private void Cancelschedule() {

        pd = new ProgressDialog(ExistingData.this);
        pd.setMessage("Cancel Order");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
          //  postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
//                        postdat.put("customerId", "C0016");
//            postdat.put("jobId", "2000309051221");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("Postdata",postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/cancelJobOrder")
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
                        final Dialog openDialog = new Dialog(ExistingData.this);
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

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
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
               final String mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("cancel",mMessage);

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);

                                if(jsonObject.getString("statusCode").equalsIgnoreCase("1")){


                                    final Dialog openDialog = new Dialog(ExistingData.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Status");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonObject.getString("status"));
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.show();
                                }

                                else {

                                    final Dialog openDialog = new Dialog(ExistingData.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Status");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonObject.getString("status"));
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();


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

    private void GetFormData() {

        final   OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/alltariff")
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mmessage = e.getMessage().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(ExistingData.this);
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

//                                                //                                          Toast.makeText(Pickup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Pickup.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });



                        openDialog.show();

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

                final String mMessage = response.body().string();

                Log.e("result",mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Tariff>>(){}.getType();
                            tarif = (List<Tariff>)  gson.fromJson(mMessage,listType);

                            for(int j = 0; j < tarif.size(); j++) {


                                DataFish dataFish = new DataFish(tarif.get(j).getId(),tarif.get(j).getType(),tarif.get(j).getPrice());
                                filterdata.add(dataFish);

                            }

                            try {
                               jsonArray = new JSONArray(mMessage);
                                for(int j = 0; j < jsonArray.length(); j++){

                                   json_data = jsonArray.getJSONObject(j);

                                    items.add(json_data.getString("category"));
                                    prize.add(json_data.getString("price"));
                                    Log.e("Dta",dd.toString());
                                }

                                setspinner();

                                getjoborder();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);
//                                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                    price = prize.get(position);
                                    type = items.get(position);
                                    type = filterdata.get(position).Dcategory;

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    quantity = qty.getText().toString();

                                    if (TextUtils.isEmpty(quantity)){
                                        Toast.makeText(ExistingData.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                                        //myHolder.qty.setError("empty");
                                    }
                                    else if (quantity.equalsIgnoreCase("0")){
                                        Toast.makeText(ExistingData.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();

                                    }

                                    else if (items.isEmpty()){

                                        Toast.makeText(getApplicationContext(), "List is Empty", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {

                                        if (fourdour.isEmpty()){

                                            fourdour.add(type);

                                            Float foo = Float.parseFloat(quantity);
                                            Float fo2 = Float.parseFloat(price);
                                            Float x = foo * fo2;
                                            amount =Float.toString(x);
                                            Log.e(type,quantity+price+amount);
                                            DataFish2 ss = new DataFish2(type, quantity, price, amount);
                                            filterdata2.add(ss);

                                        }

                                        else if (fourdour.contains(type)){

                                            Log.e("exist", "exist");


                                            for (int i=0;i<filterdata2.size();i++){


                                                if (filterdata2.get(i).item.equalsIgnoreCase(type)){


                                                    Float foo1 = Float.parseFloat(quantity);
                                                    Float fo1 = Float.parseFloat(price);
                                                    Float dd = Float.parseFloat(filterdata2.get(i).amt);
                                                    Float dd2 = Float.parseFloat(filterdata2.get(i).noofpieces);

                                                    s = (foo1+dd2)*fo1;

                                                    float sss = foo1+dd2;






                                                    DataFish2 ss = new DataFish2(type, String.valueOf(Math.round(sss)), price, String.valueOf(s));
                                                    filterdata2.set(i,ss);
                                                    break;

                                                }
                                            }

                                        }

                                        else {

                                            fourdour.add(type);

                                            Log.e("noexist", "noexist");

                                            Float foo = Float.parseFloat(quantity);
                                            Float fo2 = Float.parseFloat(price);
                                            Float x = foo * fo2;
                                            amount =Float.toString(x);
                                            Log.e(type,quantity+price+amount);
                                            DataFish2 ss = new DataFish2(type, quantity, price, amount);
                                            filterdata2.add(ss);
                                            AddtoList();

                                        }

                                        AddtoList();

                                    }


                                }
                            });

                            //  Displaylist();

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

    private void setspinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void AddtoList() {


        //   Log.e("ononcontains","oncontains");          // Log.e(u,u);
        Adapter = new AdapterFish(ExistingData.this,filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(ExistingData.this,LinearLayoutManager.VERTICAL,false));
        float sum = 0;
        for (int i = 0; i < filterdata2.size(); i++) {

            Float dd = Float.parseFloat(filterdata2.get(i).amt);
            sum += dd;
        }

        //  btmamt.setText("Sub Total = " +String.valueOf(sum));

        s =  ((0/100) *sum)+sum;
        btmtotal.setText("Total = " +getResources().getString(R.string.rupee)+String.format("%.2f",s)+"(Inc of all taxes)");

        pay.setVisibility(View.VISIBLE);
    }

    private void getjoborder() {

        pd = new ProgressDialog(ExistingData.this);
        pd.setMessage("Getting Job Orders..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/getJobOrder")
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
                        final Dialog openDialog = new Dialog(ExistingData.this);
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

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });
                        openDialog.show();
                    }
                });


                mMessage2 = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {


                pd.cancel();
                pd.dismiss();
                mMessage2 = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Log.e("Resy",mMessage2);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            //   TraverseData();

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage2);

                                Double statuscode = jsonObject.optDouble("statusCode");
                                Double jobid = jsonObject.optDouble("jobid");


                                Gson gson = new Gson();

                                JobOrder jobOrder = gson.fromJson(mMessage2,JobOrder.class);


//                                String s = jobOrder.getCustomerId();

//                                filterdata2.add(jobOrder);

                                for (int i= 0; i<jobOrder.getCategory().size(); i++){



                                    fourdour.add(jobOrder.getCategory().get(i));
                                    DataFish2 ss = new DataFish2("item","qty","price","total");

                                    Float ss2 = Float.parseFloat(jobOrder.getPrice().get(i));

                                    Float ss3 =  Float.parseFloat(jobOrder.getQuantity().get(i));
                                    Float ss4 = ss2 * ss3;
                                    DataFish2 sds = new DataFish2(jobOrder.getCategory().get(i),jobOrder.getQuantity().get(i),jobOrder.getPrice().get(i),String.valueOf(ss4));


//
//                                    for (int j=0;j<items.size();i++){
//
//                                        if (items.get(j).equalsIgnoreCase(jobOrder.getCategory().get(i))){
//
//                                            items.remove(j);
//                                            prize.remove(j);
//                                        }
//                                    }



                                    filterdata2.add(sds);
                                }




                                float sum = 0;
                                for (int i = 0; i < filterdata2.size(); i++) {

                                    Float dd = Float.parseFloat(filterdata2.get(i).amt);
                                    sum += dd;
                                }

                                //  btmamt.setText("Sub Total = " +String.valueOf(sum));

                                s =  ((0/100) *sum)+sum;
                                btmtotal.setText("Total = " +getResources().getString(R.string.rupee)+String.valueOf(s)+"(Inc of all taxes)");

                                pay.setVisibility(View.VISIBLE);

                                Adapter = new AdapterFish(ExistingData.this, filterdata2);
                                Adapter.setHasStableIds(false);
                                mRVFishPrice.setAdapter(Adapter);
                                mRVFishPrice.setHasFixedSize(false);
                                //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
                                //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
                                mRVFishPrice.setLayoutManager(new LinearLayoutManager(ExistingData.this,LinearLayoutManager.VERTICAL,true));



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


    public class DataFish2 {
        public String item;
        public String noofpieces;
        public String cost;
        public String amt;


        public DataFish2(String item,String noofpieces,String cost,String amt){

            this.item = item;
            this.noofpieces = noofpieces;
            this.cost = cost;
            this.amt = amt;
        }

    }

    public class DataFish {


        public String Did;
        public String Dcategory;
        public String Dprice;


        public DataFish(String did, String dcategory, String dprice) {
            Did = did;
            Dcategory = dcategory;
            Dprice = dprice;
        }



    }

    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DataFish2> data2 = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<DataFish2> data5) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data2 = data5;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.rowform, parent, false);
            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);
            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;
            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);
            final DataFish2 current = data2.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);



            myHolder.item.setText(current.item);
            myHolder.noofpices.setText(current.noofpieces);
            myHolder.cost.setText(current.cost);
            myHolder.amount.setText(current.amt);

            myHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                   Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();


                    fourdour.remove(current.item);
                    filterdata2.remove(position);

                    //  dd.add(current.item);
                    //                  tarif.add("fsd","rtt","trer");
                    //                 Adapter.notifyDataSetChanged();

                    Adapter = new AdapterFish(ExistingData.this, filterdata2);
                    Adapter.setHasStableIds(false);
                    mRVFishPrice.setAdapter(Adapter);
                    mRVFishPrice.setHasFixedSize(false);
                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(ExistingData.this,LinearLayoutManager.VERTICAL,false));
                    float sum = 0;
                    for (int i = 0; i < filterdata2.size(); i++) {

                        Float dd = Float.parseFloat(filterdata2.get(i).amt);
                        sum += dd;
                    }

                    //  btmamt.setText("Sub Total = " +String.valueOf(sum));

                    s =  ((0/100) *sum)+sum;
                    btmtotal.setText("Total = " +getResources().getString(R.string.rupee)+String.valueOf(s)+"(Inc of all taxes)");

                }
            });

            myHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //                   Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final EditText edittext = new EditText(context);
                    builder.setView(edittext);
                    builder.setMessage("Update Quantity")
                            .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String YouEditTextValue = edittext.getText().toString();
                                    edittext.setInputType(InputType.TYPE_CLASS_NUMBER |
                                            InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                            InputType.TYPE_NUMBER_FLAG_SIGNED);
//                                    Toast.makeText(context, YouEditTextValue, Toast.LENGTH_SHORT).show();
                                    try {
                                        int num = Integer.parseInt(YouEditTextValue);
                                        Log.i("",num+" is a number");

                                        Float foo = Float.parseFloat(YouEditTextValue);
                                        Float fo2 = Float.parseFloat(current.cost);
                                        Float x = foo * fo2;
                                        String suu =Float.toString(x);


                                        filterdata2.set(position, new DataFish2(current.item,YouEditTextValue,current.cost,suu));
                                        Adapter = new AdapterFish(ExistingData.this, filterdata2);
                                        Adapter.setHasStableIds(false);
                                        mRVFishPrice.setAdapter(Adapter);
                                        mRVFishPrice.setHasFixedSize(false);
                                        mRVFishPrice.setLayoutManager(new LinearLayoutManager(ExistingData.this,LinearLayoutManager.VERTICAL,false));
                                        float sum = 0;
                                        for (int i = 0; i < filterdata2.size(); i++) {

                                            Float dd = Float.parseFloat(filterdata2.get(i).amt);
                                            sum += dd;
                                        }

                                        //  btmamt.setText("Sub Total = " +String.valueOf(sum));

                                        s =  ((0.0/100) *sum)+sum;
                                        btmtotal.setText("Total = " +getResources().getString(R.string.rupee)+String.valueOf(s)+"(Inc of all taxes)");
                                        Log.e("rererer", String.valueOf(s));
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(context, "Enter only numbers", Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });
                    builder.show();
                    // filterdata2.set(position, new DataFish2(current.item,"XX","XYZ"));


                }
            });



        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data2.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView item;
            TextView noofpices;
            TextView cost;
            TextView amount;
            Button plus;
            ImageButton minus;
            ImageButton delete;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                item = (TextView)itemView.findViewById(R.id.item);
                noofpices = (TextView)itemView.findViewById(R.id.noofpices);
                cost = (TextView)itemView.findViewById(R.id.cost);
                amount = (TextView)itemView.findViewById(R.id.total);
                plus = (Button)itemView.findViewById(R.id.plus);
                minus = (ImageButton)itemView.findViewById(R.id.minus);
                delete = (ImageButton)itemView.findViewById(R.id.del);
                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExistingData.this,Dashpage.class);
        startActivity(intent);
    }
}
