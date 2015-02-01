package com.example.app.jpegstreamming;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    private MySurfaceView mView;
    private String screenOrientation = "ORIENTATION_PORTRAIT";
    private RetrieveUrl retrieveUrl;
    private AlertDialog.Builder alertDialog;
    private int widthScreen;
    private int heightScreen;
    private CheckingRunningApp checkRunningApp;
    private View mDecorView;
    private static final int INITIAL_HIDE_DELAY = 300;
    private LinearLayout controlsView;
    private FrameLayout contentView;
    private final Handler mHideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hideSystemUI();
        }
    };


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


        mView = new MySurfaceView(this, checkRunningApp, heightScreen, widthScreen);
        mView.setHeightScreen(heightScreen);
        mView.setWidthScreen(widthScreen);
//        mView.setCheckingRunningApp(checkRunningApp);
        mView.setDisplayScreenOrientation(screenOrientation);

        //Get Framelayout
        FrameLayout layout_Streamming = (FrameLayout) findViewById(R.id.layout_streamming);


//        setContentView(mView);
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


        //Immersive Mode
        contentView = (FrameLayout) findViewById(R.id.onFrameLayout);
        controlsView = (LinearLayout) findViewById(R.id.fullscreen_content_controls);
//        Log.d("BackEnd","controlsView value : "+controlsView);
        // Immersive Mode
        mDecorView = getWindow().getDecorView();
//        Log.d("BackEnd","mDecorView value : "+mDecorView);
        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.d("BackEnd", "mDecorView.SYSTEM_UI_FLAG_HIDE_NAVIGATION value : " + mDecorView.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                boolean visible = (visibility & mDecorView.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
                controlsView.animate()
                        .alpha(visible ? 1 : 0)
                        .translationY(visible ? 0 : controlsView.getHeight());

            }
        });
        contentView.setClickable(true);
        // Immersive Mode
        final GestureDetector clickDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        Log.d("BackEnd", "On method onSingleTapUp " + e);
                        boolean visible = (mDecorView.getSystemUiVisibility()
                                & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
                        if (visible) {
                            hideSystemUI();
                        } else {
                            showSystemUI();
                        }
                        return true;
                    }
                });

        // Immersive Mode
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("BackEnd", "On method onTouch " + event);
                return clickDetector.onTouchEvent(event);
            }
        });

        showSystemUI();

        //add surfaceView
        layout_Streamming.addView(mView);
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

//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, (android.view.Menu) menu);
//        return true;
//    }


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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // When the window loses focus (e.g. the action overflow is shown),
        // cancel any pending hide action. When the window gains focus,
        // hide the system UI.
        Log.d("BackEnd", "On method onWindowFocusChanged " + hasFocus);
        if (hasFocus) {
            delayedHide(INITIAL_HIDE_DELAY);
        } else {
            mHideHandler.removeMessages(0);
        }
//        if (hasFocus) {
//            mDecorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    // Immersive Mode
    private void hideSystemUI() {
        Log.d("BackEnd", "On method hideSystemUI ");
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // Immersive Mode
    private void showSystemUI() {
        Log.d("BackEnd", "On method showSystemUI ");
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

//    // Immersive Mode
//    private final Handler mHideHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Log.d("BackEnd","On method handleMessage "+msg);
//            super.handleMessage(msg);
//            hideSystemUI();
//        }
//    };

    // Immersive Mode
    private void delayedHide(int delayMillis) {
        Log.d("BackEnd", "On method delayedHide " + delayMillis);
        mHideHandler.removeMessages(0);
        mHideHandler.sendEmptyMessageDelayed(0, delayMillis);
    }
}