package miun.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class StartupActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
    	super.onCreateOptionsMenu(menu); 
    	getMenuInflater().inflate(R.menu.startupoptions, menu);
    	/*menu.findItem(R.id.help_menu_item).setIntent(
    		new Intent(this, StartupActivity.class));
    	menu.findItem(R.id.settings_menu_item).setIntent(
    	    new Intent(this, StartupActivity.class));*/
    	return true;
    }
}