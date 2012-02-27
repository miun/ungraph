package miun.android.ungraph.preview;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import miun.android.R;
import miun.android.ungraph.FileNotSupportedException;
import miun.android.ungraph.help.HelpActivity;
import miun.android.ungraph.process.GraphProcessingActivity;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class PreviewActivity extends Activity {
	private static final String TAG = "PreviewActivity";
	
	//onActivityResult constants
	public static final int PICK_IMAGE = 1;
	//onCreateDialog constants
	public static final int DIALOG_FILECHOOSER_UNSUPPORTED = 1;
	public static final int DIALOG_FILETYPE_UNSUPPORTED = 2;
	private CameraPreview mPreview;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Instantiated new " + this.getClass());
		
        super.onCreate(savedInstanceState);

        //TEST//
        Mat img;
        Point point;
        img = Mat.zeros(200,200,CvType.CV_8UC1);
        
/*        Line line = new Line(0,50,200,100);
        LineIterator it = new LineIterator(line,false);
        while(it.hasNext()) point = (Point)it.next();

        line = new Line(200,50,0,100);
        it = new LineIterator(line,false);
        while(it.hasNext()) point = (Point)it.next();

        line = new Line(50,0,100,200);
        it = new LineIterator(line,false);
        while(it.hasNext()) point = (Point)it.next();

        line = new Line(50,200,100,0);
        it = new LineIterator(line,false);
        while(it.hasNext()) point = (Point)it.next();*/

        //return;
        
        //setContentView(R.layout.main);
        setContentView(new CameraPreview(this));
        
    	//Connect camera preview
/*        mPreview = new CameraPreview(this);
    	FrameLayout preview = (FrameLayout) findViewById(miun.android.R.id.camera_preview);
    	preview.addView(mPreview);*/
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
				this.processSelectedFile(data.getData());
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
    
    public void startFileChooserIntent(){
    	Intent intent = new Intent();
    	intent.setType("image/*");
    	intent.setAction(Intent.ACTION_GET_CONTENT);
    	String selectImage = getResources().getString(R.string.select_picture);
    	startActivityForResult(Intent.createChooser(intent, selectImage), PreviewActivity.PICK_IMAGE);
    }
    
    /*
     * This method should process the file returned by the selector. 
     * If the file is not an image file a Dialog should be shown to inform the user that 
     * it was unsuccessful.. 
     * If the file is an image the intend to calculate the image must be started 
     */
    public void processSelectedFile(Uri source) {/*
    	String mimeType = getContentResolver().getType(source);
    	if (mimeType != null){ //Then wie got a content Uri and the mime type could be defined
    		Log.d(TAG,"Content Uri from Gallery: " + source);
    		Log.d(TAG,"Mime Type: " + mimeType);
    	}else { // we got an URL were the file Type could not be defined by the contentresolver
    		Log.d(TAG,"File URL... Path: " + source.getPath());
    		FileNameMap fileNameMap = URLConnection.getFileNameMap();
    		mimeType = fileNameMap.getContentTypeFor(source.getPath());
    		Log.d(TAG,"Mime Type: " + mimeType);
    	}
    	
    	if (!mimeType.startsWith("image/")){
    		throw new FileNotSupportedException("The File is not an image File");
    	}*/
    	
    	InputStream is = null;
    	Bitmap bm = null;
    	try {
			is = getContentResolver().openInputStream(source);
			BitmapFactory.Options op = new BitmapFactory.Options();
			//op.inPurgeable = true;
			op.inSampleSize = 8192;
			bm = BitmapFactory.decodeStream(is,null,op);
			is.close();
			if (bm == null){
				throw new FileNotSupportedException("This File type is not supported");
			}else {
				bm.recycle();
				bm = null;
			}
			System.gc();
		} catch (FileNotFoundException e) {
			// In this case no Inputstream could be read from the given Uri... Filechooser is than not supported
			//TODO show an Dialog with OK Button to inform the user about that
			Log.d(TAG,"Couldnt work with URI....");
			return;
		} catch (FileNotSupportedException e) {
			// In this case the file selected by the user is not supported (Probably no image file)
			//TODO show an Dialog with OK Button to inform the user about that
			Log.d(TAG,"Couldnt work with file....");
			showDialog(DIALOG_FILETYPE_UNSUPPORTED);
			return;
		} catch (IOException e) {
			// If the Inputstream can not be closed
			Log.d(TAG,"Could not close InputStream");
		}
		/*
		String filename = "temporary_working_file";
		File workingFile = new File(getFilesDir(),filename);
		FileOutputStream out = null;
		try {
			out = openFileOutput(filename, MODE_PRIVATE);
			Log.d(TAG,"Before compress");
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			bm.recycle();
			Log.d(TAG,"After compress");
			out.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG,"Could not create temporary file");
			return;
		} catch (IOException e) {
			// If the Outputstream can not be closed
			Log.d(TAG,"Could not close OutputStream");
		}
		*/
		Intent intent = new Intent(this, GraphProcessingActivity.class);
		intent.setData(source);
		startActivity(intent);
		//TODO I think this activity can finish here for first
		
    }
    
    
    
}