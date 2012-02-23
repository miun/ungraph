package miun.android.ungraph.help;


import miun.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class HelpActivity extends Activity {
	private static final String TAG = "HelpActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Instantiated new " + this.getClass());
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
    }
}
