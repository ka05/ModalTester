package com.example.clay.modaltester;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Clay on 4/11/2015.
 */
public class LevelView extends Activity {

    // key-value pair passed values between activities
    public static final String ROW_ID = "row_id";
    public static long levelRow;

    public static Level currentLevel;


    // adapter for populating the ListView
    private CursorAdapter levelAdapter;
    private ImageView lvlImageView;
    private ListView levelListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        levelListView = (ListView)findViewById(R.id.listViewLevels);
        levelListView.setOnItemClickListener(viewLevelListener);

        lvlImageView = (ImageView)findViewById(R.id.levelImage);

        findViewById(R.id.btnStartLevel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create and Intent to launch the ViewContact Activity
                Intent startLevelView = new Intent(LevelView.this, BallGame.class);

                //pass the selected contacts row ID as extra with the Intent
                startLevelView.putExtra(ROW_ID, levelRow);
                startActivity(startLevelView);
            }
        });


        // map each contacts name to a TextView in the ListView layout
        String[] from = new String[]{"levelname"};
        int[] to = new int[]{R.id.txtLevelName};
        levelAdapter = new SimpleCursorAdapter(
                LevelView.this, R.layout.level_table_row, null, from, to, 0
        ); // 0 = start at first row

        levelListView.setAdapter(levelAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //create a new GetContactTask and execute
        new GetLevelsTask().execute((Object[])null);

    }

    @Override
    protected void onStop(){
        Cursor cursor = levelAdapter.getCursor(); // get cursor

        if(cursor != null){
            //we have a cursor
            //so lets close - deactivate it
            cursor.close();
        }
        levelAdapter.changeCursor(null); // adapter has no cursor
        super.onStop(); //needs to be last because other code must execute first
    }


    //perform the database query outside the GUI Thread
    private class GetLevelsTask extends AsyncTask<Object, Object, Cursor> {
        DatabaseConnector databaseConnector = new DatabaseConnector(LevelView.this);

        //perform the database access
        @Override // from AsyncTask Class
        protected Cursor doInBackground(Object... params){
            databaseConnector.open();

            //get cursor containing all contacts
            return databaseConnector.getAllLevels();
        }

        // use the cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result){
            levelAdapter.changeCursor(result); //set the adapters cursor
            databaseConnector.close(); // close connection
        }
    }

    // performs database query outside GUI thread
    private class LoadLevelTask extends AsyncTask<Long, Object, Cursor>
    {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(LevelView.this);

        // perform the database access
        @Override
        protected Cursor doInBackground(Long... params)
        {
            databaseConnector.open();

            // get a cursor containing all data on given entry
            return databaseConnector.getOneLevel(params[0]);
        } // end method doInBackground

        // use the Cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result)
        {
            super.onPostExecute(result);

            result.moveToFirst(); // move to the first item

            // get the column index for each data item
            int nameIndex = result.getColumnIndex("levelname");
            int imgsrcIndex = result.getColumnIndex("imgsrc");

            currentLevel = new Level(result.getString(nameIndex), result.getString(imgsrcIndex));
            showLevelSelection();
            result.close(); // close the result cursor
            databaseConnector.close(); // close database connection
        } // end method onPostExecute
    }

    OnItemClickListener viewLevelListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // get currentLevelData
            new LoadLevelTask().execute(id);
        }
    }; //end OnItemClick


    public void showLevelSelection(){
        // parse it aas a Level
        int resId = this.getResources().getIdentifier("drawable/" + currentLevel.getImgSrc(), "drawable", this.getPackageName());
        lvlImageView.setImageResource(resId);
        lvlImageView.setContentDescription(currentLevel.getLevelName());
    }
}
