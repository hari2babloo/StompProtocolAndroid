package com.androidhari.tambola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.ViewPager.WalletTransactions;
import com.androidhari.db.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.naiksoftware.tambola.MainActivity;
import ua.naiksoftware.tambola.R;

import static android.view.View.GONE;

public class PurchaseTicket extends AppCompatActivity {


    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    SharedPreferences sp;
    String  pass;
    String gid,gamestarttime;



    ProgressDialog pd;
    JSONArray idsarray;

    private AdapterFish mAdapter;
    JSONArray postdata2 = new JSONArray();

    ArrayList<String> checkbox2 = new ArrayList<String>();
    List al = new ArrayList();

    JSONArray jsonArray5 = new JSONArray();
    ArrayList row1 = new ArrayList();
    ArrayList<String> tktrow1 = new ArrayList<String>();
    ArrayList<String> tktrow2 = new ArrayList<String>();
    ArrayList<String> tktrow3 = new ArrayList<String>();
    ArrayList<JSONObject> listdata = new ArrayList<>();
    Button checkout;
    String mMessage;
    ArrayList<String> tktids = new ArrayList<>();

    List<DataFish> filterdata=new ArrayList<>();

    private RecyclerView mRVFishPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.purchase_ticket);

//        Intent intent = getIntent();
//        String jsonArray = intent.getStringExtra("ids");
//
//        if (jsonArray != null && !jsonArray.isEmpty()) {
//
//            try {
//                idsarray = new JSONArray(jsonArray);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            // doSomething
//        }
//
//        else {
//
//            try {
//                idsarray = new JSONArray(jsonArray);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }




        checkout = (Button)findViewById(R.id.checkout);
        sp=getSharedPreferences("login",MODE_PRIVATE);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         pass=sp.getString("token",null);

         gid = sp.getString("id",null);
        gamestarttime = sp.getString("gstime",null);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (al.size()==0){

                  //  Toast.makeText(PurchaseTicket.this, "Please Select Tickets", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please Select Tickets", Snackbar.LENGTH_LONG);

                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
                else if (al.size()<2){

                 //  Toast.makeText(PurchaseTicket.this, "Select Minimum Two Tickets", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Select Minimum Two Tickets", Snackbar.LENGTH_LONG);

                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
                else if(al.size()>=2){


                  //  Checkout();
//                    TinyDB tinydb = new TinyDB(PurchaseTicket.this);
//
//
                    Intent inte = new Intent(PurchaseTicket.this,Cart.class);
                    moveTaskToBack(true);


                    inte.putExtra("ids",jsonArray5.toString());

//                    Bundle args = new Bundle();
//                    inte.putStringArrayListExtra("ids", (ArrayList<String>) al);
                   inte.putExtra("tkts",mMessage);
//                    inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    finish();
//                   // args.putSerializable("ARRAYLIST",(Serializable)object);
//    //                inte.putExtra("BUNDLE",al);
//
                   startActivity(inte);
                }

//                if (postdata2.length()==0){
//
//                    CrossAlert();
// //                   Toast.makeText(PurchaseTicket.this, "Emppty", Toast.LENGTH_SHORT).show();
//                    //Checkout();
//                }
//                if (postdata2.length()!=0){
//                    Checkout();
// //                   Toast.makeText(PurchaseTicket.this, "Not Empty", Toast.LENGTH_SHORT).show();
//                }

            }
        });
        //Table();


        Authenticate();
    }

    private void CrossAlert() {


        ImageView image = new ImageView(PurchaseTicket.this);
        image.setImageResource(R.drawable.cross);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(PurchaseTicket.this).
                        setMessage("Please Select Ticket to Purchase").
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        setView(image);
        builder.create().show();
    }


    private void Authenticate() {

        pd = new ProgressDialog(PurchaseTicket.this);
        pd.setMessage("Getting Your Tickets");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/tickets/"+gid)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)

                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                pd.dismiss();
                pd.cancel();

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mMessage, Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                mMessage = response.body().string();


                pd.dismiss();
                pd.cancel();

//                Log.w("Response", mMessage);

                if (response.isSuccessful()){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                String mm = mMessage;
                                mm=mMessage.replace("null","' '");
                                JSONObject json = new JSONObject(mm);

                                JSONArray jsonArray = json.getJSONArray("data");






                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject json_data = jsonArray.getJSONObject(i);
                                    DataFish data = new DataFish();

                                    String s = json_data.getString("id");
                                    String val = new String(json_data.getString("id"));
                                    String ticket = json_data.getString("ticket");

//                                    if (idsarray.toString().contains(val)){
//
//                                        Toast.makeText(PurchaseTicket.this, "", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    else {

                                        JSONArray row1 = (JSONArray) new JSONArray(ticket).get(0);
                                        JSONArray row2 = (JSONArray) new JSONArray(ticket).get(1);
                                        JSONArray row3 = (JSONArray) new JSONArray(ticket).get(2);



//                                    row1.add(dasd.get(0));
//                                    row2.add(dasd.get(1));
//                                    row3.add(dasd.get(2));
      //                                  String s = json_data.getString("id");

                                        for ( int j = 0; j <row1.length();j++ ){



                                            tktrow1.add(row1.getString(j));

                                            Log.w("Row 1 ", String.valueOf(tktrow1));

                                        }

                                        for ( int j = 0; j <row2.length();j++ ){



                                            tktrow2.add(row2.getString(j));

                                            Log.w("Row 2", String.valueOf(tktrow2));

                                        }


                                        for ( int j = 0; j <row3.length();j++ ){



                                            tktrow2.add(row3.getString(j));

                                            Log.w("Row 3", String.valueOf(tktrow3));

                                        }
                                        data.id = s;
                                        data.t1 = row1.getString(0);
                                        data.t2 = row1.getString(1);
                                        data.t3 = row1.getString(2);
                                        data.t4 = row1.getString(3);
                                        data.t5 = row1.getString(4);
                                        data.t6 = row1.getString(5);
                                        data.t7 = row1.getString(6);
                                        data.t8 = row1.getString(7);
                                        data.t9 = row1.getString(8);
                                        data.t10 = row2.getString(0);
                                        data.t11 = row2.getString(1);
                                        data.t12 = row2.getString(2);
                                        data.t13 = row2.getString(3);
                                        data.t14 = row2.getString(4);
                                        data.t15 = row2.getString(5);
                                        data.t16 = row2.getString(6);
                                        data.t17 = row2.getString(7);
                                        data.t18 = row2.getString(8);
                                        data.t19 = row3.getString(0);
                                        data.t20 = row3.getString(1);
                                        data.t21 = row3.getString(2);
                                        data.t22 = row3.getString(3);
                                        data.t23 = row3.getString(4);
                                        data.t24 = row3.getString(5);
                                        data.t25 = row3.getString(6);
                                        data.t26 = row3.getString(7);
                                        data.t27 = row3.getString(8);


                                        filterdata.add(data);
//                                    Log.w("Response", data.t1);

                                    }





                                //}

                                Log.e("Data", String.valueOf(row1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//                            Table();
                            mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                            mAdapter = new AdapterFish(PurchaseTicket.this, filterdata);
                            mRVFishPrice.setAdapter(mAdapter);
                            mRVFishPrice.setHasFixedSize(true);

                            mRVFishPrice.setLayoutManager(new GridLayoutManager(PurchaseTicket.this,1));
//                            Toast.makeText(PurchaseTicket.this, "PASS", Toast.LENGTH_SHORT).show();


                        }
                    });


