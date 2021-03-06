package com.androidhari.tambola;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import ua.naiksoftware.tambola.MainActivity;
import ua.naiksoftware.tambola.R;

import static android.view.View.GONE;

public class TestautoSelect extends AppCompatActivity {

    String json;
    private AdapterFish Adapter;
    private DataFish current;
    Collection secondlist = new ArrayList();
    Collection firstlist = new ArrayList();
    private boolean tvSelected1 = false;
    private boolean tvSelected2 = false;
    private boolean tvSelected3 = false;
    private boolean tvSelected4 = false;
    private boolean tvSelected5 = false;
    private boolean tvSelected6 = false;
    private boolean tvSelected7 = false;
    private boolean tvSelected8 = false;
    private boolean tvSelected9 = false;
    private boolean tvSelected10 = false;
    private boolean tvSelected11 = false;
    private boolean tvSelected12 = false;
    private boolean tvSelected13 = false;
    private boolean tvSelected14 = false;
    private boolean tvSelected15 = false;
    private boolean tvSelected16 = false;
    private boolean tvSelected17 = false;
    private boolean tvSelected18 = false;
    private boolean tvSelected19 = false;
    private boolean tvSelected20 = false;
    private boolean tvSelected21 = false;
    private boolean tvSelected22 = false;
    private boolean tvSelected23 = false;
    private boolean tvSelected24 = false;
    private boolean tvSelected25 = false;
    private boolean tvSelected26 = false;
    private boolean tvSelected27 = false;


    ArrayList<String> tktrow1 = new ArrayList<String>();
    ArrayList<String> tktrow2 = new ArrayList<String>();
    ArrayList<String> tktrow3 = new ArrayList<String>();
    JSONArray row1;
    JSONArray row2;
    JSONArray row3;

    ArrayList<String> finishedarray = new ArrayList<String>();

    SharedPreferences sp;
    String pass,gameid,gamestarttime;
    RecyclerView mRVFishPrice;

