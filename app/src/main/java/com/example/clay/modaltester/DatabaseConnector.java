// DatabaseConnector.java
// Provides easy connection and creation of UserContacts database.
package com.example.clay.modaltester;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatabaseConnector 
{
   // database name
   private static final String DATABASE_NAME = "BallGameLevels";
   private SQLiteDatabase database; // database object
   private DatabaseOpenHelper databaseOpenHelper; // database helper

   // public constructor for DatabaseConnector
   public DatabaseConnector(Context context) 
   {
      // create a new DatabaseOpenHelper
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   } // end DatabaseConnector constructor

   // open the database connection
   public void open() throws SQLException 
   {
      // create or open a database for reading/writing
      database = databaseOpenHelper.getWritableDatabase();
   } // end method open

   // close the database connection
   public void close() 
   {
      if (database != null)
         database.close(); // close the database connection
   } // end method close

   // inserts a new Level in the database
   public void insertLevel(String levelName, String imgSrc)
   {
      ContentValues newLevel = new ContentValues();
      newLevel.put("levelname", levelName);
      newLevel.put("imgsrc", levelName);

      open(); // open the database
      database.insert("levels", null, newLevel);
      close(); // close the database
   } // end method insertLevel


   // return a Cursor with all contact information in the database
   public Cursor getAllLevels()
   {
      return database.query("levels", new String[] {"_id", "levelname"},
         null, null, null, null, "levelname");
   } // end method getAllContacts

   // get a Cursor containing all information about the contact specified
   // by the given id
   public Cursor getOneLevel(long id)
   {
      return database.query(
         "levels", null, "_id=" + id, null, null, null, null);
   } // end method getOneLevel

   // delete the contact specified by the given String name
   public void deleteLevel(long id)
   {
      open(); // open the database
      database.delete("levels", "_id=" + id, null);
      close(); // close the database
   } // end method deleteContact

   public void insertMultiple(ArrayList<HashMap<String, String>> levelList){

       for(HashMap<String, String> levelData : levelList){
           String levelName = "";
           String imgSrc = "";

           Iterator it = levelData.entrySet().iterator();
           while (it.hasNext()) {
               Map.Entry pair = (Map.Entry)it.next();
               System.out.println(pair.getKey() + " = " + pair.getValue());
               if(pair.getKey() == "levelname"){
                   levelName = pair.getValue().toString();
               }
               if(pair.getKey() == "imgsrc"){
                   imgSrc = pair.getValue().toString();
               }
               it.remove(); // avoids a ConcurrentModificationException
           }
           insertLevel(levelName, imgSrc);
       }
   }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = databaseOpenHelper.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

   private class DatabaseOpenHelper extends SQLiteOpenHelper 
   {
      // public constructor
      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version) 
      {
         super(context, name, factory, version);
      } // end DatabaseOpenHelper constructor

      // creates the contacts table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
         // query to create a new table named contacts
         String createQuery = "CREATE TABLE levels" +
            "(_id integer primary key autoincrement," +
            "levelname TEXT, imgsrc TEXT);";
                  
         db.execSQL(createQuery); // execute the query
         System.out.println("db path: " + db.getPath());
         insertLevels(db);
      } // end method onCreate

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      } // end method onUpgrade


       public void insertLevels(SQLiteDatabase db){
           ArrayList<Level> levels = new ArrayList<>();
           levels.add(new Level("Level 1", "lvl1_preview"));
           levels.add(new Level("Level 2", "lvl2_preview"));
           levels.add(new Level("Level 3", "lvl3_preview"));
           levels.add(new Level("Level 4", "lvl4_preview"));
           levels.add(new Level("Level 5", "lvl5_preview"));
           levels.add(new Level("Level 6", "lvl6_preview"));
           levels.add(new Level("Level 7", "lvl7_preview"));

           for(Level level : levels) {

               String createQuery = "INSERT INTO levels" +
                       "(levelname, imgsrc) VALUES ('" + level.getLevelName() + "', '" + level.getImgSrc() + "');";

               db.execSQL(createQuery); // execute the query
           }
       }
   } // end class DatabaseOpenHelper
} // end class DatabaseConnector

