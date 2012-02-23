package miun.android.ungraph.process;

import java.io.InputStream;

import miun.android.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GraphProcessingActivity extends Activity {
	private static final String TAG = "GraphProcessingActivity";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Instantiated new " + this.getClass());
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphprocessing);
        
        ImageView imageView = (ImageView) findViewById(R.id.bitmapview);
        //InputStream is = getContentResolver().openInputStream(getIntent().getData());
		//Bitmap bm = BitmapFactory.decodeStream(is);
        imageView.setImageURI(getIntent().getData());
        
    }
}
