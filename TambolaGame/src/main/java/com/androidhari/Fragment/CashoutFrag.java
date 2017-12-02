package com.androidhari.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import ua.naiksoftware.tambola.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashoutFrag extends Fragment implements View.OnClickListener {


    View view;

    RadioGroup radioGroup;
    Button next;
    public CashoutFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.cashoutfrag, container, false);

        radioGroup = (RadioGroup) view.findViewById(R.id.radiogrp);
        radioGroup.clearCheck();
        next = (Button) view.findViewById(R.id.next);

        next.setOnClickListener(this);

        return view;


    }

    @Override
    public void onClick(View v) {

        int selectedId = radioGroup.getCheckedRadioButtonId();

        Log.e("radio", String.valueOf(selectedId));

        // find the radiobutton by returned id

        RadioButton ra = (RadioButton) view.findViewById(selectedId);

      //  Toast.makeText(getContext(), ra.getText(), Toast.LENGTH_SHORT).show();


        if (ra.getText().toString().equalsIgnoreCase("Paytm")){


            Intent inte = new Intent(getContext(),PaytmTransfer.class);
            startActivity(inte);
        }

        else if (ra.getText().toString().equalsIgnoreCase("Bank Account")){
            Intent inte = new Intent(getContext(),BankTransfer.class);
            startActivity(inte);

        }
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.homemenu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

}
