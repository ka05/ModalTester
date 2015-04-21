package com.example.clay.modaltester;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BallGame extends Activity implements SensorEventListener {
    private CustomDrawableView mCustomDrawableView = null;
    private ShapeDrawable mDrawable = new ShapeDrawable();
    private float xPosition, pitch , xVelocity = 0.0f;
    private float yPosition, roll , yVelocity = 0.0f;
    private int xMax, yMax;
    private int totalLevelCoinCount = 0;
    private Bitmap mBitmap;
    private Bitmap mWood;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private ArrayList<Point> levelCollisionPoints;
    private ArrayList<Coin> levelCoins;
    private float[] gravity, geomagnetic;
    private float frameTime = 0.666f;
    private Level currentLevel;
    public int windowHeight, windowWidth;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        // Set to fullscreen and landscape
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // instantiate reference to the sensorManager, accelerometer,magnetometer
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initializeLevel();

        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
        // setContentView(R.layout.main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        xMax = size.x - 50;
        yMax = size.y - 50;
        System.out.println("XMax: " + xMax + " yMax: " + yMax);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;
    }

    public void initializeLevel(){
        Bundle b = this.getIntent().getExtras();
        long currentLevelId = 0;
        if(b!=null)
            currentLevelId = b.getLong(LevelView.ROW_ID);

        new LoadLevelTask().execute(currentLevelId);

        // use this to hold collision points for walls
        levelCollisionPoints = new ArrayList<>();
        levelCollisionPoints.add(new Point(20, yMax));
    }


    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        // get values generated by the sensors
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) gravity = sensorEvent.values;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) geomagnetic = sensorEvent.values;

        // get the orientation
        if (gravity != null && geomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean gotRotationMatrix = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);

            // check for success
            if (gotRotationMatrix) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                pitch = orientation[1];
                roll = orientation[2];
                updateBall();
            }
        }
    }

    private void updateBall() {

        //Calculate new speed
        xVelocity += (pitch * frameTime);
        yVelocity += (roll * frameTime);

        //Calc distance travelled in that time
        float xS = ((xVelocity / 2) * frameTime);
        float yS = ((yVelocity / 2) * frameTime);

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition -= yS;

        if (xPosition > xMax || xPosition < 0) {
            xVelocity *= -0.5;
            xS = ((xVelocity / 2) * frameTime);
            yS = ((yVelocity / 2) * frameTime);
            xPosition -= xS;
            yPosition -= yS;
        }
        else if (yPosition > yMax || yPosition < 0) {
            yVelocity *= -0.5;
            xS = ((xVelocity / 2) * frameTime);
            yS = ((yVelocity / 2) * frameTime);
            xPosition -= xS;
            yPosition -= yS;
        }

        if (xPosition > xMax) {
            xPosition = xMax;
        } else if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition > yMax) {
            yPosition = yMax;
        } else if (yPosition < 0) {
            yPosition = 0;
        }

        // loop through coins array and see if the ball hits one of them.
        for(int i = 0; i < levelCoins.size(); i++){
            Coin coin = levelCoins.get(i);
            if ( ( (int)xPosition > coin.getxPos() && (int)xPosition < coin.getxPos() + 50 ) && ( (int)yPosition > coin.getyPos() && (int)yPosition < coin.getyPos() + 50 )) {
                System.out.println("[X]: coin pos: " + coin.getxPos() + " ball pos: " + xPosition);
                System.out.println("[Y]: coin pos: " + coin.getyPos() + " ball pos: " + yPosition + "Hit a Coin of type:" + coin.getType());
                updateCoinCount(coin.getValue());
                levelCoins.remove(coin);
            }
        }
    }

    public void updateCoinCount(int val){
        totalLevelCoinCount += val;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    public class CustomDrawableView extends View
    {
        final int ROTATE_TIME_MILLIS = 200;
        Matrix matrix;

        public CustomDrawableView(Context context)
        {
            super(context);
            Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            final int dstWidth = 50;
            final int dstHeight = 50;
            mBitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
//            mWood = BitmapFactory.decodeResource(getResources(), R.drawable.wood);
            matrix = new Matrix();

        }

        protected void onDraw(Canvas canvas)
        {
            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setTextSize(24);
            canvas.drawText("Score: " + totalLevelCoinCount, yMax - 100, 40, p);
            final Bitmap bitmap = mBitmap;
//            canvas.drawBitmap(mWood, 0, 0, null); // for background image on canvas
            canvas.drawBitmap(bitmap, xPosition, yPosition, null);

            // loop through coins array and place on
            for(Coin coin : levelCoins){

                final Bitmap coinBitmap = getCoinBitmap(coin.getType());

                float angle = (float) (System.currentTimeMillis() % ROTATE_TIME_MILLIS)
                        / ROTATE_TIME_MILLIS * 360;
                matrix.reset();
                matrix.postTranslate(-coinBitmap.getWidth() / 2, -coinBitmap.getHeight() / 2);
                matrix.postRotate(angle);
                matrix.postTranslate(coin.getxPos(), coin.getyPos());
                canvas.drawBitmap(coinBitmap, coin.getxPos(), coin.getyPos(), null);
                invalidate();
            }


            invalidate();
        }

    }

    public Bitmap getCoinBitmap(String coinType){

        switch(coinType){
            case "reg":
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin2), 50, 50, true);
            case "sp":
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin_extra), 50, 50, true);
            default:
                return null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // performs database query outside GUI thread
    private class LoadLevelTask extends AsyncTask<Long, Object, Cursor>
    {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(BallGame.this);

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

            levelCoins = currentLevel.getCoins();
            result.close(); // close the result cursor
            databaseConnector.close(); // close database connection
        } // end method onPostExecute
    }
}
