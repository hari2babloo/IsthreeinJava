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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.JobOrder;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.R;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SummaryReport extends AppCompatActivity {


ProgressDialog pd;
    RecyclerView mRVFishPrice;
    TableLayout tableLayout;
    TextView btmtotal,grdtotal;
    List<DataFish2> filterdata2=new ArrayList<DataFish2>();
    private AdapterFish Adapter;
    Button home;
    double s;
    TinyDB tinyDB;
    String mMessage2;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_report);
        tinyDB = new TinyDB(SummaryReport.this);
        home =  (Button)findViewById(R.id.home);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryReport.this,Dashpage.class);

                startActivity(intent);
            }
        });
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
        tableLayout = (TableLayout)findViewById(R.id.tabl);
        btmtotal = (TextView)findViewById(R.id.btmtotal);
        grdtotal = (TextView)findViewById(R.id.grdtotal);

     //   filterdata2 =   (ArrayList<DataFish2>)getIntent().getSerializableExtra("FILES_TO_SEND");
        
       getjoborder();
       
    }

    private void getjoborder() {

        pd = new ProgressDialog(SummaryReport.this);
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
                        final Dialog openDialog = new Dialog(SummaryReport.this);
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

//                                                //                                          Toast.makeText(SummaryReport.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
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


                                    DataFish2 ss = new DataFish2("item","qty","price","total");

                                    Float ss2 = Float.parseFloat(jobOrder.getPrice().get(i));

                                    Float ss3 =  Float.parseFloat(jobOrder.getQuantity().get(i));




                                    Float ss4 = ss2 * ss3;
                                    DataFish2 sds = new DataFish2(jobOrder.getCategory().get(i),jobOrder.getQuantity().get(i),jobOrder.getPrice().get(i),String.valueOf(ss4));


                                    filterdata2.add(sds);
                                }

                                float sum = 0;
                                float garmentscount = 0;
                                for (int i=0;i<filterdata2.size();i++){


                                    float foo = Float.parseFloat(filterdata2.get(i).noofpieces);
                                    float foo3 = Float.parseFloat(filterdata2.get(i).amt);



                                    sum+=foo3;

                                    garmentscount+= foo;
                                 //   quantity.put(filterdata2.get(i).noofpieces);
                                }

                                btmtotal.setText(String.valueOf(Math.round(garmentscount)));
                                s =  ((0.0/100) *sum)+sum;
                                grdtotal.setText("Total : " +getResources().getString(R.string.rupee)+String.format("%.2f",s)+"(Inc of all taxes)");
                                Adapter = new SummaryReport.AdapterFish(SummaryReport.this, filterdata2);
                                Adapter.setHasStableIds(false);
                                mRVFishPrice.setAdapter(Adapter);
                                mRVFishPrice.setHasFixedSize(false);
                                //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
                                //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
                                mRVFishPrice.setLayoutManager(new LinearLayoutManager(SummaryReport.this,LinearLayoutManager.VERTICAL,true));



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
            myHolder.plus.setVisibility(View.GONE);
//            myHolder.minus.setVisibility(View.GONE);
            myHolder.delete.setVisibility(View.GONE);




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

        Intent intent = new Intent(SummaryReport.this,Dashpage.class);
        startActivity(intent);
    }
}
