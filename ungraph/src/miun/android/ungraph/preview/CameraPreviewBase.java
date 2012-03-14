package miun.android.ungraph.preview;

import java.io.IOException;
import java.util.List;

import org.opencv.core.Mat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class CameraPreviewBase extends SurfaceView implements SurfaceHolder.Callback,PreviewCallback {
    private static final String TAG = "Ungraph.CameraPreviewBase";

    private Camera              mCamera;
    private SurfaceHolder       mHolder;
    private int                 mFrameWidth;
    private int                 mFrameHeight;
    private byte[]              mFrame;
    protected Bitmap			mBmp;
    protected Mat				mlastPreview;

    public CameraPreviewBase(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    public int getFrameWidth() {
        return mFrameWidth;
    }

    public int getFrameHeight() {
        return mFrameHeight;
    }

    public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged");
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPreviewSizes();
            mFrameWidth = width;
            mFrameHeight = height;

            // selecting optimal camera preview size
            {
                double minDiff = Double.MAX_VALUE;
                for (Camera.Size size : sizes) {
                    if (Math.abs(size.height - height) < minDiff) {
                        mFrameWidth = size.width;
                        mFrameHeight = size.height;
                        minDiff = Math.abs(size.height - height);
                    }
                }
            }

            params.setPreviewSize(getFrameWidth(), getFrameHeight());
            params.setPictureSize(640,480);
            mCamera.setParameters(params);

            //Create buffer
            try {
            	mFrame = new byte[mFrameWidth * mFrameHeight * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8];
				mCamera.setPreviewDisplay(null);
            	mCamera.addCallbackBuffer(mFrame);
			} catch (IOException e) {
				Log.e(TAG, "mCamera.setPreviewDisplay fails: " + e);
			}
            
            //(Re)create preview bitmap
            if(mBmp != null) mBmp.recycle();
            mBmp = Bitmap.createBitmap(mFrameWidth,mFrameHeight,Bitmap.Config.ARGB_8888);
            
            //Start preview
            mCamera.startPreview();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        openCam();
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
    	if(mFrame != data) {
    		//Something went wrong
    		return; //assert(false);
    	}
    	
        mlastPreview = processFrame(mFrame);

        if (mlastPreview!=null) {
		    Canvas canvas = mHolder.lockCanvas();
		    if (canvas != null) {
		        canvas.drawBitmap(mBmp, (canvas.getWidth() - getFrameWidth()) / 2, (canvas.getHeight() - getFrameHeight()) / 2, null);
		        mHolder.unlockCanvasAndPost(canvas);
		    }
		}

        mCamera.addCallbackBuffer(mFrame);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        releaseCam();
    }

    protected abstract Mat processFrame(byte[] data);
    
    public void takePicture(PreviewActivity context) {
    	if (mlastPreview!=null) new PreviewProcessor(mlastPreview, context);
    	/*if(mCamera != null) {
    		Size size = mCamera.getParameters().getPreviewSize();
    		int format = mCamera.getParameters().getPictureFormat();
    		mFrame = new byte[size.width * size.height * ImageFormat.getBitsPerPixel(format) / 8];
    		mCamera.addCallbackBuffer(mFrame);
    		mCamera.takePicture(new ShutterCallback() {
				public void onShutter() {
					Log.d(TAG, "SHUTTER");
				}
			},new PictureCallback() {
				public void onPictureTaken(byte[] data, Camera camera) {
					Log.d(TAG, "RAW");
				}
			}, new PictureCallback() {
				public void onPictureTaken(byte[] data, Camera camera) {
					Log.d(TAG, "POSTVIEW");
				}
			}, new PictureCallback() {
				public void onPictureTaken(byte[] data, Camera camera) {
					Log.d(TAG, "JPEG");
				}
			});
    	}*/
    }
    
    public void openCam() {
    	//Open camera and set preview callback class
        mCamera = Camera.open();
        mCamera.setPreviewCallbackWithBuffer(this);
        
        Parameters cp = mCamera.getParameters();
        cp.setPictureFormat(ImageFormat.NV21);
        mCamera.setParameters(cp);
    }
    
    public void releaseCam() {
    	if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            
            if(mBmp != null) mBmp = null;
        }
    }
    
}
