package com.example.superfit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.zip.Inflater;

public class mainScreen extends AppCompatActivity {

    Button btn1;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);





    }


    public void Seeall(View view1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater ItInflater = getLayoutInflater();
        View view = ItInflater.inflate(R.layout.dialog, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        btn1 = view.findViewById(R.id.btnok);
        btn2 = view.findViewById(R.id.btnno);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void Recipes(View view) {
        Intent intent = new Intent(this, RecipesList.class);
        startActivity(intent);
    }
}