package miun.android.ungraph.preview;

import miun.android.R;
import miun.android.ungraph.help.HelpActivity;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PreviewActivity extends Activity implements CameraButtonReceiver,PictureCallback {
	private static final int PICK_IMAGE = 1;
	private CameraPreview mPreview;
	private CameraButton mCameraButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
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
    	switch (requestCode) {
    	//In this case the user selected an image from his harddrive
		case PreviewActivity.PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				this.processSelectedFile(data.getData());
			}
			break;
		}
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
        	Intent intent = new Intent();
        	intent.setType("image/*");
        	intent.setAction(Intent.ACTION_GET_CONTENT);
        	String selectImage = getResources().getString(R.string.select_picture);
        	startActivityForResult(Intent.createChooser(intent, selectImage), PreviewActivity.PICK_IMAGE);
        	return true;
        case R.id.help:
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        }
        return false;
    }
    
    /*
     * This method should process the file returned by the selector. 
     * If the file is not an image file a Dialog should be shown to inform the user that 
     * it was unsuccessful.. 
     * If the file is an image the intend to calculate the image must be started 
     */
    public void processSelectedFile(Uri source) {
    	
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Result Cancelled...");
		alertDialog.setMessage("Are you sure?");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
		      // here you can add functions
		   }
		});
		//alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
    }

	public void onCameraButtonPressed() {
		mPreview.takePicture(this);
	}

	public void onPictureTaken(byte[] arg0, Camera arg1) {
		System.out.println(arg0.length);
	}
}