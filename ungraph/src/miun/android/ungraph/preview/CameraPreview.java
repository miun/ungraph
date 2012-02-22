package miun.android.ungraph.preview;

import miun.android.ungraph.Line;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class CameraPreview extends CameraPreviewBase {
    private Mat mYuv;
    private Mat mRgba;
    private Mat mGraySubmat;

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
        }
    }

    @Override
    protected boolean processFrame(byte[] data) {
        Mat houghlines = new Mat();
        Line lines[];
    	
    	//Put YUV image to matrix
    	mYuv.put(0, 0, data);
    	
	    Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
		Imgproc.Canny(mGraySubmat, mGraySubmat, 40, 100);
		Imgproc.HoughLines(mGraySubmat, houghlines, 1, 3.1415926 / 180, 150);
	        	
		lines = new Line[houghlines.cols()];
		
		for(int i = 0; i < houghlines.cols(); i++) {
			double x[] = houghlines.get(0,i);
			lines[i] = new Line(x[0],x[1],new Rect(0,0,getFrameWidth(),getFrameHeight()));
//			mRgba.
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
}
