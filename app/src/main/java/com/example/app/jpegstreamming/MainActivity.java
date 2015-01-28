package com.example.app.jpegstreamming;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.jar.Attributes;

public class MainActivity extends Activity {

    private MySurfaceView mView;
    private String screenOrientation = "ORIENTATION_PORTRAIT";
    private RetrieveUrl retrieveUrl;
    private AlertDialog.Builder alertDialog;
    private int widthScreen;
    private int heightScreen;
    private CheckingRunningApp checkRunningApp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steaminginterface);
        Log.d("BackEnd", "On method onCreate ");

        Log.d("BackEnd", "behide set the layout ");
        //get size of Screen Divice
       /* Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthScreen = size.x;
        int heightScreen = size.y;*/

        checkRunningApp = new CheckingRunningApp();


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        heightScreen = metrics.heightPixels;
        widthScreen = metrics.widthPixels;

        //Get current screen orientation
        Display displayRotate = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = displayRotate.getOrientation();


        mView = new MySurfaceView(this);
        mView.setHeightScreen(heightScreen);
        mView.setWidthScreen(widthScreen);
        mView.setDisplayScreenOrientation(screenOrientation);

        //initialize AlertDialog
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Connection Problem")
                .setMessage("Error to Connect 192.168.1.1/8080 ,Please Check connection again!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);


        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                screenOrientation = "ORIENTATION_PORTRAIT";
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                screenOrientation = "ORIENTATION_LANDSCAPE";
                break;
        }


        retrieveUrl = new RetrieveUrl(heightScreen, widthScreen, screenOrientation, mView, getApplicationContext(), getBaseContext());
        retrieveUrl.setAlertDialog(alertDialog);
        retrieveUrl.execute();

    }


    // Check screen orientation or screen rotate event here

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            mView.setDisplayScreenOrientation("ORIENTATION_LANDSCAPE");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            mView.setDisplayScreenOrientation("ORIENTATION_PORTRAIT");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, (android.view.Menu) menu);
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        checkRunningApp.activityPaused();
        super.onPause();
    }

    @Override
    protected void onResume() {
        checkRunningApp.activityResumed();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        checkRunningApp.activityPaused();
        super.onBackPressed();
    }
}