//                    mAdapter.notifyDataSetChanged();

                }

                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(mMessage);
                            String status = json.getString("status");
                            String message = json.getString("message");
                            //title = name;
                            if (status.equalsIgnoreCase("401")){
                                sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                Toast.makeText(PurchaseTicket.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PurchaseTicket.this,Signin.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });
    }

    private void Table() {

        LinearLayout table = (LinearLayout) findViewById(R.id.myTableLayout);
        for(int i=0;i<row1.size();i++)
        {

            TableRow row=new TableRow(this);
            String debt = tktrow1.get(i);
            String fee = tktrow2.get(i);
            TextView tvDebt=new TextView(this);
            tvDebt.setText(""+debt);
            TextView tvFee=new TextView(this);
            tvFee.setText(""+fee);
            row.addView(tvDebt);
            row.addView(tvFee);
            table.addView(row);
//            TableRow row=new TableRow(this);
//            double debt = debtList.get(i);
//            double fee = feeList.get(i);
//            TextView tvDebt=new TextView(this);
//            tvDebt.setText(""+debt);
//            TextView tvFee=new TextView(this);
//            tvFee.setText(""+fee);
//            row.addView(tvDebt);
//            row.addView(tvFee);
//            table.addView(row);
        }
    }


    public class DataFish {



        public String preferredName;
        public String presentCount;
        public String rollGroup;
        public String id;
        public String t1;
        public String t2;
        public String t3;
        public String t4;
        public String t5;
        public String t6;
        public String t7;
        public String t8;
        public String t9;
        public String t10;
        public String t11;
        public String t12;
        public String t13;
        public String t14;
        public String t15;
        public String t16;
        public String t17;
        public String t18;
        public String t19;
        public String t20;
        public String t21;
        public String t22;
        public String t23;
        public String t24;
        public String t25;
        public String t26;
        public String t27;


    }

    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

            SparseBooleanArray selectedItems = new SparseBooleanArray();


        List<DataFish> data = Collections.emptyList();
        DataFish current;
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
            View view = inflater.inflate(R.layout.ticket, parent, false);

            final MyHolder holder = new MyHolder(view);

            return holder;
        }



        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


          //  holder.myBackground.setSelected(selectedItems.get(position, false));

            // Get current position of item in recyclerview to bind data and assign values from list
            final MyHolder myHolder = (MyHolder) holder;
            holder.setIsRecyclable(false);
