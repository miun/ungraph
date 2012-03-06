package miun.android.ungraph.process;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class GraphProcessingActivity extends Activity implements OnTouchListener {
	private static final String TAG = "GraphProcessingActivity";

	private Mat mMat;
	private Bitmap mBmp;
	private Bitmap mDisplayBmp;
	
	private Line horz = null,vert = null;
	
	private ImageView mImageView;
	private Map<Integer,Double> data;
	
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
        		Bitmap temp = mBmp.copy(Config.ARGB_8888,true);
        		mBmp = temp;
        		temp = null;
        	}
        	
        	//Convert to Matrix
        	mMat = Utils.bitmapToMat(mBmp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		data = new HashMap<Integer,Double>();
		
		//Analyse image
		analyseImage();
   }
    
    private void analyseImage() {
    	Mat graySubmat,cannyMat,dilateMat;
    	Mat horzSubmat,vertSubmat;
    	
    	Mat houghlines = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(4,4));
        Size size;
    	
	    //Create gray picture
        graySubmat = new Mat(mMat.rows(),mMat.cols(),CvType.CV_8UC1);
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
			horz = new Line(Math.round(x[0]),Math.round(x[1] + mMat.height() * 0.45),Math.round(x[2]),Math.round(x[3] + mMat.height() * 0.45));
			Core.line(mMat,horz.begin(),horz.end(), new Scalar(255,0,0,255),2);
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
			vert = new Line(Math.round(x[0] + mMat.width() * 0.45),Math.round(x[1]),Math.round(x[2] + mMat.width() * 0.45),Math.round(x[3]));
			Core.line(mMat,vert.begin(),vert.end(), new Scalar(255,0,0,255),2);
		}

		//Draw raster
		Core.line(mMat,new Point(0,size.height * 0.45),new Point(size.width,size.height * 0.45),new Scalar(0,255,0,255));
		Core.line(mMat,new Point(0,size.height * 0.55),new Point(size.width,size.height * 0.55),new Scalar(0,255,0,255));
		Core.line(mMat,new Point(size.width * 0.45,0),new Point(size.width * 0.45,size.height),new Scalar(0,255,0,255));
		Core.line(mMat,new Point(size.width * 0.55,0),new Point(size.width * 0.55,size.height),new Scalar(0,255,0,255));
		
    	//Allow to free memory
    	cannyMat.release(); cannyMat = null;
    	dilateMat.release(); dilateMat = null;
    	horzSubmat.release(); horzSubmat = null;
    	vertSubmat.release(); vertSubmat = null;
    	
		//Create display bitmap
    	mDisplayBmp = Bitmap.createBitmap(mMat.width(),mMat.height(),Config.ARGB_8888);
		Utils.matToBitmap(mMat, mDisplayBmp);
    	mImageView.setImageBitmap(mDisplayBmp);

		//Detect graph line
    	if(horz != null && vert != null) {
    		detectGraph(graySubmat);
        	mMat.release(); mMat = null;
    	}
    	else {
        	mMat.release(); mMat = null;
    		//TODO show no graph found dialog
    	}
    }
    
    private boolean detectGraph(Mat grayMat) {
    	Mat roiMat,canny;
    	Rect roi;
    	ArrayList<Integer> pixels;
    	int nLast = -1;
    	int nBest;
    	int nAxisPos;
    	byte[] red = new byte[4];
    	int lastPercent = -1;
    	
    	red[0] = 0;
    	red[1] = (byte)255;
    	red[2] = (byte)255;
    	red[3] = (byte)255;
    	
    	//No lines available
    	if(horz == null || vert == null) return false;
    	
    	//Set ROI to the place where the graph should be
    	roi = new Rect((int)Math.round(Math.min(horz.begin().x,horz.end().x)),(int)Math.round(Math.min(vert.begin().y,vert.end().y)),(int)Math.round(Math.abs(horz.end().x - horz.begin().x)),(int)Math.round(Math.abs(vert.end().y - vert.begin().y)));

    	//Move lines to rectangle boundaries
    	horz = new Line((int)Math.round(horz.begin().x - roi.x),(int)Math.round(horz.begin().y - roi.y),(int)Math.round(horz.end().x - roi.x),(int)Math.round(horz.end().y - roi.y));
    	vert = new Line((int)Math.round(vert.begin().x - roi.x),(int)Math.round(vert.begin().y - roi.y),(int)Math.round(vert.end().x - roi.x),(int)Math.round(vert.end().y - roi.y));
    	
    	//Correct line direction (must go from left to right!)
    	if(horz.begin().x > horz.end().x) {
    		horz = new Line((int)Math.round(horz.end().x),(int)Math.round(horz.end().y),(int)Math.round(horz.begin().x),(int)Math.round(horz.begin().y));
    	}
    	
    	// !!!!! x is y and width is height for matrices !!!!! (because matrix definition begins with column followed by row
    	int temp = roi.x;
    	roi.x = roi.y;
    	roi.y = temp;
    	temp = roi.width;
    	roi.width = roi.height;
    	roi.height = temp;
    	
    	//Create matrix of graph region
    	roiMat = new Mat(grayMat,roi);
    	
    	//Canny pic
    	canny = new Mat();
    	Imgproc.Canny(roiMat, canny, 40,100);
    	roiMat.release(); roiMat = null;
    	
    	for(int i = 0; i < canny.width(); i++) {
    		pixels = getColPixels(canny,i);
    		
    		//Update progress bar if necessary
    		if(Math.round(100.0 / canny.width() * i) != lastPercent) {
    			lastPercent = (int)Math.round(100.0 / canny.width() * i);
    			processStatus(lastPercent);
    		}
    		
    		//If there are any pixels
    		if(!pixels.isEmpty()) {
	    		//Calculate current x axis position
	    		//nAxisPos = (int)Math.round(horz.begin().y + (horz.end().y - horz.begin().y) / (horz.end().x - horz.begin().x) * i);
	    		nAxisPos = (int)Math.round(horz.begin().y + (horz.end().y - horz.begin().y) / horz.end().x * i);
	    		
    			//First pixel => search the one that is farthest away from axis
    			nBest = 0;
    			for(int j = 1; j < pixels.size(); j++) {
    				if(Math.abs(pixels.get(j) - nAxisPos) > Math.abs(pixels.get(nBest) - nAxisPos)) {
    					nBest = j;
    				}
    			}
	    			
    			//Remember decision
    			nLast = pixels.get(nBest);
	    		
	    		//Add point to list
	    		data.put(i,(2.0 / canny.height() * nLast) - 1.0);
	    		
	    		//Draw new point
	    		mMat.put(nLast + roi.x, i + roi.y, red);
	    		mMat.put(nLast + roi.x + 1, i + roi.y, red);
	    		mMat.put(nLast + roi.x, i + roi.y + 1, red);
	    		mMat.put(nLast + roi.x - 1, i + roi.y, red);
	    		mMat.put(nLast + roi.x, i + roi.y - 1, red);
    		}
    	}
    	
    	//Display graph
    	Utils.matToBitmap(mMat, mDisplayBmp);
    	return true;
    }
    
    private void setDataSelector(float pos) {
    	int nPos;
    	int nWidth;
    	
    	//Calculate bitmap width
    	nWidth = mDisplayBmp.getHeight() / mImageView.getHeight() * mDisplayBmp.getWidth();
    	
    	//Calculate pixel position in bitmap 
    	nPos = 
    	
    	//nPos = 
    }
    
    //Find column pixels
    private ArrayList<Integer> getColPixels(Mat mat,int col) {
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	double[] temp;

    	for(int i = 0; i < mat.height(); i++) {
    		temp = mat.get(i, col);
    		if(temp[0] != 0) {
    			list.add(i);
    		}
    	}
    	
    	return list;
    }

	public boolean onTouch(View view, MotionEvent event) {
		//InputDevice dev = InputDevice.getDevice(InputDevice.SOURCE_CLASS_POINTER);
		//MotionRange range = dev.getMotionRange(InputDevice.MOTION_RANGE_X);

		//Get touch event
		if(event.getAction() == MotionEvent.ACTION_MOVE) {
			float x = event.getX();
			//float y = event.getY(); //Not used at the moment
			
			setDataSelector(x);
		}
		
		return true;
	}
	
	private void processStatus(int percent) {
		//TODO Hier prozente machen
	}
}
