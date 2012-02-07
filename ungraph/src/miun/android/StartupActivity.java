package miun.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class StartupActivity extends Activity {
	
	private static final int PICK_IMAGE = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
		case StartupActivity.PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				
			}else {
				
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
        	startActivityForResult(Intent.createChooser(intent, "Select Picture"), StartupActivity.PICK_IMAGE);
        	return true;
        case R.id.help:
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        }
        return false;
    }
    
    //@Override
    
    
    
}