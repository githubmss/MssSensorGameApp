package com.mss.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mss.utils.Alert;
import com.mss.utils.Session;

/**
 * @author master software solutions
 *         <p>
 *         All logic of game implemented here
 */

public class PlayActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer, mSensorGyroscope;
    private TextView txtAccelYaxisValue, txtAccelYaxisKey, txtGyroXaxisValue, txtGyroYaxisKey,
            mTxtGpsXaxisValue, mTxtGpsYaxisKey, mtxtconnection;
    public CheckBox chkAccelXaxis, chkGyroXaxis, chkGpsXaxis;
    public long gyroTime, accelTime, gpsTime;
    int accelKeyInt, gyroKeyInt, gpsKeyInt;

    private static final int RESULT_SETTINGS = 1;

    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private static final String NMEA_SATELLITES_IN_VIEW = "GPRMC";
    private static final String NMEA_DELIMITER = ",";
    private Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initUI();
    }

    /**
     * Initialization of UI for the Activity
     */
    private void initUI() {
        txtAccelYaxisValue = (TextView) findViewById(R.id.txt_accelValue);
        txtAccelYaxisKey = (TextView) findViewById(R.id.txt_accelKey);

        txtGyroXaxisValue = (TextView) findViewById(R.id.txt_gyroValue);
        txtGyroYaxisKey = (TextView) findViewById(R.id.txt_gyroKey);

        mTxtGpsXaxisValue = (TextView) findViewById(R.id.txt_gpsValue);
        mTxtGpsYaxisKey = (TextView) findViewById(R.id.txt_gpsKey);
        mtxtconnection = (TextView) findViewById(R.id.connection);

        chkAccelXaxis = (CheckBox) findViewById(R.id.chk_accel);
        chkGyroXaxis = (CheckBox) findViewById(R.id.chk_gyro);
        chkGpsXaxis = (CheckBox) findViewById(R.id.chk_gps);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        handleGPS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO for future implementations

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//		handleGPS();
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                handleAccelerometer(event.values);
                break;
            case Sensor.TYPE_GYROSCOPE:
                handleGyroscope(event.values);
                break;

        }

    }

    /**
     * This method handles the value Gyroscope
     */

    private void handleGyroscope(float[] values) {
        int gyroint = Math.abs((int) (Math.round(values[0] * 100.0) / 100.0));
        String gyroValue = String.valueOf(gyroint * 10);
        txtGyroXaxisValue.setText(gyroValue);
        gyroKeyInt = (int) (Session.getGyroValue() * 10);
        txtGyroYaxisKey.setText(String.valueOf(gyroKeyInt));
        chkGyroXaxis.setChecked(false);

        if (gyroValue.contains(String.valueOf(gyroKeyInt))) {
            chkGyroXaxis.setChecked(true);
            gyroTime = System.currentTimeMillis();
            checkResult();
        }

    }

    /**
     * This method handles the Accelerometer
     */
    private void handleAccelerometer(float[] values) {
        int accelint = Math.abs((int) (Math.round(values[1] * 100.0) / 100.0));
        String accelValue = String.valueOf(accelint * 10);
        txtAccelYaxisValue.setText(accelValue);
        accelKeyInt = (int) (Session.getAccerlValue() * 10);
        txtAccelYaxisKey.setText(String.valueOf(accelKeyInt));
        chkAccelXaxis.setChecked(false);

        if (accelValue.contains(String.valueOf(accelKeyInt))) {
            chkAccelXaxis.setChecked(true);
            accelTime = System.currentTimeMillis();
            checkResult();

        }

    }

    /**
     * This method handles the Gps speed
     */
    private void handleGPS() {

        flag = displayGpsStatus();

        if (flag) {

            locationListener = new MyLocationListener();

            locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);

            locationMangaer.addNmeaListener(new NmeaListener() {
                public void onNmeaReceived(long timestamp, String nmea) {

                    // Log.d(TAG, "Nmea Received :");
                    // Log.d(TAG, "Timestamp is :" + timestamp + "   nmea is :"
                    // + nmea);

                    if (nmea.contains(NMEA_SATELLITES_IN_VIEW)) {
                        // Separate out everything for parsing
                        String[] nmeaParts = nmea.split(NMEA_DELIMITER);

                        String speed = nmeaParts[7].toString();
                        String conn = nmeaParts[2].toString();

                        // String speeda = "number of items   " +
                        // nmeaParts.length + "  \n" + nmeaParts[0] + " \n"
                        // + nmeaParts[1] + " \n" + nmeaParts[2] + " \n" +
                        // nmeaParts[3] + " \n" + nmeaParts[4]
                        // + " \n" + nmeaParts[5] + " \n" + nmeaParts[6] + " \n"
                        // + "Speed is   " + speed;

                        if (speed.equalsIgnoreCase(""))
                            speed = "0";

                        float meterSpeed = 0.0f;
                        float temp1 = Float.parseFloat(speed);

//						meterSpeed = (temp1 * 1.852f) * 1000f;
                        // meterSpeed = (temp1 * 1.852f) * 0.277777778f;
                        meterSpeed = (temp1 * 1.852f) * 0.609f;

//						int gpsint = Math.abs((int) (Math.round(meterSpeed * 100.0) / 10000.0));
                        int gpsint = Math.abs((int) (meterSpeed));
                        String gpsValue = String.valueOf(gpsint);
                        mTxtGpsXaxisValue.setText(gpsValue);

                        mtxtconnection.setText("Speed: " + speed + "mph");
                        mtxtconnection.append("\n            " + conn);
                        mtxtconnection.append("\n gpsvalue:   " + gpsValue);

                        gpsKeyInt = (int) ((Session.getGpsValue()));
                        mTxtGpsYaxisKey.setText(String.valueOf(gpsKeyInt));
                        chkGpsXaxis.setChecked(false);

                        if (gpsValue.contains(String.valueOf(gpsKeyInt))) {
                            chkGpsXaxis.setChecked(true);
                            gpsTime = System.currentTimeMillis();
                            checkResult();

                        }

                    }
                }
            });

        }

    }

    /*----------Method to Check GPS is enable or disable ------------- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener, NmeaListener {
        @Override
        public void onLocationChanged(Location loc) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onNmeaReceived(long timestamp, String nmea) {
            // TODO Auto-generated method stub
            // txtSpeed.append("NMEA Sentence: " + nmea);
        }
    }

    /**
     * This method checks result and show alert if all values match
     */
    public void checkResult() {

        {
            if (gpsTime != 0) {
                long deltaTime = Math.abs(accelTime - gyroTime);
                mtxtconnection.append("\n delta time:  " + deltaTime);
                if (deltaTime > 1 && deltaTime < 1000) {
                    chkAccelXaxis.setChecked(true);
                    chkGyroXaxis.setChecked(true);
                    chkGpsXaxis.setChecked(true);

                    mSensorManager.unregisterListener(this, mSensorGyroscope);
                    mSensorManager.unregisterListener(this, mSensorAccelerometer);

                    txtAccelYaxisValue.setText(String.valueOf(accelKeyInt));
                    txtGyroXaxisValue.setText(String.valueOf(gyroKeyInt));
                    mTxtGpsXaxisValue.setText(String.valueOf(gpsKeyInt));

                    new Alert(this).showAlert("You won..!!! \nAll Sensor's Match:- Jackpot!!! ");

                    // handler used for finish activity after 5 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PlayActivity.this.finish();
                        }
                    }, 5000);

                }
            }
        }
    }
}
