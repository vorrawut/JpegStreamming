package com.example.app.jpegstreamming;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Menu extends Activity {

    private String wifi;
    private TextView wifiStatus;
    private TextView wifiName;
    private ConnectivityManager connManager;
    private NetworkInfo mWifi;
    private Intent mainActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        mainActivityIntent = new Intent(Menu.this, MainActivity.class);

//        ImageView securityImg = (ImageView) findViewById(R.id.pic_security);
////        int res = R.drawable.it_photo_124543;
////        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
////        securityImg.setImageBitmap(bitmap);
//        securityImg.setImageResource(R.drawable.it_photo_124543);

        wifiStatus = (TextView) findViewById(R.id.wifiStatus);
        wifiName = (TextView) findViewById(R.id.wifiName);
        Button mButton = (Button) findViewById(R.id.btn);

//        new Thread(new Runnable() {
//            public void run() {


        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Log.d("BackEnd", "Wifi connect : " + mWifi.isConnected());
        wifiStatus.setText("" + mWifi.isConnected());

        if (mWifi.isConnected()) {
            Log.d("BackEnd", "Wifi status : " + mWifi);
            wifiName.setText(mWifi.getExtraInfo());
            Log.d("BackEnd", "Wifi status : " + mWifi.getExtraInfo());
            wifi = mWifi.getExtraInfo().substring(1, mWifi.getExtraInfo().length() - 1);
            if (!wifi.equals("ALV-RPi")) {
                Toast.makeText(Menu.this, "Please Connect ALV Wifi", Toast.LENGTH_LONG).show();
                Log.d("BackEnd", "It Equal");
            } else {
                Toast.makeText(Menu.this, "Successful Loading ", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(Menu.this, "Please Open Wifi cannot connect this program ", Toast.LENGTH_LONG).show();
        }
//            }
//
//    }).start();


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWifi.isConnected()) {
                    if (!wifi.equals("ALV-RPi")) {
                        Toast.makeText(Menu.this, "Please Connect ALV Wifi", Toast.LENGTH_LONG).show();
                        Log.d("BackEnd", "It Equal");
                    } else {

                        startActivity(mainActivityIntent);


                    }
                } else {
                    Toast.makeText(Menu.this, "Please Open Wifi cannot connect this program ", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
