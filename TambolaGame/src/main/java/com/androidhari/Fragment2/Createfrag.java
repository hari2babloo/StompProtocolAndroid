package com.androidhari.Fragment2;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.androidhari.Fragment.BankTransfer;
import com.androidhari.Fragment.PaytmTransfer;

import ua.naiksoftware.tambola.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Createfrag extends Fragment {


    View view;


    public Createfrag() {
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

        view = inflater.inflate(R.layout.creategrag, container, false);



        return view;


    }


}
