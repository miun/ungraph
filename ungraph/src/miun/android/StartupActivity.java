package miun.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class StartupActivity extends Activity {
	
	public static final int PICK_IMAGE = 1;
	public static final int DIALOG_UNSUPPORTEDFILE_ID = 1;
	
	private Camera mCamera;
	private CameraPreview mPreview;
	
	//Return available camera or null
	private Camera getFirstCam() {
		try {
			return Camera.open();
		}
		catch (Exception e) {
			Log.e("ungraph.cam","Cannot open camera");
			return null;
		}
	}
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mCamera = getFirstCam();
        
        if(mCamera != null) {
        	mPreview = new CameraPreview(this,mCamera);
        	FrameLayout preview = (FrameLayout) findViewById(miun.android.R.id.camera_preview);
        	preview.addView(mPreview);
        }
        Log.d("Startup","create");
    }
    
    /*
     * Is called when an other activity passes a result to this activity
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d("Startup","onActivityResult wird aufgerufen "+ resultCode);
    	switch (requestCode) {
    	//In this case the user selected an image from his harddrive
		case StartupActivity.PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				this.processSelectedFile(data.getData());
			}else {
				Toast.makeText(this, R.string.no_file_selected, Toast.LENGTH_SHORT).show();
			}
			break;
		}
    }
    /*
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog;
    	switch (id) {
    	case DIALOG_UNSUPPORTEDFILE_ID:
    		
    		break;
    		
    	default:
    		dialog = null;
    	}
    	return dialog;
    }
    */
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
        	startActivityForResult(Intent.createChooser(intent, selectImage), StartupActivity.PICK_IMAGE);
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
    	Toast.makeText(this, getContentResolver().getType(source), Toast.LENGTH_LONG).show();
    	
    }
    
    
    
}