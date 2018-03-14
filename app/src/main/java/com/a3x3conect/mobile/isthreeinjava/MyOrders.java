package com.a3x3conect.mobile.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.JobOrder;
import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.modelmyorders;
import com.example.hari.isthreeinjava.R;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyOrders extends AppCompatActivity {

    
    ProgressDialog pd;
    String mMessage;

    TinyDB tinyDB;

    List<modelmyorders> jobOrder ;
    private AdapterFish Adapter;
    RecyclerView mRVFishPrice;

    ArrayList<DataFish2> filterdata2 = new ArrayList<>();

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_orders);

        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList); 
        tinyDB  = new TinyDB(MyOrders.this);
        getmyorders();
    }

    private void getmyorders() {

        pd = new ProgressDialog(MyOrders.this);
        pd.setMessage("Getting Your Orders..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url("http://52.172.191.222/isthree/index.php/services/customerInvoices")
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
                        final Dialog openDialog = new Dialog(MyOrders.this);
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


                String mMessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {


                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();


                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONArray jsonArray = new JSONArray(mMessage);

                                for(int j = 0; j < jsonArray.length(); j++){

                                 String ss = jsonArray.get(j).toString();

                                 JSONObject  jsonObject = new JSONObject(ss);

                                 if(jsonObject.optString("statusCode").equalsIgnoreCase("0")){


                                        final Dialog openDialog = new Dialog(MyOrders.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("No Internet");
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

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MyOrders.this,Dashpage.class);
                                                startActivity(intent);
                                            }
                                        });



                                        openDialog.show();


                                 }
                                }



                                TraverseData();





                                } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Log.e("Resy",mMessage);
                            // Toast.makeText(MyOrders.this, mMessage, Toast.LENGTH_SHORT).show();


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

        Gson gson = new Gson();
        Type listType = new TypeToken<List<modelmyorders>>(){}.getType();
        jobOrder = (List<modelmyorders>)  gson.fromJson(mMessage,listType);



        for(int j = 0; j < jobOrder.size(); j++){

            DataFish2 dd = new DataFish2(jobOrder.get(j).getDate(),jobOrder.get(j).getDeliveryStatus(),jobOrder.get(j).getInvoiceId());
            filterdata2.add(dd);

        }


        Adapter = new AdapterFish(MyOrders.this, filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(MyOrders.this,LinearLayoutManager.VERTICAL,true));




    }


    public class DataFish2 {
        public String date;
        public String status;
        public String invoicenum;



        public DataFish2(String date,String status,String invoicenum){

            this.date = date;
            this.status = status;
            this.invoicenum = invoicenum;

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
            View view = inflater.inflate(R.layout.myordersrow, parent, false);
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

myHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        Intent intent = new Intent(MyOrders.this,MyOrderDetails.class);

        intent.putExtra("message",mMessage);
        intent.putExtra("position",position);
        startActivity(intent);


    }
});
            final DataFish2 current = data2.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);


            myHolder.status.setText(current.status);
            myHolder.date.setText(current.date);


        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data2.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView date;
            TextView status;


            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                date = (TextView)itemView.findViewById(R.id.date);
                status = (TextView)itemView.findViewById(R.id.status);

                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }
}
