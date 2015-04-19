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

    private static String aboutString = "This app is a rolling ball game \n" +
            " designed by Wyatt McBain and Clay Herendeen for Android Class @ RIT. Enjoy!";

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

        // launch modalButton
        findViewById(R.id.btnLaunchModal).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HashMap<String, String> options = new HashMap<String, String>();
                // options:
//                options.put("", "");
                launchModal("level_complete", options);

            }

        });

        // about button
        findViewById(R.id.btnViewAbout).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HashMap<String, String> options = new HashMap<String, String>();
                // options:
                //
                options.put("modal_title", "About This App");
                options.put("modal_content", aboutString);

                launchModal("info", options);

            }

        });

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

    /**
     *
     * @param modalType : type of modal you want (ex: "level_complete", "info", "alert"
     * @param options
     */
    public void launchModal(String modalType, HashMap<String, String> options){

        final Dialog d = new Dialog(this,R.style.CustomDialogTheme);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setCanceledOnTouchOutside(false); // cancel any outside touches
        d.getWindow().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.modal_bg));

        switch(modalType){
            case "level_complete":
                d.setContentView(R.layout.level_complete_modal_content);
                Button btnShowLevels = (Button)d.findViewById(R.id.btnShowLevels);
                btnShowLevels.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        d.dismiss();
                        showLevels();
                    }
                });

                break;
            case "info":
                d.setContentView(R.layout.info_modal_content);
                TextView modalTitle = (TextView) d.findViewById(R.id.txtModalTitle);
                modalTitle.setText(options.get("modal_title").toString());
                TextView modalContent = (TextView)d.findViewById(R.id.txtModalContent);
                modalContent.setText(options.get("modal_content").toString());
                break;
            case "alert":
                break;
        }

        d.show();

        Button close_btn = (Button) d.findViewById(R.id.btnClose);
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d.dismiss();
                // will probably later want to make navigation back to main View (HOME)
            }
        });
    }




    public void showLevels(){
        // create and Intent to launch the ViewContact Activity
        Intent viewLevels = new Intent(ModalTester.this, LevelView.class);

        startActivity(viewLevels);
    }

}
