package com.example.clay.modaltester;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class ModalTester extends Activity {

    private static String aboutString =
            "This app is a rolling ball game designed by\n" +
            "Wyatt McBain and Clay Herendeen \n" +
            "for Android Class @ RIT. Enjoy!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modal_tester);

        initSplashButtons();
    }

    /**
     * sets up listeners for splash buttons
     */
    public void initSplashButtons(){

        // push to levels activity
        findViewById(R.id.btnViewLevels).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showLevels();
            }

        });

        // about button
        findViewById(R.id.btnViewAbout).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HashMap<String, String> options = new HashMap<String, String>();
                // options:
                options.put("modal_title", "About This App");
                options.put("modal_content", aboutString);
                CustomModal cm = new CustomModal(ModalTester.this, "info", options);
            }

        });
// if you want to see whats in the DB
//        Button btnDB = (Button)findViewById(R.id.btnDB);
//        btnDB.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent dbmanager = new Intent(ModalTester.this,AndroidDatabaseManager.class);
//                startActivity(dbmanager);
//            }
//
//        });
    }

    public void showLevels(){
        // create and Intent to launch the ViewContact Activity
        Intent viewLevels = new Intent(ModalTester.this, LevelView.class);
        startActivity(viewLevels);
    }
}