//            setHasStableIds(false);
            final DataFish current = data.get(position);

            //myHolder.checkBox.setChecked(selectedItems.get(position, false));
            //.myBackground.setSelected(selectedItems.get(position, false));



//            holder.itemView.setBackgroundColor(current.isSelected() ? Color.CYAN : Color.WHITE);




            String s = data.get(position).id;
            myHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (selectedItems.get(position, false)) {

                        //if it is in the array we delete it. So clicking a second time on an item will uncheck it.
                        selectedItems.delete(position);

                        for (int i = 0; i < jsonArray5.length(); i++) {
                            try {
                                if (jsonArray5.get(i).equals(data.get(position).id)) {
                                    jsonArray5.remove(i);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        al.remove(new Integer(data.get(position).id));
                        myHolder.checkBox.setChecked(selectedItems.get(position,false));


                  //      myHolder.checkBox.setChecked(selectedItems.get(position, false));

                        Log.e("sapared", String.valueOf(jsonArray5));
                        Log.e("sapare", String.valueOf(selectedItems));
                    }
                    else {
                        selectedItems.put(position, true);
                        // here get a ref to the view checkbox and *check* it

                        al.add(new Integer(data.get(position).id));


                        jsonArray5.put(data.get(position).id);
                        myHolder.checkBox.setChecked(selectedItems.get(position,true));
                        Log.e("sapared", String.valueOf(jsonArray5));
                        Log.e("selecteditems sapare", String.valueOf(selectedItems));
                    }

//                    SparseBooleanArray selectedItems = new SparseBooleanArray();
                    // Save the selected positions to the SparseBooleanArray
//
//                    if (b==true){
//
//                        Log.e("postdat2", String.valueOf(b));
//
//                        postdata2.put(data.get(position).id);
//                         ;   //.put(data.get(position).id);
//                    //    myHolder.checkBox.setChecked(false);
//                        Log.e("postdat", String.valueOf(postdata2));
//
//                    }
//
//                    else
//                    {
//                        Log.e("postdat3", String.valueOf(b));
//                    //    postdata2.remove(data.get(position).id);   //.put(data.get(position).id);
//                      //  myHolder.checkBox.setChecked(true);
//                        Log.e("postdat", String.valueOf(postdata2));
//
//                    }

//                    claimid = data.get(position).id;
                    postdata2.put(data.get(position).id);
//                    filterdata.remove(current);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position,filterdata.size());
//                    Toast.makeText(PurchaseTicket.this, current.id, Toast.LENGTH_SHORT).show();

                    Log.e("Selected TicketIds", postdata2.toString());
 //                   Toast.makeText(PurchaseTicket.this, current.id, Toast.LENGTH_SHORT).show();

                    //                  Log.e("Final", String.valueOf(isChecked));

                }
            });



            myHolder.id.setText("Select Ticket " );


