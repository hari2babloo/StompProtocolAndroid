package com.androidhari.Fragment;


import android.accessibilityservice.GestureDescription;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.ViewPager.WalletTransactions;
import com.androidhari.tambola.Countdown;
import com.androidhari.tambola.HomeScreen;
import com.androidhari.tambola.Signin;
import com.androidhari.tambola.Wallet;
import com.androidhari.tambola.WalletPurchasehistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.naiksoftware.tambola.MainActivity;
import ua.naiksoftware.tambola.R;

import static android.content.Context.MODE_PRIVATE;
import static ua.naiksoftware.tambola.R.id.status;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMoneyFrag extends Fragment {




    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    String pass,mvalue,status;
    View view;
    ProgressDialog pd;
    SharedPreferences sp;
    Button add,redeem;
    TextView vouchervaluetxt;
    EditText money,voucher;
    ArrayList<prizes> dataModels = new ArrayList<>();
    ListView plist;


    RecyclerView recyclerView;
    public AddMoneyFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp= this.getActivity().getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.addmoneyfrag, container, false);
        money = (EditText) view.findViewById(R.id.addmoney);
        voucher = (EditText) view.findViewById(R.id.voucher);
        vouchervaluetxt = (TextView)view.findViewById(R.id.vouchervalue);
        vouchervaluetxt.setVisibility(View.GONE);

        plist = (ListView) view.findViewById(R.id.prizelist);

    

        add = (Button)view.findViewById(R.id.add);

        add.setVisibility(View.GONE);
        redeem  = (Button)view.findViewById(R.id.redeem);

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redeemvoucher();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addmoney();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void redeemvoucher() {
        pd = new ProgressDialog(getContext());
        pd.setMessage("Validating your Voucher");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
          //  postdata.put("money", money.getText().toString());
            postdata.put("voucherCode", voucher.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());


        final Request request = new Request.Builder()
                .url("http://52.172.191.222/vms/index.php/voucher/wsVoucherInfo")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)

                .build();


        Log.e("dasdasd", body.toString());



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.cancel();
                pd.cancel();

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);

