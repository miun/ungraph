package miun.android.ungraph.process;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import miun.android.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class GraphProcessingActivity extends Activity {
	private static final String TAG = "GraphProcessingActivity";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Instantiated new " + this.getClass());
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphprocessing);
        /*try {
			Mat mat = Utils.bitmapToMat(BitmapFactory.decodeStream(getContentResolver().openInputStream(getIntent().getData())));
			Bitmap b = Bitmap.createBitmap(200,100,Bitmap.Config.ARGB_8888);
			Utils.matToBitmap(mat, b);
			ImageView imageView = (ImageView) findViewById(R.id.bitmapview);
			imageView.setImageBitmap(b);
			b.recycle();
			b = null;
			System.gc();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
        
        InputStream is = null;
		try {
			is = getContentResolver().openInputStream(getIntent().getData());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Bitmap bm = BitmapFactory.decodeStream(is);
        Uri uri = getIntent().getData();
        Mat mat = null;
        try {
			mat = Utils.bitmapToMat(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageView imageView = (ImageView) findViewById(R.id.bitmapview);
		if(mat != null){
			Bitmap b = Bitmap.createBitmap(mat.width(),mat.height(),Bitmap.Config.ARGB_8888);
			Utils.matToBitmap(mat, b);
			imageView.setImageBitmap(b);
		}else {
			imageView.setImageURI(getIntent().getData());
		}
    }
}
