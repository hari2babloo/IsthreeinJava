package com.a3x3conect.mobile.isthreeinjava;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.JobOrder;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Models.modelmyorders;
import com.example.hari.isthreeinjava.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyOrderDetails extends AppCompatActivity {



    ProgressDialog pd;
    String mMessage;

    Button home;
    TextView jobid,status,date,grantotal,custid;

    TinyDB tinyDB;

    List<modelmyorders> jobOrder ;
    private AdapterFish Adapter;
    RecyclerView mRVFishPrice;
    Integer position;
    ArrayList<DataFish2> filterdata2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_details);


        home = (Button)findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyOrderDetails.this, Dashpage.class);
                startActivity(intent);
            }
        });
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
        jobid = (TextView)findViewById(R.id.jobid);
        status = (TextView)findViewById(R.id.delstatus);
        date = (TextView)findViewById(R.id.date);
        grantotal = (TextView)findViewById(R.id.grandtotal);
        custid = (TextView)findViewById(R.id.custid);
        Bundle bundle = getIntent().getExtras();
        mMessage = bundle.getString("message");
        position = bundle.getInt("position");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<modelmyorders>>(){}.getType();
        jobOrder = (List<modelmyorders>)  gson.fromJson(mMessage,listType);

        jobOrder.get(position).getDate();


        jobid.setText(jobOrder.get(position).getJobid());
        date.setText(jobOrder.get(position).getDate());
        status.setText(jobOrder.get(position).getDeliveryStatus());
        grantotal.setText(jobOrder.get(position).getGrandTotal());
        custid.setText(jobOrder.get(position).getCustomerId());



        for(int j = 0; j < jobOrder.get(position).getCategory().size(); j++){







            //           Log.e("String",jobOrder.get(j).toString());


       //     DataFish2 dd = new DataFish2("","","","");

            DataFish2 dd = new DataFish2(jobOrder.get(position).getCategory().get(j),jobOrder.get(position).getPrice().get(j),jobOrder.get(position).getQuantity().get(j),jobOrder.get(position).getSubTotal().get(j));


            filterdata2.add(dd);





        }

        Log.e("log",jobOrder.get(position).getDate());

//        for(int j = 0; j < jobOrder.size(); j++){
//
//
//            //           Log.e("String",jobOrder.get(j).toString());
//
//
//            MyOrders.DataFish2 dd = new MyOrders.DataFish2(jobOrder.get(j).getDate(),jobOrder.get(j).getDeliveryStatus(),jobOrder.get(j).getInvoiceId());
//
//            filterdata2.add(dd);
//
//
//
//
//
//        }


        Adapter = new AdapterFish(MyOrderDetails.this, filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(MyOrderDetails.this,LinearLayoutManager.VERTICAL,true));
    }

    public class DataFish2 {
        public String category;
        public String price;
        public String quantity;
        public String subtotal;



        public DataFish2(String category,String price,String quantity,String subtotal){

            this.category = category;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
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



            myHolder.item.setText(current.category);
            myHolder.noofpices.setText(current.quantity);
            myHolder.cost.setText(current.price);
            myHolder.amount.setText(current.subtotal);
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
}
