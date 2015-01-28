package com.example.app.jpegstreamming;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.app.jpegstreamming.R;

import java.util.jar.Attributes;

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
    private Bitmap newBitmap ;
    private AlertDialog.Builder alertDialog;
    private int newWidth;
    private int newHeight;
    private int viewWidth;
    private int viewHeight;
    private CheckingRunningApp checkingRunningApp;
    private int heightLayout;
    private int widthLayout;

    public MySurfaceView(Context context,CheckingRunningApp checkingRunningApp,int widthScreen,int heightScreen) {
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
        init(context, attrs, defStyle,0);
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
        _thread = new TutorialThread(getHolder(), this,checkingRunningApp);
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

            Log.d("BackEnd", "On Method onDraw ");
        Bitmap _scratch = BitmapFactory.decodeResource(getResources(),
                R.drawable.cat);

//        int cal = (int)Math.floor((float)(widthImg/heightImg)*getHeight());
//        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, getHeight(), cal, true);
//        Log.d("BackEnd", "Bitmap : " + _scratch);
//            bitmap = BitmapFactory.decodeResource(getResources(),
//                    R.drawable.cat);

            viewWidth = canvas.getHeight();
            viewHeight = canvas.getHeight();

            Log.d("BackEnd", "widthScreen : " + widthScreen);
            Log.d("BackEnd", "heightScreen : " + heightScreen);
            Log.d("BackEnd", "getHeight : " + getHeight());
            Log.d("BackEnd", "getWidth : " + getWidth());
            Log.d("BackEnd", "viewWidth : " + viewWidth);
            Log.d("BackEnd", "heightLayout : " + heightLayout);
            Log.d("BackEnd", "widthLayout : " + widthLayout);
            Log.d("BackEnd", "viewHeight : " + viewHeight);
            Log.d("BackEnd", "heightImg : " + heightImg);
            Log.d("BackEnd", "widthImg : " + widthImg);
//            newHeight = (int) Math.floor((float) (heightImg / widthImg) * getWidth());
//            newBitmap = Bitmap.createScaledBitmap(bitmap, viewWidth, newHeight, false);
//            canvas.drawBitmap(bitmap, 0, 0, null);

        if (displayScreenOrientation.equals("ORIENTATION_PORTRAIT")) {
            if (bitmap != null) {
                newHeight = (int) Math.floor(((float)heightImg / (float)widthImg) * (float)getWidth());
                Log.d("BackEnd", "newHeight : " + newHeight);
                if(newHeight != 0) {
                    newBitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), newHeight, false);
                    if (newBitmap != null) {
                        canvas.drawBitmap(newBitmap, 0, 0, null);
                    }
                }

            }
        } else if (displayScreenOrientation.equals("ORIENTATION_LANDSCAPE")) {
            if (bitmap != null) {
                newWidth = (int) Math.floor(((float)widthImg / (float)heightImg) * (float)getHeight());
                Log.d("BackEnd", "newWidth : " + newWidth);
                if(newWidth != 0) {
                    newBitmap = Bitmap.createScaledBitmap(bitmap, getHeight(), newWidth, false);
                    if (newBitmap != null) {
                        canvas.drawBitmap(newBitmap, 0, 0, null);
                    }
                }
            }
        } else {
            alertDialog.show();
        }

        }catch (Exception e){
            Log.d("BackEnd","Error"+ e);
        }

    }

    public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Log.d("BackEnd","On method surfaceCreated "+ checkingRunningApp.isActivityVisible());
        if (_thread.getState() == Thread.State.NEW)
        {
            _thread.setRunning(true);
            _thread.start();
        }else{
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

    public void setCheckingRunningApp(CheckingRunningApp checkingRunningApp) {
        this.checkingRunningApp = checkingRunningApp;
    }

    class TutorialThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private MySurfaceView _panel;
        private boolean _run = false;

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
    }
}