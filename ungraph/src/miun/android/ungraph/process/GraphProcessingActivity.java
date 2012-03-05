package miun.android.ungraph.process;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import miun.android.R;
import miun.android.ungraph.help.HelpActivity;
import miun.android.ungraph.preview.PreviewActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class GraphProcessingActivity extends Activity {
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
		
		//Show picture
		if(mMat == null || mMat.width() <= 0 || mMat.height() <= 0) {
			//TODO error dialog
		}
		
		//Create data map
		data = new HashMap<Double,Double>();
		
	    //Imgproc.cvtColor(mMat, mBmp, Imgproc.COLOR_YUV420sp2RGB, 4);
		mImageView.setImageBitmap(mBmp);
		
		//Analyse image
		analyseImage();
    }
    
    private void analyseImage() {/*
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
    	
    */}
    
    private boolean detectGraph() {
    	data.put(0.2,0.3);
    	return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
    	super.onCreateOptionsMenu(menu); 
    	getMenuInflater().inflate(R.menu.processingactivity_optionsmenu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
        switch(item.getItemId()){
        case R.id.next:
        	this.goBackToPreviewActivity();
        	return true;
        case R.id.help:
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        }
        return false;
    }
    
    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.goBackToPreviewActivity();
			return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
    
    private void goBackToPreviewActivity() {
		this.startActivity(new Intent(this, PreviewActivity.class));
		this.finish();
    }
}
