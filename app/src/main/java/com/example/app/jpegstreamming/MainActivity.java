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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int INITIAL_HIDE_DELAY = 300;
    private final Handler mHideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hideSystemUI();
        }
    };
    private MySurfaceView mView;
    private String screenOrientation = "ORIENTATION_PORTRAIT";
    private RetrieveUrl retrieveUrl;
    private AlertDialog.Builder alertDialog;
    private int widthScreen;
    private int heightScreen;
    private CheckingRunningApp checkRunningApp;
    private View mDecorView;
    private View controlsView;
    private View contentView;
    private View buttonView;
    private Button disconnectBtn;
    private int timesPressed;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steaminginterface);

        Log.d("BackEnd", "On method onCreate ");
//
//        Log.d("BackEnd", "behide set the layout ");
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
        LinearLayout layout_Streamming = (LinearLayout) findViewById(R.id.jpeg_SurfaceView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mView.setLayoutParams(params);
        layout_Streamming.addView(mView);

//        FrameLayout inside = (FrameLayout) findViewById(R.id.layout_streamming);
//        inside.setVisibility(View.VISIBLE);

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
        controlsView = findViewById(R.id.fullscreen_content_controls);
        contentView = findViewById(R.id.jpeg_SurfaceView);
//        buttonView = findViewById(R.id.placeholder_button);

//        Log.d("BackEnd","controlsView value : "+controlsView);
        // Immersive Mode
        mDecorView = getWindow().getDecorView();
//        Log.d("BackEnd","mDecorView value : "+mDecorView);
        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
//                Log.d("BackEnd", "mDecorView.SYSTEM_UI_FLAG_HIDE_NAVIGATION value : " + mDecorView.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                boolean visible = (visibility & mDecorView.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
                controlsView.animate()
                        .alpha(visible ? 1 : 0)
                        .translationY(visible ? 0 : controlsView.getHeight());
//                    buttonView.animate()
//                            .alpha(visible ? 1 : 0)
//                            .translationY(visible ? 0 : controlsView.getHeight());

            }
        });
        contentView.setClickable(true);
        // Immersive Mode
        final GestureDetector clickDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        boolean visible = (mDecorView.getSystemUiVisibility()
                                & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
                        if (visible) {
                            hideSystemUI();
                        } else {
//                            Log.d("BackEnd", "In clickDetector showSytemUI");
                            showSystemUI();
                        }
                        return true;
                    }
                });

        // Immersive Mode
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.d("BackEnd", "On method onTouch " + event);
                return clickDetector.onTouchEvent(event);
            }
        });

        /*buttonView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("BackEnd", "On method onTouch " + event);
                return clickDetector.onTouchEvent(event);
            }
        });*/

        showSystemUI();

        //add surfaceView

        disconnectBtn = (Button) findViewById(R.id.disconnect_button);
        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMe();

            }
        });

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
        Log.d("BackEnd", "onStop");
//        checkRunningApp.activityPaused();
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.d("BackEnd", "onStart");
        super.onStart();
    }


    @Override
    protected void onPause() {
        checkRunningApp.activityPaused();
        Log.d("BackEnd", "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        checkRunningApp.activityResumed();
        Log.d("BackEnd", "onResume");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Log.d("BackEnd", "onBackPressed");
//        try {
        //Thread.currentThread().destroy();

//        }catch (UnsupportedOperationException e){
//                   Log.d("BackEnd","Error on method onBackPressed : " +e);
//            onStop();
//             onDestroy();
//        }

        super.onBackPressed();
        killMe();
    }

    private void killMe() {
        retrieveUrl.cancel(true);
        retrieveUrl = null;
        mView = null;

        this.finish();
        return;
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // if the back button is pressed
//        Thread time = new Thread();
//        Log.d("BackEnd","In method onKeyDown");
//        if(keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            Log.d("BackEnd","result : true ");
//            timesPressed++; //increment on every back button pressed
//
//            //on first press start timer(sleep thread) and wait for key press
//            if(timesPressed == 1)
//            {
//                Toast.makeText(
//                        getBaseContext(),
//                        "Waiting to consider , Press again to exit.",
//                        Toast.LENGTH_SHORT)
//                        .show();
//                time = new Thread(){
//                    public void run(){
//                        try {
//                            synchronized(this){
//
//                                // Wait given period of time or exit on touch
//                                wait(2500);
//                                //if the key is still 1 then the user has not clicked back button
//                                if(timesPressed == 1){
//                                    timesPressed = 0;    //reset for next press
//                                }
//                                //else times pressed will be more than 1 as the (thread woken) will resume @ line 142
//                                else{
//                                    //finish activity
//                                    checkRunningApp.activityPaused();
////                                    Thread.currentThread().interrupt();
//                                    finish();
//                                }
//                            }
//                        }
//                        catch(InterruptedException ex){
//                            ex.printStackTrace();
//                        }
//                    }
//                };
//                time.start();
//                return false;
//            }
//            //if key pressed wake thread
//            else {
//                //wake thread from sleep
//                synchronized(time){
//                    time.notifyAll();
//                }
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // When the window loses focus (e.g. the action overflow is shown),
        // cancel any pending hide action. When the window gains focus,
        // hide the system UI.
//        Log.d("BackEnd", "On method onWindowFocusChanged " + hasFocus);
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
//        Log.d("BackEnd", "On method showSystemUI ");
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
//        Log.d("BackEnd", "On method delayedHide " + delayMillis);
        mHideHandler.removeMessages(0);
        mHideHandler.sendEmptyMessageDelayed(0, delayMillis);
    }
}