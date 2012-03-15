package miun.android.ungraph.preview;
import java.io.FileNotFoundException;
import java.util.List;

import miun.android.R;
import miun.android.ungraph.FileNotSupportedException;
import miun.android.ungraph.help.HelpActivity;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.Toast;

public class PreviewActivity extends Activity implements CameraButtonReceiver,PictureCallback, OnTouchListener {
	private static final String TAG = "PreviewActivity";
	
	//onActivityResult constants
	public static final int PICK_IMAGE = 1;
	//onCreateDialog constants
	public static final int DIALOG_FILECHOOSER_UNSUPPORTED = 1;
	public static final int DIALOG_FILETYPE_UNSUPPORTED = 2;

	private CameraPreview mPreview;
	private CameraButton mCameraButton;
	
	private boolean bAbortFollowingCallbacks = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Instantiated new " + this.getClass());
        super.onCreate(savedInstanceState);
        
        //setContentView(new CameraPreview(this));
        
    	//Connect camera preview
        mPreview = new CameraPreview(this);
        mPreview.setOnTouchListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(miun.android.R.layout.main);
        FrameLayout preview = (FrameLayout) findViewById(miun.android.R.id.camera_preview);
    	preview.addView(mPreview);

    	//Init camera button
        mCameraButton = new CameraButton(this,this);
        mCameraButton.registerCameraButton();
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		mPreview.releasecam();
	}
    
    @Override
	protected void onDestroy() {
    	mCameraButton.unregisterCameraButton();
    	mPreview = null;
		super.onDestroy();
	}

	/*
     * Is called when an other activity passes a result to this activity
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.i(TAG, "called onActivityResult with resultCode: " + resultCode + "and requestCode: " + requestCode);
    	switch (requestCode) {
		case PreviewActivity.PICK_IMAGE:
			//User really picked something from a file browser
			if (resultCode == Activity.RESULT_OK) {
				try {
					PreviewProcessor pp = new PreviewProcessor(data.getData(),this);
				} catch (FileNotFoundException e) {
					// In this case no Inputstream could be read from the given Uri... Filechooser is than not supported
					Log.d(TAG,"Couldnt work with URI....");
					showDialog(DIALOG_FILECHOOSER_UNSUPPORTED);
					return;
				} catch (FileNotSupportedException e) {
					// In this case the file selected by the user is not supported (Probably no image file)
					Log.d(TAG,"Couldnt work with file....");
					showDialog(DIALOG_FILETYPE_UNSUPPORTED);
					return;
				}
			}else { //User cancelled file picking
				Toast.makeText(this, R.string.no_file_selected, Toast.LENGTH_SHORT).show();
			}
			break;
		}
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog;
    	AlertDialog.Builder builder;
    	switch (id) {
    	case DIALOG_FILECHOOSER_UNSUPPORTED:
    		builder = new AlertDialog.Builder(this);
    		builder.setMessage(R.string.wrong_filechooser)
    			.setCancelable(false)
    			.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
    		           public void onClick(DialogInterface dialog, int id) {
    		                startFileChooserIntent();
    		           }
    		       });
    		dialog = builder.create();
    		break;
    	
    	case DIALOG_FILETYPE_UNSUPPORTED:
    		builder = new AlertDialog.Builder(this);
    		builder.setMessage(R.string.wrong_file_type)
				.setCancelable(false)
				.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                startFileChooserIntent();
		           }
		       });
    		dialog = builder.create();
    		break;
    		
    	default:
    		dialog = null;
    	}
    	return dialog;
    }
    
    /*
     * Is called when the user presses the handsets menu button
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
    	super.onCreateOptionsMenu(menu); 
    	getMenuInflater().inflate(R.menu.startupoptions, menu);
    	return true;
    }
    
    /*
     * Is called when one of the item of the optionsmenu get selected
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
        switch(item.getItemId()){
        case R.id.gallery:
        	startFileChooserIntent();
        	return true;
        case R.id.help:
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        }
        return false;
    }
    
    public void startFileChooserIntent() {
    	Intent intent = new Intent();
    	intent.setType("image/*");
    	intent.setAction(Intent.ACTION_GET_CONTENT);
    	String selectImage = getResources().getString(R.string.select_picture);
    	startActivityForResult(Intent.createChooser(intent, selectImage), PreviewActivity.PICK_IMAGE);
    }

	public void onCameraButtonPressed() {
		//mPreview.takePicture(this);
		new PreviewProcessor(mPreview.getPreview(), this);
		Log.v(TAG,"Camerabutton pressed");
	}

	/////////This is unused in the actual version
	public void onPictureTaken(byte[] data, Camera cam) {
		if(data != null && bAbortFollowingCallbacks == false) {
			//Create YUV image from data
			Parameters param = cam.getParameters();
			Size size = param.getPictureSize();

			//Create a matrix for YUV420
			Mat mYuv = new Mat(size.height + size.height / 2, size.width, CvType.CV_8UC1);
	        mYuv.put(0, 0, data);
	        Mat mRgb = new Mat();
	        Imgproc.cvtColor(mYuv,mRgb,Imgproc.COLOR_YUV420sp2RGB,4);
	        
			//Forward matrix data
			new PreviewProcessor(mRgb,this);
		}
	}
	//////////////////////////////////
	
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			//mPreview.takePicture(this);
			new PreviewProcessor(mPreview.getPreview(), this);
			Log.v(TAG,"Picture taken by screen touch");
		}
		
		return true;
	}
}