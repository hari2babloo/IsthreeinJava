//office
package com.example.hari.isthreeinjava;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.Tariff;
import com.example.hari.isthreeinjava.Models.TinyDB;
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
import java.util.Scanner;

public class Pickup extends AppCompatActivity {


    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    JSONArray jsonArray;
    ListView plist;
    List<Tariff> tarif,tarrif2;
    RecyclerView mRVFishPrice,mRVFishPrice2;

    private AdapterFish2 Adapter2;
    List<DataFish> filterdata=new ArrayList<DataFish>();
    ArrayList<DataFish2> filterdata2=new ArrayList<DataFish2>();

    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> prize = new ArrayList<>();

    String mMessage;
    Button pay;
    TinyDB tinyDB;
    double s;

    DataFish data = new DataFish();
    //    DataFish2 data2 = new DataFish2();
    String price,type,quantity,amount;
    TextView btmamt,btmtotal;
    TableLayout tableLayout;
    Spinner spinner;
    EditText qty;
    Button add;
    final ArrayList<String> dd = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup);
        tinyDB = new TinyDB(this);
        pay = (Button)findViewById(R.id.pay);

        spinner  = (Spinner) findViewById(R.id.spinner);
        qty = (EditText)findViewById(R.id.qty);
        add = (Button)findViewById(R.id.add) ;

        mRVFishPrice2 = (RecyclerView)findViewById(R.id.fishPriceList2);
        btmamt = (TextView)findViewById(R.id.btmamt);
        tableLayout = (TableLayout)findViewById(R.id.tabl);
        tableLayout.setVisibility(View.GONE);
        btmtotal = (TextView)findViewById(R.id.btmtotal);
        pay.setVisibility(View.GONE);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paydata();
            }
        });

        GetFormData();
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
                        final Dialog openDialog = new Dialog(Pickup.this);
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

                mMessage = response.body().string();

                Log.e("result",mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONArray array = new JSONArray(mMessage);
                                for(int j = 0; j < array.length(); j++){

                                    JSONObject json_data = array.getJSONObject(j);
                                    items.add(json_data.getString("category"));
                                    prize.add(json_data.getString("price"));
                                    Log.e("Dta",dd.toString());
                                }


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_spinner_item,items);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);
                                    price = prize.get(position);
                                    type = items.get(position);


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
                                        Toast.makeText(Pickup.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                                        //myHolder.qty.setError("empty");
                                    }
                                    else if (quantity.equalsIgnoreCase("0")){
                                        Toast.makeText(Pickup.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        if (filterdata2.isEmpty()){
                                            Float foo = Float.parseFloat(quantity);
                                            Float fo2 = Float.parseFloat(price);
                                            Float x = foo * fo2;
                                            amount =Float.toString(x);
                                            Log.e(type,quantity+price+amount);
                                            DataFish2 ss = new DataFish2(type, quantity, price, amount);
                                            filterdata2.add(ss);
                                            AddtoList();
                                        }
                                        else if (filterdata2.contains(type)){

                                            Log.e("NOt availavcle","Not available");

                                            for (int k = 0; k < filterdata2.size(); k++) {

                                                //String s = filterdata2.get(k).item;
                                                if (filterdata2.get(k).item.contains(type)) {
                                                    //  String oldqty = filterdata2.get(k).noofpieces;
                                                    Log.e(filterdata2.get(k).noofpieces, quantity);
                                                    Float foo1 = Float.parseFloat(quantity) + Float.parseFloat(filterdata2.get(k).noofpieces);
                                                    Float fo1 = Float.parseFloat(price);
                                                    Float xy = foo1 * fo1;
                                                    String amount2 = Float.toString(xy);
                                                    String newqty = Float.toString(foo1);
                                                    Log.e("exist", "exist");
                                                    DataFish2 ss = new DataFish2(filterdata2.get(k).item, newqty, price, amount2);

                                                    //  filterdata2.remove(k);
                                                    filterdata2.add(k, ss);

                                                    AddtoList();

                                                }
//                                                else{
//
//                                                    Float foo = Float.parseFloat(quantity);
//                                                    Float fo2 = Float.parseFloat(price);
//                                                    Float x = foo * fo2;
//                                                    amount =Float.toString(x);
//                                                    DataFish2 ss = new DataFish2(type, quantity, price, amount);
//                                                    filterdata2.set(k,ss);
//
//                                                    AddtoList();
//                                                    break;
//                                                }
                                            }


                                        }
                                        else  {


                                            Float foo = Float.parseFloat(quantity);
                                            Float fo2 = Float.parseFloat(price);
                                            Float x = foo * fo2;
                                            amount =Float.toString(x);
                                            DataFish2 ss = new DataFish2(type, quantity, price, amount);
                                            filterdata2.add(ss);
                                            AddtoList();
                                        }

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




    public class DataFish {
        public String Type;
        public String cost;
        public ArrayList<String> spinlist;

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

    private void AddtoList() {

     //   Log.e("ononcontains","oncontains");          // Log.e(u,u);
        Adapter2 = new AdapterFish2(Pickup.this,filterdata2);
        Adapter2.setHasStableIds(false);
        mRVFishPrice2.setAdapter(Adapter2);
        mRVFishPrice2.setHasFixedSize(false);
        mRVFishPrice2.setLayoutManager(new LinearLayoutManager(Pickup.this,LinearLayoutManager.VERTICAL,false));
        float sum = 0;
        for (int i = 0; i < filterdata2.size(); i++) {

            Float dd = Float.parseFloat(filterdata2.get(i).amt);
            sum += dd;
        }
        Log.e("rererer", String.valueOf(sum));
        //   btmamt.setText("Sub Total = " +String.valueOf(sum));
        double s =  ((18/100) *sum)+sum;
        btmtotal.setText("Total = "+getResources().getString(R.string.rupee) +String.valueOf(s)+"(Inc of all taxes)");

        pay.setVisibility(View.VISIBLE);


    }

    public class AdapterFish2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DataFish2> data2 = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish2(Context context, List<DataFish2> data5) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data2 = data5;
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.rowform, parent, false);
            final MyHolder holder = new MyHolder(view);
            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final MyHolder myHolder = (MyHolder) holder;
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

                    filterdata2.remove(position);

                    //  dd.add(current.item);
                    //                  tarif.add("fsd","rtt","trer");
                    //                 Adapter.notifyDataSetChanged();

                    Adapter2 = new AdapterFish2(Pickup.this, filterdata2);
                    Adapter2.setHasStableIds(false);
                    mRVFishPrice2.setAdapter(Adapter2);
                    mRVFishPrice2.setHasFixedSize(false);
                    mRVFishPrice2.setLayoutManager(new LinearLayoutManager(Pickup.this,LinearLayoutManager.VERTICAL,false));
                    float sum = 0;
                    for (int i = 0; i < filterdata2.size(); i++) {

                        Float dd = Float.parseFloat(filterdata2.get(i).amt);
                        sum += dd;
                    }
                    Log.e("rererer", String.valueOf(sum));
                    //btmamt.setText("Sub Total = " +String.valueOf(sum));

                    s =  ((18.0/100) *sum)+sum;
                    btmtotal.setText("Total = " +getResources().getString(R.string.rupee) +String.valueOf(s)+"(Inc of all taxes)");
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
                                        Adapter2 = new AdapterFish2(Pickup.this, filterdata2);
                                        Adapter2.setHasStableIds(false);
                                        mRVFishPrice2.setAdapter(Adapter2);
                                        mRVFishPrice2.setHasFixedSize(false);
                                        mRVFishPrice2.setLayoutManager(new LinearLayoutManager(Pickup.this,LinearLayoutManager.VERTICAL,false));
                                        float sum = 0;
                                        for (int i = 0; i < filterdata2.size(); i++) {

                                            Float dd = Float.parseFloat(filterdata2.get(i).amt);
                                            sum += dd;
                                        }

                                        //  btmamt.setText("Sub Total = " +String.valueOf(sum));

                                        double s =  ((18.0/100) *sum)+sum;
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
        for (int i=0;i<filterdata2.size();i++){

            quantity.put(filterdata2.get(i).noofpieces);
        }

        String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        try {
            postdat.put("status", "PICKUP-CONFIRMED");
            postdat.put("gstPercentage", "18%");
            postdat.put("customerId",tinyDB.getString("custid"));
            postdat.put("grandTotal",String.valueOf(s));
            postdat.put("invoiceDateTime",timeStamp2);
            postdat.put("jobid",tinyDB.getString("jobid"));
            postdat.put("itemType",itemType);
            postdat.put("unitPrice",unitPrice);
            postdat.put("subTotal",subTotal);
            postdat.put("quantity",quantity);

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("array", String.valueOf(postdat));
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/deliveryInvoice")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(Pickup.this);
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

                mMessage = response.body().string();
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

                                    if (jsonResponse.getString("statusCode").equalsIgnoreCase("0")){

                                        final Dialog openDialog = new Dialog(Pickup.this);
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

//                                                //                                          Toast.makeText(Pickup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Pickup.this,Dashpage.class);
//                                                startActivity(intent);
                                            }
                                        });

                                        openDialog.show();
                                    }

                                    else if (jsonResponse.getString("statusCode").equalsIgnoreCase("1")){



                                        final Dialog openDialog = new Dialog(Pickup.this);
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

                                                //                                          Toast.makeText(Pickup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Pickup.this,Dashpage.class);
                                                startActivity(intent);
                                            }
                                        });
                                        //
                                        Log.e("json",sss);

                                        openDialog.show();
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
