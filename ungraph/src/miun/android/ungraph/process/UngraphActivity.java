package miun.android.ungraph.process;


import miun.android.R;
import miun.android.ungraph.preview.CameraPreviewBase;
import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class UngraphActivity extends Activity {
	private Camera mCamera;
	private CameraPreviewBase mPreview;
	
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
        
        //Camera tests
        mCamera = getFirstCam();
        
        if(mCamera != null) {
        	mPreview = new CameraPreviewBase(this,mCamera);
        	FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        	preview.addView(mPreview);
        }
    }
}