package miun.android.ungraph.help;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miun.android.R;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class HelpActivity extends ExpandableListActivity {
	private static final String TAG = "HelpActivity";
	
	private static final String PARENT = "parent";
	private static final String CHILD = "child";
	
	private List<HashMap<String,String>> parents;
	private List<List<HashMap<String, String>>> childs;
		
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Instantiated new " + this.getClass());
		
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.help);
    	parents = createParentList();
    	childs = createChildList();
        HelpsExpandableListAdapter expListAdapter = new HelpsExpandableListAdapter(
			this,
			parents, 						// Describing data of group List
			R.layout.help_group_item,		// Group item layout XML.
			new String[] {PARENT},			// the key of group item.
			new int[] {R.id.row_name},		// ID of each group item.-Data under the key goes into this TextView.
			childs,							// childData describes second-level entries.
			R.layout.help_child_item,		// Layout for sub-level entries(second level).
			new String[] {CHILD},			// Keys in childData maps to display.
			new int[] {R.id.grp_child}		// Data under the keys above go into these TextViews.
		);
        setListAdapter(expListAdapter);		// setting the adapter in the list.
    }

	/* Creating the Hashmap for the row */
	private List<HashMap<String,String>> createParentList() {
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
	  	HashMap<String,String> m = new HashMap<String,String>();
	  	m.put(PARENT,getResources().getString(R.string.help_parent_1));
	  	result.add(m);
	  	m = new HashMap<String,String>();
	  	m.put(PARENT,getResources().getString(R.string.help_parent_2));
	  	result.add(m);
	  	m = new HashMap<String,String>();
	  	m.put(PARENT,getResources().getString(R.string.help_parent_3));
	  	result.add(m);
	  	return result;
    }

	/* creatin the HashMap for the children */
	private List<List<HashMap<String, String>>> createChildList() {
    	ArrayList<List<HashMap<String, String>>> result = new ArrayList<List<HashMap<String, String>>>();
    	for (int i=0;i<parents.size();i++){
    		HashMap<String, String> sec = parents.get(i);
    		ArrayList<HashMap<String, String>> secChild = new ArrayList<HashMap<String, String>>();
    		HashMap<String,String> child = new HashMap<String,String>();
    		if(sec.containsValue(getResources().getString(R.string.help_parent_1))){
    			child.put(CHILD, getResources().getString(R.string.help_child_1));
    		}else if(sec.containsValue(getResources().getString(R.string.help_parent_2))){
    			child.put(CHILD, getResources().getString(R.string.help_child_2));
    		}else if(sec.containsValue(getResources().getString(R.string.help_parent_3))){
    			child.put(CHILD, getResources().getString(R.string.help_child_3));
    		}else if(sec.containsValue(getResources().getString(R.string.help_parent_4))){
    			child.put(CHILD, getResources().getString(R.string.help_child_4));
    		}
    		secChild.add(child);
    		result.add(secChild);
    	}
    	return result;
    }
}
