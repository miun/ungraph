package miun.android.ungraph.preview;

import miun.android.ungraph.Line;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class CameraPreview extends CameraPreviewBase {
    private Mat mYuv;
    private Mat mRgba;
    private Mat mGraySubmat;
    private Mat mHorzSubmat,mVertSubmat;

    public CameraPreview(Context context) {
        super(context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
        super.surfaceChanged(_holder, format, width, height);

        synchronized (this) {
            // initialize Mats before usage
            mYuv = new Mat(getFrameHeight() + getFrameHeight() / 2, getFrameWidth(), CvType.CV_8UC1);
            mGraySubmat = mYuv.submat(0, getFrameHeight(), 0, getFrameWidth());
            mHorzSubmat = mGraySubmat.submat((int)Math.round(getFrameHeight() * 0.45), (int)Math.round(getFrameHeight() * 0.55), 0, getFrameWidth());
            mVertSubmat = mGraySubmat.submat(0,getFrameHeight(),(int)Math.round(getFrameWidth() * 0.45), (int)Math.round(getFrameWidth() * 0.55));

            mRgba = new Mat();
        }
    }

    @Override
    protected boolean processFrame(byte[] data) {
        Mat cannyMat,dilateMat;
    	Mat houghlines = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(4,4));
        Line lines[];
        Size size;
    	
    	//Put YUV image to matrix
    	mYuv.put(0, 0, data);
	    Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);

	    //Detect horizontal line;
	    cannyMat = new Mat(mHorzSubmat.size(),CvType.CV_8UC1);
	    dilateMat = new Mat(mHorzSubmat.size(),CvType.CV_8UC1);
	    
	    Imgproc.Canny(mHorzSubmat, cannyMat, 40, 100);
		Imgproc.dilate(cannyMat,dilateMat,kernel);
//		Imgproc.HoughLines(dilateMat, houghlines, 1, 3.1415926 / 180, 150);
		Imgproc.HoughLinesP(dilateMat, houghlines, 1, Math.PI / 180, 150, Math.round(dilateMat.cols() * 0.5),10);
		
		lines = new Line[houghlines.rows()];
		size = mRgba.size();
		
		for(int i = 0; i < lines.length; i++) {
			//Create line object
			double x[] = houghlines.get(i,0);
			lines[i] = new Line(Math.round(x[0]),Math.round(x[1] + getFrameHeight() * 0.45),Math.round(x[2]),Math.round(x[3] + getFrameHeight() * 0.45));
			Core.line(mRgba,lines[i].begin(),lines[i].end(), new Scalar(255,0,0,255),2);
		}

	    //Detect vertical line;
	    cannyMat = new Mat(mVertSubmat.size(),CvType.CV_8UC1);
	    dilateMat = new Mat(mVertSubmat.size(),CvType.CV_8UC1);
	    
	    Imgproc.Canny(mVertSubmat, cannyMat, 40, 100);
		Imgproc.dilate(cannyMat,dilateMat,kernel);
//		Imgproc.HoughLines(dilateMat, houghlines, 1, 3.1415926 / 180, 150);
		Imgproc.HoughLinesP(dilateMat, houghlines, 1, Math.PI / 180, 150, Math.round(dilateMat.cols() * 0.5),10);
		
		lines = new Line[houghlines.rows()];
		size = mRgba.size();
		
		for(int i = 0; i < lines.length; i++) {
			//Create line object
			double x[] = houghlines.get(i,0);
			lines[i] = new Line(Math.round(x[0] + getFrameWidth() * 0.45),Math.round(x[1]),Math.round(x[2] + getFrameWidth() * 0.45),Math.round(x[3]));
			Core.line(mRgba,lines[i].begin(),lines[i].end(), new Scalar(255,0,0,255),2);
		}

		//Draw raster
		Core.line(mRgba,new Point(0,size.height * 0.45),new Point(size.width,size.height * 0.45),new Scalar(0,255,0,255));
		Core.line(mRgba,new Point(0,size.height * 0.55),new Point(size.width,size.height * 0.55),new Scalar(0,255,0,255));
		Core.line(mRgba,new Point(size.width * 0.45,0),new Point(size.width * 0.45,size.height),new Scalar(0,255,0,255));
		Core.line(mRgba,new Point(size.width * 0.55,0),new Point(size.width * 0.55,size.height),new Scalar(0,255,0,255));

		//Return bitmap
		return Utils.matToBitmap(mRgba, mBmp);
    }

    @Override
    public void run() {
        super.run();

        synchronized (this) {
            // Explicitly deallocate Mats
            if (mYuv != null)
                mYuv.release();
            if (mRgba != null)
                mRgba.release();
            if (mGraySubmat != null)
                mGraySubmat.release();
            if (mHorzSubmat != null)
            	mHorzSubmat.release();
            if (mVertSubmat != null)
            	mVertSubmat.release();
  
            mYuv = null;
            mRgba = null;
            mGraySubmat = null;
            mHorzSubmat = null;
            mVertSubmat = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	super.surfaceDestroyed(holder);
    	
        if (mYuv != null)
            mYuv.release();
        if (mRgba != null)
            mRgba.release();
        if (mGraySubmat != null)
            mGraySubmat.release();
        if (mHorzSubmat != null)
        	mHorzSubmat.release();
        if (mVertSubmat != null)
        	mVertSubmat.release();

        mYuv = null;
        mRgba = null;
        mGraySubmat = null;
        mHorzSubmat = null;
        mVertSubmat = null;
    }
}
