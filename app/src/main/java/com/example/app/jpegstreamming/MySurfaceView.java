package com.example.app.jpegstreamming;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private TutorialThread _thread;
    private int heightImg;
    private int widthImg;
    private String displayScreenOrientation = "ORIENTATION_PORTRAIT";
    private Bitmap bitmap;
    private int heightScreen;
    private int widthScreen;
    private Context context;
    private AttributeSet attrs;
    private int defStyleAttr;
    private int defStyleRes;
    private Bitmap newBitmap;
    private AlertDialog.Builder alertDialog;
    private int newWidth;
    private int newHeight;
    private int viewWidth;
    private int viewHeight;
    private CheckingRunningApp checkingRunningApp;
    private int heightLayout;
    private int widthLayout;

    public MySurfaceView(Context context, CheckingRunningApp checkingRunningApp, int widthScreen, int heightScreen) {
        super(context);
        this.checkingRunningApp = checkingRunningApp;
        this.widthScreen = widthScreen;
        this.heightScreen = heightScreen;
        init(context, null, 0, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.context = context;
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        this.defStyleRes = defStyleRes;
        getHolder().addCallback(this);
        _thread = new TutorialThread(getHolder(), this, checkingRunningApp);
        heightLayout = getHeight();
        widthLayout = getWidth();
        initAlertDialog();

    }

    private void initAlertDialog() {
        alertDialog = new AlertDialog.Builder(context)
                .setTitle("Interface problem")
                .setMessage("Error Interface drawing ,Please check solution fo Orientation Screen ")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(android.R.drawable.ic_dialog_alert);
    }


   /* public MySurfaceView(Context context, int heightScreen, int widthScreen) {
        super(context);
        this.heightScreen = heightScreen;
        this.widthScreen = widthScreen;
        getHolder().addCallback(this);
//        _thread = new TutorialThread(getHolder(), this);
    }*/


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {

//            Log.d("BackEnd", "On Method onDraw ");
//        Bitmap _scratch = BitmapFactory.decodeResource(getResources(),
//                R.drawable.cat);

//        int cal = (int)Math.floor((float)(widthImg/heightImg)*getHeight());
//        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, getHeight(), cal, true);
//        Log.d("BackEnd", "Bitmap : " + _scratch);
//            bitmap = BitmapFactory.decodeResource(getResources(),
//                    R.drawable.cat);

//            viewWidth = canvas.getHeight();
//            viewHeight = canvas.getHeight();

          /*  Log.d("BackEnd", "widthScreen : " + widthScreen);
            Log.d("BackEnd", "heightScreen : " + heightScreen);
            Log.d("BackEnd", "getHeight : " + getHeight());
            Log.d("BackEnd", "getWidth : " + getWidth());
            Log.d("BackEnd", "viewWidth : " + viewWidth);
            Log.d("BackEnd", "heightLayout : " + heightLayout);
            Log.d("BackEnd", "widthLayout : " + widthLayout);
            Log.d("BackEnd", "viewHeight : " + viewHeight);
            Log.d("BackEnd", "heightImg : " + heightImg);
            Log.d("BackEnd", "widthImg : " + widthImg);*/
//            newHeight = (int) Math.floor((float) (heightImg / widthImg) * getWidth());
//            newBitmap = Bitmap.createScaledBitmap(bitmap, viewWidth, newHeight, false);
//            canvas.drawBitmap(bitmap, 0, 0, null);

            if (displayScreenOrientation.equals("ORIENTATION_PORTRAIT")) {
                if (bitmap != null) {
                    newHeight = (int) Math.floor(((float) heightImg / (float) widthImg) * (float) getWidth());
//                Log.d("BackEnd", "newHeight : " + newHeight);
                    if (newHeight != 0) {
                        newBitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), newHeight, false);
                        if (newBitmap != null) {
                            canvas.drawBitmap(newBitmap, 0, 0, null);
                        }
                    }

                }
            } else if (displayScreenOrientation.equals("ORIENTATION_LANDSCAPE")) {
                if (bitmap != null) {
                    newWidth = (int) Math.floor(((float) widthImg / (float) heightImg) * (float) getHeight());
                    if (newWidth != 0) {
                        newBitmap = Bitmap.createScaledBitmap(bitmap, getHeight(), newWidth, false);
                        if (newBitmap != null) {
                            canvas.drawBitmap(newBitmap, 0, 0, null);
                        }
                    }
                }
            } else {
                alertDialog.show();
            }

        } catch (Exception e) {
            Log.d("BackEnd", "Error" + e);
        }

    }

    public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Log.d("BackEnd", "On method surfaceCreated " + checkingRunningApp.isActivityVisible());
        if (_thread.getState() == Thread.State.NEW) {
            _thread.setRunning(true);
            _thread.start();
        } else {
            _thread.setRunning(true);
            _thread.run();
        }

    /*
        _thread.setRunning(true);
        _thread.start();*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        boolean retry = true;
        _thread.setRunning(false);
        while (retry) {
            try {
                _thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public void setHeightImg(int heightImg) {
        this.heightImg = heightImg;
    }

    public void setWidthImg(int widthImg) {
        this.widthImg = widthImg;
    }

    public void setDisplayScreenOrientation(String displayScreenOrientation) {
        this.displayScreenOrientation = displayScreenOrientation;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setHeightScreen(int heightScreen) {
        this.heightScreen = heightScreen;
    }

    public void setWidthScreen(int widthScreen) {
        this.widthScreen = widthScreen;
    }

//    public void setCheckingRunningApp(CheckingRunningApp checkingRunningApp) {
//        this.checkingRunningApp = checkingRunningApp;
//    }

    class TutorialThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private MySurfaceView _panel;
        private boolean _run = false;
     /*   private long beginTime;
        private int frameSkipped;
        private long timeDiff;
        private int sleepTime;
        // desired fps
        private final static int    MAX_FPS = 50;
                // maximum number of frames to be skipped
        private final static int    MAX_FRAME_SKIPS = 5;
                // the frame period
        private final static int    FRAME_PERIOD = 1000 / MAX_FPS;
                // Stuff for stats *//*
        private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
                // we'll be reading the stats every second
        private final static int    STAT_INTERVAL = 1000; //ms
                // the average will be calculated by storing
                // the last n FPSs
        private final static int    FPS_HISTORY_NR = 10;
                // last time the status was stored
        private long lastStatusStore = 0;
                // the status time counter
        private long statusIntervalTimer    = 0l;
                // number of frames skipped since the game started
        private long totalFramesSkipped         = 0l;
                // number of frames skipped in a store cycle (1 sec)
        private long framesSkippedPerStatCycle  = 0l;
                // number of rendered frames in an interval
        private int frameCountPerStatCycle = 0;
        private long totalFrameCount = 0l;
                // the last FPS values
        private double  fpsStore[];
                // the number of times the stat has been read
        private long    statsCount = 0;
                // the average FPS since the game started
        private double  averageFps = 0.0;
        private long framesSkipped;*/


        public TutorialThread(SurfaceHolder surfaceHolder, MySurfaceView panel, CheckingRunningApp checkingRunningApp) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _panel.onDraw(c);
                    }
                } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

       /* @Override
        public void run() {
            Canvas c;
            Log.d("Thread","Start Thread Loop ");
            // Initialize timing elements for start gathering
            initTimingElements();



            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {

                        beginTime = System.currentTimeMillis();
                        frameSkipped = 0 ;

                        //update canvas state
                        _panel.onDraw(c);

                        timeDiff = System.currentTimeMillis()- beginTime;

                        //calculate sleep time
                        sleepTime = (int)(FRAME_PERIOD - timeDiff);

                        if( sleepTime > 0 ){
                            //if sleepTime > 0 we're ok
                            try{
                                //send the thread to sleep for a short period
                                //very useful battery saving
                                Thread.sleep(sleepTime);
                            }catch (Exception e){}
                        }

                        while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {

                            // we need to catch up



                            sleepTime += FRAME_PERIOD;  // add frame period to check if in next frame

                            framesSkipped++;

                        }
                        if (framesSkipped > 0) {

                            Log.d("Thread", "Skipped:" + framesSkipped);

                        }
                        // for statistics

                        framesSkippedPerStatCycle += framesSkipped;

                        // calling the routine to store the gathered statistics

                        storeStats();
                }
                } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }


        private void storeStats() {

            frameCountPerStatCycle++;

            totalFrameCount++;


            // check the actual time

            statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);


            if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {

                // calculate the actual frames pers status check interval

                double actualFps = (double) (frameCountPerStatCycle / (STAT_INTERVAL / 1000));


                //stores the latest fps in the array

                fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

                // increase the number of times statistics was calculated

                statsCount++;


                double totalFps = 0.0;

                // sum up the stored fps values

                for (int i = 0; i < FPS_HISTORY_NR; i++) {

                    totalFps += fpsStore[i];

                }

                // obtain the average

                if (statsCount < FPS_HISTORY_NR) {

                    // in case of the first 10 triggers

                    averageFps = totalFps / statsCount;

                } else {

                    averageFps = totalFps / FPS_HISTORY_NR;

                }
                // saving the number of total frames skipped

                totalFramesSkipped += framesSkippedPerStatCycle;

                // resetting the counters after a status record (1 sec)

                framesSkippedPerStatCycle = 0;

                statusIntervalTimer = 0;

                frameCountPerStatCycle = 0;
                statusIntervalTimer = System.currentTimeMillis();

                lastStatusStore = statusIntervalTimer;

                Log.d("Thread", "Average FPS:" + df.format(averageFps));

            }
        }



        private void initTimingElements() {
            // initialise timing elements
            fpsStore = new double[FPS_HISTORY_NR];
            for (int i = 0; i < FPS_HISTORY_NR; i++) {

                fpsStore[i] = 0.0;

            }

            Log.d("Thread"+ ".initTimingElements()", "Timing elements for stats initialised");

        }*/
    }
}