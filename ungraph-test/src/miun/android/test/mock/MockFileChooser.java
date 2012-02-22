package miun.android.test.mock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import miun.android.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MockFileChooser extends Activity {
	public static final int CANCELLED = 1;
	public static final int UNSUPPORTED_FILE = 2;
	public static final int SUPPORTED_FILE = 3;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int simulatedOperation = intent.getIntExtra("simu", CANCELLED);
        Intent resultIntent;
        File f;
        switch (simulatedOperation){
        case CANCELLED:
        	resultIntent = new Intent();
        	setResult(RESULT_CANCELED,resultIntent);
        	break;
        case UNSUPPORTED_FILE:
        	resultIntent = new Intent();
        	Log.d("Mock", Uri.parse("android.resource://miun.android.test/"+miun.android.test.R.raw.mocktxt).toString());
        	resultIntent.setData(Uri.parse("android.resource://miun.android.test.mock/"+miun.android.test.R.raw.mocktxt));
        	setResult(RESULT_OK,resultIntent);
        	break;
        case SUPPORTED_FILE:
        	resultIntent = new Intent();
        	resultIntent.setData(putToApplicationFolder(getResources().openRawResource(miun.android.test.R.raw.mockjpeg)));
        	setResult(RESULT_OK,resultIntent);
        	break;
        }
        finish();
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("Mock","destroy");
	}
	
	private Uri putToApplicationFolder(InputStream in){
		try {
			File f = new File(getFilesDir(),"mockFile");
			OutputStream out = openFileOutput("mockFile", MODE_WORLD_READABLE);
			byte buf[] = new byte[1024];
			int len;
			while((len=in.read(buf))>0)
				  out.write(buf,0,len);
			out.close();
			in.close();
			return Uri.fromFile(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d("Mock","Kann Mockfile nicht erstellen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("Mock","Kann aus Mockfile nicht lesen");
		}
		return null;
	}
	
}
