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
    private Mat mCannyMat;

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
            mRgba = new Mat();
            mCannyMat = new Mat();
        }
    }

    @Override
    protected boolean processFrame(byte[] data) {
        Mat houghlines = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(4,4));
        Line lines[];
    	
    	//Put YUV image to matrix
    	mYuv.put(0, 0, data);
    	
	    Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
		Imgproc.Canny(mGraySubmat, mCannyMat, 40, 100);
		Imgproc.dilate(mCannyMat,mGraySubmat,kernel);
		Imgproc.HoughLines(mCannyMat, houghlines, 1, 3.1415926 / 180, 150);
        //Imgproc.cvtColor(mGraySubmat, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
	    
		lines = new Line[houghlines.cols()];
		
		for(int i = 0; i < houghlines.cols(); i++) {
			double x[] = houghlines.get(0,i);
			lines[i] = new Line(x[0],x[1],new Rect(0,0,getFrameWidth() - 1,getFrameHeight() - 1));

			//Core.line(mRgba,lines[i].begin(),lines[i].end(), new Scalar(0,255,0,255),1);
			if(lines[i].analyseLineLength(mGraySubmat).length() == 0) lines[i] = null;
			if(lines[i] != null) Core.line(mRgba,lines[i].begin(),lines[i].end(), new Scalar(255,0,0,255),2);
		}

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
  
            mYuv = null;
            mRgba = null;
            mGraySubmat = null;
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
        if (mCannyMat != null)
            mCannyMat.release();

        mYuv = null;
        mRgba = null;
        mGraySubmat = null;
        mCannyMat = null;
    }
}
