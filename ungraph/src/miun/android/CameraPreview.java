package miun.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback {
/*	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		Paint paint = new Paint();
		paint.setColor(10);
		
		canvas.drawLine(0, 0, 100,100,paint);
		invalidate();
	}*/

	private Camera mCamera;
	private SurfaceHolder mHolder;
	private byte[] mBuffer;
	private Bitmap bmp;
	
	public CameraPreview(Context context, Camera camera) {
        super(context);

        Camera.Size size;
        int bpp;

        //Init buffer and callback
        mCamera = camera;
    	size = mCamera.getParameters().getPreviewSize();
        bpp = ImageFormat.getBitsPerPixel(mCamera.getParameters().getPreviewFormat());
        mBuffer = new byte[size.width * size.height * bpp / 8];

		//Init holder
    	mHolder = getHolder();
		mHolder.addCallback(this);
		//mHolder.setType(SurfaceHolder.);
		
		//Create bitmap for preview
		bmp = Bitmap.createBitmap(500,500,Bitmap.Config.ARGB_8888);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		mCamera.addCallbackBuffer(mBuffer);
		mCamera.setPreviewCallbackWithBuffer(this);
		mCamera.startPreview();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		//Check some stuff
		if(mHolder.getSurface() == null) return;
		try { mCamera.stopPreview(); } catch (Exception e) {}
		
		try {
			//mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		}
		catch (Exception e) {
			Log.d("ungraph-cam","Cannot restart preview");
		}
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Paint paint;
		byte b;
		
		for(int i = 0; i < 500; i++) {
			for(int j = 0; j < 500; j++) {
				b = data[j * 640 + i];
				bmp.setPixel(i, j, Color.rgb(b,b,b));
			}
		}
		
		//Draw test line
		Canvas canvas = mHolder.lockCanvas();
		canvas.drawBitmap(bmp, 0,0,null);
		mHolder.unlockCanvasAndPost(canvas);

		//Add buffer for next preview frame
		camera.addCallbackBuffer(mBuffer);
	}
}
