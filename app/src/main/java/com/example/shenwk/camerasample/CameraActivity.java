package com.example.shenwk.camerasample;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback{
    private final static String TAG = "CameraActivity";
    private CameraView mCameraView = null;
    SurfaceView mSufaceView = null;
    SurfaceHolder mHolder = null;
    boolean mSurfaceCreated = false;
    private int previewHeight = 100;
    private int previewWidth = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG,"before request for permission" + Build.VERSION.SDK_INT);
        // Android 6.0 及以上需要申请运行权限
        // Request permissions at runtime on Android 6.0 or later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestAllPermissionsIfNeed();
        }

        mSufaceView = findViewById(R.id.mSurfaceView);
        mHolder = mSufaceView.getHolder();
        mHolder.addCallback(this);
        mCameraView = new CameraView(this, mHolder);
        mSufaceView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getRawX();
                float y = event.getRawY();
                Log.i(TAG, "x = " + x + "," + " y = " + y);
                mCameraView.takePicture();
                return false;
            }
        });
        Log.i(TAG, "surface is valid? : " + mHolder.getSurface().isValid());
    }

    @TargetApi(23)
    private void requestAllPermissionsIfNeed(){
        // 申请相机权限
        // Camera permission
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Log.i(TAG,"show");
                //AlertError.showDialog(this, getResources().getString(R.string.error_title), getResources().getString(R.string.no_camera_perm_hint));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage("ask for camera permission");
                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissDialog(which);
                    }
                });
                builder.create().show();
            } else{
                Log.i(TAG,"ask for permission");
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 0);
            }
        }
        else{
            Log.i(TAG,"Permission have already been granted");
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            // 申请相机权限成功，打开相机，开始预览
            // camera permission granted, open camera and start preview
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG,"Camera permission have been granted");
            }
            else{
                Log.i(TAG,"Camera permission request have been rejected");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.releaseCamera();
        Log.i(TAG, "stop preview");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.startCameraPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceCreated = true;
        mCameraView.startCameraPreview();
        Log.i(TAG,"start preview");
        Log.i(TAG, "surface is valid? : " + mHolder.getSurface().isValid());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surface changed");
        mCameraView.stopCameraPreview();
        mCameraView.startCameraPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceCreated = false;
        Log.i(TAG, "surface destroyed");
        Log.i(TAG, "surface is valid? : " + mHolder.getSurface().isValid());
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }
}
