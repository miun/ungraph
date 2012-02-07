package miun.android;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private Camera mCamera;
	private SurfaceHolder mHolder;
	
	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d("ungraph-cam", "Error setting camera preview");
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		//Check some stuff
		if(mHolder.getSurface() == null) return;
		try { mCamera.stopPreview(); } catch (Exception e) {}
		
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		}
		catch (Exception e) {
			Log.d("ungraph-cam","Cannot restart preview");
		}
	}

}