//            myHolder.one.setText("Name: " + current.preferredName + "  " + current.surname);
            myHolder.one.setText(current.t1);
            myHolder.two.setText( current.t2);
            myHolder.three.setText(current.t3);
            myHolder.four.setText(current.t4);
            myHolder.five.setText(current.t5);
            myHolder.six.setText(current.t6);
            myHolder.seven.setText(current.t7);
            myHolder.eight.setText(current.t8);
            myHolder.nine.setText(current.t9);
            myHolder.ten.setText(current.t10);
            myHolder.eleven.setText( current.t11);
            myHolder.twelve.setText(current.t12);
            myHolder.thirteen.setText(current.t13);
            myHolder.fourteen.setText(current.t14);
            myHolder.fifteen.setText(current.t15);
            myHolder.sixteen.setText(current.t16);
            myHolder.seventeen.setText(current.t17);
            myHolder.eighteen.setText(current.t18);
            myHolder.nineteen.setText(current.t19);
            myHolder.twenty.setText( current.t20);
            myHolder.twentyone.setText(current.t21);
            myHolder.twentytwo.setText(current.t22);
            myHolder.twentythree.setText(current.t23);
            myHolder.twentyfour.setText(current.t24);
            myHolder.twentyfive.setText(current.t25);
            myHolder.twentysix.setText(current.t26);
            myHolder.twentyseven.setText(current.t27);


//            myHolder.nine.setText(current.t9);



//            myHolder.two.setVisibility(View.GONE);
//            myHolder.three.setText("RollGroup: " + current.rollGroup);
//
//  myHolder.four.setText("Present Count: " + current.presentCount);
//            //  myHolder.textPrice.setText("Rs. " + current.Title + "\\Kg");

            // load image into imageview using glide
//            Glide.with(context).load("http://192.168.1.7/test/images/" + current.fishImage)
//                    .placeholder(R.drawable.ic_img_error)
//                    .error(R.drawable.ic_img_error)
//                    .into(myHolder.ivFish);

        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }






        class MyHolder extends RecyclerView.ViewHolder {

            TextView one;

            TextView id;
            CheckBox checkBox;
            Button claim;

            TextView two;
            TextView three;
            TextView four;
            TextView five;
            TextView six;
            TextView seven;
            TextView eight;
            TextView nine;
            TextView ten;
            TextView eleven;
            TextView twelve;
            TextView thirteen;
            TextView fourteen;
            TextView fifteen;
            TextView sixteen;
            TextView seventeen;
            TextView eighteen;
            TextView nineteen;
            TextView twenty;
            TextView twentyone;
            TextView twentytwo;
            TextView twentythree;
            TextView twentyfour;
            TextView twentyfive;
            TextView twentysix;
            TextView twentyseven;
            ImageButton imgbtn;


            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                id= (TextView)itemView.findViewById(R.id.id);
                one = (TextView) itemView.findViewById(R.id.t1);
                two = (TextView) itemView.findViewById(R.id.t2);
                three = (TextView) itemView.findViewById(R.id.t3);
                four = (TextView) itemView.findViewById(R.id.t4);
                five = (TextView) itemView.findViewById(R.id.t5);
                six = (TextView) itemView.findViewById(R.id.t6);
                seven = (TextView) itemView.findViewById(R.id.t7);
                eight = (TextView) itemView.findViewById(R.id.t8);
                nine = (TextView) itemView.findViewById(R.id.t9);
                ten = (TextView)itemView.findViewById(R.id.t10);
                eleven= (TextView)itemView.findViewById(R.id.t11);
                twelve= (TextView)itemView.findViewById(R.id.t12);
                thirteen =  (TextView)itemView.findViewById(R.id.t13);
                fourteen= (TextView)itemView.findViewById(R.id.t14);
                fifteen= (TextView)itemView.findViewById(R.id.t15);
                sixteen= (TextView)itemView.findViewById(R.id.t16);
                seventeen= (TextView)itemView.findViewById(R.id.t17);
                eighteen= (TextView)itemView.findViewById(R.id.t18);
                nineteen= (TextView)itemView.findViewById(R.id.t19);
                twenty = (TextView)itemView.findViewById(R.id.t20);
                twentyone= (TextView)itemView.findViewById(R.id.t21);
                twentytwo= (TextView)itemView.findViewById(R.id.t22);
                twentythree= (TextView)itemView.findViewById(R.id.t23);
                twentyfour= (TextView)itemView.findViewById(R.id.t24);
                twentyfive= (TextView)itemView.findViewById(R.id.t25);
                twentysix= (TextView)itemView.findViewById(R.id.t26);
                twentyseven= (TextView)itemView.findViewById(R.id.t27);
                checkBox = (CheckBox)itemView.findViewById(R.id.checkedTextView);
                claim = (Button)itemView.findViewById(R.id.claim);
                claim.setVisibility(View.GONE);
                imgbtn = (ImageButton)itemView.findViewById(R.id.delete);
                imgbtn.setVisibility(GONE);




//                five.setVisibility(View.GONE);
//
//                six = (TextView) itemView.findViewById(R.id.six);
//                six.setVisibility(View.GONE);-
//                seven = (TextView) itemView.findViewById(R.id.seven);
//                seven.s
// etVisibility(View.GONE);
//                eight = (TextView) itemView.findViewById(R.id.eight);
//                eight.setVisibility(View.GONE);
//                nine = (TextView) itemView.findViewById(R.id.nine);
//                nine.setVisibility(View.GONE);
//                ten = (TextView) itemView.findViewById(R.id.ten);
//                ten.setVisibility(View.GONE);
            }



        }

    }


    private void Checkout() {

        pd = new ProgressDialog(PurchaseTicket.this);
        pd.setMessage("Purchasing Tickets");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();



        try {
            postdata.put("gameId", gid);
            postdata.put("ticketIds", postdata2);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());




        //       RequestBody body = RequestBody.create(mediaType, "{\n\"gameId\":\"78\",\n\"ticketIds\":[\"5\",\"5\",\"6\"]\n}");
        Log.w("Post", body.toString());
        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/game/buyticket")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("authorization",pass)
                .post(body)
                .build();


        Log.e("dasdasd", body.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.dismiss();
                pd.cancel();

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);  Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mMessage, Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String mMessage = response.body().string();
                pd.dismiss();
                pd.cancel();

                Log.w("Response", mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String s = json.getString("message");

                                Alert();

//                                Toast.makeText(PurchaseTicket.this, s, Toast.LENGTH_SHORT).show();

                                //   Toast.makeText(SigninForm.this, s, Toast.LENGTH_SHORT).show();
//                                Intent in = new Intent(PurchaseTicket.this,HomeScreen.class);
//                              startActivity(in);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//                            Toast.makeText(SigninForm.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                pd.cancel();
                                pd.dismiss();
                                JSONObject json = new JSONObject(mMessage);
                                String status = json.getString("status");
                                String message = json.getString("message");
                                //title = name;
                             //   Toast.makeText(PurchaseTicket.this, message, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
                                if (status.equalsIgnoreCase("401")){

                                    sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();




                                    Intent intent = new Intent(PurchaseTicket.this,Signin.class);
                                    startActivity(intent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }

            private void Alert() {



                ImageView image = new ImageView(PurchaseTicket.this);
                image.setImageResource(R.drawable.tick);

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(PurchaseTicket.this).
                                setMessage("You Have Succesfully Purchased").
                                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        SharedPreferences.Editor e = sp.edit();
                                        e.putString("gno",gid);
                                        e.putString("gstime",gamestarttime);

                                        e.commit();

                                      //  PurchaseTicket.this.finish();
                                        Intent intent = new Intent(PurchaseTicket.this,Countdown.class);

                                        startActivity(intent);

                                        dialog.dismiss();

                                    }
                                }).
                                setView(image);
                builder.create().show();
            }
        });


