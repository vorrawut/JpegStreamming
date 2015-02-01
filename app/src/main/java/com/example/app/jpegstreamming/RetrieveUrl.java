package com.example.app.jpegstreamming;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveUrl extends AsyncTask<Void, Void, String> {
    private final Context baseContext;
    private AlertDialog.Builder alertDialog;
    private Context applicationContext;
    private String displayScreenOrientation;
    private int widthScreen;
    private int heightScreen;
    private HttpURLConnection conn;
    private MySurfaceView mView;
    private int HTTP_CONNECT_TIMEOUT = 5000;
    private int HTTP_SOCKET_TIMEOUT = 5000;
    private int heightImg;
    private int widthImg;


    public RetrieveUrl(int heightScreen, int widthScreen, String displayScreenOrientation, MySurfaceView mView, Context applicationContext, Context baseContext) {
        this.mView = mView;
        this.widthScreen = widthScreen;
        this.heightScreen = heightScreen;
        this.displayScreenOrientation = displayScreenOrientation;
        this.applicationContext = applicationContext;
        this.baseContext = baseContext;
        mView.setHeightImg(heightScreen);
        mView.setWidthImg(widthScreen);

//        alertDialog = new AlertDialog.Builder();
    }

    public void setDisplayScreenOrientation(String displayScreenOrientation) {
        this.displayScreenOrientation = displayScreenOrientation;
    }

    protected String doInBackground(Void... params) {
        Log.d("vut", "In method doInBackground");
        long lastLoopTime = System.currentTimeMillis();
        try {
            URL aURL = new URL("http://192.168.1.1:8080/?action=snapshot&n=");
            int i = 0;
            while (true) {

                conn = (HttpURLConnection) aURL.openConnection();
                conn.getResponseMessage();
                conn.setConnectTimeout(HTTP_CONNECT_TIMEOUT); //set timeout to 5 seconds
                conn.setReadTimeout(HTTP_SOCKET_TIMEOUT);
                switch (conn.getResponseCode()) {
                    case HttpURLConnection.HTTP_OK:
//                        Log.e("" + aURL, " **OK**");

                    case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                        Log.w("" + aURL, " **gateway timeout**");
                        break;// retry
                    case HttpURLConnection.HTTP_UNAVAILABLE:
                        Log.w("" + aURL, "**unavailable**");
                        break;// retry, server is unstable
                    default:
                        Log.w("" + aURL, " **unknown response code**.");
                        break; // abort
                }


                System.out.println(conn.getURL().toString());
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                mView.setDisplayScreenOrientation(displayScreenOrientation);
                heightImg = bitmap.getHeight();
                widthImg = bitmap.getWidth();
                if (heightImg != 0 && widthImg != 0) {
                    mView.setHeightImg(heightImg);
                    mView.setWidthImg(widthImg);
                    Log.d("vut", " heightImg In Loop: " + heightImg);
                    Log.d("vut", " widthImg In Loop: " + widthImg);

                    if (bitmap != null) {
//                    mView.setRes(R.drawable.ic_launcher);

                        mView.setBitmap(bitmap);
                        mView.onDraw(new Canvas());
//                    mView.paintPic(bitmap);
                        Log.d("vut", "i: " + i);
//                    Log.d("vut", "imgHeight : " + heightImg );
//                    Log.d("vut", "imgwidth : " + widthImg );
                        i++;
                        long delta = System.currentTimeMillis() - lastLoopTime;
                        lastLoopTime = System.currentTimeMillis();
//                        Log.d("BackEnd", "lastLooptime : " + lastLoopTime);
                    }
                }
            }

        } catch (java.net.SocketTimeoutException e) {
//            Toast.makeText(applicationContext, "Please Connect ALV Wifi", Toast.LENGTH_LONG).show();
            Log.d("BackEnd", "Error :" + e);
        } catch (ConnectException e) {
//            Toast.makeText(applicationContext, "failed to connect to /192.168.1.1 (port 8080)", Toast.LENGTH_LONG).show();

//            e.printStackTrace();
            Log.d("BackEnd", "Error :" + e);


        } catch (java.io.IOException e) {
//            Toast.makeText(applicationContext, "Please Connect ALV Wifi", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
            Log.d("BackEnd", "Error :" + e);
        }

        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
//
//        this.runOnUiThread(new Runnable() {
//            public void run() {
//                new AlertDialog.Builder(applicationContext)
//                        .setTitle("Delete entry")
//                        .setMessage("Are you sure you want to delete this entry?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        });
//        Toast.makeText(applicationContext, "failed to connect to /192.168.1.1 (port 8080)", Toast.LENGTH_LONG).show();
//
//        alertDialog.setTitle("The Process");
////        alertDialog.setIcon(R.drawable.);
//        alertDialog.setCancelable(false);
//        alertDialog.setMessage("All done!");
//        alertDialog.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
////                        Intent A = new Intent(DownloadActivity.this, Menu_activity.class);
////                        startActivity(A);
////                        finish();
//                        Log.d("BackEnd", "Error :" );
//                    }
//                });
//        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
////                Intent A = new Intent(DownloadActivity.this, Menu_activity.class);
////                startActivity(A);
////                finish();
//                Log.d("BackEnd", "No Error :");
//            }
//        });
        alertDialog.setMessage("failed to connect to /192.168.1.1 (port 8080)");
        alertDialog.show();
    }

    public void setAlertDialog(AlertDialog.Builder alertDialog) {
        this.alertDialog = alertDialog;
    }


}
