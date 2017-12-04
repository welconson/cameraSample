package com.example.shenwk.camerasample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import javax.security.auth.login.LoginException;

/**
 * Created by shenwk on 2017/11/30.
 */

public class CameraView implements Camera.PictureCallback{
    private final static String TAG = "CameraView";
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mHolder = null;
    private Camera mCamera = null;
    private int mPreviewWidth = 1080;
    private int mPreviewHeight = 2160;
    private Camera.Parameters mParameters = null;
    private Context mActivityContext = null;

    public CameraView(Context mActivityContext, SurfaceHolder surfaceHolder) {
        this.mActivityContext = mActivityContext;
        this.mCamera = Camera.open();
        this.mHolder = surfaceHolder;;
        mParameters = mCamera.getParameters();
        Log.i(TAG, "Height = " + mPreviewHeight + " ,width = " + mPreviewWidth);
    }

    public void startCameraPreview(){
        if(mCamera == null){
            mCamera = Camera.open();
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(mParameters);
        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
    }

    public void stopCameraPreview(){
        if(mCamera == null)
            return;
    mCamera.stopPreview();
    }

    public void releaseCamera(){
        if(mCamera == null)
            return;
        mCamera.release();
        mCamera = null;
    }

    public void takePicture(){
        if(mCamera == null)
            return;
        Log.i(TAG, "takePicture");
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i(TAG,"Picture has been taken");

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivityContext);
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setTitle("take picture")
                .setMessage("save or discard ?")
                .show();
        startCameraPreview();
    }
}
