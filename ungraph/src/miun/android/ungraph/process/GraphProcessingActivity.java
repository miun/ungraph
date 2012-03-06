package miun.android.ungraph.process;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import miun.android.R;
import miun.android.ungraph.Line;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.InputDevice;
import android.view.InputDevice.MotionRange;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class GraphProcessingActivity extends Activity implements OnTouchListener {
	private static final String TAG = "GraphProcessingActivity";

	private Mat mMat;
	private Bitmap mBmp;
	private ImageView mImageView;
	private Map<Double,Double> data;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Instantiated new " + this.getClass());
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphprocessing);
	
        Uri uri = getIntent().getData();
        try {
        	mBmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        	
        	//Convert color space if necessary
        	if(!mBmp.getConfig().equals(Config.ARGB_8888)) {
        		//Convert to ARGB_8888
        		Bitmap temp = mBmp.copy(Config.ARGB_8888,false);
        		mBmp = temp;
        		temp = null;
        	}
        	
        	//Convert to Matrix
        	mMat = Utils.bitmapToMat(mBmp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catch (Exception e) {
        	Log.e(TAG,"Cannot load picture");
		}
        
		//Get image view
        mImageView = (ImageView) findViewById(R.id.bitmapview);
        mImageView.setOnTouchListener(this);
		
		//Show picture
		if(mMat == null || mMat.width() <= 0 || mMat.height() <= 0) {
			//TODO error dialog
		}
		
		//Create data map
		data = new HashMap<Double,Double>();
		
		//Analyse image
		analyseImage();
   }
    
    private void analyseImage() {
    	Mat graySubmat,cannyMat,dilateMat;
    	Mat horzSubmat,vertSubmat;
    	
    	Mat houghlines = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(4,4));
        Line horzLine = null,vertLine = null;
        Size size;
    	
	    //Create gray picture
        graySubmat = new Mat(mMat.rows(),mMat.cols(),CvType.CV_8UC1);
        
        int test = mMat.type();
        
        Imgproc.cvtColor(mMat, graySubmat, Imgproc.COLOR_RGBA2GRAY); 

        //Find horizontal lines
        horzSubmat = graySubmat.submat((int)Math.round(mMat.height() * 0.45), (int)Math.round(mMat.height() * 0.55), 0, mMat.width());
        vertSubmat = graySubmat.submat(0,mMat.height(),(int)Math.round(mMat.width() * 0.45), (int)Math.round(mMat.width() * 0.55));

        cannyMat = new Mat(horzSubmat.size(),CvType.CV_8UC1);
	    dilateMat = new Mat(horzSubmat.size(),CvType.CV_8UC1);
	    
	    Imgproc.Canny(horzSubmat, cannyMat, 40, 100);
		Imgproc.dilate(cannyMat,dilateMat,kernel);
		Imgproc.HoughLinesP(dilateMat, houghlines, 1, Math.PI / 180, 150, Math.round(dilateMat.cols() * 0.5),10);
		
		size = mMat.size();
		
		if(houghlines.rows() > 0) {
			//Create line object
			double x[] = houghlines.get(0,0);
			horzLine = new Line(Math.round(x[0]),Math.round(x[1] + mMat.height() * 0.45),Math.round(x[2]),Math.round(x[3] + mMat.height() * 0.45));
			Core.line(mMat,horzLine.begin(),horzLine.end(), new Scalar(255,0,0,255),2);
		}

	    //Detect vertical line;
	    cannyMat = new Mat(vertSubmat.size(),CvType.CV_8UC1);
	    dilateMat = new Mat(vertSubmat.size(),CvType.CV_8UC1);
	    
	    Imgproc.Canny(vertSubmat, cannyMat, 40, 100);
		Imgproc.dilate(cannyMat,dilateMat,kernel);
		Imgproc.HoughLinesP(dilateMat, houghlines, 1, Math.PI / 180, 150, Math.round(dilateMat.cols() * 0.5),10);
		
		size = mMat.size();
		
		if(houghlines.rows() > 0) {
			//Create line object
			double x[] = houghlines.get(0,0);
			vertLine = new Line(Math.round(x[0] + mMat.width() * 0.45),Math.round(x[1]),Math.round(x[2] + mMat.width() * 0.45),Math.round(x[3]));
			Core.line(mMat,vertLine.begin(),vertLine.end(), new Scalar(255,0,0,255),2);
		}

		//Draw raster
		Core.line(mMat,new Point(0,size.height * 0.45),new Point(size.width,size.height * 0.45),new Scalar(0,255,0,255));
		Core.line(mMat,new Point(0,size.height * 0.55),new Point(size.width,size.height * 0.55),new Scalar(0,255,0,255));
		Core.line(mMat,new Point(size.width * 0.45,0),new Point(size.width * 0.45,size.height),new Scalar(0,255,0,255));
		Core.line(mMat,new Point(size.width * 0.55,0),new Point(size.width * 0.55,size.height),new Scalar(0,255,0,255));
		
		//Show detected lines
    	Utils.matToBitmap(mMat, mBmp);
    	mImageView.setImageBitmap(mBmp);
    	
    	//Allow to free memory
    	cannyMat.release(); cannyMat = null;
    	dilateMat.release(); dilateMat = null;
    	horzSubmat.release(); horzSubmat = null;
    	vertSubmat.release(); vertSubmat = null;
    	
    	//Detect graph line
    	if(horzLine != null && vertLine != null) {
    		detectGraph(horzLine,vertLine);
    	}
    	else {
    		//TODO show no graph found dialog
    	}
    }
    
    private boolean detectGraph(Line horz,Line vert) {
    	Mat canny;
    	
    	
    	data.put(0.2,0.3);
    	return false;
    }

	public boolean onTouch(View view, MotionEvent event) {
		//InputDevice dev = InputDevice.getDevice(InputDevice.SOURCE_CLASS_POINTER);
		//MotionRange range = dev.getMotionRange(InputDevice.MOTION_RANGE_X);

		//Get touch event
		if(event.getAction() == MotionEvent.ACTION_MOVE) {
			float x = event.getX();
			float y = event.getY();
			
			updateDataPoint(x,0,0);
		}
		
		return true;
	}
	
	private void updateDataPoint(float x,float min,float max) {
		Log.v(TAG,new Float(x).toString()  + " - " + new Float(min).toString()  + " - " + new Float(max).toString());
	}
}
