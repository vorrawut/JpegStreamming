package com.example.app.jpegstreamming;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import info.hoang8f.widget.FButton;


public class Menu extends Activity {

    private String wifi;
    private TextView wifiStatus;
    private TextView wifiName;
    private ConnectivityManager connManager;
    private NetworkInfo mWifi;
    private Intent mainActivityIntent;
    private AlertDialog.Builder wifiAlertDialog;
    private int REQ_TRANS_CHECK = 0;
    private int heightScreen;
    private int widthScreen;
    private Toast toast;
    private TextView title;
    private TextView wifiStatusTopic;
    private TextView wifiNameTopic;
    private WifiManager wifiManager;
    private Intent immersive_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        mainActivityIntent = new Intent(Menu.this, MainActivity.class);
//        immersive_activity = new Intent(Menu.this, ImmersiveStickyActivity.class);
//        ImageView securityImg = (ImageView) findViewById(R.id.pic_security);
////        int res = R.drawable.it_photo_124543;
////        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
////        securityImg.setImageBitmap(bitmap);
//        securityImg.setImageResource(R.drawable.it_photo_124543);

        wifiStatus = (TextView) findViewById(R.id.wifiStatus);
        wifiName = (TextView) findViewById(R.id.wifiName);
//        title = (TextView) findViewById(R.id.textView);
        wifiStatusTopic = (TextView) findViewById(R.id.wifiText);
        wifiNameTopic = (TextView) findViewById(R.id.wifiNameText);


        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

        Typeface boldFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface mediumFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface lightFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        wifiStatus.setTypeface(lightFont);
        wifiName.setTypeface(lightFont);
//        title.setTypeface(boldFont);
        wifiStatusTopic.setTypeface(mediumFont);
        wifiNameTopic.setTypeface(mediumFont);

        FButton connectBtn = (FButton) findViewById(R.id.btn);
//        FButton disConnectBtn = (FButton) findViewById(R.id.btn2);
//        new Thread(new Runnable() {
//            public void run() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        heightScreen = metrics.heightPixels;
        widthScreen = metrics.widthPixels;


        checkWifiConnecting();


        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWifi.isConnected()) {
                    if (!wifi.equals("ALV-RPi")) {
//                        Toast.makeText(Menu.this, "Please Connect ALV Wifi", Toast.LENGTH_LONG).show();

                        toast = Toast.makeText(Menu.this,
                                "Please Connect ALV Wifi", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 450);
                        toast.show();
                        Log.d("BackEnd", "Not It Equal");
                    } else {

                        startActivity(mainActivityIntent);


                    }
                } else {
//                    Toast.makeText(Menu.this, "Please Open Wifi cannot connect this program ", Toast.LENGTH_LONG).show();
//                    toast = Toast.makeText(Menu.this,
//                            "Please Open Wifi cannot connect this program ", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 450);
//                    toast.show();

                    checkWifiConnecting();
                }
            }
        });

//        disConnectBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                wifiManager.setWifiEnabled(false);
//                wifiStatus.setText("False");
//                wifiName.setText("NULL");
//            }
//        });

    }

    private void checkWifiConnecting() {

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
//                Toast.makeText(Menu.this, "Please Connect ALV Wifi", Toast.LENGTH_LONG).show();
                toast = Toast.makeText(Menu.this,
                        "Please Connect ALV Wifi", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 450);
                toast.show();

                Log.d("BackEnd", "It Equal");
            } else {
//                Toast.makeText(Menu.this, "Successful Loading ", Toast.LENGTH_LONG).show();

//                toast = Toast.makeText(Menu.this,
//                        "Successful Loading ", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 450);
//                toast.show();
            }
        } else {
            Toast.makeText(Menu.this, "Please Open Wifi cannot connect this program ", Toast.LENGTH_LONG).show();
            wifiAlertDialog = new AlertDialog.Builder(this)
                    .setTitle("Connection Problem")
                    .setMessage("Please connection Wifi !")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), REQ_TRANS_CHECK);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            wifiAlertDialog.show();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_TRANS_CHECK) {
            checkWifiConnecting();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
