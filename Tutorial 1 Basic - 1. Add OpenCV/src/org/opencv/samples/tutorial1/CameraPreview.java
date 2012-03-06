package org.opencv.samples.tutorial1;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}/*

	public void surfaceCreated(SurfaceHolder holder) {
	// The Surface has been created, now tell the camera where to draw the preview.
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
		Log.d(TAG, "Error setting camera preview: " + e.getMessage());
	}
	Ê Ê}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		
		if (mHolder.getSurface() == null){
			Ê Ê Ê Ê Ê// preview surface does not exist
			Ê Ê Ê Ê Êreturn;
			Ê Ê Ê Ê}

		Ê Ê Ê Ê// stop preview before making changes
		Ê Ê Ê Êtry {
			Ê Ê Ê Ê Ê ÊmCamera.stopPreview();
			Ê Ê Ê Ê} catch (Exception e){
				Ê Ê Ê Ê Ê// ignore: tried to stop a non-existent preview
				Ê Ê Ê Ê}

			Ê Ê Ê Ê// set preview size and make any resize, rotate or
			Ê Ê Ê Ê// reformatting changes here

			Ê Ê Ê Ê// start preview with new settings
			Ê Ê Ê Êtry {
				Ê Ê Ê Ê Ê ÊmCamera.setPreviewDisplay(mHolder);
				Ê Ê Ê Ê Ê ÊmCamera.startPreview();

				Ê Ê Ê Ê} catch (Exception e){
					Ê Ê Ê Ê Ê ÊLog.d(TAG, "Error starting camera preview: " + e.getMessage());
					Ê Ê Ê Ê}
				Ê Ê}*/
}