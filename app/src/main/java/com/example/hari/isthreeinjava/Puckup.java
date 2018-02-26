//office
package com.example.hari.isthreeinjava;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Models.Tariff;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Puckup extends AppCompatActivity {


    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    ListView plist;
     List<Tariff> tarif;
    RecyclerView mRVFishPrice,mRVFishPrice2;
    private AdapterFish Adapter;
    private AdapterFish2 Adapter2;
    List<DataFish> filterdata=new ArrayList<DataFish>();
    List<DataFish2> filterdata2=new ArrayList<DataFish2>();
     String mMessage;
    DataFish data = new DataFish();
//    DataFish2 data2 = new DataFish2();
    String price,type,quantity,amount;
    TextView btmamt,btmtotal;
    TableLayout tableLayout;
    final ArrayList<String> dd = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puckup);
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
        mRVFishPrice2 = (RecyclerView)findViewById(R.id.fishPriceList2);
        btmamt = (TextView)findViewById(R.id.btmamt);
        tableLayout = (TableLayout)findViewById(R.id.tabl);
        tableLayout.setVisibility(View.GONE);
        btmtotal = (TextView)findViewById(R.id.btmtotal);
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
            }

            @Override
            public void onResponse(Response response) throws IOException {

                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           Displaylist();

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

    private void Displaylist() {

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Tariff>>(){}.getType();
       tarif = (List<Tariff>)  gson.fromJson(mMessage,listType);

        for(int j = 0; j < tarif.size(); j++){
            tarif.get(j).getType();
            dd.add(tarif.get(j).getType());
            Log.e("Dta",dd.toString());
        }

        try {
            JSONArray jsonArray = new JSONArray(mMessage);
            for (int i = 0; i < jsonArray.length(); i++) {



//                JSONObject json_data = jsonArray.getJSONObject(i);

//                data.cost = (json_data.getString("price"));
//                data.Type = (json_data.getString("Type"));




      //          Log.e("fdsfds",json_data.getString("Type")+json_data.getString("price"));
            }
            data.spinlist = (dd);
            filterdata.add(data);
            Adapter = new AdapterFish(Puckup.this, filterdata);
            Adapter.setHasStableIds(false);
            mRVFishPrice.setAdapter(Adapter);
            mRVFishPrice.setHasFixedSize(false);
            //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
            //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
            mRVFishPrice.setLayoutManager(new LinearLayoutManager(Puckup.this,LinearLayoutManager.VERTICAL,true));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//                            dataModels.add((prizes) gson.fromJson(mMessage,listType));

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
    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DataFish> data = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<DataFish> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.row, parent, false);

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
            final DataFish current = data.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_spinner_item,current.spinlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            myHolder.spinner.setAdapter(adapter);
            myHolder.qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    quantity = myHolder.qty.getText().toString();
                    Toast.makeText(context, quantity, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            myHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    price = tarif.get(position).getPrice();
                    type = tarif.get(position).getType();
                    Toast.makeText(context,price, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            myHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (TextUtils.isEmpty(quantity)){
                        Toast.makeText(context, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                        //myHolder.qty.setError("empty");


                    }

else
                    {
                        AddtoList();
                        tableLayout.setVisibility(View.VISIBLE);

                    }

                }
            });

        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
          EditText qty;
            Spinner spinner;
            ImageButton plus;
            ImageButton minus;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                qty = (EditText)itemView.findViewById(R.id.qty);
                spinner = (Spinner)itemView.findViewById(R.id.spinner);
                plus = (ImageButton)itemView.findViewById(R.id.plus);
                minus = (ImageButton)itemView.findViewById(R.id.minus);
                //  id= (TextView)itemView.findViewById(R.id.id);
            }

        }

    }
    private void AddtoList() {
                   // Log.e(u,u);
                    Float foo = Float.parseFloat(quantity);
                    Float fo2 = Float.parseFloat(price);
                    Float x = foo * fo2;
                    amount =Float.toString(x);
        filterdata2.add(new DataFish2(type,quantity,price,amount));

        Log.e("Arraydata",filterdata2.toString());
        Adapter2 = new AdapterFish2(Puckup.this, filterdata2);
        Adapter2.setHasStableIds(false);
        mRVFishPrice2.setAdapter(Adapter2);
        mRVFishPrice2.setHasFixedSize(false);
        mRVFishPrice2.setLayoutManager(new LinearLayoutManager(Puckup.this,LinearLayoutManager.VERTICAL,false));
        float sum = 0;
        for (int i = 0; i < filterdata2.size(); i++) {

            Float dd = Float.parseFloat(filterdata2.get(i).amt);
            sum += dd;
        }
        Log.e("rererer", String.valueOf(sum));
        btmamt.setText("Sub Total = " +String.valueOf(sum));

        double s =  ((18.0/100) *sum)+sum;
        btmtotal.setText("Total = " +String.valueOf(s));

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
                    Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();

                    filterdata2.remove(position);
                    Adapter2 = new AdapterFish2(Puckup.this, filterdata2);
                    Adapter2.setHasStableIds(false);
                    mRVFishPrice2.setAdapter(Adapter2);
                    mRVFishPrice2.setHasFixedSize(false);
                    mRVFishPrice2.setLayoutManager(new LinearLayoutManager(Puckup.this,LinearLayoutManager.VERTICAL,false));
                    float sum = 0;
                    for (int i = 0; i < filterdata2.size(); i++) {

                        Float dd = Float.parseFloat(filterdata2.get(i).amt);
                        sum += dd;
                    }
                    Log.e("rererer", String.valueOf(sum));
                    btmamt.setText("Sub Total = " +String.valueOf(sum));

                    double s =  ((18.0/100) *sum)+sum;
                    btmtotal.setText("Total = " +String.valueOf(s));
                }
            });
            myHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final EditText edittext = new EditText(context);
                    builder.setView(edittext);
                    builder.setMessage("Update Quantity")
                            .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String YouEditTextValue = edittext.getText().toString();
                                    Toast.makeText(context, YouEditTextValue, Toast.LENGTH_SHORT).show();
                                    Float foo = Float.parseFloat(YouEditTextValue);
                                    Float fo2 = Float.parseFloat(current.cost);
                                    Float x = foo * fo2;
                                    String suu =Float.toString(x);
                                    filterdata2.set(position, new DataFish2(current.item,YouEditTextValue,current.cost,suu));
                                    Adapter2 = new AdapterFish2(Puckup.this, filterdata2);
                                    Adapter2.setHasStableIds(false);
                                    mRVFishPrice2.setAdapter(Adapter2);
                                    mRVFishPrice2.setHasFixedSize(false);
                                    mRVFishPrice2.setLayoutManager(new LinearLayoutManager(Puckup.this,LinearLayoutManager.VERTICAL,false));
                                    float sum = 0;
                                    for (int i = 0; i < filterdata2.size(); i++) {

                                        Float dd = Float.parseFloat(filterdata2.get(i).amt);
                                        sum += dd;
                                    }

                                    btmamt.setText("Sub Total = " +String.valueOf(sum));

                                    double s =  ((18.0/100) *sum)+sum;
                                    btmtotal.setText("Total = " +String.valueOf(s));
                                    Log.e("rererer", String.valueOf(s));

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
            ImageButton plus;
            ImageButton minus;
            ImageButton delete;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                item = (TextView)itemView.findViewById(R.id.item);
                noofpices = (TextView)itemView.findViewById(R.id.noofpices);
                cost = (TextView)itemView.findViewById(R.id.cost);
                amount = (TextView)itemView.findViewById(R.id.total);
                plus = (ImageButton)itemView.findViewById(R.id.plus);
                minus = (ImageButton)itemView.findViewById(R.id.minus);
                delete = (ImageButton)itemView.findViewById(R.id.del);
                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }
}