//        Log.e("Final", String.valueOf(isChecked));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homemenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // getMenuInflater().inflate(R.menu.homemenu, (Menu) item);
        int id = item.getItemId();


        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.action_item_two:

                Intent intent = new Intent(PurchaseTicket.this, WalletTransactions.class);
                startActivity(intent);
                // Do something
                return true;
            case R.id.action_item_one:

                Intent intent2 = new Intent(PurchaseTicket.this, HomeScreen.class);
                startActivity(intent2);
                // Do something
                return true;
            case R.id.action_item_three:

                Logout();
                // Do something
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }

    }

    private void Logout() {

        pd = new ProgressDialog(PurchaseTicket.this);
        pd.setMessage("Logging You Out");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/user/logout")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.dismiss();
                pd.cancel();
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mMessage, Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pd.dismiss();
                pd.cancel();
                final String mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);

                                String s = json.getString("message");


                          //      Toast.makeText(PurchaseTicket.this, s, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG);

                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
                                sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                             //   finish();

                                Intent in = new Intent(PurchaseTicket.this,FirstPage.class);
                                startActivity(in);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }

                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                pd.cancel();
                                pd.dismiss();
                                JSONObject json = new JSONObject(mMessage);
                                String status = json.getString("status");
                                String message = json.getString("message");
                                //title = name;

                                if (status.equalsIgnoreCase("401")){

                                    sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();



                            //        Toast.makeText(PurchaseTicket.this, message, Toast.LENGTH_SHORT).show();
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

                                    View snackBarView = snackbar.getView();
                                    snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    snackbar.show();
                                    Intent intent = new Intent(PurchaseTicket.this,Signin.class);
                                    startActivity(intent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }


}


