package com.ece150.bw.ece150251homework2;

import java.io.IOException;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder someHolder;
    private Camera theCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        theCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        someHolder = getHolder();
        someHolder.addCallback(this);
        //just in case for devices lower than 3.0
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            someHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // surface created, say where to draw preview
        // preview rotated, need to fix
        theCamera.setDisplayOrientation(90);
        try {
            theCamera.setPreviewDisplay(holder);
            theCamera.startPreview();
        } catch (IOException e) {
            Log.d("CameraPreview", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // implement in main
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (someHolder.getSurface() == null){
            // preview surface DNE
            return;
        }

        // stop preview before making changes
        try {
            theCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            theCamera.setPreviewDisplay(someHolder);
            theCamera.startPreview();

        } catch (Exception e){
            Log.d("CameraPreview", "Error starting camera preview: " + e.getMessage());
        }
    }

}