//                Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String mMessage = response.body().string();
                pd.cancel();
                pd.dismiss();

                Log.w("Response", mMessage);
                if (response.isSuccessful()){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONArray jarray = new JSONArray(mMessage);

                                JSONObject json = jarray.getJSONObject(0);

                                String voucherstatus = json.getString("voucherValidityStatus");

                                status = json.getString("status");

                                mvalue = json.getString("voucherValue");
                                Log.d("worth",mvalue);

                                if (voucherstatus.equalsIgnoreCase("0")){






                                }
                                else if (voucherstatus.equalsIgnoreCase("1")){

                                    redeem.setVisibility(View.GONE);

                                    vouchervaluetxt.setVisibility(View.VISIBLE);
                                    vouchervaluetxt.setText("Your Voucher Value is worth Rs:" +json.getString("voucherValue"));

                                    add.setVisibility(View.VISIBLE);



                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            Intent in = new Intent(getContext(), Wallet.class);
//
//                            startActivity(in);
 //                          Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String status = json.getString("status");
                                String message = json.getString("message");
                                //title = name;

                                if (status.equalsIgnoreCase("401")){
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();

                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(),Signin.class);
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




    public class prizes{

        String gno;
        String gname;
        String gstime;
        String status;

        public prizes(String gno, String gname, String gstime, String status) {
            this.gno = gno;
            this.gname = gname;
            this.gstime = gstime;
            this.status = status;
        }

        public String getGno() {
            return gno;
        }

        public void setGno(String gno) {
            this.gno = gno;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getGstime() {
            return gstime;
        }

        public void setGstime(String gstime) {
            this.gstime = gstime;
        }

    }

    public class CustomAdapter extends ArrayAdapter<prizes> implements View.OnClickListener {

        private ArrayList<prizes> dataSet;
        Context mContext;

        // View lookup cache
        private class ViewHolder {
            TextView gno;
            TextView gname;
            TextView gstime;
            Button status;
            ImageView img;
        }

        public CustomAdapter(ArrayList<prizes> data, Context context) {
            super(context, R.layout.tablist, data);
            this.dataSet = data;
            this.mContext = context;

        }

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag();
            Object object = getItem(position);
            prizes dataModel = (prizes) object;

            switch (v.getId()) {
//                case R.id.item_info:
//                    Snackbar.make(v, "Release date " +dataModel.getGname(), Snackbar.LENGTH_LONG)
//                            .setAction("No action", null).show();
//                    break;


            }
        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            prizes dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new CustomAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.tablist, parent, false);
                viewHolder.gno = (TextView) convertView.findViewById(R.id.gno);
                viewHolder.gname = (TextView) convertView.findViewById(R.id.gname);
                viewHolder.gstime = (TextView) convertView.findViewById(R.id.gstime);
                viewHolder.status = (Button) convertView.findViewById(R.id.status);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);

                //       viewHolder.info = (ImageView) convertView.findViewBy-Id(R.id.item_info);

                result = convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
                result = convertView;
            }

            Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/segoeuil.ttf");

//            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//            result.startAnimation(animation);
            lastPosition = position;

            //          viewHolder.txtName.setText(dataModel.getPcost());
            viewHolder.gno.setText("Game Number:" + dataModel.getGno());
            viewHolder.gno.setTypeface(face);
            viewHolder.gname.setText("Game Name:" + dataModel.getGname());
            viewHolder.gname.setTypeface(face);
            viewHolder.gstime.setText("Start Time:" + dataModel.getGstime());
            viewHolder.gstime.setTypeface(face);

            if (dataModel.status.equalsIgnoreCase("COMPLETED")) {

                viewHolder.status.setText("FINISHED");
                viewHolder.status.setEnabled(false);
                viewHolder.status.setBackgroundColor(Color.RED);

            } else {

                viewHolder.status.setText("PLAY");
                viewHolder.status.setBackgroundColor(Color.parseColor("#FFFF8800"));
                viewHolder.status.setEnabled(true);
            }
            //  viewHolder.status.setText(dataModel.status);

            viewHolder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences.Editor e = sp.edit();
                    e.putString("gno", dataModel.getGno());
                    e.putString("gstime", dataModel.getGstime());
                    e.commit();


                    Date date = new Date();
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
                    dateFormat.format(date);
                    System.out.println(dateFormat.format(date));
                    System.out.println(dataModel.getGstime());

                    try {
                        if (dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(dataModel.getGstime()))) {
                            Intent in = new Intent(mContext, MainActivity.class);
                            startActivity(in);

                            System.out.println("Current time is greater than 12.07");
                        } else {
                            Intent in = new Intent(mContext, Countdown.class);
                            startActivity(in);
                            System.out.println("Current time is less than 12.07");
                        }
                    } catch (ParseException X) {
                        X.printStackTrace();
                    }


                //    Toast.makeText(mContext, dataModel.getGstime(), Toast.LENGTH_SHORT).show();

                }
            });
            viewHolder.img.setImageResource(R.drawable.img);
            return convertView;
        }

    }
    private void addmoney() {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Adding Money to Your Wallet");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("money", mvalue);
            postdata.put("voucherCode", voucher.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());


        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/wallet")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)

                .build();


        Log.e("dasdasd", body.toString());



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.cancel();
                pd.cancel();

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);


//                Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String mMessage = response.body().string();
                pd.cancel();
                pd.dismiss();

                Log.w("Response", mMessage);
                if (response.isSuccessful()){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject json = null;
                            try {
                                json = new JSONObject(mMessage);
                                String s = json.getString("message");
//                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
                                Intent in = new Intent(getContext(), WalletTransactions.class);
                                startActivity(in);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                JSONObject json = new JSONObject(mMessage);

                                String s = json.getString("message");



                                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
                               // Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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