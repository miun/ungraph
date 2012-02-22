package miun.android;

import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GraphProcessingActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphprocessing);
        
        ImageView imageView = (ImageView) findViewById(R.id.bitmapview);
        //InputStream is = getContentResolver().openInputStream(getIntent().getData());
		//Bitmap bm = BitmapFactory.decodeStream(is);
        imageView.setImageURI(getIntent().getData());
        
    }
}