    Button compare;
    List<DataFish> filterdata=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_auto_select);

        finishedarray.add("3");
        finishedarray.add("1");
        finishedarray.add("2");
        finishedarray.add("9");
        finishedarray.add("27");
        finishedarray.add("36");
        finishedarray.add("51");
        finishedarray.add("2");
        finishedarray.add("28");


        compare = (Button)findViewById(R.id.compare);

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loadJSONFromAsset();
    }

    private String loadJSONFromAsset() {

        try {

            InputStream is = getAssets().open("data.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

            json=json.replace("null","' '");
            JSONObject jsondata = new JSONObject(json);

            JSONArray jsonArray = jsondata.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject json_data = jsonArray.getJSONObject(i).getJSONObject("ticket");
                DataFish data = new DataFish();
                String ticket = json_data.getString("ticket");
                 row1 = (JSONArray) new JSONArray(ticket).get(0);
                 row2 = (JSONArray) new JSONArray(ticket).get(1);
                 row3 = (JSONArray) new JSONArray(ticket).get(2);
                String s = json_data.getString("id");

                for ( int j = 0; j <row1.length();j++ ){

                    tktrow1.add(row1.getString(j));


  //                  Log.w("Row 1 ", String.valueOf(tktrow1));

                }

                for ( int j = 0; j <row2.length();j++ ){

                    tktrow2.add(row2.getString(j));

//                    Log.w("Row 2", String.valueOf(tktrow2));

                }

                for ( int j = 0; j <row3.length();j++ ){

                    tktrow2.add(row3.getString(j));

 //                   Log.w("Row 3", String.valueOf(tktrow3));

                }


                data.id =
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


//            Log.e("JSONDATA",json);


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
        Adapter = new AdapterFish(TestautoSelect.this, filterdata);
        mRVFishPrice.setAdapter(Adapter);
//                            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        mRVFishPrice.setLayoutManager(new GridLayoutManager(TestautoSelect.this,1));
        return json;

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
            View view = inflater.inflate(R.layout.ticket, parent, false);

            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);
            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;
            current = data.get(position);

//            myHolder.one.setText("Name: " + current.preferredName + "  " + current.surname);
            //     myHolder.checkBox.setVisibility(View.GONE);


            String s = current.t1;
            Log.e("s",current.t1);
            if(finishedarray.contains(s)){

                myHolder.one.setText("M");
                Log.e("fdfdf", String.valueOf(Arrays.asList(finishedarray).contains(s)));


            }
            else {

                myHolder.one.setText("0");

            }

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
            Button claim;




            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                //  id= (TextView)itemView.findViewById(R.id.id);
                claim = (Button)itemView.findViewById(R.id.claim);
                claim.setVisibility(View.GONE);
                checkBox =(CheckBox)findViewById(R.id.checkedTextView);


                one = (TextView) itemView.findViewById(R.id.t1);
                two = (TextView) itemView.findViewById(R.id.t2);

                one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (tvSelected1) {
//
//
                            one.setBackground(getDrawable(R.drawable.ticketborder2));
              tvSelected1 = false;
                        }
                        else {

                            one.setBackground(getDrawable(R.drawable.ticketbrder));
                            tvSelected1 = true;
                        }
                    }
                });


                two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected2) {
//                            one.setBackgroundColor(Color.WHITE);
                            two.setWidth(60);
                            two.setHeight(60);
                            two.setBackgroundColor(Color.WHITE);
                            two.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            two.setBackground(sd);

                            //five.setBackgroundColor(Color.YELLOW);

//                            five.setTextColor(Color.BLACK);
                            tvSelected2 = false;
                        }
                        else {

                            two.setWidth(60);
                            two.setHeight(60);
                            two.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            two.setBackground(sd);
                            two.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected2 = true;
                        }

                    }
                });

                three = (TextView) itemView.findViewById(R.id.t3);
                three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (tvSelected3) {
//                            one.setBackgroundColor(Color.WHITE);
                            three.setWidth(60);
                            three.setHeight(60);
                            three.setBackgroundColor(Color.WHITE);
                            three.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            three.setBackground(sd);

                            //five.setBackgroundColor(Color.YELLOW);

//                            five.setTextColor(Color.BLACK);
                            tvSelected3 = false;
                        }
                        else {

                            three.setWidth(60);
                            three.setHeight(60);
                            three.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            three.setBackground(sd);
                            three.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected3 = true;
                        }

                    }
                });
                four = (TextView) itemView.findViewById(R.id.t4);
                four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected4) {
//                            one.setBackgroundColor(Color.WHITE);
                            four.setWidth(60);
                            four.setHeight(60);
                            four.setBackgroundColor(Color.WHITE);
                            four.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            four.setBackground(sd);

                            //five.setBackgroundColor(Color.YELLOW);

//                            five.setTextColor(Color.BLACK);
                            tvSelected4 = false;
                        }
                        else {

                            four.setWidth(60);
                            four.setHeight(60);
                            four.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            four.setBackground(sd);
                            four.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected4 = true;
                        }
                    }
                });
                five = (TextView) itemView.findViewById(R.id.t5);

                five.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected5) {
//                            one.setBackgroundColor(Color.WHITE);
                            five.setWidth(60);
                            five.setHeight(60);
                            five.setBackgroundColor(Color.WHITE);
                            five.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            five.setBackground(sd);

                            //five.setBackgroundColor(Color.YELLOW);

//                            five.setTextColor(Color.BLACK);
                            tvSelected5 = false;
                        }
                        else {

                            five.setWidth(60);
                            five.setHeight(60);
                            five.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            five.setBackground(sd);
                            five.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected5 = true;
                        }
                    }
                });
                six = (TextView) itemView.findViewById(R.id.t6);

                six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected6) {
//                            one.setBackgroundColor(Color.WHITE);
                            six.setWidth(60);
                            six.setHeight(60);
                            six.setBackgroundColor(Color.WHITE);
                            six.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            six.setBackground(sd);

                            //six.setBackgroundColor(Color.YELLOW);

//                            six.setTextColor(Color.BLACK);
                            tvSelected6 = false;
                        }
                        else {

                            six.setWidth(60);
                            six.setHeight(60);
                            six.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            six.setBackground(sd);
                            six.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected6 = true;
                        }
                    }
                });
                seven = (TextView) itemView.findViewById(R.id.t7);

                seven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected7) {
//                            one.setBackgroundColor(Color.WHITE);
                            seven.setWidth(60);
                            seven.setHeight(60);
                            seven.setBackgroundColor(Color.WHITE);
                            seven.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            seven.setBackground(sd);

                            //seven.setBackgroundColor(Color.YELLOW);

//                            seven.setTextColor(Color.BLACK);
                            tvSelected7 = false;
                        }
                        else {

                            seven.setWidth(60);
                            seven.setHeight(60);
                            seven.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            seven.setBackground(sd);
                            seven.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected7 = true;
                        }
                    }
                });
                eight = (TextView) itemView.findViewById(R.id.t8);
                eight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected8) {
//                            one.setBackgroundColor(Color.WHITE);
                            eight.setWidth(60);
                            eight.setHeight(60);
                            eight.setBackgroundColor(Color.WHITE);
                            eight.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            eight.setBackground(sd);

                            //eight.setBackgroundColor(Color.YELLOW);

//                            eight.setTextColor(Color.BLACK);
                            tvSelected8 = false;
                        }
                        else {

                            eight.setWidth(60);
                            eight.setHeight(60);
                            eight.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            eight.setBackground(sd);
                            eight.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected8 = true;
                        }
                    }
                });
                nine = (TextView) itemView.findViewById(R.id.t9);

                nine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected9) {
//                            one.setBackgroundColor(Color.WHITE);
                            nine.setWidth(60);
                            nine.setHeight(60);
                            nine.setBackgroundColor(Color.WHITE);
                            nine.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            nine.setBackground(sd);

                            //nine.setBackgroundColor(Color.YELLOW);

//                            nine.setTextColor(Color.BLACK);
                            tvSelected9 = false;
                        }
                        else {

                            nine.setWidth(60);
                            nine.setHeight(60);
                            nine.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            nine.setBackground(sd);
                            nine.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected9 = true;
                        }
                    }
                });
                ten = (TextView)itemView.findViewById(R.id.t10);

                ten.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected10) {
//                            one.setBackgroundColor(Color.WHITE);
                            ten.setWidth(60);
                            ten.setHeight(60);
                            ten.setBackgroundColor(Color.WHITE);
                            ten.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            ten.setBackground(sd);

                            //ten.setBackgroundColor(Color.YELLOW);

//                            ten.setTextColor(Color.BLACK);
                            tvSelected10 = false;
                        }
                        else {

                            ten.setWidth(60);
                            ten.setHeight(60);
                            ten.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            ten.setBackground(sd);
                            ten.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected10 = true;
                        }
                    }
                });
                eleven= (TextView)itemView.findViewById(R.id.t11);

                eleven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected11) {
//                            one.setBackgroundColor(Color.WHITE);
                            eleven.setWidth(60);
                            eleven.setHeight(60);
                            eleven.setBackgroundColor(Color.WHITE);
                            eleven.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            eleven.setBackground(sd);

                            //eleven.setBackgroundColor(Color.YELLOW);

//                            eleven.setTextColor(Color.BLACK);
                            tvSelected11 = false;
                        }
                        else {

                            eleven.setWidth(60);
                            eleven.setHeight(60);
                            eleven.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            eleven.setBackground(sd);
                            eleven.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected11 = true;
                        }
                    }
                });
                twelve= (TextView)itemView.findViewById(R.id.t12);

                twelve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected12) {
//                            one.setBackgroundColor(Color.WHITE);
                            twelve.setWidth(60);
                            twelve.setHeight(60);
                            twelve.setBackgroundColor(Color.WHITE);
                            twelve.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twelve.setBackground(sd);

                            //twelve.setBackgroundColor(Color.YELLOW);

//                            twelve.setTextColor(Color.BLACK);
                            tvSelected12 = false;
                        }
                        else {

                            twelve.setWidth(60);
                            twelve.setHeight(60);
                            twelve.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twelve.setBackground(sd);
                            twelve.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected12 = true;
                        }
                    }
                });
                thirteen =  (TextView)itemView.findViewById(R.id.t13);
                thirteen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected13) {
//                            one.setBackgroundColor(Color.WHITE);
                            thirteen.setWidth(60);
                            thirteen.setHeight(60);
                            thirteen.setBackgroundColor(Color.WHITE);
                            thirteen.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            thirteen.setBackground(sd);

                            //thirteen.setBackgroundColor(Color.YELLOW);

//                            thirteen.setTextColor(Color.BLACK);
                            tvSelected13 = false;
                        }
                        else {

                            thirteen.setWidth(60);
                            thirteen.setHeight(60);
                            thirteen.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            thirteen.setBackground(sd);
                            thirteen.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected13 = true;
                        }
                    }
                });
                fourteen= (TextView)itemView.findViewById(R.id.t14);

                fourteen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected14) {
//                            one.setBackgroundColor(Color.WHITE);
                            fourteen.setWidth(60);
                            fourteen.setHeight(60);
                            fourteen.setBackgroundColor(Color.WHITE);
                            fourteen.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            fourteen.setBackground(sd);

                            //fourteen.setBackgroundColor(Color.YELLOW);

//                            fourteen.setTextColor(Color.BLACK);
                            tvSelected14 = false;
                        }
                        else {

                            fourteen.setWidth(60);
                            fourteen.setHeight(60);
                            fourteen.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            fourteen.setBackground(sd);
                            fourteen.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected14 = true;
                        }
                    }
                });
                fifteen= (TextView)itemView.findViewById(R.id.t15);
                fifteen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected15) {
//                            one.setBackgroundColor(Color.WHITE);
                            fifteen.setWidth(60);
                            fifteen.setHeight(60);
                            fifteen.setBackgroundColor(Color.WHITE);
                            fifteen.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            fifteen.setBackground(sd);

                            //fifteen.setBackgroundColor(Color.YELLOW);

//                            fifteen.setTextColor(Color.BLACK);
                            tvSelected15 = false;
                        }
                        else {

                            fifteen.setWidth(60);
                            fifteen.setHeight(60);
                            fifteen.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            fifteen.setBackground(sd);
                            fifteen.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected15 = true;
                        }
                    }
                });
                sixteen= (TextView)itemView.findViewById(R.id.t16);

                sixteen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    { if (tvSelected16) {
//                            one.setBackgroundColor(Color.WHITE);
                        sixteen.setWidth(60);
                        sixteen.setHeight(60);
                        sixteen.setBackgroundColor(Color.WHITE);
                        sixteen.setPadding(10,10,10,10);
                        ShapeDrawable sd = new ShapeDrawable();

                        // Specify the shape of ShapeDrawable
                        sd.setShape(new RectShape());

                        // Specify the border color of shape
                        sd.getPaint().setColor(Color.BLUE);

                        // Set the border width
                        sd.getPaint().setStrokeWidth(5f);

                        // Specify the style is a Stroke
                        sd.getPaint().setStyle(Paint.Style.STROKE);

                        // Finally, add the drawable background to TextView
                        sixteen.setBackground(sd);

                        //sixteen.setBackgroundColor(Color.YELLOW);

//                            sixteen.setTextColor(Color.BLACK);
                        tvSelected16 = false;
                    }
                    else {

                        sixteen.setWidth(60);
                        sixteen.setHeight(60);
                        sixteen.setPadding(10,10,10,10);


                        ShapeDrawable sd = new ShapeDrawable();

                        // Specify the shape of ShapeDrawable
                        sd.setShape(new RectShape());

                        // Specify the border color of shape
                        sd.getPaint().setColor(Color.GREEN);

                        // Set the border width
                        sd.getPaint().setStrokeWidth(5f);

                        // Specify the style is a Stroke
                        sd.getPaint().setStyle(Paint.Style.STROKE);

                        // Finally, add the drawable background to TextView
                        sixteen.setBackground(sd);
                        sixteen.setBackgroundColor(Color.parseColor("#FFFF8800"));
                        tvSelected16 = true;
                    }


                    }
                });
                seventeen= (TextView)itemView.findViewById(R.id.t17);
                seventeen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected17) {
//                            one.setBackgroundColor(Color.WHITE);
                            seventeen.setWidth(60);
                            seventeen.setHeight(60);
                            seventeen.setBackgroundColor(Color.WHITE);
                            seventeen.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            seventeen.setBackground(sd);

                            //seventeen.setBackgroundColor(Color.YELLOW);

//                            seventeen.setTextColor(Color.BLACK);
                            tvSelected17 = false;
                        }
                        else {

                            seventeen.setWidth(60);
                            seventeen.setHeight(60);
                            seventeen.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            seventeen.setBackground(sd);
                            seventeen.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected17 = true;
                        }
                    }
                });
                eighteen= (TextView)itemView.findViewById(R.id.t18);

                eighteen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected18) {
//                            one.setBackgroundColor(Color.WHITE);
                            eighteen.setWidth(60);
                            eighteen.setHeight(60);
                            eighteen.setBackgroundColor(Color.WHITE);
                            eighteen.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            eighteen.setBackground(sd);

                            //eighteen.setBackgroundColor(Color.YELLOW);

//                            eighteen.setTextColor(Color.BLACK);
                            tvSelected18 = false;
                        }
                        else {

                            eighteen.setWidth(60);
                            eighteen.setHeight(60);
                            eighteen.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            eighteen.setBackground(sd);
                            eighteen.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected18 = true;
                        }
                    }
                });
                nineteen= (TextView)itemView.findViewById(R.id.t19);

                nineteen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected19) {
//                            one.setBackgroundColor(Color.WHITE);
                            nineteen.setWidth(60);
                            nineteen.setHeight(60);
                            nineteen.setBackgroundColor(Color.WHITE);
                            nineteen.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            nineteen.setBackground(sd);

                            //nineteen.setBackgroundColor(Color.YELLOW);

//                            nineteen.setTextColor(Color.BLACK);
                            tvSelected19 = false;
                        }
                        else {

                            nineteen.setWidth(60);
                            nineteen.setHeight(60);
                            nineteen.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            nineteen.setBackground(sd);
                            nineteen.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected19 = true;
                        }
                    }
                });
                twenty = (TextView)itemView.findViewById(R.id.t20);

                twenty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected20) {
//                            one.setBackgroundColor(Color.WHITE);
                            twenty.setWidth(60);
                            twenty.setHeight(60);
                            twenty.setBackgroundColor(Color.WHITE);
                            twenty.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twenty.setBackground(sd);

                            //twenty.setBackgroundColor(Color.YELLOW);

//                            twenty.setTextColor(Color.BLACK);
                            tvSelected20 = false;
                        }
                        else {

                            twenty.setWidth(60);
                            twenty.setHeight(60);
                            twenty.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twenty.setBackground(sd);
                            twenty.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected20 = true;
                        }
                    }
                });
                twentyone= (TextView)itemView.findViewById(R.id.t21);
                twentyone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected21) {
//                            one.setBackgroundColor(Color.WHITE);
                            twentyone.setWidth(60);
                            twentyone.setHeight(60);
                            twentyone.setBackgroundColor(Color.WHITE);
                            twentyone.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentyone.setBackground(sd);

                            //twentyone.setBackgroundColor(Color.YELLOW);

//                            twentyone.setTextColor(Color.BLACK);
                            tvSelected21 = false;
                        }
                        else {

                            twentyone.setWidth(60);
                            twentyone.setHeight(60);
                            twentyone.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentyone.setBackground(sd);
                            twentyone.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected21 = true;
                        }
                    }
                });
                twentytwo= (TextView)itemView.findViewById(R.id.t22);

                twentytwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected22) {
//                            one.setBackgroundColor(Color.WHITE);
                            twentytwo.setWidth(60);
                            twentytwo.setHeight(60);
                            twentytwo.setBackgroundColor(Color.WHITE);
                            twentytwo.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentytwo.setBackground(sd);

                            //twentytwo.setBackgroundColor(Color.YELLOW);

//                            twentytwo.setTextColor(Color.BLACK);
                            tvSelected22 = false;
                        }
                        else {

                            twentytwo.setWidth(60);
                            twentytwo.setHeight(60);
                            twentytwo.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentytwo.setBackground(sd);
                            twentytwo.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected22 = true;
                        }
                    }
                });
                twentythree= (TextView)itemView.findViewById(R.id.t23);

                twentythree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected23) {
//                            one.setBackgroundColor(Color.WHITE);
                            twentythree.setWidth(60);
                            twentythree.setHeight(60);
                            twentythree.setBackgroundColor(Color.WHITE);
                            twentythree.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentythree.setBackground(sd);

                            //twentythree.setBackgroundColor(Color.YELLOW);

//                            twentythree.setTextColor(Color.BLACK);
                            tvSelected23 = false;
                        }
                        else {

                            twentythree.setWidth(60);
                            twentythree.setHeight(60);
                            twentythree.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentythree.setBackground(sd);
                            twentythree.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected23 = true;
                        }
                    }
                });
                twentyfour= (TextView)itemView.findViewById(R.id.t24);
                twentyfour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected24) {
//                            one.setBackgroundColor(Color.WHITE);
                            twentyfour.setWidth(60);
                            twentyfour.setHeight(60);
                            twentyfour.setBackgroundColor(Color.WHITE);
                            twentyfour.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentyfour.setBackground(sd);

                            //twentyfour.setBackgroundColor(Color.YELLOW);

//                            twentyfour.setTextColor(Color.BLACK);
                            tvSelected24 = false;
                        }
                        else {

                            twentyfour.setWidth(60);
                            twentyfour.setHeight(60);
                            twentyfour.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentyfour.setBackground(sd);
                            twentyfour.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected24 = true;
                        }
                    }
                });
                twentyfive= (TextView)itemView.findViewById(R.id.t25);
                twentyfive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected25) {
//                            one.setBackgroundColor(Color.WHITE);
                            twentyfive.setWidth(60);
                            twentyfive.setHeight(60);
                            twentyfive.setBackgroundColor(Color.WHITE);
                            twentyfive.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentyfive.setBackground(sd);

                            //twentyfive.setBackgroundColor(Color.YELLOW);

//                            twentyfive.setTextColor(Color.BLACK);
                            tvSelected25 = false;
                        }
                        else {

                            twentyfive.setWidth(60);
                            twentyfive.setHeight(60);
                            twentyfive.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentyfive.setBackground(sd);
                            twentyfive.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected25 = true;
                        }
                    }
                });
                twentysix= (TextView)itemView.findViewById(R.id.t26);
                twentysix.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected26) {
//                            one.setBackgroundColor(Color.WHITE);
                            twentysix.setWidth(60);
                            twentysix.setHeight(60);
                            twentysix.setBackgroundColor(Color.WHITE);
                            twentysix.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentysix.setBackground(sd);

                            //twentysix.setBackgroundColor(Color.YELLOW);

//                            twentysix.setTextColor(Color.BLACK);
                            tvSelected26 = false;
                        }
                        else {

                            twentysix.setWidth(60);
                            twentysix.setHeight(60);
                            twentysix.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentysix.setBackground(sd);
                            twentysix.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected26 = true;
                        }
                    }
                });
                twentyseven= (TextView)itemView.findViewById(R.id.t27);

                twentyseven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvSelected27) {
//                            one.setBackgroundColor(Color.WHITE);
                            twentyseven.setWidth(60);
                            twentyseven.setHeight(60);
                            twentyseven.setBackgroundColor(Color.WHITE);
                            twentyseven.setPadding(10,10,10,10);
                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.BLUE);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentyseven.setBackground(sd);

                            //twentyseven.setBackgroundColor(Color.YELLOW);

//                            twentyseven.setTextColor(Color.BLACK);
                            tvSelected27 = false;
                        }
                        else {

                            twentyseven.setWidth(60);
                            twentyseven.setHeight(60);
                            twentyseven.setPadding(10,10,10,10);


                            ShapeDrawable sd = new ShapeDrawable();

                            // Specify the shape of ShapeDrawable
                            sd.setShape(new RectShape());

                            // Specify the border color of shape
                            sd.getPaint().setColor(Color.GREEN);

                            // Set the border width
                            sd.getPaint().setStrokeWidth(5f);

                            // Specify the style is a Stroke
                            sd.getPaint().setStyle(Paint.Style.STROKE);

                            // Finally, add the drawable background to TextView
                            twentyseven.setBackground(sd);
                            twentyseven.setBackgroundColor(Color.parseColor("#FFFF8800"));
                            tvSelected27 = true;
                        }
                    }
                });

            }


        }

    }
}
