package miun.android.ungraph.preview;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class CameraPreviewBase extends SurfaceView implements SurfaceHolder.Callback,Runnable,PreviewCallback {
    private static final String TAG = "Ungraph.CameraPreviewBase";

    private Camera              mCamera;
    private SurfaceHolder       mHolder;
    private int                 mFrameWidth;
    private int                 mFrameHeight;
    private byte[]              mFrame;
    protected Bitmap			mBmp;
    private boolean             mThreadRun;

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
        
        //Open camera and set preview callback class
        mCamera = Camera.open();
        mCamera.setPreviewCallbackWithBuffer(this);
        
        //(new Thread(this)).start();
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
    	if(mFrame != data) {
    		//Something went wrong
    		assert(false);
    	}
    	
        boolean result = processFrame(mFrame);

        if (result) {
		    Canvas canvas = mHolder.lockCanvas();
		    if (canvas != null) {
		        canvas.drawBitmap(mBmp, (canvas.getWidth() - getFrameWidth()) / 2, (canvas.getHeight() - getFrameHeight()) / 2, null);
		        mHolder.unlockCanvasAndPost(canvas);
		    }
		}

        mCamera.addCallbackBuffer(mFrame);

/*        synchronized (CameraPreviewBase.this) {
        	//mFrame = data;
            CameraPreviewBase.this.notify();
        }*/
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        mThreadRun = false;
        if (mCamera != null) {
//            synchronized (this) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
                
                if(mBmp != null) mBmp = null;
//            }
        }
    }

    protected abstract boolean processFrame(byte[] data);
    
    public void takePicture(PictureCallback callback) {
    	if(mCamera != null) {
    		mCamera.takePicture(null,callback,null);
    	}
    }

    public void run() {
    	Boolean result;
    	
    	mThreadRun = true;
        Log.i(TAG, "Starting processing thread");
        while (mThreadRun) {
        	synchronized (this) {
                try {
                    this.wait();
                    result = processFrame(mFrame);
                    //result = false;
                    mCamera.addCallbackBuffer(mFrame);
                } catch (InterruptedException e) {
                	result = false;
                    e.printStackTrace();
                }
        	}

            if (result) {
                Canvas canvas = mHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawBitmap(mBmp, (canvas.getWidth() - getFrameWidth()) / 2, (canvas.getHeight() - getFrameHeight()) / 2, null);
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
